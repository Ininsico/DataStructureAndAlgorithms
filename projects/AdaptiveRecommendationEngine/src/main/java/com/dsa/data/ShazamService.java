package com.dsa.data;

import com.dsa.core.Track;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Shazam-like audio recognition service
 * Records audio from microphone and identifies the song using ACRCloud API
 */
public class ShazamService {
    private static final String ACRCLOUD_HOST = "identify-eu-west-1.acrcloud.com";
    private static final String ACRCLOUD_ACCESS_KEY = "e582fe76e4a4c02117e4056c10da736c";
    private static final String ACRCLOUD_SECRET_KEY = "SXe3gZz5SQZp1GrBQFTv1sVYDIOpWWOEVxeb2POV";

    private String spotifyToken;
    private HttpClient client;

    public ShazamService(String spotifyToken) {
        this.spotifyToken = spotifyToken;
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Record audio from microphone for 10 seconds and identify the song
     */
    public Track identifySong() throws Exception {
        System.out.println("🎤 Listening to audio...");

        // Record 10 seconds of audio (increased for better accuracy)
        byte[] audioData = recordAudio(10);

        System.out.println("🔍 Identifying song...");

        // Use Audd.io (FREE and RELIABLE!)
        System.out.println("📡 Using Audd.io for recognition (FREE, no API key needed)...");
        Track track = identifyWithAuddio(audioData);
        if (track != null) {
            return track;
        }

        // Fallback: Use Spotify's currently playing
        System.out.println("⚠️ Audd.io couldn't identify the song.");
        System.out.println("📻 Using Spotify fallback: Checking what YOU are currently playing...");
        System.out.println("💡 TIP: Play a song on YOUR Spotify and try again!");
        return getCurrentlyPlayingFromSpotify();
    }

    /**
     * Identify song using Audd.io API (FREE and simple!)
     */
    private Track identifyWithAuddio(byte[] audioData) {
        try {
            System.out.println("📡 Sending audio to Audd.io...");

            // Audd.io API token (300 requests/day for FREE!)
            String apiToken = "aac5880379bddead6ab9ad2cf2097d11";

            // Audd.io uses simple multipart upload
            String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Add API token
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write("Content-Disposition: form-data; name=\"api_token\"\r\n\r\n".getBytes());
            outputStream.write((apiToken + "\r\n").getBytes());

            // Add audio file
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write("Content-Disposition: form-data; name=\"file\"; filename=\"audio.wav\"\r\n".getBytes());
            outputStream.write("Content-Type: audio/wav\r\n\r\n".getBytes());
            outputStream.write(audioData);
            outputStream.write("\r\n".getBytes());

            // Add return parameter (get Spotify data)
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write("Content-Disposition: form-data; name=\"return\"\r\n\r\n".getBytes());
            outputStream.write("spotify\r\n".getBytes());

            // End boundary
            outputStream.write(("--" + boundary + "--\r\n").getBytes());

            byte[] requestBody = outputStream.toByteArray();

            // Send HTTP request to Audd.io (FREE - no API key needed!)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.audd.io/"))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                    .timeout(java.time.Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Audd.io Response: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                String status = json.get("status").getAsString();

                if (status.equals("success") && json.has("result") && !json.get("result").isJsonNull()) {
                    JsonObject result = json.getAsJsonObject("result");

                    String title = result.get("title").getAsString();
                    String artist = result.get("artist").getAsString();
                    String album = result.has("album") ? result.get("album").getAsString() : "Unknown Album";
                    String label = result.has("label") ? result.get("label").getAsString() : "";
                    String timecode = result.has("timecode") ? result.get("timecode").getAsString() : "";
                    String songLink = result.has("song_link") ? result.get("song_link").getAsString() : "";
                    String releaseDate = result.has("release_date") ? result.get("release_date").getAsString() : "";

                    System.out.println("✅ Audd.io identified: " + title + " - " + artist);
                    System.out.println("   📀 Album: " + album);
                    System.out.println("   🏷️  Label: " + label);
                    System.out.println("   ⏱️  Timecode: " + timecode);
                    System.out.println("   🔗 Link: " + songLink);
                    System.out.println("   📅 Release: " + releaseDate);

                    // Use placeholder image or Spotify data if available
                    String imageUrl = "https://via.placeholder.com/300x300?text=" +
                            java.net.URLEncoder.encode(title, StandardCharsets.UTF_8);

                    if (result.has("spotify") && !result.get("spotify").isJsonNull()) {
                        JsonObject spotify = result.getAsJsonObject("spotify");
                        if (spotify.has("album") && spotify.getAsJsonObject("album").has("images")) {
                            JsonArray images = spotify.getAsJsonObject("album").getAsJsonArray("images");
                            if (images.size() > 0) {
                                imageUrl = images.get(0).getAsJsonObject().get("url").getAsString();
                            }
                        }
                    }

                    // Return track with Audd.io data (NO Spotify search!)
                    return new Track("audd-" + System.currentTimeMillis(), title, artist, album, 0.5, 0.5, imageUrl);
                } else {
                    System.out.println("⚠️ Audd.io: No result found (song not in database or audio unclear)");
                }
            }

            return null;
        } catch (Exception e) {
            System.err.println("Audd.io error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Record audio from default microphone
     */
    private byte[] recordAudio(int seconds) throws Exception {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false); // Stereo for better quality

        // Try to find Stereo Mix / What U Hear / Loopback first
        TargetDataLine line = null;
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        System.out.println("🔍 Looking for audio input devices...");

        // First, try to find Stereo Mix or similar
        for (Mixer.Info mixerInfo : mixers) {
            String mixerName = mixerInfo.getName().toLowerCase();
            System.out.println("  Found: " + mixerInfo.getName());

            if (mixerName.contains("stereo mix") ||
                    mixerName.contains("wave out") ||
                    mixerName.contains("what u hear") ||
                    mixerName.contains("loopback")) {

                try {
                    Mixer mixer = AudioSystem.getMixer(mixerInfo);
                    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                    line = (TargetDataLine) mixer.getLine(info);
                    System.out.println("✅ Using: " + mixerInfo.getName() + " (system audio)");
                    break;
                } catch (Exception e) {
                    // This mixer doesn't support our format, try next
                }
            }
        }

        // If no Stereo Mix found, use default microphone
        if (line == null) {
            System.out.println("⚠️ Stereo Mix not found, using default microphone");
            System.out.println("💡 TIP: Enable 'Stereo Mix' in Windows Sound settings for better quality!");
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                throw new Exception("No audio input available");
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
        }

        line.open(format);
        line.start();

        System.out.println("🎤 Recording started... Play music now!");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        long startTime = System.currentTimeMillis();
        long duration = seconds * 1000;
        int lastSecond = 0;

        while (System.currentTimeMillis() - startTime < duration) {
            bytesRead = line.read(buffer, 0, buffer.length);
            out.write(buffer, 0, bytesRead);

            // Show timer every second
            int currentSecond = (int) ((System.currentTimeMillis() - startTime) / 1000);
            if (currentSecond > lastSecond) {
                lastSecond = currentSecond;
                int remaining = seconds - currentSecond;
                System.out.println(
                        "⏱️  Recording... " + currentSecond + "s / " + seconds + "s (" + remaining + "s remaining)");
            }
        }

        line.stop();
        line.close();

        System.out.println("🎤 Recording finished! Captured " + out.size() + " bytes");

        // Convert raw PCM to WAV format
        byte[] pcmData = out.toByteArray();
        byte[] wavData = createWavFile(pcmData, format);

        // DEBUG: Save to file to check what we're recording
        try {
            java.nio.file.Files.write(
                    java.nio.file.Paths.get("recorded_audio.wav"),
                    wavData);
            System.out.println("💾 Saved audio to: recorded_audio.wav (check this file!)");
        } catch (Exception e) {
            System.err.println("Could not save debug file: " + e.getMessage());
        }

        return wavData;
    }

    /**
     * Create WAV file from raw PCM data
     */
    private byte[] createWavFile(byte[] pcmData, AudioFormat format) throws IOException {
        ByteArrayOutputStream wavOut = new ByteArrayOutputStream();

        int sampleRate = (int) format.getSampleRate();
        int channels = format.getChannels();
        int bitsPerSample = format.getSampleSizeInBits();
        int byteRate = sampleRate * channels * bitsPerSample / 8;
        int blockAlign = channels * bitsPerSample / 8;

        // Write WAV header
        wavOut.write("RIFF".getBytes());
        writeInt(wavOut, 36 + pcmData.length); // File size - 8
        wavOut.write("WAVE".getBytes());

        // Write fmt chunk
        wavOut.write("fmt ".getBytes());
        writeInt(wavOut, 16); // Chunk size
        writeShort(wavOut, 1); // Audio format (1 = PCM)
        writeShort(wavOut, channels);
        writeInt(wavOut, sampleRate);
        writeInt(wavOut, byteRate);
        writeShort(wavOut, blockAlign);
        writeShort(wavOut, bitsPerSample);

        // Write data chunk
        wavOut.write("data".getBytes());
        writeInt(wavOut, pcmData.length);
        wavOut.write(pcmData);

        return wavOut.toByteArray();
    }

    private void writeInt(ByteArrayOutputStream out, int value) throws IOException {
        out.write(value & 0xFF);
        out.write((value >> 8) & 0xFF);
        out.write((value >> 16) & 0xFF);
        out.write((value >> 24) & 0xFF);
    }

    private void writeShort(ByteArrayOutputStream out, int value) throws IOException {
        out.write(value & 0xFF);
        out.write((value >> 8) & 0xFF);
    }

    /**
     * Identify song using ACRCloud API
     */
    private Track identifyWithACRCloud(byte[] audioData) {
        // Try different ACRCloud hosts (different regions)
        // User's account is on Asia Pacific, so try that first
        String[] hosts = {
                "identify-ap-southeast-1.acrcloud.com", // Asia Pacific (USER'S REGION)
                "identify-eu-west-1.acrcloud.com", // Europe
                "identify-us-west-2.acrcloud.com" // USA
        };

        for (String host : hosts) {
            try {
                System.out.println("🔍 Trying ACRCloud host: " + host);
                Track result = tryACRCloudHost(host, audioData);
                if (result != null) {
                    System.out.println("✅ Successfully identified using: " + host);
                    return result;
                }
            } catch (Exception e) {
                System.err.println("❌ Failed with " + host + ": " + e.getMessage());
            }
        }

        System.out.println("⚠️ All ACRCloud hosts failed. Using Spotify fallback.");
        return null;
    }

    /**
     * Try identifying with a specific ACRCloud host
     */
    private Track tryACRCloudHost(String host, byte[] audioData) throws Exception {
        // ACRCloud requires HMAC-SHA1 signature
        String httpMethod = "POST";
        String httpUri = "/v1/identify";
        String dataType = "audio";
        String signatureVersion = "1";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        // Create signature string
        String stringToSign = httpMethod + "\n" + httpUri + "\n" + ACRCLOUD_ACCESS_KEY + "\n" +
                dataType + "\n" + signatureVersion + "\n" + timestamp;

        // Generate HMAC-SHA1 signature
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(
                ACRCLOUD_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        mac.init(secretKey);
        byte[] signatureBytes = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getEncoder().encodeToString(signatureBytes);

        // Create multipart form data
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Add form fields
        addFormField(outputStream, boundary, "access_key", ACRCLOUD_ACCESS_KEY);
        addFormField(outputStream, boundary, "data_type", dataType);
        addFormField(outputStream, boundary, "signature_version", signatureVersion);
        addFormField(outputStream, boundary, "signature", signature);
        addFormField(outputStream, boundary, "sample_bytes", String.valueOf(audioData.length));
        addFormField(outputStream, boundary, "timestamp", timestamp);

        // Add audio file
        addFileField(outputStream, boundary, "sample", "audio.wav", audioData);

        // End boundary
        outputStream.write(("--" + boundary + "--\r\n").getBytes());

        byte[] requestBody = outputStream.toByteArray();

        // Send HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + host + httpUri))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("ACRCloud Response (" + host + "): " + response.statusCode());
        System.out.println("Response body: " + response.body());

        if (response.statusCode() == 200) {
            // Parse ACRCloud response
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject status = json.getAsJsonObject("status");

            int statusCode = status.get("code").getAsInt();
            String statusMsg = status.get("msg").getAsString();

            System.out.println("ACRCloud Status: " + statusCode + " - " + statusMsg);

            if (statusCode == 0) {
                // Success - song identified
                JsonObject metadata = json.getAsJsonObject("metadata");
                if (metadata.has("music") && metadata.getAsJsonArray("music").size() > 0) {
                    JsonObject music = metadata.getAsJsonArray("music").get(0).getAsJsonObject();

                    String title = music.get("title").getAsString();
                    String artist = music.getAsJsonArray("artists").get(0)
                            .getAsJsonObject().get("name").getAsString();

                    System.out.println("✅ ACRCloud identified: " + title + " - " + artist);

                    // Search Spotify for this song to get full details
                    return searchSpotify(title, artist);
                } else {
                    System.out.println("⚠️ No music found in metadata");
                }
            } else if (statusCode == 1001) {
                throw new Exception("Missing/Invalid Access Key");
            } else if (statusCode == 3001) {
                System.out.println("⚠️ No result found - song not in database or audio unclear");
            } else {
                throw new Exception("ACRCloud error: " + statusMsg);
            }
        } else {
            throw new Exception("HTTP " + response.statusCode() + ": " + response.body());
        }

        return null;
    }

    /**
     * Add a form field to multipart request
     */
    private void addFormField(ByteArrayOutputStream out, String boundary, String name, String value)
            throws IOException {
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n").getBytes());
        out.write((value + "\r\n").getBytes());
    }

    /**
     * Add a file field to multipart request
     */
    private void addFileField(ByteArrayOutputStream out, String boundary, String name, String filename, byte[] data)
            throws IOException {
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n")
                .getBytes());
        out.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());
        out.write(data);
        out.write("\r\n".getBytes());
    }

    /**
     * Fallback: Get currently playing song from Spotify
     */
    private Track getCurrentlyPlayingFromSpotify() {
        try {
            String url = "https://api.spotify.com/v1/me/player/currently-playing";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + spotifyToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.body().isEmpty()) {
                return null;
            }

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            if (!json.has("item"))
                return null;

            JsonObject item = json.getAsJsonObject("item");
            String id = item.get("id").getAsString();
            String name = item.get("name").getAsString();
            String artist = item.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
            String img = item.getAsJsonObject("album").getAsJsonArray("images").get(0).getAsJsonObject().get("url")
                    .getAsString();

            return new Track(id, name, artist, "Identified", 0.5, 0.5, img);
        } catch (Exception e) {
            System.err.println("Error getting currently playing: " + e.getMessage());
            return null;
        }
    }

