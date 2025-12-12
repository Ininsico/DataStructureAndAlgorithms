package com.dsa.ui;

import com.dsa.algorithms.RecommendationEngine;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MetricsPanel {
    private VBox view;
    private RecommendationEngine engine;

    public MetricsPanel(RecommendationEngine engine) {
        this.engine = engine;
        initView();
    }

    private void initView() {
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label header = new Label("Performance Metrics");
        header.getStyleClass().add("header-label");
        
        Label info = new Label("See console logs for detailed timing.");
        
        Label complexity = new Label(
            "Theoretical Complexity:\n" +
            "- Collaborative Filtering (Cosine): O(U * I)\n" + 
            "- Graph BFS (Depth 2): O(V + E) local neighborhood\n" +
            "- Priority Queue Insert/Poll: O(log K)"
        );
        complexity.getStyleClass().add("card");
        
        view.getChildren().addAll(header, info, complexity);
    }

    public Node getView() {
        return view;
    }
}
