package com.dsa.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a User in the recommendation system.
 * Holds profile information and interaction history.
 */
public class User {
    private int id;
    private String name;
    private Map<Integer, Double> interactions; // ItemID -> Rating (or strength)
    private Set<String> tags; // Preferences or tags derived from high-rated items

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.interactions = new HashMap<>();
        this.tags = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addInteraction(int itemId, double rating) {
        interactions.put(itemId, rating);
    }

    public Double getInteraction(int itemId) {
        return interactions.get(itemId);
    }

    public Map<Integer, Double> getInteractions() {
        return interactions;
    }
    
    public void addTag(String tag) {
        tags.add(tag);
    }

    public Set<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", interactions=" + interactions.size() +
                '}';
    }
}
