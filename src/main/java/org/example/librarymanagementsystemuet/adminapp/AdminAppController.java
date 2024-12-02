package org.example.librarymanagementsystemuet.adminapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.librarymanagementsystemuet.HumamiLibraryApplication;
import org.example.librarymanagementsystemuet.adminapp.bookmanagement.BookManagementDashboardController;
import org.example.librarymanagementsystemuet.adminapp.usermanagement.UserManagementController;
import org.example.librarymanagementsystemuet.adminapp.bookmanagement.BookListController;
import org.example.librarymanagementsystemuet.obj.Admin;
import org.example.librarymanagementsystemuet.obj.AlertMessage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.obj.Admin.DEFAULT_AVATAR_PATH;

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
    private Button changeMenuSizeButton;

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

    Admin sessionAdmin;

    String[] typeSearchArrays = {"User", "Book"};

    public void setAdminInfo() {
        sessionAdmin = Admin.getInstance();
    }

    public void showAdminInfo() {
        if (sessionAdmin == null) {
            setAdminInfo();
        }
        // Correct the image path
        Image adminAvatarImg = new Image(getClass().getResourceAsStream(DEFAULT_AVATAR_PATH));
        this.adminAvatar.setFill(new ImagePattern(adminAvatarImg));
        adminName.setText(sessionAdmin.getName());
    }

    public AnchorPane getMainBox() {
        return contentPane;
    }

    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        HumamiLibraryApplication humamiLibraryApplication = new HumamiLibraryApplication();
        humamiLibraryApplication.start(new Stage());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showHomePageHBox(null);

        selectTypeSearch.getItems().addAll(typeSearchArrays);
        selectTypeSearch.setValue("User");

        showAdminInfo();

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
        for (Node node : contentPane.getChildren()) {
            if (node instanceof HBox) {
                ((HBox) node).prefWidthProperty().bind(contentPane.widthProperty());
            }
        }
    }

    public void showHomePageHBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/app-home-page.fxml"));
            HBox homePageHBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(homePageHBox);
            adjustContentSize();
            disableChangeMenuSizeButton();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showBookManagementHBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/book-management-dashboard.fxml"));
            HBox bookManagementDashboardBox = loader.load();
            BookManagementDashboardController controller = loader.getController();
            controller.setParentController(this);
            contentPane.getChildren().clear();
            contentPane.getChildren().add(bookManagementDashboardBox);
            adjustContentSize();
            enableChangeMenuSizeButton();
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
            adjustContentSize();
            disableChangeMenuSizeButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserRequestManagementHBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-request-management.fxml"));
            HBox userRequestManagementHBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(userRequestManagementHBox);
            adjustContentSize();
            enableChangeMenuSizeButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserPenaltyHBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/penalty-list-view.fxml"));
            HBox userPenaltyHBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(userPenaltyHBox);
            adjustContentSize();
            enableChangeMenuSizeButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showVIPForumBox(ActionEvent event) {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/discussion-page.fxml"));
            HBox vipForumBox = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(vipForumBox);
            adjustContentSize();
            enableChangeMenuSizeButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void search(ActionEvent event) {
        if (searchTextField.getText().isEmpty()) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage("Please enter the search text");
        }
        else if (selectTypeSearch.getValue().equals("User")) {
            showUserListResults();
        } else if (selectTypeSearch.getValue().equals("Book")) {
            showBookListResults();
        }
    }

    public void showBookListResults() {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/book-management-book-list.fxml"));
            HBox bookManagementBookListHBox = loader.load();
            BookListController controller = loader.getController();
            String searchText = searchTextField.getText();
            controller.searchBooks(searchText);

            contentPane.getChildren().add(bookManagementBookListHBox);
            adjustContentSize();
            enableChangeMenuSizeButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserListResults() {
        contentPane.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/user-management-user-list.fxml"));
            HBox userManagementDashboardHBox = loader.load();


            UserManagementController controller = loader.getController();
            String searchText = searchTextField.getText();

            controller.UserManagement(UserManagementController.LOAD_DATA_BY_INFO, searchText);

            contentPane.getChildren().add(userManagementDashboardHBox);

            adjustContentSize();

            enableChangeMenuSizeButton();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}
