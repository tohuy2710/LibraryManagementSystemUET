package org.example.librarymanagementsystemuet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import package1.AlertMessage;
import package1.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.Database.connectDB;

public class UserManagementController implements Initializable {

    @FXML
    private TableView<User> userManagement_TV;
    @FXML
    private TableColumn<User, String> emailCol, fullnameCol, optionsCol, passwordCol, phoneNumberCol, userIdCol, usernameCol;
    @FXML
    private HBox userManagement_HB;
    @FXML
    private TextField emailUpdate, fullnameUpdate, phonenumberUpdate, usernameUpdate, passwordUpdate;
    @FXML
    private Label labelUpdate;
    @FXML
    private Button saveButtonUpdate, cancelUpdateButton;
    @FXML
    private AnchorPane updateUserPane;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ResultSet resultSet;

    protected static final String LOAD_FULL_DATA = "load-full-data";
    protected static final String LOAD_DATA_BY_INFO = "load-any-data";

    public void loadFullUsersData() throws SQLException {
        userList.clear();
        String query = "SELECT * FROM users";

        try (Connection conn = connectDB();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

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
                                                new AlertMessage().errorMessage("Deletion failed. No rows affected.");
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
                                    User updatedUser = getTableView().getItems().get(getIndex());
                                    if (updatedUser != null) {
                                        userManagement_HB.setVisible(false);
                                        updateUserPane.setVisible(true);

                                        usernameUpdate.setText(updatedUser.getUsername());
                                        fullnameUpdate.setText(updatedUser.getName());
                                        passwordUpdate.setText(updatedUser.getPassword());
                                        emailUpdate.setText(updatedUser.getEmail());
                                        phonenumberUpdate.setText(updatedUser.getPhoneNumber());

                                        saveButtonUpdate.setOnAction(event1 -> {
                                            try (Connection conn = connectDB()) {
                                                String updateQuery = "UPDATE users SET username = ?, name = ?, password = ?, email = ?, phonenumber = ? WHERE id = ?";
                                                try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {

                                                    updateStatement.setString(1, usernameUpdate.getText());
                                                    updateStatement.setString(2, fullnameUpdate.getText());
                                                    updateStatement.setString(3, passwordUpdate.getText());
                                                    updateStatement.setString(4, emailUpdate.getText());
                                                    updateStatement.setString(5, phonenumberUpdate.getText());
                                                    updateStatement.setString(6, updatedUser.getId());

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
                                                e.printStackTrace();
                                            }
                                        });

                                        cancelUpdateButton.setOnAction(event1 -> updateUserPane.setVisible(false));
                                    } else {
                                        new AlertMessage().errorMessage("No user selected.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
