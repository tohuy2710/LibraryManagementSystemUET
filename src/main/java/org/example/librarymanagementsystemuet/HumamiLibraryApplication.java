package org.example.librarymanagementsystemuet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
//org.example.librarymanagementsystemuet.userapp.UserPayController.java
public class HumamiLibraryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HumamiLibraryApplication
                .class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("Humami Library");

        Image logo = new Image(HumamiLibraryApplication.class.getResourceAsStream("/asset/img/logo.png"));
        stage.getIcons().add(logo);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}