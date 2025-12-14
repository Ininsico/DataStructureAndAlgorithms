package com.dsa.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Spotify Playback Controller
 * Controls Spotify player - play, pause, skip, volume, etc.
 */
public class SpotifyPlayer {
    private String token;
    private HttpClient client;
    private static final String API_BASE = "https://api.spotify.com/v1";

    public SpotifyPlayer(String token) {
        this.token = token;
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Play a specific track on Spotify
     */
    public boolean playTrack(String trackId) {
        try {
            String url = API_BASE + "/me/player/play";
            String body = "{\"uris\":[\"spotify:track:" + trackId + "\"]}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                System.out.println("▶️ Playing track: " + trackId);
                return true;
            } else if (response.statusCode() == 404) {
                System.err.println("❌ No active Spotify device found. Please open Spotify on your phone/computer.");
                return false;
            } else {
                System.err.println("Playback error: " + response.statusCode() + " - " + response.body());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error playing track: " + e.getMessage());
            return false;
        }
    }

    /**
     * Pause playback
     */
    public boolean pause() {
        try {
            String url = API_BASE + "/me/player/pause";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error pausing: " + e.getMessage());
            return false;
        }
    }

    /**
     * Resume playback
     */
    public boolean resume() {
        try {
            String url = API_BASE + "/me/player/play";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error resuming: " + e.getMessage());
            return false;
        }
    }

    /**
     * Skip to next track
     */
    public boolean next() {
        try {
            String url = API_BASE + "/me/player/next";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error skipping: " + e.getMessage());
            return false;
        }
    }

    /**
     * Go to previous track
     */
    public boolean previous() {
        try {
            String url = API_BASE + "/me/player/previous";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error going back: " + e.getMessage());
            return false;
        }
    }

    /**
     * Set volume (0-100)
     */
    public boolean setVolume(int volume) {
        try {
            volume = Math.max(0, Math.min(100, volume)); // Clamp to 0-100
            String url = API_BASE + "/me/player/volume?volume_percent=" + volume;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error setting volume: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get current playback state
     */
    public PlaybackState getPlaybackState() {
        try {
            String url = API_BASE + "/me/player";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.body().isEmpty()) {
                return new PlaybackState(false, null, 0, 0);
            }

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            boolean isPlaying = json.get("is_playing").getAsBoolean();
            int progress = json.get("progress_ms").getAsInt();

            String trackName = null;
            int duration = 0;
            if (json.has("item") && !json.get("item").isJsonNull()) {
                JsonObject item = json.getAsJsonObject("item");
                trackName = item.get("name").getAsString();
                duration = item.get("duration_ms").getAsInt();
            }

            return new PlaybackState(isPlaying, trackName, progress, duration);
        } catch (Exception e) {
            System.err.println("Error getting playback state: " + e.getMessage());
            return new PlaybackState(false, null, 0, 0);
        }
    }

    /**
     * Add track to queue
     */
    public boolean addToQueue(String trackId) {
        try {
            String url = API_BASE + "/me/player/queue?uri=spotify:track:" + trackId;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error adding to queue: " + e.getMessage());
            return false;
        }
    }

    /**
     * Playback state data class
     */
    public static class PlaybackState {
        public final boolean isPlaying;
        public final String trackName;
        public final int progressMs;
        public final int durationMs;

        public PlaybackState(boolean isPlaying, String trackName, int progressMs, int durationMs) {
            this.isPlaying = isPlaying;
            this.trackName = trackName;
            this.progressMs = progressMs;
            this.durationMs = durationMs;
        }

        public String getProgressString() {
            int progressSec = progressMs / 1000;
            int durationSec = durationMs / 1000;
            return String.format("%d:%02d / %d:%02d",
                    progressSec / 60, progressSec % 60,
                    durationSec / 60, durationSec % 60);
        }
    }
}
