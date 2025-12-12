package com.dsa.core;

import java.util.*;

/**
 * Bipartite Graph implementation using Adjacency List.
 * Stores Users and Items as nodes.
 */
public class Graph {
    // Adjacency list: Node ID -> List of Connected Node IDs
    // We differentiate User IDs and Item IDs. 
    // Since real IDs might overlap, we can map them internally or maintain separate maps.
    // For simplicity in this bipartite graph, let's store edges from User -> Items and Item -> Users.
    
    // User ID -> Set of Item IDs
    private Map<Integer, Set<Integer>> userAdj;
    // Item ID -> Set of User IDs
    private Map<Integer, Set<Integer>> itemAdj;

    public Graph() {
        this.userAdj = new HashMap<>();
        this.itemAdj = new HashMap<>();
    }

    public void addUser(int userId) {
        userAdj.putIfAbsent(userId, new HashSet<>());
    }

    public void addItem(int itemId) {
        itemAdj.putIfAbsent(itemId, new HashSet<>());
    }

    public void addEdge(int userId, int itemId) {
        addUser(userId);
        addItem(itemId);
        userAdj.get(userId).add(itemId);
        itemAdj.get(itemId).add(userId);
    }

    public Set<Integer> getUserNeighbors(int userId) {
        return userAdj.getOrDefault(userId, Collections.emptySet());
    }

    public Set<Integer> getItemNeighbors(int itemId) {
        return itemAdj.getOrDefault(itemId, Collections.emptySet());
    }
    
    public int getUserCount() {
        return userAdj.size();
    }
    
    public int getItemCount() {
        return itemAdj.size();
    }
    
    public int getEdgeCount() {
        // Since it's undirected/bipartite, total edges is sum of all list sizes / 2, or just sum of one side.
        int count = 0;
        for (Set<Integer> neighbors : userAdj.values()) {
            count += neighbors.size();
        }
        return count;
    }
}
