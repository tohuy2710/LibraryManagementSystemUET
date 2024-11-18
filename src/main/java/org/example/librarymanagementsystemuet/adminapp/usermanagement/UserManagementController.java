package org.example.librarymanagementsystemuet.adminapp.usermanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.Database.connectDB;
import static org.example.librarymanagementsystemuet.obj.Account.*;

public class UserManagementController implements Initializable {

    @FXML
    private Button cancelUpdateButton, saveUpdateButton;

    @FXML
    private TableColumn<User, String> usernameCol, emailCol, passwordCol, fullnameCol, userIdCol, phoneNumberCol, optionsCol;

    @FXML
    private TextField usernameUpdate, emailUpdate, passwordUpdate, phoneNumberUpdate;

    @FXML
    private Label labelUpdate;

    @FXML
    private AnchorPane updateUserPane;

    @FXML
    private HBox userManagement_HB;

    @FXML
    private StackPane userManagement_SP;

    @FXML
    private TableView<User> userManagement_TV;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ResultSet resultSet;

    public static final String LOAD_FULL_DATA = "load-full-data";
    public static final String LOAD_DATA_BY_INFO = "load-any-data";

    public void setUserData(ResultSet resultSet) throws SQLException {
        while (resultSet != null && resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setName(resultSet.getString("name"));
            user.setEmail(resultSet.getString("email"));
            user.setPhoneNumber(resultSet.getString("phonenumber"));
            userList.add(user);
        }
    }

    public void loadFullUsersData() throws SQLException {
        userList.clear();
        String query = "SELECT * FROM users";

        try (Connection conn = connectDB();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            setUserData(resultSet);
        }
        userManagement_TV.setItems(userList);
    }

    public void loadUserDataByInfo(String userInfo) throws SQLException {
        userList.clear();

        if (userInfo == null || userInfo.isEmpty()) {
            return;
        }

        String query = "SELECT * FROM users WHERE id LIKE ? OR username LIKE ?";

        try (Connection conn = connectDB();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + userInfo + "%");
            preparedStatement.setString(2, "%" + userInfo + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                setUserData(resultSet);
            }
        }

