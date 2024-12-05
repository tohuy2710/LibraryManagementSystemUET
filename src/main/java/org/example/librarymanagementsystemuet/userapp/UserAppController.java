package org.example.librarymanagementsystemuet.userapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.librarymanagementsystemuet.Controller;
import org.example.librarymanagementsystemuet.HumamiLibraryApplication;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.User;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserAppController extends Controller implements Initializable {

    @FXML
    AnchorPane paneMenuMini;

    @FXML
    AnchorPane contentPane;

    @FXML
    Label coinNumLabel;

    @FXML
    Label nameLabel;

    @FXML
    Circle avtShape;

    @FXML
    ImageView vipBanner;

    private void showVipBanner() {
        if (UserSession.getInstance().getUserType().equals("VIP")) {
            vipBanner.setVisible(true);
        }
        else {
            vipBanner.setVisible(false);
        }
    }

    public void showHomePageBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-home-page-2.fxml"));
            HBox userHomePageHBox = loader.load();
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
            contentPane.getChildren().add(userDetailsBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBuyVipBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-pay.fxml"));
            AnchorPane userPayBox = loader.load();
            contentPane.getChildren().add(userPayBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public TextField searchTextField;

    public void showSearchBooks(ActionEvent event) {
        if (searchTextField.getText().isEmpty() || searchTextField.getText().isBlank()) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage("Please enter search key!");
        }

        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-browser-book.fxml"));
            HBox userBrowserBookHBox = loader.load();
            contentPane.getChildren().add(userBrowserBookHBox);
            UserBrowserBookController controller = loader.getController();
            controller.searchBooks(searchTextField.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showVIPForum(ActionEvent event) {
        if (!UserSession.getInstance().getUserType().equals("VIP")) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage("You must be a VIP to access this feature!");
            return;
        }

        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/discussion-page.fxml"));
            HBox discussionPage = loader.load();
            DiscussionPageController controller = loader.getController();
            controller.setRole("USER");
            System.out.println(controller.getRole());
            contentPane.getChildren().add(discussionPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) contentPane.getScene().getWindow();
        stage.close();

        HumamiLibraryApplication humamiLibraryApplication = new HumamiLibraryApplication();
        humamiLibraryApplication.start(new Stage());
        UserSession.logout();
    }

    public void setInfoUser() {
        nameLabel.setText(UserSession.getInstance().getName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfoUser();
        showVipBanner();
        showHomePageBox(null);
        coinNumLabel.textProperty().bind(UserSession.getInstance().hmCoinProperty().asString());
    }
}

//org.example.librarymanagementsystemuet.userapp.UserAppController
