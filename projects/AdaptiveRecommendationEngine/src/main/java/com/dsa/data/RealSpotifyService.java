package com.dsa.data;

import com.dsa.core.SpotifyConfig;
import com.dsa.core.Track;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REAL Spotify Service - Fetches data from Spotify's MASSIVE database
 * Not just "currently playing" - this builds a comprehensive music catalog
 */
public class RealSpotifyService {
    private HttpClient client;
    private String token;
    private Map<String, Track> trackCache = new HashMap<>();

    // Genre seeds available in Spotify
    private static final String[] GENRES = {
            "pop", "rock", "hip-hop", "electronic", "indie", "r-n-b",
            "latin", "k-pop", "edm", "country", "jazz", "classical",
            "metal", "punk", "reggae", "blues", "soul", "funk"
    };

    public RealSpotifyService(String token) {
        this.client = HttpClient.newHttpClient();
        this.token = token;
    }

    /**
     * Build a massive track database from Spotify
     * Fetches thousands of tracks across multiple genres and playlists
     */
    public List<Track> buildMusicDatabase() {
        System.out.println("🎵 Building comprehensive music database from Spotify...");
        List<Track> allTracks = new ArrayList<>();

        try {
            // 1. Get user's top tracks (personalization)
            allTracks.addAll(getUserTopTracks("short_term", 50));
            allTracks.addAll(getUserTopTracks("medium_term", 50));
            allTracks.addAll(getUserTopTracks("long_term", 50));

            // 2. Get user's saved tracks
            allTracks.addAll(getUserSavedTracks(100));

            // 3. Fetch tracks from featured playlists
            allTracks.addAll(getFeaturedPlaylistTracks(200));

            // 4. Get recommendations for each genre
            for (String genre : GENRES) {
                allTracks.addAll(getGenreRecommendations(genre, 30));
            }

            // 5. Get new releases
            allTracks.addAll(getNewReleases(100));

            // Remove duplicates
            allTracks = allTracks.stream()
                    .distinct()
                    .collect(Collectors.toList());

            // Fetch audio features for all tracks
            enrichWithAudioFeatures(allTracks);

            System.out.println("✅ Database built: " + allTracks.size() + " unique tracks!");

        } catch (Exception e) {
            System.err.println("Error building database: " + e.getMessage());
            e.printStackTrace();
        }

        return allTracks;
    }

