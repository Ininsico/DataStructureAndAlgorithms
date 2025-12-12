package com.dsa.ui;

import com.dsa.algorithms.RecommendationEngine;
import com.dsa.core.Item;
import com.dsa.core.User;
import com.dsa.data.DataLoader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class RecommendationsPanel {
    private VBox view;
    private DataLoader dataLoader;
    private RecommendationEngine engine;
    
    private ComboBox<User> userCombo;
    private TableView<Item> recoTable;
    private Label statusLabel;

    public RecommendationsPanel(DataLoader dataLoader, RecommendationEngine engine) {
        this.dataLoader = dataLoader;
        this.engine = engine;
        initView();
    }

    private void initView() {
        view = new VBox(20);
        view.getStyleClass().add("content-area");
        
        Label header = new Label("User Recommendations");
        header.getStyleClass().add("header-label");

        // Controls
        HBox controls = new HBox(15);
        controls.setPadding(new Insets(10));
        controls.getStyleClass().add("card");
        
        Label lblUser = new Label("Select User:");
        userCombo = new ComboBox<>();
        userCombo.setItems(FXCollections.observableArrayList(dataLoader.getUsers().values()));
        userCombo.setConverter(new javafx.util.StringConverter<User>() {
            @Override
            public String toString(User object) {
                return object == null ? "" : object.getName() + " (ID:" + object.getId() + ")";
            }

            @Override
            public User fromString(String string) {
                return null; // Not needed
            }
        });
        
        Button btnCollab = new Button("Collaborative Filtering");
        btnCollab.getStyleClass().add("action-button");
        btnCollab.setOnAction(e -> generateRecs(true));

        Button btnGraph = new Button("Graph-Based (BFS)");
        btnGraph.getStyleClass().add("secondary-button");
        btnGraph.setOnAction(e -> generateRecs(false));
        
        controls.getChildren().addAll(lblUser, userCombo, btnCollab, btnGraph);

        // Table
        recoTable = new TableView<>();
        TableColumn<Item, String> nameCol = new TableColumn<>("Item Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Item, String> catCol = new TableColumn<>("Categories");
        catCol.setCellValueFactory(data -> new SimpleStringProperty(String.join(", ", data.getValue().getCategories())));
        
        TableColumn<Item, Double> ratingCol = new TableColumn<>("Avg Rating");
        ratingCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAverageRating()));

        recoTable.getColumns().addAll(nameCol, catCol, ratingCol);
        recoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        statusLabel = new Label("Select a user and method to generate recommendations.");
        statusLabel.getStyleClass().add("sub-header");

        view.getChildren().addAll(header, controls, statusLabel, recoTable);
    }
    
    private void generateRecs(boolean collaborative) {
        User selected = userCombo.getValue();
        if (selected == null) {
            statusLabel.setText("Please select a user first.");
            return;
        }
        
        long start = System.nanoTime();
        List<Item> recs;
        if (collaborative) {
            recs = engine.getCollaborativeRecommendations(selected.getId(), 10);
            statusLabel.setText("Collaborative Filtering Results for " + selected.getName());
        } else {
            recs = engine.getGraphRecommendations(selected.getId(), 10);
            statusLabel.setText("Graph-Based Results for " + selected.getName());
        }
        long end = System.nanoTime();
        
        recoTable.setItems(FXCollections.observableArrayList(recs));
        statusLabel.setText(statusLabel.getText() + String.format(" (Took %.2f ms)", (end - start) / 1e6));
    }

    public Node getView() {
        return view;
    }
}
