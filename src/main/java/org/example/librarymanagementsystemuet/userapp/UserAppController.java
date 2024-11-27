package org.example.librarymanagementsystemuet.userapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.librarymanagementsystemuet.HumamiLibraryApplication;
import org.example.librarymanagementsystemuet.adminapp.bookmanagement.BookManagementDashboardController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserAppController implements Initializable {

    @FXML
    Button changeMenuSizeButton;

    @FXML
    AnchorPane paneMenuFull;

    @FXML
    AnchorPane paneMenuMini;

    @FXML
    AnchorPane contentPane;

    public void showHomePageBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-home-page-2.fxml"));
            HBox userHomePageHBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(userHomePageHBox);
            adjustContentSize();
            enableChangeMenuSizeButton();
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
            adjustContentSize();
            enableChangeMenuSizeButton();
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

    private void adjustContentSize() {
        if (paneMenuFull.isVisible() && !paneMenuMini.isVisible()) {
            AnchorPane.setLeftAnchor(contentPane, 240.0);
        } else if (!paneMenuFull.isVisible() && paneMenuMini.isVisible()) {
            AnchorPane.setLeftAnchor(contentPane, 80.0);
        }
        AnchorPane.setRightAnchor(contentPane, 0.0);

        contentPane.applyCss();
        adjustHBoxSize();
    }

    private void adjustHBoxSize() {
        for (Node node : contentPane.getChildren()) {
            if (node instanceof HBox) {
                ((HBox) node).prefWidthProperty().bind(contentPane.widthProperty());
            }
        }
    }

    public void showPaneMenuMini() {
        paneMenuFull.setVisible(false);
        paneMenuMini.setVisible(true);
        adjustHBoxSize(); adjustContentSize();
    }

    public void showPaneMenuFull() {
        paneMenuFull.setVisible(true);
        paneMenuMini.setVisible(false);
        adjustHBoxSize(); adjustContentSize();
    }

    public void changeMenuSize(ActionEvent event) {
        if (paneMenuFull.isVisible()) {
            showPaneMenuMini();
        } else {
            showPaneMenuFull();
        }
        adjustHBoxSize(); adjustContentSize();
    }

    public void disableChangeMenuSizeButton() {
        changeMenuSizeButton.setDisable(true);
    }

    public void enableChangeMenuSizeButton() {
        changeMenuSizeButton.setDisable(false);
    }

    public boolean isShowingPaneMenuFull() {
        return paneMenuFull.isVisible();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPaneMenuMini();
        paneMenuFull.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && paneMenuMini.isVisible()) {
                paneMenuMini.setVisible(false);
            }
            adjustContentSize();
        });

        paneMenuMini.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && paneMenuFull.isVisible()) {
                paneMenuFull.setVisible(false);
            }
            adjustContentSize();
        });

        showHomePageBox(null);

        contentPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            adjustHBoxSize();
        }); }
}

//org.example.librarymanagementsystemuet.userapp.UserAppController