        userManagement_TV.setItems(userList);
    }

    public void UserManagement(String typeLoadData, String userInfo) throws SQLException {
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        fullnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));

        if (typeLoadData.equals(LOAD_FULL_DATA)) {
            loadFullUsersData();
        } else if (typeLoadData.equals(LOAD_DATA_BY_INFO)) {
            if (userInfo != null) {
                loadUserDataByInfo(userInfo);
            }
        }

        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory =
                (TableColumn<User, String> param) -> new TableCell<>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Button deleteButton = new Button("Delete");
                            Button updateButton = new Button("Update");

                            deleteButton.getStyleClass().add("button6");
                            updateButton.getStyleClass().add("button6");

                            deleteButton.setOnMouseClicked(event -> {
                                try {
                                    User deletedUser = getTableView().getItems().get(getIndex());
                                    if (deletedUser != null) {
                                        String deleteQuery = "DELETE FROM users WHERE id = ?";
                                        try (Connection conn = connectDB();
                                             PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery)) {

                                            deleteStatement.setString(1, deletedUser.getId());
                                            int deleteSuccess = deleteStatement.executeUpdate();

                                            if (deleteSuccess > 0) {
                                                loadFullUsersData();
                                                new AlertMessage().successMessage("User deleted successfully!");
                                            } else {
                                                new AlertMessage().errorMessage("Deletion failed!");
                                            }
                                        }
                                    } else {
                                        new AlertMessage().errorMessage("No user selected.");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            });

                            updateButton.setOnMouseClicked(event -> {
                                try {
                                    // Get the selected user
                                    User updatedUser = getTableView().getItems().get(getIndex());

                                    saveUpdateButton.getStyleClass().add("button6");
                                    cancelUpdateButton.getStyleClass().add("button6");

                                    // Check if selected user is not null
                                    if (updatedUser != null) {
                                        // Prepare query to display current user data
                                        String displayUserInfoQuery = "SELECT * FROM users WHERE id = ?";
                                        Connection conn = connectDB();
                                        PreparedStatement preparedStatement = conn.prepareStatement(displayUserInfoQuery);
                                        preparedStatement.setString(1, updatedUser.getId());
                                        resultSet = preparedStatement.executeQuery();

                                        if (resultSet != null && resultSet.next()) {
                                            updateUserPane.setVisible(true);

                                            usernameUpdate.setText(updatedUser.getUsername());
                                            passwordUpdate.setText(updatedUser.getPassword());
                                            emailUpdate.setText(updatedUser.getEmail());
                                            phoneNumberUpdate.setText(updatedUser.getPhoneNumber());

                                            // Save new user data
                                            saveUpdateButton.setOnAction(event1 -> {
                                                try {
                                                    String updateQuery = "UPDATE users " +
                                                            "SET username = ?, " +
                                                            "password = ?, " +
                                                            "email = ?, " +
                                                            "phonenumber = ? " +
                                                            "WHERE id = ?";
                                                    PreparedStatement updateStatement = conn.prepareStatement(updateQuery);

                                                    AlertMessage alertMessage = new AlertMessage();

                                                    if (usernameUpdate.getText().isEmpty() || passwordUpdate.getText().isEmpty()
                                                            || emailUpdate.getText().isEmpty() || phoneNumberUpdate.getText().isEmpty()) {
                                                        // Check if any field is empty
                                                        alertMessage.errorMessage("Please fill in all fields.");
                                                    } else if (!isValidUsername(usernameUpdate.getText())) {
                                                        // Check if new username is valid
                                                        alertMessage.errorMessage("Username must be between 6 and 12 characters.");
                                                    } else if (!isValidPassword(passwordUpdate.getText())) {
                                                        // Check if new password is valid
                                                        alertMessage.errorMessage("Password must contain at least one uppercase letter, " +
                                                                "one lowercase letter, one digit, one special character, and is at least 8 characters long.");
                                                    } else if (!isValidEmail(emailUpdate.getText())) {
                                                        // Check if new email is valid
                                                        alertMessage.errorMessage("Invalid email format.");
                                                    } else if (!isValidPhoneNumber(phoneNumberUpdate.getText())) {
                                                        // Check if new phone number is valid
                                                        alertMessage.errorMessage("Invalid phone number format.");
                                                    } else {
                                                        updateStatement.setString(1, usernameUpdate.getText());
                                                        updateStatement.setString(2, passwordUpdate.getText());
                                                        updateStatement.setString(3, emailUpdate.getText());
                                                        updateStatement.setString(4, phoneNumberUpdate.getText());
                                                        updateStatement.setString(5, updatedUser.getId());

                                                        int updateSuccess = updateStatement.executeUpdate();

                                                        if (updateSuccess > 0) {
                                                            loadFullUsersData();
                                                            updateUserPane.setVisible(false);
                                                            new AlertMessage().successMessage("User information updated successfully!");
                                                        } else {
                                                            new AlertMessage().errorMessage("Update failed. No rows affected.");
                                                        }
                                                    }
                                                } catch (SQLException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            });

                                            // // Cancel update and keep current user data
                                            cancelUpdateButton.setOnAction(event1 -> updateUserPane.setVisible(false));
                                        }
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            HBox managebtn = new HBox(deleteButton, updateButton);
                            managebtn.setStyle("-fx-alignment: CENTER");
                            HBox.setMargin(deleteButton, new Insets(2, 2, 0, 3));
                            HBox.setMargin(updateButton, new Insets(2, 3, 0, 2));
                            setGraphic(managebtn);
                            setText(null);
                        }
                    }
                };

        optionsCol.setCellFactory(cellFactory);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            UserManagement(LOAD_FULL_DATA, null);
            updateUserPane.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
