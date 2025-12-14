package com.dsa.algorithms;

import com.dsa.core.Track;
import java.util.*;
import java.util.stream.Collectors;

public class SpotifyRecommender {

    /**
     * "Made For You" - Recommendation Engine
     * Logic:
     * 1. If history exists, calculate user's "Vibe Vector" (Avg Energy, Avg
     * Valence).
     * 2. Find closest songs in vector space (Euclidean Distance).
     * 3. Boost score if Genre matches recently played genres.
     */
    public List<Track> getRecommendations(List<Track> history, List<Track> allTracks) {
        // If no history, return trending/random mix
        if (history.isEmpty()) {
            List<Track> random = new ArrayList<>(allTracks);
            Collections.shuffle(random);
            return random.subList(0, Math.min(6, random.size()));
        }

        // 1. User Profile
        double userEnergy = history.stream().mapToDouble(Track::getEnergy).average().orElse(0.5);
        double userValence = history.stream().mapToDouble(Track::getValence).average().orElse(0.5);

        Set<String> topGenres = history.stream()
                .map(Track::getGenre)
                .collect(Collectors.groupingBy(g -> g, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // Descending
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // 2. Score Items
        Map<Track, Double> scores = new HashMap<>();
        for (Track t : allTracks) {
            if (history.contains(t))
                continue; // Skip played

            // Vector Distance (Lower is better)
            double dist = Math
                    .sqrt(Math.pow(t.getEnergy() - userEnergy, 2) + Math.pow(t.getValence() - userValence, 2));

            // Genre Boost (Reduce distance if genre matches)
            if (topGenres.contains(t.getGenre())) {
                dist *= 0.5; // 50% closer
            }

            scores.put(t, dist);
        }

        // 3. Sort & Return
        return scores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(6)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * "Daily Mixes" - Clustering
     * Clusters songs into vibe-based playlists.
     */
    public Map<String, List<Track>> generateDailyMixes(List<Track> allTracks) {
        Map<String, List<Track>> mixes = new LinkedHashMap<>();

        // 1. Desi Hits (Hindi/Punjabi/Urdu)
        List<Track> desi = allTracks.stream()
                .filter(t -> t.getGenre().equals("Hindi") || t.getGenre().equals("Punjabi")
                        || t.getGenre().contains("Urdu"))
                .collect(Collectors.toList());
        Collections.shuffle(desi);
        mixes.put("Desi Mix", desi.subList(0, Math.min(6, desi.size())));

        // 2. K-Pop Essentials
        List<Track> kpop = allTracks.stream()
                .filter(t -> t.getGenre().equals("K-Pop"))
                .collect(Collectors.toList());
        mixes.put("K-Pop Essentials", kpop.subList(0, Math.min(6, kpop.size())));

        // 3. High Energy (Workout)
        List<Track> hype = allTracks.stream()
                .filter(t -> t.getEnergy() > 0.75)
                .collect(Collectors.toList());
        Collections.shuffle(hype);
        mixes.put("Hype Mode", hype.subList(0, Math.min(6, hype.size())));

        // 4. Chill Vibes
        List<Track> chill = allTracks.stream()
                .filter(t -> t.getEnergy() < 0.5 && t.getValence() < 0.5)
                .collect(Collectors.toList());
        mixes.put("Chill Station", chill.subList(0, Math.min(6, chill.size())));

        return mixes;
    }
}
