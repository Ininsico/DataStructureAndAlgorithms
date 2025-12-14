package com.dsa.ui;

import com.dsa.algorithms.SpotifyRecommender;
import com.dsa.core.Track;
import com.dsa.core.Trie;
import com.dsa.data.RealSpotifyService;
import com.dsa.data.ShazamService;
import com.dsa.data.SpotifyAuth;
import com.dsa.data.SpotifyPlayer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainApp extends Application {

    private RealSpotifyService spotifyService;
    private SpotifyRecommender recommender = new SpotifyRecommender();
    private Trie trackDb = new Trie(); // DSA: Fast search
    private ShazamService shazamService;
    private SpotifyPlayer spotifyPlayer;

    private List<Track> allTracks = new ArrayList<>();
    private List<Track> userHistory = new ArrayList<>();

    // UI Components
    private VBox mainContent;
    private TextField searchField;
    private VBox resultsBox;
    private Label statusLabel;
    private Button loginBtn;
    private Button shazamBtn;
    private VBox shazamResultBox;
    private HBox playbackControls;
    private Label nowPlayingLabel;
    private Label timerLabel;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0e27, #000000);");

        // === TOP BAR ===
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // === TAB PANE for Shazam and Recommendations ===
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");

        // Tab 1: Recommendations
        Tab recommendationsTab = new Tab("🎵 Recommendations");
        recommendationsTab.setClosable(false);

        VBox recContent = new VBox(20);
        recContent.setPadding(new Insets(30));
        recContent.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("🎵 Adaptive Recommendation Engine");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Powered by Spotify API + DSA Algorithms");
        subtitle.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");

        statusLabel = new Label("Connect to Spotify to get started");
        statusLabel.setStyle("-fx-text-fill: #1DB954; -fx-font-size: 16px; -fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Search for songs, artists...");
        searchField.setPrefWidth(500);
        searchField.setStyle(
                "-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
                        "-fx-prompt-text-fill: #666; -fx-font-size: 16px; " +
                        "-fx-padding: 15; -fx-background-radius: 25;");
        searchField.setDisable(true);
        searchField.setOnKeyReleased(e -> performSearch(searchField.getText()));

        resultsBox = new VBox(15);
        resultsBox.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(resultsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(600);

        recContent.getChildren().addAll(title, subtitle, statusLabel, searchField, scrollPane);
        recommendationsTab.setContent(recContent);

        // Tab 2: Shazam
        Tab shazamTab = new Tab("🎤 Identify Songs");
        shazamTab.setClosable(false);
        VBox shazamContent = createShazamSection();
        shazamTab.setContent(shazamContent);

        tabPane.getTabs().addAll(recommendationsTab, shazamTab);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Adaptive Recommendation Engine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15, 30, 15, 30));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: rgba(26, 26, 46, 0.8);");

        loginBtn = new Button("🔗 Connect Spotify");
        loginBtn.setStyle(
                "-fx-background-color: #1DB954; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; " +
                        "-fx-background-radius: 20; -fx-cursor: hand;");
        loginBtn.setOnAction(e -> connectToSpotify());

        topBar.getChildren().add(loginBtn);
        return topBar;
    }

    private void connectToSpotify() {
        loginBtn.setText("Connecting...");
        loginBtn.setDisable(true);
        statusLabel.setText("Authenticating with Spotify...");

        new Thread(() -> {
            try {
                // 1. Authenticate
                String token = SpotifyAuth.authenticate();
                spotifyService = new RealSpotifyService(token);

                // Initialize Shazam and Player services
                shazamService = new ShazamService(token);
                spotifyPlayer = new SpotifyPlayer(token);

                Platform.runLater(() -> {
                    loginBtn.setText("✓ Connected");
                    statusLabel.setText("Building music database from Spotify...");
                    shazamBtn.setDisable(false); // Enable Shazam button
                });

                // 2. Build comprehensive database
                allTracks = spotifyService.buildMusicDatabase();

                // 3. Build Trie index for fast search
                Platform.runLater(() -> statusLabel.setText("Indexing tracks for search..."));
                for (Track track : allTracks) {
                    trackDb.insert(track.getName().toLowerCase(), track);
                    trackDb.insert(track.getArtist().toLowerCase(), track);
                }

                // 4. Get user's listening history
                userHistory.addAll(spotifyService.getUserTopTracks("short_term", 50));

                Platform.runLater(() -> {
                    statusLabel.setText("✅ Ready! Database: " + allTracks.size() + " tracks");
                    searchField.setDisable(false);
                    showRecommendations();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    loginBtn.setText("❌ Failed");
                    loginBtn.setDisable(false);
                    statusLabel.setText("Connection failed: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void showRecommendations() {
        resultsBox.getChildren().clear();

        // Section: Made For You
        Label madeForYouLabel = new Label("🎯 Made For You");
        madeForYouLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        resultsBox.getChildren().add(madeForYouLabel);

        List<Track> recommendations = recommender.getRecommendations(userHistory, allTracks);
        HBox recBox = new HBox(15);
        recBox.setAlignment(Pos.CENTER);
        for (Track track : recommendations) {
            recBox.getChildren().add(createTrackCard(track));
        }
        resultsBox.getChildren().add(recBox);

        // Section: Daily Mixes
        Label mixesLabel = new Label("🎧 Daily Mixes");
        mixesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 20 0 0 0;");
        resultsBox.getChildren().add(mixesLabel);

        Map<String, List<Track>> mixes = recommender.generateDailyMixes(allTracks);
        for (Map.Entry<String, List<Track>> entry : mixes.entrySet()) {
            Label mixLabel = new Label("📀 " + entry.getKey());
            mixLabel.setStyle("-fx-text-fill: #1DB954; -fx-font-size: 18px; -fx-font-weight: bold;");
            resultsBox.getChildren().add(mixLabel);

            HBox mixBox = new HBox(15);
            mixBox.setAlignment(Pos.CENTER);
            for (Track track : entry.getValue()) {
                mixBox.getChildren().add(createTrackCard(track));
            }
            resultsBox.getChildren().add(mixBox);
        }
    }

    private void performSearch(String query) {
        if (query.trim().isEmpty()) {
            showRecommendations();
            return;
        }

        resultsBox.getChildren().clear();

        Label searchLabel = new Label("🔍 Search Results for: \"" + query + "\"");
        searchLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        resultsBox.getChildren().add(searchLabel);

        // Use Trie for fast prefix search
        List<Track> results = trackDb.search(query.toLowerCase());

        if (results.isEmpty()) {
            Label noResults = new Label("No results found");
            noResults.setStyle("-fx-text-fill: #888; -fx-font-size: 18px;");
            resultsBox.getChildren().add(noResults);
        } else {
            // Limit to 20 results
            results = results.subList(0, Math.min(20, results.size()));

            FlowPane flowPane = new FlowPane(15, 15);
            flowPane.setAlignment(Pos.CENTER);
            for (Track track : results) {
                flowPane.getChildren().add(createTrackCard(track));
            }
            resultsBox.getChildren().add(flowPane);
        }
    }

    private VBox createTrackCard(Track track) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(150, 200);
        card.setStyle(
                "-fx-background-color: #1a1a2e; -fx-background-radius: 10; " +
                        "-fx-padding: 15; -fx-cursor: hand;");
        card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.5)));

        // Album Art
        ImageView albumArt = new ImageView();
        try {
            if (track.getImageUrl() != null && !track.getImageUrl().isEmpty()) {
                albumArt.setImage(new Image(track.getImageUrl(), 120, 120, true, true));
            }
        } catch (Exception e) {
            // Use placeholder
        }
        albumArt.setFitWidth(120);
        albumArt.setFitHeight(120);

        // Track Name
        Label name = new Label(track.getName());
        name.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        name.setWrapText(true);
        name.setMaxWidth(130);

        // Artist
        Label artist = new Label(track.getArtist());
        artist.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");
        artist.setWrapText(true);
        artist.setMaxWidth(130);

        card.getChildren().addAll(albumArt, name, artist);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #2a2a4e; -fx-background-radius: 10; " +
                        "-fx-padding: 15; -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #1a1a2e; -fx-background-radius: 10; " +
                        "-fx-padding: 15; -fx-cursor: hand;"));

        // Click to PLAY on Spotify + add to history
        card.setOnMouseClicked(e -> {
            if (spotifyPlayer != null) {
                statusLabel.setText("▶️ Playing: " + track.getName());
                new Thread(() -> {
                    boolean success = spotifyPlayer.playTrack(track.getId());
                    Platform.runLater(() -> {
                        if (success) {
                            if (!userHistory.contains(track)) {
                                userHistory.add(track);
                            }
                            statusLabel.setText("🎵 Now Playing: " + track.getName() + " - " + track.getArtist());
                            // Refresh recommendations
                            if (searchField.getText().trim().isEmpty()) {
                                showRecommendations();
                            }
                        } else {
                            statusLabel.setText("❌ Playback failed. Open Spotify on your device first!");
                        }
                    });
                }).start();
            } else {
                statusLabel.setText("⚠️ Connect to Spotify first!");
            }
        });

        return card;
    }

    /**
     * Create Shazam section with big button to identify songs
     */
    private VBox createShazamSection() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox section = new VBox(20);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(40));
        section.setStyle("-fx-background-color: transparent;");

        Label shazamTitle = new Label("🎤 Identify Songs Playing Around You");
        shazamTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label shazamSubtitle = new Label("Play music from any source (phone, speaker, etc.) and click Listen");
        shazamSubtitle.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");
        shazamSubtitle.setWrapText(true);
        shazamSubtitle.setMaxWidth(600);
        shazamSubtitle.setAlignment(Pos.CENTER);

        // Timer Label (BIG and VISIBLE)
        timerLabel = new Label("");
        timerLabel.setStyle(
                "-fx-text-fill: #3b82f6; -fx-font-size: 48px; -fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(59, 130, 246, 0.8), 20, 0.7, 0, 0);");
        timerLabel.setVisible(false);

        shazamBtn = new Button("🎵 Listen");
        shazamBtn.setPrefSize(150, 150);
        shazamBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #3b82f6, #8b5cf6); " +
                        "-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 75; -fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(59, 130, 246, 0.6), 25, 0.5, 0, 0);");
        shazamBtn.setDisable(true);
        shazamBtn.setOnAction(e -> performShazam());

        // Result box
        shazamResultBox = new VBox(15);
        shazamResultBox.setAlignment(Pos.CENTER);
        shazamResultBox.setVisible(false);
        shazamResultBox.setStyle(
                "-fx-background-color: rgba(26, 26, 46, 0.8); " +
                        "-fx-background-radius: 15; -fx-padding: 20;");
        shazamResultBox.setMaxWidth(500);

        section.getChildren().addAll(shazamTitle, shazamSubtitle, timerLabel, shazamBtn, shazamResultBox);
        scrollPane.setContent(section);

        VBox wrapper = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        return wrapper;
    }

    /**
     * Perform Shazam-like audio recognition
     */
    private void performShazam() {
        shazamBtn.setDisable(true);
        shazamBtn.setText("🎤 Listening...");
        shazamResultBox.setVisible(false);
        timerLabel.setVisible(true);
        statusLabel.setText("🎤 Listening to audio for 10 seconds... Keep music playing!");

        // Create a timer to update UI every second
        final int[] secondsElapsed = { 0 };
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                secondsElapsed[0]++;
                int remaining = 10 - secondsElapsed[0];
                Platform.runLater(() -> {
                    if (remaining > 0) {
                        // Update BIG timer label
                        timerLabel.setText("⏱️ " + secondsElapsed[0] + "s / 10s");
                        statusLabel.setText(
                                "🎵 Recording... " + remaining + "s remaining");
                    } else {
                        timerLabel.setText("🔍 Processing...");
                        statusLabel.setText("🔍 Processing audio... Please wait...");
                    }
                });

                if (secondsElapsed[0] >= 10) {
                    timer.cancel();
                }
            }
        }, 1000, 1000); // Update every 1 second

        new Thread(() -> {
            try {
                Track identified = shazamService.identifySong();

                Platform.runLater(() -> {
                    timer.cancel(); // Stop timer
                    timerLabel.setVisible(false);
                    shazamBtn.setDisable(false);
                    shazamBtn.setText("🎵 Listen");

                    if (identified != null) {
                        showShazamResult(identified);
                        statusLabel.setText("✅ Found: " + identified.getName() + " - " + identified.getArtist());

                        // Add to database if not already there
                        if (!allTracks.contains(identified)) {
                            allTracks.add(identified);
                            trackDb.insert(identified.getName().toLowerCase(), identified);
                            trackDb.insert(identified.getArtist().toLowerCase(), identified);
                        }

                        // Add to history
                        if (!userHistory.contains(identified)) {
                            userHistory.add(identified);
                        }
                    } else {
                        statusLabel.setText("❌ Could not identify song. Make sure music is playing!");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    timer.cancel(); // Stop timer on error
                    timerLabel.setVisible(false);
                    shazamBtn.setDisable(false);
                    shazamBtn.setText("🎵 Listen");
                    statusLabel.setText("❌ Error: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Show Shazam result
     */
    private void showShazamResult(Track track) {
        shazamResultBox.getChildren().clear();

        ImageView albumArt = new ImageView();
        try {
            if (track.getImageUrl() != null && !track.getImageUrl().isEmpty()) {
                albumArt.setImage(new Image(track.getImageUrl(), 100, 100, true, true));
            }
        } catch (Exception e) {
            // Use placeholder
        }
        albumArt.setFitWidth(100);
        albumArt.setFitHeight(100);
        albumArt.setEffect(new DropShadow(15, Color.BLACK));

        Label trackName = new Label(track.getName());
        trackName.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label artistName = new Label(track.getArtist());
        artistName.setStyle("-fx-text-fill: #1DB954; -fx-font-size: 14px;");

        Button playBtn = new Button("▶️ Play on Spotify");
        playBtn.setStyle(
                "-fx-background-color: #1DB954; -fx-text-fill: white; " +
                        "-fx-font-size: 12px; -fx-padding: 8 15; -fx-background-radius: 15; -fx-cursor: hand;");
        playBtn.setOnAction(e -> {
            if (spotifyPlayer != null) {
                new Thread(() -> spotifyPlayer.playTrack(track.getId())).start();
                statusLabel.setText("▶️ Playing: " + track.getName());
            }
        });

        shazamResultBox.getChildren().addAll(albumArt, trackName, artistName, playBtn);
        shazamResultBox.setVisible(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
