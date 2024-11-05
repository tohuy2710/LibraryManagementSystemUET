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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.Database.connectDB;
import static org.example.librarymanagementsystemuet.Database.preparedStatement;

public class UserManagementController implements Initializable {

    @FXML
    private TableView<User> userManagement_TV;

    @FXML
    private TableColumn<User, String> emailCol;

    @FXML
    private TableColumn<User, String> fullnameCol;

    @FXML
    private TableColumn<User, String> optionsCol;

    @FXML
    private TableColumn<User, String> passwordCol;

    @FXML
    private TableColumn<User, String> phoneNumberCol;

    @FXML
    private TableColumn<User, String> userIdCol;

    @FXML
    private HBox userManagement_HB;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TextField emailUpdate;

    @FXML
    private TextField fullnameUpdate;

    @FXML
    private Label labelUpdate;

    @FXML
    private TextField phonenumberUpdate;

    @FXML
    private Button saveButtonUpdate;

    @FXML
    private AnchorPane updateUserPane;

    @FXML
    private TextField usernameUpdate;

    @FXML
    private TextField passwordUpdate;

    @FXML
    private Button cancelUpdateButton;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ResultSet resultSet;

    public void loadUserData() throws SQLException {
        userList.clear();

        Database.connect = connectDB();

        String query = "SELECT * FROM users";
        preparedStatement = Database.connect.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

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

        userManagement_TV.setItems(userList);
    }

    public void UserManagement() throws SQLException {
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        fullnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));

        loadUserData();

        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory =
                (TableColumn<User, String> param) -> {
                    final TableCell<User, String> cell = new TableCell<>() {
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

                                // Delete button click
                                deleteButton.setOnMouseClicked(event -> {
                                    try {
                                        // Get the selected user
                                        User deletedUser = getTableView().getItems().get(getIndex());

                                        // Check if a user is selected
                                        if (deletedUser != null) {
                                            // Prepare the query
                                            String deleteQuery = "DELETE FROM users WHERE id = ?";
                                            PreparedStatement preparedStatement =
                                                    Database.connect.prepareStatement(deleteQuery);

                                            // Set the id parameter
                                            preparedStatement.setString(1, deletedUser.getId());

                                            // Execute the query
                                            int deleteSuccess = preparedStatement.executeUpdate();

                                            // Check if the deletion was successful
                                            if (deleteSuccess > 0) {
                                                // Reload data in the TableView
                                                loadUserData();

                                                // Alert for successful delete
                                                AlertMessage alertMessage = new AlertMessage();
                                                alertMessage.successMessage("User deleted successfully!");
                                            } else {
                                                // Alert for failed delete
                                                AlertMessage alertMessage = new AlertMessage();
                                                alertMessage.errorMessage("Deletion failed. No rows affected.");
                                            }
                                        } else {
                                            // Alert for failed delete
                                            AlertMessage alertMessage = new AlertMessage();
                                            alertMessage.errorMessage("No user selected.");
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                });

                                // Update button click
                                updateButton.setOnMouseClicked(event -> {
                                    try {
                                        // Get the selected user
                                        User updatedUser = getTableView().getItems().get(getIndex());

                                        // Check if a user is selected
                                        if (updatedUser != null) {
                                            // Prepare the query
                                            String displayUserInfoQuery = "SELECT * FROM users WHERE id = ?";
                                            PreparedStatement preparedStatement =
                                                    Database.connect.prepareStatement(displayUserInfoQuery);
                                            preparedStatement.setString(1, updatedUser.getId());

                                            // Execute the query
                                            resultSet = preparedStatement.executeQuery();

                                            if (resultSet != null && resultSet.next()) {
                                                // Display update pane to update user data
                                                userManagement_HB.setVisible(false);
                                                updateUserPane.setVisible(true);

                                                // Display current user data before updating
                                                usernameUpdate.setText(resultSet.getString("username"));
                                                fullnameUpdate.setText(resultSet.getString("name"));
                                                passwordUpdate.setText(resultSet.getString("password"));
                                                emailUpdate.setText(resultSet.getString("email"));
                                                phonenumberUpdate.setText(resultSet.getString("phonenumber"));

                                                // Save new user data when save button is clicked
                                                saveButtonUpdate.setOnAction(event1 -> {
                                                    try {
                                                        // Get new data of text field that user typed in update pane
                                                        String username = usernameUpdate.getText();
                                                        String fullname = fullnameUpdate.getText();
                                                        String password = passwordUpdate.getText();
                                                        String email = emailUpdate.getText();
                                                        String phonenumber = phonenumberUpdate.getText();

                                                        // Update user details in the database
                                                        String updateQuery = "UPDATE users " +
                                                                "SET username = ?, " +
                                                                "name = ?, " +
                                                                "password = ?, " +
                                                                "email = ?, " +
                                                                "phonenumber = ? WHERE id = ?";
                                                        PreparedStatement updateStatement =
                                                                Database.connect.prepareStatement(updateQuery);

                                                        updateStatement.setString(1, username);
                                                        updateStatement.setString(2, fullname);
                                                        updateStatement.setString(3, password);
                                                        updateStatement.setString(4, email);
                                                        updateStatement.setString(5, phonenumber);
                                                        updateStatement.setString(6, updatedUser.getId());

                                                        // Execute the update query
                                                        int updateSuccess = updateStatement.executeUpdate();

                                                        // Check if update is success
                                                        if (updateSuccess > 0) {
                                                            // Update the user object
                                                            updatedUser.setUsername(username);
                                                            updatedUser.setName(fullname);
                                                            updatedUser.setPassword(password);
                                                            updatedUser.setEmail(email);
                                                            updatedUser.setPhoneNumber(phonenumber);

                                                            // Reload data in the TableView
                                                            loadUserData();

                                                            // Hide the update pane
                                                            updateUserPane.setVisible(false);

                                                            // Alert for successful update
                                                            AlertMessage alertMessage = new AlertMessage();
                                                            alertMessage.successMessage("User information updated successfully!");
                                                        } else {
                                                            // Alert for failed update
                                                            AlertMessage alertMessage = new AlertMessage();
                                                            alertMessage.errorMessage("Updated failed. No rows affected.");
                                                        }
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                });

                                                // Cancel update and keep current user data when cancel button is clicked
                                                cancelUpdateButton.setOnAction(event1 -> {
                                                    try {
                                                        // Not do anything and hide update user pane
                                                        updateUserPane.setVisible(false);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                });
                                            } else {
                                                // Alert for not found user
                                                AlertMessage alertMessage = new AlertMessage();
                                                alertMessage.errorMessage("User not found.");
                                            }
                                        } else {
                                            // Alert for not select user
                                            AlertMessage alertMessage = new AlertMessage();
                                            alertMessage.errorMessage("No user selected.");
                                        }
                                    } catch(Exception e) {
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
                    return cell;
                };

        optionsCol.setCellFactory(cellFactory);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            UserManagement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
