package org.example.librarymanagementsystemuet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminAppController implements Initializable {
    @FXML
    private Circle adminAvatar;

    @FXML
    private HBox adminInfoBox;

    @FXML
    private Label adminName;

    @FXML
    private Button booksManagementButton;

    @FXML
    private Button booksManagementButtonMinimize;

    @FXML
    private Button changeMenuSize;

    @FXML
    protected AnchorPane contentPane;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button dashboardButtonMinimize;

    @FXML
    private Button logoutButton;

    @FXML
    private Button logoutButtonMinimize;

    @FXML
    private AnchorPane paneMenuFull;

    @FXML
    private AnchorPane paneMenuMini;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private ChoiceBox<String> selectTypeSearch;

    @FXML
    private Button usersManagementButton;

    @FXML
    private Button usersManagementButtonMinimize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paneMenuFull.setVisible(false);
        paneMenuMini.setVisible(true);
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

        contentPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            adjustHBoxSize();
        });
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
        for (Node node : contentPane.getChildren()) { //HBox ở trong contentPane sẽ thay đổi kích thước theo contentPane
            if (node instanceof HBox) {
                ((HBox) node).prefWidthProperty().bind(contentPane.widthProperty());
            }
        }
    }

    public void showBookManagementHBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/book-management-dashboard.fxml"));
            HBox bookManagementDashboardHBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(bookManagementDashboardHBox);
            adjustContentSize(); adjustHBoxSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserManagementHBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-management-user-list.fxml"));
            HBox userManagementDashboardHBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(userManagementDashboardHBox);
            adjustContentSize(); adjustHBoxSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeMenuSize(ActionEvent event) {
        if (paneMenuFull.isVisible()) {
            paneMenuFull.setVisible(false);
            paneMenuMini.setVisible(true);
        } else {
            paneMenuFull.setVisible(true);
            paneMenuMini.setVisible(false);
        }
    }
}
