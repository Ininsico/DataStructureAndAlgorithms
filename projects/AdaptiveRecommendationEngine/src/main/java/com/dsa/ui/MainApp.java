package com.dsa.ui;

import com.dsa.algorithms.RecommendationEngine;
import com.dsa.data.DataLoader;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private BorderPane root;
    private DataLoader dataLoader;
    private RecommendationEngine engine;
    
    // Panels
    private RecommendationsPanel recommendationsPanel;
    private BrowserPanel browserPanel;
    private SimulatorPanel simulatorPanel;
    private GraphPanel graphPanel;
    private MetricsPanel metricsPanel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize Data
        dataLoader = new DataLoader();
        // Generate dummy data if no files provided
        generateDummyData();
        
        engine = new RecommendationEngine(dataLoader.getUsers(), dataLoader.getItems(), dataLoader.getGraph());

        // UI Setup
        root = new BorderPane();
        
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);
        
        // Default View
        showDashboard();

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setTitle("Adaptive Recommendation Engine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateDummyData() {
        // Create 20 users and 50 items and random interactions
        System.out.println("Generating dummy data...");
        for (int i = 1; i <= 50; i++) {
            dataLoader.getGraph().addItem(i);
            com.dsa.core.Item item = new com.dsa.core.Item(i, "Movie " + i);
            item.addCategory(i % 2 == 0 ? "Action" : "Drama");
            dataLoader.getItems().put(i, item);
        }
        
        for (int u = 1; u <= 20; u++) {
            com.dsa.core.User user = new com.dsa.core.User(u, "User " + u);
            dataLoader.getUsers().put(u, user);
            dataLoader.getGraph().addUser(u);
            
            // Random interactions
            int numInteractions = 5 + (int)(Math.random() * 10);
            for (int k = 0; k < numInteractions; k++) {
                int itemId = 1 + (int)(Math.random() * 50);
                double rating = 1.0 + (int)(Math.random() * 5); // 1-5
                user.addInteraction(itemId, rating);
                dataLoader.getItems().get(itemId).addRating(rating);
                dataLoader.getGraph().addEdge(u, itemId);
            }
        }
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(220);

        Label title = new Label("RecoEngine");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 0 20 0;");

        Button btnDashboard = createNavButton("Dashboard");
        btnDashboard.setOnAction(e -> showDashboard());

        Button btnRecs = createNavButton("Recommendations");
        btnRecs.setOnAction(e -> showRecommendations());

        Button btnBrowser = createNavButton("Item Browser");
        btnBrowser.setOnAction(e -> showBrowser());
        
        Button btnSim = createNavButton("Simulator");
        btnSim.setOnAction(e -> showSimulator());

        Button btnGraph = createNavButton("Graph Viz");
        btnGraph.setOnAction(e -> showGraph());
        
        Button btnMetrics = createNavButton("Metrics");
        btnMetrics.setOnAction(e -> showMetrics());

        sidebar.getChildren().addAll(title, btnDashboard, btnRecs, btnBrowser, btnSim, btnGraph, btnMetrics);
        return sidebar;
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("sidebar-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private void showDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.getStyleClass().add("content-area");
        
        Label header = new Label("System Overview");
        header.getStyleClass().add("header-label");
        
        Label stats = new Label("Users: " + dataLoader.getUsers().size() + 
                              "\nItems: " + dataLoader.getItems().size() + 
                              "\nEdges: " + dataLoader.getGraph().getEdgeCount());
        stats.getStyleClass().add("card");
        stats.setMaxWidth(Double.MAX_VALUE);
        
        dashboard.getChildren().addAll(header, stats);
        root.setCenter(dashboard);
    }

    private void showRecommendations() {
        if (recommendationsPanel == null) {
            recommendationsPanel = new RecommendationsPanel(dataLoader, engine);
        }
        root.setCenter(recommendationsPanel.getView());
    }

    private void showBrowser() {
        if (browserPanel == null) {
            browserPanel = new BrowserPanel(dataLoader);
        }
        root.setCenter(browserPanel.getView());
    }
    
    private void showSimulator() {
        if (simulatorPanel == null) {
            simulatorPanel = new SimulatorPanel(dataLoader, engine);
        }
        root.setCenter(simulatorPanel.getView());
    }
    
    private void showGraph() {
        if (graphPanel == null) {
            graphPanel = new GraphPanel(dataLoader.getGraph());
        }
        root.setCenter(graphPanel.getView());
    }
    
    private void showMetrics() {
        if (metricsPanel == null) {
            metricsPanel = new MetricsPanel(engine);
        }
        root.setCenter(metricsPanel.getView());
    }
}