    /**
     * Search Spotify for a song by name and artist (with better accuracy)
     */
    public Track searchSpotify(String songName, String artistName) {
        try {
            // Use Spotify's advanced search syntax for better accuracy
            String query = "track:\"" + songName + "\" artist:\"" + artistName + "\"";
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://api.spotify.com/v1/search?q=" + encodedQuery + "&type=track&limit=5";

            System.out.println("🔍 Searching Spotify for: " + songName + " - " + artistName);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + spotifyToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray items = json.getAsJsonObject("tracks").getAsJsonArray("items");

            if (items.size() == 0) {
                System.out.println("⚠️ No Spotify results found for: " + songName);
                return null;
            }

            // Find best match (check if song name and artist match closely)
            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                String spotifyName = item.get("name").getAsString().toLowerCase();
                String spotifyArtist = item.getAsJsonArray("artists").get(0).getAsJsonObject()
                        .get("name").getAsString().toLowerCase();

                // Check if it's a close match
                if (spotifyName.contains(songName.toLowerCase()) || songName.toLowerCase().contains(spotifyName)) {
                    String id = item.get("id").getAsString();
                    String name = item.get("name").getAsString();
                    String artist = item.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
                    String img = item.getAsJsonObject("album").getAsJsonArray("images").get(0).getAsJsonObject()
                            .get("url").getAsString();

                    System.out.println("✅ Found on Spotify: " + name + " - " + artist);
                    return new Track(id, name, artist, "Identified", 0.5, 0.5, img);
                }
            }

            // If no close match, return first result
            JsonObject item = items.get(0).getAsJsonObject();
            String id = item.get("id").getAsString();
            String name = item.get("name").getAsString();
            String artist = item.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
            String img = item.getAsJsonObject("album").getAsJsonArray("images").get(0).getAsJsonObject().get("url")
                    .getAsString();

            System.out.println("⚠️ Best match: " + name + " - " + artist);
            return new Track(id, name, artist, "Search Result", 0.5, 0.5, img);
        } catch (Exception e) {
            System.err.println("Search error: " + e.getMessage());
            return null;
        }
    }
}
