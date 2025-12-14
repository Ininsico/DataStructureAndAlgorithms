package com.dsa.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom Trie (Prefix Tree) implementation for efficient O(L) text search.
 * Supports prefix search to find all tracks matching a query.
 */
public class Trie {
    private class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
        Track data; // Store the Track object at the leaf
    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String title, Track track) {
        TrieNode current = root;
        String text = title.toLowerCase();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
        current.data = track;
    }

    /**
     * Search for all tracks that match the given prefix
     */
    public List<Track> search(String prefix) {
        List<Track> results = new ArrayList<>();
        TrieNode current = root;
        String text = prefix.toLowerCase();

        // Navigate to the prefix node
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return results; // No matches
            }
            current = node;
        }

        // Collect all tracks under this prefix
        collectAllTracks(current, results);
        return results;
    }

    /**
     * Recursively collect all tracks from this node downwards
     */
    private void collectAllTracks(TrieNode node, List<Track> results) {
        if (node == null)
            return;

        if (node.isEndOfWord && node.data != null) {
            results.add(node.data);
        }

        for (TrieNode child : node.children.values()) {
            collectAllTracks(child, results);
        }
    }
}
