package com.stockwise.controller;

import com.stockwise.model.User;
import com.stockwise.service.AuthService;
import com.stockwise.util.SceneSwitcher;
import com.stockwise.util.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private AuthService authService = new AuthService();

    @FXML
    private void handleLogin(ActionEvent event) {

        User user = authService.login(
                usernameField.getText(),
                passwordField.getText());

        if (user != null) {
            UserSession.set(user);
            SceneSwitcher.switchScene(event, "/fxml/dashboard.fxml");
        } else if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText("Please input username and password");
        } else {
            errorLabel.setText("Username or password incorrect");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/fxml/register.fxml");
    }

}
