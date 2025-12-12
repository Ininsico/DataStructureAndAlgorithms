package com.dsa.data;

import com.dsa.core.Graph;
import com.dsa.core.Item;
import com.dsa.core.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles loading data from files (MovieLens format).
 */
public class DataLoader {
    private Map<Integer, User> users;
    private Map<Integer, Item> items;
    private Graph graph;

    public DataLoader() {
        this.users = new HashMap<>();
        this.items = new HashMap<>();
        this.graph = new Graph();
    }

    public void loadData(String itemsFile, String ratingsFile) throws IOException {
        loadItems(itemsFile);
        loadRatings(ratingsFile);
    }

    private void loadItems(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skipping header if exists, usually MovieLens has no header in .item files in 100k, but let's assume CSV with header for simplicity or robust checks
            // Assuming format: itemId,title,genres
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                
                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    // Clean up name
                    name = name.replace("\"", "");
                    
                    Item item = new Item(id, name);
                    if (parts.length > 2) {
                        String[] genres = parts[2].split("\\|");
                        for (String g : genres) {
                            item.addCategory(g);
                        }
                    }
                    items.put(id, item);
                    graph.addItem(id);
                } catch (NumberFormatException e) {
                    // Skip header or malformed lines
                    continue;
                }
            }
        }
    }

    private void loadRatings(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Assuming format: userId,itemId,rating,timestamp
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                try {
                    int userId = Integer.parseInt(parts[0]);
                    int itemId = Integer.parseInt(parts[1]);
                    double rating = Double.parseDouble(parts[2]);

                    User user = users.computeIfAbsent(userId, k -> {
                        graph.addUser(k);
                        return new User(k, "User " + k);
                    });

                    // Add interaction to User model
                    user.addInteraction(itemId, rating);
                    
                    // Update Item stats
                    Item item = items.get(itemId);
                    if (item != null) {
                        item.addRating(rating);
                    }
                    
                    // Add edge to Graph
                    graph.addEdge(userId, itemId);

                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public Map<Integer, Item> getItems() {
        return items;
    }

    public Graph getGraph() {
        return graph;
    }
}
