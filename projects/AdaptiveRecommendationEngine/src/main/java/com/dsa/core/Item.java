package com.dsa.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Item (e.g., Movie) in the system.
 */
public class Item {
    private int id;
    private String name;
    private Set<String> categories;
    private double sumRatings;
    private int countRatings;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
        this.categories = new HashSet<>();
        this.sumRatings = 0.0;
        this.countRatings = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void addRating(double rating) {
        sumRatings += rating;
        countRatings++;
    }

    public double getAverageRating() {
        if (countRatings == 0) return 0.0;
        return sumRatings / countRatings;
    }

    public int getPopularity() {
        return countRatings;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
