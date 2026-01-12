package com.stockwise.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {

    public static void switchScene(ActionEvent event, String fxml) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene().getWindow();

            Scene scene = new Scene(
                    FXMLLoader.load(SceneSwitcher.class.getResource(fxml)));

            scene.getStylesheets().add(
                    SceneSwitcher.class
                            .getResource("/css/style.css")
                            .toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchTo(String fxml) {
        try {
            Stage stage = (Stage) Stage.getWindows()
                    .filtered(w -> w.isShowing())
                    .get(0);

            Scene scene = new Scene(
                    FXMLLoader.load(SceneSwitcher.class.getResource(fxml)));

            scene.getStylesheets().add(
                    SceneSwitcher.class
                            .getResource("/css/style.css")
                            .toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
