package com.stockwise.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.stockwise.model.Product;
import com.stockwise.repository.ProductRepository;
import com.stockwise.service.ProductService;
import com.stockwise.util.CSVExporter;
import com.stockwise.util.CSVImporter;
import com.stockwise.util.SceneSwitcher;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ProductController implements Initializable {

    @FXML
    private TableView<Product> tableView;
    @FXML
    private TableColumn<Product, Integer> noCol;
    @FXML
    private TableColumn<Product, String> idCol;
    @FXML
    private TableColumn<Product, String> catCol;
    @FXML
    private TableColumn<Product, String> nameCol;
    @FXML
    private TableColumn<Product, String> priceCol;
    @FXML
    private TableColumn<Product, Integer> stockCol;

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField searchField;

    @FXML
    private HeaderController headerController;

    private ObservableList<Product> masterData;
    private final ProductService service = new ProductService();
    private final ProductRepository productRepo = new ProductRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        headerController.setTitle("Product Management");

        Platform.runLater(() -> {
            Scene scene = tableView.getScene();
            scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    SceneSwitcher.switchTo("/fxml/dashboard.fxml");
                }
            });
            scene.getWindow().showingProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    reload();
                }
            });
        });

        noCol.setCellFactory(column -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
        idCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFormattedId()));
        catCol.setCellValueFactory(d -> d.getValue().categoryProperty());
        nameCol.setCellValueFactory(d -> d.getValue().nameProperty());
        priceCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFormattedPrice()));
        stockCol.setCellValueFactory(d -> d.getValue().stockProperty().asObject());

        noCol.setStyle("-fx-alignment: CENTER;");
        idCol.setStyle("-fx-alignment: CENTER;");
        catCol.setStyle("-fx-alignment: CENTER;");
        stockCol.setStyle("-fx-alignment: CENTER;");

        noCol.setPrefWidth(60);
        idCol.setPrefWidth(80);
        catCol.setPrefWidth(100);
        nameCol.setPrefWidth(250);
        priceCol.setPrefWidth(100);
        stockCol.setPrefWidth(60);

        masterData = FXCollections.observableArrayList(service.getAllProducts());
        tableView.setItems(masterData);

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, selected) -> {
                    if (selected != null) {
                        nameField.setText(selected.getName());
                        categoryField.setText(selected.getCategory());
                        priceField.setText(String.valueOf(selected.getPrice()));
                    }
                });

        searchField.textProperty().addListener((obs, old, val) -> {
            tableView.setItems(
                    masterData.filtered(p -> p.getName().toLowerCase().contains(val.toLowerCase())));
        });
    }

    private void reload() {
        masterData.setAll(service.getAllProducts());
    }

    @FXML
    private void handleAdd() {
        if (!isValidInput())
            return;

        String newName = nameField.getText().trim().toLowerCase();
        boolean exists = masterData.stream()
                .anyMatch(p -> p.getName().toLowerCase().equals(newName));
        if (exists) {
            showAlert("Validation Error", "Product name already exists!");
            return;
        }
        String newId = Product.generateNextId();
        service.addProduct(
                newId,
                nameField.getText(),
                categoryField.getText(),
                Integer.parseInt(priceField.getText())

        );
        reload();
        nameField.clear();
        categoryField.clear();
        priceField.clear();

    }

    @FXML
    private void handleUpdate() {
        Product selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a product first");
            return;
        }

        if (!isValidInput())
            return;

        service.updateProduct(
                selected.getId(),
                nameField.getText(),
                categoryField.getText(),
                Integer.parseInt(priceField.getText()));
        reload();
    }

    @FXML
    private void handleDelete() {
        Product selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.deleteProduct(selected.getId());
            reload();
        }
    }

    @FXML
    private void handleDeleteAll() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete All Products");
        alert.setHeaderText("Delete all products?");
        alert.setContentText("This action cannot be undone.\nAre you sure?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(noButton, yesButton);

        ((Stage) alert.getDialogPane().getScene().getWindow())
                .getIcons().add(new Image("/images/iconLogo.png"));

        alert.showAndWait().ifPresent(result -> {
            if (result == yesButton) {
                service.deleteAllProducts();
                Product.resetIdCounter();
                reload();
            }
        });
    }

    @FXML
    private void handleClearField() {
        nameField.clear();
        categoryField.clear();
        priceField.clear();
        searchField.clear();
    }

    private boolean isValidInput() {
        if (nameField.getText().isEmpty()
                || priceField.getText().isEmpty()) {
            showAlert("Validation Error", "All fields must be filled");
            return false;
        }

        try {
            int price = Integer.parseInt(priceField.getText());

            if (price < 0) {
                showAlert("Validation Error", "Price must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Price must be numeric");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ((Stage) alert.getDialogPane().getScene().getWindow())
                .getIcons().add(new Image("/images/iconLogo.png"));

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    @FXML
    private void exportCSV1() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.setInitialFileName("products.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = (Stage) tableView.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            List<Product> products = productRepo.findAll();
            if (products.isEmpty()) {
                showAlert("Info",
                        "No products to export. CSV file created with header only: " + selectedFile.getName());
            } else {
                CSVExporter.exportProducts(products, selectedFile);
                showAlert("Success", "Products exported to " + selectedFile.getName());
            }
        }
    }

    @FXML
    private void importCSV1() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = (Stage) tableView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                CSVImporter.importProducts(selectedFile.getAbsolutePath());
                reload();
                showAlert("Success", "Products imported successfully from " + selectedFile.getName());
            } catch (Exception e) {
                showAlert("Error", "Failed to import products: " + e.getMessage());
            }
        }
    }

}
