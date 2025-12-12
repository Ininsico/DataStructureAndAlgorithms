package com.dsa.ui;

import com.dsa.core.Item;
import com.dsa.data.DataLoader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class BrowserPanel {
    private VBox view;
    private DataLoader dataLoader;
    private TableView<Item> itemTable;
    private TextField searchField;

    public BrowserPanel(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
        initView();
    }

    private void initView() {
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label header = new Label("Item Browser");
        header.getStyleClass().add("header-label");

        searchField = new TextField();
        searchField.setPromptText("Search items...");
        
        itemTable = new TableView<>();
        
        TableColumn<Item, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Item, String> catCol = new TableColumn<>("Categories");
        catCol.setCellValueFactory(data -> new SimpleStringProperty(String.join(", ", data.getValue().getCategories())));
        
        TableColumn<Item, Number> popCol = new TableColumn<>("Popularity");
        popCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPopularity()));

        TableColumn<Item, Number> ratingCol = new TableColumn<>("Avg Rating");
        ratingCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAverageRating()));

        itemTable.getColumns().addAll(idCol, nameCol, catCol, popCol, ratingCol);
        
        // Data & Filtering
        FilteredList<Item> filteredData = new FilteredList<>(FXCollections.observableArrayList(dataLoader.getItems().values()), p -> true);
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        
        itemTable.setItems(filteredData);
        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        view.getChildren().addAll(header, searchField, itemTable);
    }

    public Node getView() {
        return view;
    }
}
