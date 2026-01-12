package com.stockwise.controller;

import com.stockwise.service.ProductService;
import com.stockwise.service.TransactionService;
import com.stockwise.util.SceneSwitcher;
import com.stockwise.util.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Button productMenu;

    @FXML
    private Button usersMenu;

    @FXML
    private Label productCountLabel;

    @FXML
    private Label transactionCountLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        if (!UserSession.get().getRole().equals("ADMIN")) {
            productMenu.setVisible(false);
            productMenu.setManaged(false);
            usersMenu.setVisible(false);
            usersMenu.setManaged(false);
        }

        welcomeLabel.setText("Welcome " + UserSession.get().getUsername() + "!");

        ProductService productService = new ProductService();
        TransactionService transactionService = new TransactionService();
        int productCount = productService.getAllProducts().size();
        int transactionCount = transactionService.getAllTransactions().size();
        productCountLabel.setText(String.valueOf(productCount));
        transactionCountLabel.setText(String.valueOf(transactionCount));
    }

    @FXML
    private void goToProduct(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/fxml/product.fxml");
    }

    @FXML
    private void goToUsers(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/fxml/users.fxml");
    }

    @FXML
    private void goToTransaction(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/fxml/transaction.fxml");
    }

    @FXML
    private void logout(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/fxml/login.fxml");
    }

}
