package com.dsa.algorithms;

import com.dsa.core.Graph;
import com.dsa.core.Item;
import com.dsa.core.PriorityQueue;
import com.dsa.core.User;

import java.util.*;

/**
 * Core engine for generating recommendations.
 * Implements Collaborative Filtering and Graph-based traversals.
 */
public class RecommendationEngine {
    private Map<Integer, User> users;
    private Map<Integer, Item> items;
    private Graph graph;
    
    // Lazy recommendation cache: UserId -> List of Recommendations
    // Ideally should expire or invalidate on interaction
    private Map<Integer, List<Item>> recommendationCache;

    public RecommendationEngine(Map<Integer, User> users, Map<Integer, Item> items, Graph graph) {
        this.users = users;
        this.items = items;
        this.graph = graph;
        this.recommendationCache = new HashMap<>();
    }

    /**
     * Calculates Cosine Similarity between two users based on their interaction vectors.
     * O(N) where N is number of items. Optimizable to O(min(|A|, |B|)) using maps.
     */
    public double calculateUserSimilarity(User u1, User u2) {
        Map<Integer, Double> ratings1 = u1.getInteractions();
        Map<Integer, Double> ratings2 = u2.getInteractions();

        // Optimized intersection
        if (ratings1.size() > ratings2.size()) {
            return calculateUserSimilarity(u2, u1); // Swap to iterate smaller map
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Map.Entry<Integer, Double> entry : ratings1.entrySet()) {
            int itemId = entry.getKey();
            double r1 = entry.getValue();
            if (ratings2.containsKey(itemId)) {
                double r2 = ratings2.get(itemId);
                dotProduct += r1 * r2;
            }
            norm1 += r1 * r1;
        }

        for (double r : ratings2.values()) {
            norm2 += r * r;
        }

        if (norm1 == 0 || norm2 == 0) return 0.0;

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public List<Item> getCollaborativeRecommendations(int targetUserId, int topK) {
        User targetUser = users.get(targetUserId);
        if (targetUser == null) return Collections.emptyList();

        // 1. Find K Nearest Neighbors (Users)
        // Using a Min-Heap to keep top N similar users
        final int NUM_NEIGHBORS = 10;
        PriorityQueue<UserSimilarity> pq = new PriorityQueue<>((a, b) -> Double.compare(a.similarity, b.similarity));

        for (User u : users.values()) {
            if (u.getId() == targetUserId) continue;
            double sim = calculateUserSimilarity(targetUser, u);
            if (sim > 0.01) { // Threshold
                 pq.offer(new UserSimilarity(u, sim));
                 if (pq.size() > NUM_NEIGHBORS) {
                     pq.poll(); // Remove lowest similarity
                 }
            }
        }
        
        List<UserSimilarity> neighbors = new ArrayList<>();
        while (!pq.isEmpty()) {
            neighbors.add(pq.poll());
        }

        // 2. Aggregate votes from neighbors
        Map<Integer, Double> candidateItems = new HashMap<>();
        Set<Integer> watchedItems = targetUser.getInteractions().keySet();

        for (UserSimilarity us : neighbors) {
            for (Map.Entry<Integer, Double> entry : us.user.getInteractions().entrySet()) {
                int itemId = entry.getKey();
                // Skip if already watched
                if (watchedItems.contains(itemId)) continue;

                double rating = entry.getValue();
                candidateItems.put(itemId, candidateItems.getOrDefault(itemId, 0.0) + (rating * us.similarity));
            }
        }

        // 3. Select Top-K Items
        // Use PriorityQueue again
        PriorityQueue<Map.Entry<Integer, Double>> itemPq = new PriorityQueue<>((a, b) -> Double.compare(a.getValue(), b.getValue()));
        
        for (Map.Entry<Integer, Double> entry : candidateItems.entrySet()) {
            itemPq.offer(entry);
            if (itemPq.size() > topK) {
                itemPq.poll();
            }
        }

        List<Item> recommendations = new ArrayList<>();
        while (!itemPq.isEmpty()) {
            Map.Entry<Integer, Double> best = itemPq.poll();
            recommendations.add(items.get(best.getKey()));
        }
        Collections.reverse(recommendations); // Highest first
        return recommendations;
    }
    
    // Helper class for Priority Queue
    private static class UserSimilarity {
        User user;
        double similarity;
        public UserSimilarity(User user, double similarity) {
            this.user = user;
            this.similarity = similarity;
        }
    }

    /**
     * Graph-based recommendation using BFS (Depth 2).
     * Finds items liked by users who liked items the target user liked.
     */
    public List<Item> getGraphRecommendations(int targetUserId, int topK) {
        // User -> Items -> Users -> Items
        Set<Integer> visitedUsers = new HashSet<>();
        Set<Integer> visitedItems = new HashSet<>();
        
        // Items the user has interacted with
        Set<Integer> userItems = graph.getUserNeighbors(targetUserId);
        visitedItems.addAll(userItems);

        Map<Integer, Integer> itemFrequency = new HashMap<>();

        for (int itemId : userItems) {
            // Level 1: Users who liked this item
            Set<Integer> otherUsers = graph.getItemNeighbors(itemId);
            for (int otherUserId : otherUsers) {
                if (otherUserId == targetUserId || visitedUsers.contains(otherUserId)) continue;
                visitedUsers.add(otherUserId);

                // Level 2: Items those users liked
                Set<Integer> candidateItems = graph.getUserNeighbors(otherUserId);
                for (int candidateId : candidateItems) {
                    if (visitedItems.contains(candidateId)) continue;
                    
                    itemFrequency.put(candidateId, itemFrequency.getOrDefault(candidateId, 0) + 1);
                }
            }
        }

        // Sort by frequency
        PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.getValue(), b.getValue()));
        for (Map.Entry<Integer, Integer> entry : itemFrequency.entrySet()) {
            pq.offer(entry);
            if (pq.size() > topK) {
                pq.poll();
            }
        }

        List<Item> recs = new ArrayList<>();
        while (!pq.isEmpty()) {
            recs.add(items.get(pq.poll().getKey()));
        }
        Collections.reverse(recs);
        return recs;
    }
    
    public void addInteraction(int userId, int itemId, double rating) {
        // Update User
        User u = users.get(userId);
        if (u != null) {
            u.addInteraction(itemId, rating);
        }
        
        // Update Item
        Item i = items.get(itemId);
        if (i != null) {
            i.addRating(rating);
        }
        
        // Update Graph
        graph.addEdge(userId, itemId);
        
        // Invalidate cache if implemented
    }
}