    /**
     * Get user's top tracks from different time ranges
     */
    public List<Track> getUserTopTracks(String timeRange, int limit) {
        List<Track> tracks = new ArrayList<>();
        try {
            String url = SpotifyConfig.API_BASE + "/me/top/tracks?limit=" + limit + "&time_range=" + timeRange;
            String response = get(url);
            if (response != null) {
                tracks = parseTrackArray(JsonParser.parseString(response).getAsJsonObject().getAsJsonArray("items"));
            }
        } catch (Exception e) {
            System.err.println("Error fetching top tracks: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Get user's saved/liked tracks
     */
    public List<Track> getUserSavedTracks(int limit) {
        List<Track> tracks = new ArrayList<>();
        try {
            String url = SpotifyConfig.API_BASE + "/me/tracks?limit=" + Math.min(limit, 50);
            String response = get(url);
            if (response != null) {
                JsonArray items = JsonParser.parseString(response).getAsJsonObject().getAsJsonArray("items");
                for (JsonElement item : items) {
                    JsonObject trackObj = item.getAsJsonObject().getAsJsonObject("track");
                    tracks.add(parseTrack(trackObj));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching saved tracks: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Get tracks from Spotify's featured playlists
     */
    public List<Track> getFeaturedPlaylistTracks(int totalLimit) {
        List<Track> tracks = new ArrayList<>();
        try {
            // Get featured playlists
            String url = SpotifyConfig.API_BASE + "/browse/featured-playlists?limit=10";
            String response = get(url);
            if (response != null) {
                JsonArray playlists = JsonParser.parseString(response).getAsJsonObject()
                        .getAsJsonObject("playlists")
                        .getAsJsonArray("items");

                int perPlaylist = totalLimit / playlists.size();
                for (JsonElement playlist : playlists) {
                    String playlistId = playlist.getAsJsonObject().get("id").getAsString();
                    tracks.addAll(getPlaylistTracks(playlistId, perPlaylist));
                    if (tracks.size() >= totalLimit)
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching featured playlists: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Get tracks from a specific playlist
     */
    public List<Track> getPlaylistTracks(String playlistId, int limit) {
        List<Track> tracks = new ArrayList<>();
        try {
            String url = SpotifyConfig.API_BASE + "/playlists/" + playlistId + "/tracks?limit=" + Math.min(limit, 100);
            String response = get(url);
            if (response != null) {
                JsonArray items = JsonParser.parseString(response).getAsJsonObject().getAsJsonArray("items");
                for (JsonElement item : items) {
                    JsonObject trackObj = item.getAsJsonObject().getAsJsonObject("track");
                    if (trackObj != null && !trackObj.isJsonNull()) {
                        tracks.add(parseTrack(trackObj));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching playlist tracks: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Get recommendations for a specific genre
     */
    public List<Track> getGenreRecommendations(String genre, int limit) {
        List<Track> tracks = new ArrayList<>();
        try {
            String url = SpotifyConfig.API_BASE + "/recommendations?limit=" + limit +
                    "&seed_genres=" + URLEncoder.encode(genre, StandardCharsets.UTF_8);
            String response = get(url);
            if (response != null) {
                tracks = parseTrackArray(JsonParser.parseString(response).getAsJsonObject().getAsJsonArray("tracks"));
            }
        } catch (Exception e) {
            System.err.println("Error fetching genre recommendations: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Get new releases
     */
    public List<Track> getNewReleases(int limit) {
        List<Track> tracks = new ArrayList<>();
        try {
            String url = SpotifyConfig.API_BASE + "/browse/new-releases?limit=" + Math.min(limit, 50);
            String response = get(url);
            if (response != null) {
                JsonArray albums = JsonParser.parseString(response).getAsJsonObject()
                        .getAsJsonObject("albums")
                        .getAsJsonArray("items");

                for (JsonElement album : albums) {
                    String albumId = album.getAsJsonObject().get("id").getAsString();
                    tracks.addAll(getAlbumTracks(albumId));
                    if (tracks.size() >= limit)
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching new releases: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Get tracks from an album
     */
    public List<Track> getAlbumTracks(String albumId) {
        List<Track> tracks = new ArrayList<>();
        try {
            String url = SpotifyConfig.API_BASE + "/albums/" + albumId + "/tracks";
            String response = get(url);
            if (response != null) {
                tracks = parseTrackArray(JsonParser.parseString(response).getAsJsonObject().getAsJsonArray("items"));
            }
        } catch (Exception e) {
            System.err.println("Error fetching album tracks: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Search for tracks by query
     */
    public List<Track> searchTracks(String query, int limit) {
        List<Track> tracks = new ArrayList<>();
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = SpotifyConfig.API_BASE + "/search?q=" + encodedQuery +
                    "&type=track&limit=" + Math.min(limit, 50);
            String response = get(url);
            if (response != null) {
                JsonArray items = JsonParser.parseString(response).getAsJsonObject()
                        .getAsJsonObject("tracks")
                        .getAsJsonArray("items");
                tracks = parseTrackArray(items);
            }
        } catch (Exception e) {
            System.err.println("Error searching tracks: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Get advanced recommendations based on seeds and audio features
     */
    public List<Track> getAdvancedRecommendations(List<Track> seedTracks, Map<String, Double> targetFeatures,
            int limit) {
        List<Track> tracks = new ArrayList<>();
        try {
            StringBuilder url = new StringBuilder(SpotifyConfig.API_BASE + "/recommendations?limit=" + limit);

            // Add seed tracks (max 5)
            if (!seedTracks.isEmpty()) {
                String seeds = seedTracks.stream()
                        .limit(5)
                        .map(Track::getId)
                        .collect(Collectors.joining(","));
                url.append("&seed_tracks=").append(seeds);
            }

            // Add target audio features
            if (targetFeatures != null) {
                for (Map.Entry<String, Double> entry : targetFeatures.entrySet()) {
                    url.append("&target_").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }

            String response = get(url.toString());
            if (response != null) {
                tracks = parseTrackArray(JsonParser.parseString(response).getAsJsonObject().getAsJsonArray("tracks"));
            }
        } catch (Exception e) {
            System.err.println("Error fetching advanced recommendations: " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Enrich tracks with audio features (energy, valence, tempo, etc.)
     */
    private void enrichWithAudioFeatures(List<Track> tracks) {
        try {
            // Process in batches of 100 (Spotify API limit)
            for (int i = 0; i < tracks.size(); i += 100) {
                int end = Math.min(i + 100, tracks.size());
                List<Track> batch = tracks.subList(i, end);

                String ids = batch.stream()
                        .map(Track::getId)
                        .collect(Collectors.joining(","));

                String url = SpotifyConfig.API_BASE + "/audio-features?ids=" + ids;
                String response = get(url);

                if (response != null) {
                    JsonArray features = JsonParser.parseString(response).getAsJsonObject()
                            .getAsJsonArray("audio_features");

                    for (int j = 0; j < features.size() && j < batch.size(); j++) {
                        JsonElement featureElement = features.get(j);
                        if (!featureElement.isJsonNull()) {
                            JsonObject feature = featureElement.getAsJsonObject();
                            Track track = batch.get(j);

                            double energy = feature.get("energy").getAsDouble();
                            double valence = feature.get("valence").getAsDouble();

                            // Update track with audio features
                            track.setEnergy(energy);
                            track.setValence(valence);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error enriching with audio features: " + e.getMessage());
        }
    }

    /**
     * Parse a JSON array of tracks
     */
    private List<Track> parseTrackArray(JsonArray items) {
        List<Track> tracks = new ArrayList<>();
        for (JsonElement item : items) {
            try {
                tracks.add(parseTrack(item.getAsJsonObject()));
            } catch (Exception e) {
                // Skip invalid tracks
            }
        }
        return tracks;
    }

    /**
     * Parse a single track from JSON
     */
    private Track parseTrack(JsonObject obj) {
        String id = obj.get("id").getAsString();
        String name = obj.get("name").getAsString();

        String artist = "Unknown";
        if (obj.has("artists") && obj.getAsJsonArray("artists").size() > 0) {
            artist = obj.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
        }

        String img = "";
        if (obj.has("album")) {
            JsonObject album = obj.getAsJsonObject("album");
            if (album.has("images") && album.getAsJsonArray("images").size() > 0) {
                img = album.getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
            }
        }

        // Genre will be updated later or set to default
        return new Track(id, name, artist, "Unknown", 0.5, 0.5, img);
    }

    /**
     * HTTP GET request
     */
    private String get(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return null;
        }

        if (response.statusCode() != 200) {
            System.err.println("API Error " + response.statusCode() + ": " + response.body());
            return null;
        }
        return response.body();
    }
}
