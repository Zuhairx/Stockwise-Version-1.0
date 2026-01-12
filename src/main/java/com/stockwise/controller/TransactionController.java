package com.stockwise.controller;

import com.stockwise.model.Product;
import com.stockwise.model.Transaction;
import com.stockwise.repository.TransactionRepository;
import com.stockwise.service.ProductService;
import com.stockwise.service.TransactionService;
import com.stockwise.util.CSVExporter;
import com.stockwise.util.CSVImporter;
import com.stockwise.util.SceneSwitcher;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    @FXML
    private ComboBox<Product> productBox;
    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private TextField qtyField;

    @FXML
    private TableView<Transaction> tableView;
    @FXML
    private TableColumn<Transaction, Integer> noCol;
    @FXML
    private TableColumn<Transaction, String> t_idCol;
    @FXML
    private TableColumn<Transaction, String> p_idCol;
    @FXML
    private TableColumn<Transaction, String> productCol;
    @FXML
    private TableColumn<Transaction, String> typeCol;
    @FXML
    private TableColumn<Transaction, Integer> qtyCol;
    @FXML
    private TableColumn<Transaction, String> dateCol;

    @FXML
    private HeaderController headerController;

    private ObservableList<Transaction> masterData;
    private final TransactionService transactionService = new TransactionService();
    private final ProductService productService = new ProductService();
    private final TransactionRepository transactionRepo = new TransactionRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (headerController != null) {
            headerController.setTitle("Transaction Management");
        }

        Platform.runLater(() -> {
            Scene scene = qtyField.getScene();
            scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    SceneSwitcher.switchTo("/fxml/dashboard.fxml");
                }
            });
        });

        productBox.setPromptText("Choose product");

        productBox.setItems(
                FXCollections.observableArrayList(productService.getAllProducts()));

        productBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Product p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? "" : p.getName());
            }
        });

        productBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Product p, boolean empty) {
                super.updateItem(p, empty);

                if (empty || p == null) {
                    setText(productBox.getPromptText());
                } else {
                    setText(p.getName());

                }
            }
        });

        typeBox.setPromptText("Type");
        typeBox.setItems(
                FXCollections.observableArrayList("IN", "OUT"));

        typeBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(typeBox.getPromptText());
                } else {
                    setText(item);
                }
            }
        });

        noCol.setCellFactory(column -> new TableCell<Transaction, Integer>() {
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

        t_idCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFormattedId()));
        p_idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        noCol.setStyle("-fx-alignment: CENTER;");
        t_idCol.setStyle("-fx-alignment: CENTER;");
        p_idCol.setStyle("-fx-alignment: CENTER;");
        typeCol.setStyle("-fx-alignment: CENTER;");
        qtyCol.setStyle("-fx-alignment: CENTER;");
        dateCol.setStyle("-fx-alignment: CENTER;");

        noCol.setPrefWidth(60);
        t_idCol.setPrefWidth(90);
        p_idCol.setPrefWidth(90);
        productCol.setPrefWidth(250);
        typeCol.setPrefWidth(60);
        qtyCol.setPrefWidth(60);
        dateCol.setPrefWidth(150);

        masterData = FXCollections.observableArrayList();
        tableView.setItems(masterData);

        loadTable();

    }

    private void loadTable() {
        masterData.setAll(transactionService.getAllTransactions());
    }

    @FXML
    private void handleSubmit() {

        Product product = productBox.getValue();
        String type = typeBox.getValue();

        if (product == null || type == null) {
            showAlert("Error", "Please complete the form");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "quantity must be numeric!");
            return;
        }

        if (!transactionService.process(product, type, qty)) {
            showAlert("Error", "Stock not enough!");
            return;
        }

        productBox.setItems(
                FXCollections.observableArrayList(productService.getAllProducts()));

        loadTable();
        tableView.refresh();
        productBox.setValue(null);
        typeBox.setValue(null);
        qtyField.clear();

    }

    @FXML
    private void handleDelete2() {
        Transaction selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            transactionService.deleteTransaction(selected.getId());
            loadTable();
            tableView.refresh();
            productBox.setItems(
                    FXCollections.observableArrayList(productService.getAllProducts()));
        }
    }

    @FXML
    private void handleDeleteAll2() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete All Transactions");
        alert.setHeaderText("Delete all transactions?");
        alert.setContentText("This action cannot be undone.\nAre you sure?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(noButton, yesButton);

        ((Stage) alert.getDialogPane().getScene().getWindow())
                .getIcons().add(new Image("/images/iconLogo.png"));

        alert.showAndWait().ifPresent(result -> {
            if (result == yesButton) {
                transactionService.deleteAllTransactions();
                loadTable();
                tableView.refresh();
            }
        });
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
    private void exportCSV2() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.setInitialFileName("transactions.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = (Stage) tableView.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            List<Transaction> transactions = transactionRepo.findAll();
            CSVExporter.exportTransactions(transactions, selectedFile);
            if (transactions.isEmpty()) {
                showAlert("Info",
                        "No transactions to export. CSV file created with header only: " + selectedFile.getName());
            } else {
                showAlert("Success", "Transactions exported to " + selectedFile.getName());
            }
        }
    }

    @FXML
    private void importCSV2() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = (Stage) tableView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                CSVImporter.importTransactions(selectedFile.getAbsolutePath());
                showAlert("Success", "Transactions imported successfully from " + selectedFile.getName());
                loadTable();
                tableView.refresh();
            } catch (Exception e) {
                showAlert("Error", "Failed to import CSV: " + e.getMessage());
            }
        }
    }

}
