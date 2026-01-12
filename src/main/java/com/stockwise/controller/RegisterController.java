package com.stockwise.controller;

import com.stockwise.service.AuthService;
import com.stockwise.util.SceneSwitcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<String> roleChoiceBox;
    @FXML
    private Label statusLabel;

    private AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        roleChoiceBox.getItems().addAll("staff", "admin");
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill all fields");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean success = authService.register(username, password, role);

        if (success) {
            statusLabel.setText("Registration Successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");

        } else {
            statusLabel.setText("Registration failed. Username already exist.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/fxml/login.fxml");
    }
}
