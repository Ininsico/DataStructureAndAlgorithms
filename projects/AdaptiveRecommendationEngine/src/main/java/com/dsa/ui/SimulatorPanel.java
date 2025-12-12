package com.dsa.ui;

import com.dsa.algorithms.RecommendationEngine;
import com.dsa.core.Item;
import com.dsa.core.User;
import com.dsa.data.DataLoader;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SimulatorPanel {
    private VBox view;
    private DataLoader dataLoader;
    private RecommendationEngine engine;
    
    private ComboBox<User> userCombo;
    private ComboBox<Item> itemCombo;
    private Spinner<Double> ratingSpinner;
    private TextArea logArea;

    public SimulatorPanel(DataLoader dataLoader, RecommendationEngine engine) {
        this.dataLoader = dataLoader;
        this.engine = engine;
        initView();
    }

    private void initView() {
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label header = new Label("Interaction Simulator");
        header.getStyleClass().add("header-label");

        HBox form = new HBox(15);
        form.getStyleClass().add("card");
        form.setPrefHeight(100);
        form.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        userCombo = new ComboBox<>(FXCollections.observableArrayList(dataLoader.getUsers().values()));
        userCombo.setPromptText("Select User");
        userCombo.setConverter(new javafx.util.StringConverter<User>() {
                 @Override public String toString(User u) { return u == null ? "" : u.getName(); }
                 @Override public User fromString(String s) { return null; }
        });
        
        itemCombo = new ComboBox<>(FXCollections.observableArrayList(dataLoader.getItems().values()));
        itemCombo.setPromptText("Select Item");
        itemCombo.setConverter(new javafx.util.StringConverter<Item>() {
                 @Override public String toString(Item i) { return i == null ? "" : i.getName(); }
                 @Override public Item fromString(String s) { return null; }
        });
        
        Label lblRating = new Label("Rating:");
        ratingSpinner = new Spinner<>(1.0, 5.0, 5.0, 0.5);
        ratingSpinner.setPrefWidth(80);

        Button btnAdd = new Button("Add Interaction");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(e -> addInteraction());

        form.getChildren().addAll(userCombo, itemCombo, lblRating, ratingSpinner, btnAdd);
        
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(300);

        view.getChildren().addAll(header, form, new Label("Simulation Log:"), logArea);
    }

    private void addInteraction() {
        User u = userCombo.getValue();
        Item i = itemCombo.getValue();
        if (u == null || i == null) return;
        
        double rating = ratingSpinner.getValue();
        
        engine.addInteraction(u.getId(), i.getId(), rating);
        logArea.appendText(String.format("Added: %s rated '%s' with %.1f\n", u.getName(), i.getName(), rating));
        logArea.appendText("Graph updated. Similarities marked stale (Lazy Recalculation).\n");
    }

    public Node getView() {
        return view;
    }
}
