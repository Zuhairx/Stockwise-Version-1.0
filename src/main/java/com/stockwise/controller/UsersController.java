package com.stockwise.controller;

import com.stockwise.model.User;
import com.stockwise.repository.UserRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class UsersController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private HeaderController headerController;

    private UserRepository userRepository = new UserRepository();
    private ObservableList<User> usersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        headerController.setTitle("User Management");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("passwordHash"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        idColumn.setStyle("-fx-alignment: CENTER;");
        roleColumn.setStyle("-fx-alignment: CENTER;");

        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setUsername(event.getNewValue());
            userRepository.updateUsername(user.getId(), event.getNewValue());
        });

        roleColumn.setCellFactory(ComboBoxTableCell.forTableColumn("ADMIN", "STAFF"));
        roleColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setRole(event.getNewValue());
            userRepository.updateRole(user.getId(), event.getNewValue());
        });

        usersTable.setEditable(true);

        loadUsers();
    }

    private void loadUsers() {
        usersList.clear();
        usersList.addAll(userRepository.getAllUsers());
        usersTable.setItems(usersList);
    }

    @FXML
    private void refreshUsers(ActionEvent event) {
        loadUsers();
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No User Selected");
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.WARNING);
        confirmation.setTitle("Delete User");
        confirmation.setHeaderText("Delete User: " + selectedUser.getUsername());
        confirmation.setContentText("Are you sure you want to delete this user?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean deleted = userRepository.deleteUser(selectedUser.getId());
                if (deleted) {
                    loadUsers();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Delete Failed");
                    error.setHeaderText("Failed to delete user");
                    error.setContentText("An error occurred while deleting the user.");
                    error.showAndWait();
                }
            }
        });
    }
}
