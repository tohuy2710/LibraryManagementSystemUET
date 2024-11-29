package org.example.librarymanagementsystemuet.userapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.librarymanagementsystemuet.HumamiLibraryApplication;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserAppController implements Initializable {

    @FXML
    AnchorPane paneMenuMini;

    @FXML
    AnchorPane contentPane;

    @FXML
    Label coinNumLabel;

    public void showHomePageBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-home-page-2.fxml"));
            HBox userHomePageHBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(userHomePageHBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBookBrowserBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-browser-book.fxml"));
            HBox userBrowserBookHBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(userBrowserBookHBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserDetailsBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-detail-view-pane.fxml"));
            HBox userDetailsBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(userDetailsBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) contentPane.getScene().getWindow();
        stage.close();

        HumamiLibraryApplication humamiLibraryApplication = new HumamiLibraryApplication();
        humamiLibraryApplication.start(new Stage());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showHomePageBox(null);
        coinNumLabel.textProperty().bind(UserSession.getInstance().hmCoinProperty().asString());
    }
}

//org.example.librarymanagementsystemuet.userapp.UserAppController
