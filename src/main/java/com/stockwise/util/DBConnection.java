package com.stockwise.util;

import java.sql.Connection;
import java.sql.DriverManager;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/stockwise2_db?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to database: " + conn.getCatalog());
            return conn;
        } catch (Exception e) {
           showAlert("Error", "Database connection failed!" );
            e.printStackTrace();
            return null;
        }
    }

     private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        ((Stage) alert.getDialogPane().getScene().getWindow())
                .getIcons().add(new Image("/images/iconLogo.png"));
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
