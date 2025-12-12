package com.dsa.ui;

import com.dsa.core.Graph;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Random;

public class GraphPanel {
    private VBox view;
    private Graph graph;
    private Canvas canvas;

    public GraphPanel(Graph graph) {
        this.graph = graph;
        initView();
    }

    private void initView() {
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label header = new Label("Graph Visualization");
        header.getStyleClass().add("header-label");
        
        Label sub = new Label("Visual subset of the User-Item Bipartite Graph (Randomized Layout)");
        
        ScrollPane scroll = new ScrollPane();
        canvas = new Canvas(1000, 800);
        drawGraph();
        
        scroll.setContent(canvas);
        view.getChildren().addAll(header, sub, scroll);
    }
    
    private void drawGraph() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        
        // Simple random visualization for demo
        // In a real app, use a force-directed layout
        Random rand = new Random();
        
        // Draw Nodes
        // Users on left, Items on right roughly? or just random
        // Let's do random for now as force-directed is complex from scratch
        
        // Just draw a few nodes to show concept, drawing 1000 nodes is messy
        int numUsers = Math.min(graph.getUserCount(), 30); 
        int numItems = Math.min(graph.getItemCount(), 30);
        
        double[][] userPos = new double[numUsers + 1][2];
        double[][] itemPos = new double[numItems + 1][2];
        
        gc.setFill(Color.BLUE);
        for (int i = 1; i <= numUsers; i++) {
            userPos[i][0] = 50 + rand.nextDouble() * 300;
            userPos[i][1] = 50 + rand.nextDouble() * 700;
            gc.fillOval(userPos[i][0], userPos[i][1], 10, 10);
            gc.fillText("U" + i, userPos[i][0], userPos[i][1]);
        }
        
        gc.setFill(Color.GREEN);
        for (int i = 1; i <= numItems; i++) {
            itemPos[i][0] = 500 + rand.nextDouble() * 300;
            itemPos[i][1] = 50 + rand.nextDouble() * 700;
            gc.fillOval(itemPos[i][0], itemPos[i][1], 10, 10);
            gc.fillText("I" + i, itemPos[i][0], itemPos[i][1]);
        }
        
        // Draw edges (random subset)
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(0.5);
        for (int u = 1; u <= numUsers; u++) {
            for (int item : graph.getUserNeighbors(u)) {
                if (item <= numItems) {
                    gc.strokeLine(userPos[u][0] + 5, userPos[u][1] + 5, itemPos[item][0] + 5, itemPos[item][1] + 5);
                }
            }
        }
    }

    public Node getView() {
        return view;
    }
}
