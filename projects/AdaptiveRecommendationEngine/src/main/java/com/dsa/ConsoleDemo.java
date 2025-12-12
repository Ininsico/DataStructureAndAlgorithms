package com.dsa;

import com.dsa.algorithms.RecommendationEngine;
import com.dsa.core.Item;
import com.dsa.core.User;
import com.dsa.data.DataLoader;
import java.util.List;

public class ConsoleDemo {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   Adaptive Recommendation Engine (CLI)   ");
        System.out.println("==========================================\n");

        // 1. Initialize System
        System.out.println("[INFO] initializing Data Loader and generating dummy data...");
        DataLoader dataLoader = new DataLoader();
        generateDummyData(dataLoader);
        System.out.println("[INFO] Data Loaded: " + dataLoader.getUsers().size() + " Users, " +
                dataLoader.getItems().size() + " Items, " +
                dataLoader.getGraph().getEdgeCount() + " Interactions.");

        // 2. Initialize Engine
        RecommendationEngine engine = new RecommendationEngine(
                dataLoader.getUsers(),
                dataLoader.getItems(),
                dataLoader.getGraph());

        // 3. Test Collaborative Filtering
        int targetUserId = 1;
        System.out.println("\n--- Test 1: Collaborative Filtering (User " + targetUserId + ") ---");
        long start = System.nanoTime();
        List<Item> collabRecs = engine.getCollaborativeRecommendations(targetUserId, 5);
        long end = System.nanoTime();

        System.out.println("Found " + collabRecs.size() + " recommendations in " + (end - start) / 1e6 + "ms:");
        for (Item item : collabRecs) {
            System.out.printf("   > %s (Avg Rating: %.2f)\n", item.getName(), item.getAverageRating());
        }

        // 4. Test Graph-Based BFS
        System.out.println("\n--- Test 2: Graph-Based BFS (User " + targetUserId + ") ---");
        start = System.nanoTime();
        List<Item> graphRecs = engine.getGraphRecommendations(targetUserId, 5);
        end = System.nanoTime();

        System.out.println("Found " + graphRecs.size() + " recommendations in " + (end - start) / 1e6 + "ms:");
        for (Item item : graphRecs) {
            System.out.printf("   > %s (Categories: %s)\n", item.getName(), item.getCategories());
        }

        // 5. Test Adaptive Learning
        System.out.println("\n--- Test 3: Simulating Interaction (Adaptive Learning) ---");
        // Let's have User 1 rate a new item high, which should influence
        // recommendations
        // Pick an item they haven't seen.
        int newItemId = 15; // Assuming random data didn't hit this, or just adding weight
        System.out.println("[ACTION] User " + targetUserId + " rates Item " + newItemId + " with 5.0 stars.");
        engine.addInteraction(targetUserId, newItemId, 5.0);

        System.out.println("[INFO] Graph updated. Re-running Graph Recommendations...");
        start = System.nanoTime();
        graphRecs = engine.getGraphRecommendations(targetUserId, 5);
        end = System.nanoTime();

        System.out.println("Found " + graphRecs.size() + " recommendations in " + (end - start) / 1e6 + "ms:");
        for (Item item : graphRecs) {
            System.out.printf("   > %s\n", item.getName());
        }

        System.out.println("\n==========================================");
        System.out.println("   DEMO COMPLETED SUCCESSFULLY");
        System.out.println("==========================================");
    }

    private static void generateDummyData(DataLoader dataLoader) {
        for (int i = 1; i <= 50; i++) {
            dataLoader.getGraph().addItem(i);
            com.dsa.core.Item item = new com.dsa.core.Item(i, "Movie #" + i);
            item.addCategory(i % 2 == 0 ? "Sci-Fi" : "Romance");
            dataLoader.getItems().put(i, item);
        }
        for (int u = 1; u <= 20; u++) {
            com.dsa.core.User user = new com.dsa.core.User(u, "User #" + u);
            dataLoader.getUsers().put(u, user);
            dataLoader.getGraph().addUser(u);
            int numInteractions = 5 + (int) (Math.random() * 10);
            for (int k = 0; k < numInteractions; k++) {
                int itemId = 1 + (int) (Math.random() * 50);
                double rating = 1.0 + (int) (Math.random() * 5);
                user.addInteraction(itemId, rating);
                dataLoader.getItems().get(itemId).addRating(rating);
                dataLoader.getGraph().addEdge(u, itemId);
            }
        }
    }
}
