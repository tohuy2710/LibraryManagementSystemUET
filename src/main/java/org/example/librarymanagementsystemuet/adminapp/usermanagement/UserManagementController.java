package org.example.librarymanagementsystemuet.adminapp.usermanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.exception.LogicException;
import org.example.librarymanagementsystemuet.obj.*;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static org.example.librarymanagementsystemuet.Database.connectDB;
import static org.example.librarymanagementsystemuet.obj.Account.*;

public class UserManagementController implements Initializable {

    public Label labelUpdate, vipPointLabel;

    @FXML
    private Button cancelUpdateButton, saveUpdateButton, viewUserDetailButton, backButton, saveButton1;

    @FXML
    private TableColumn<User, String> usernameCol, emailCol, passwordCol,
            fullnameCol, userIdCol, phoneNumberCol, optionsCol;

    @FXML
    private ImageView userAvatar;

    @FXML
    private TextField usernameUpdate, emailUpdate, passwordUpdate,
            phoneNumberUpdate, emailField, fullNameField,
            passwordField, phoneNumberField, userIdField,
            usernameField, answerField, vipPointField;

    @FXML
    private AnchorPane updateUserPane;

    @FXML
    private TableView<User> userManagement_TV;

    @FXML
    private TableColumn<UserRequest, String> bookIdCol_UserRequestTable, createdTimeCol_UserRequestTable,
            lastUpdateTimeCol_UserRequestTable, requestIdCol_UserRequestTable,
            statusCol_UserRequestTable, numberOfRequestsCol_UserRequestTable;

    @FXML
    private TableView<BorrowRecord> borrowBook_TV;

    @FXML
    private TableColumn<BorrowRecord, String> dueDateCol_borrowBookTable, requestIdCol_BorrowBookTable,
            returnDateCol_borrowBookTable, startDateCol_borrowBookTable;

    @FXML
    private ChoiceBox<String> questionField;

    @FXML
    private TableView<UserRequest> userRequest_TV;

    @FXML
    private HBox viewUserDetailPane_HB;

    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private final ObservableList<UserRequest> userRequestList = FXCollections.observableArrayList();
    private final ObservableList<BorrowRecord> borrowBookList = FXCollections.observableArrayList();
    private ResultSet resultSet;

    private final String[] questionList = {"Who is your crush?",
            "How many of ex do you have?",
            "What is the name of your first pet?",
            "What is the name of your first school?",
            "What is your favorite movie?",
            "What is your favorite book?",
            "What is your favorite food?",
            "What is your favorite color?"};

    public static final String LOAD_FULL_DATA = "load-full-data";
    public static final String LOAD_DATA_BY_INFO = "load-any-data";

    public void setUserData(ResultSet resultSet) throws SQLException {
        while (resultSet != null && resultSet.next()) {
            User user = UserFactory.createUser(
                    resultSet.getString("id"),
                    resultSet.getString("username"),
                    resultSet.getString("name"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("registered_date"),
                    resultSet.getString("phonenumber"),
                    resultSet.getString("question"),
                    resultSet.getString("answer"),
                    resultSet.getInt("hmCoin"),
                    resultSet.getString("expiredVipDate")
            );
            userList.add(user);
        }
    }

    public void setUserRequestData(ResultSet resultSet) throws SQLException, LogicException {
        while (resultSet != null && resultSet.next()) {
            UserRequest userRequest = new UserRequest();
            userRequest.setRequestID(resultSet.getString("id"));
            userRequest.setBookID(resultSet.getString("bookId"));
            userRequest.setStatus(resultSet.getString("status"));
            userRequest.setCreatedTime(resultSet.getString("createdTime"));
            userRequest.setLastUpdatedTime(resultSet.getString("lastUpdatedTime"));
            userRequest.setNoOfBooks(resultSet.getString("noOfBooks"));
            userRequestList.add(userRequest);
        }
    }

    public void setBorrowBookData(ResultSet resultSet, String userID) throws SQLException {
        while (resultSet != null && resultSet.next()) {
            BorrowRecord borrowRecord = new BorrowRecord(resultSet.getString("request_id"),
                    userID,
                    resultSet.getString("start_date"),
                    resultSet.getString("due_date"),
                    resultSet.getString("return_date"));
            borrowBookList.add(borrowRecord);
        }
    }

    public void loadFullUsersData() throws SQLException {
        userList.clear();
        String query = "SELECT * FROM users";
        Connection conn = connectDB();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        setUserData(resultSet);
        userManagement_TV.setItems(userList);
    }

    public void loadUserDataByInfo(String userInfo) throws SQLException {
        userList.clear();
        if (userInfo == null || userInfo.isEmpty()) {
            return;
        }
        String query = "SELECT * FROM users WHERE id LIKE ? OR username LIKE ?";
        Connection conn = connectDB();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, "%" + userInfo + "%");
        preparedStatement.setString(2, "%" + userInfo + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        setUserData(resultSet);
        userManagement_TV.setItems(userList);
    }

    public void loadUserRequestTable(String userID) throws SQLException, LogicException {
        requestIdCol_UserRequestTable.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        bookIdCol_UserRequestTable.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        statusCol_UserRequestTable.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdTimeCol_UserRequestTable.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        lastUpdateTimeCol_UserRequestTable.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedTime"));
        numberOfRequestsCol_UserRequestTable.setCellValueFactory(new PropertyValueFactory<>("noOfBooks"));

        String query = "SELECT * FROM usersrequest WHERE userId = ?";
        userRequestList.clear();

        Connection conn = connectDB();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, userID);
        ResultSet resultSet = preparedStatement.executeQuery();
        setUserRequestData(resultSet);
        userRequest_TV.setItems(userRequestList);
    }

    public void loadBorrowBookTable(String userID) throws SQLException, LogicException {
        requestIdCol_BorrowBookTable.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        startDateCol_borrowBookTable.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        dueDateCol_borrowBookTable.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateCol_borrowBookTable.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        String query = "SELECT * FROM borrowbooks WHERE request_id IN "
                + "(SELECT id FROM usersrequest WHERE userId = ?)";
        borrowBookList.clear();

        Connection conn = connectDB();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, userID);
        resultSet = preparedStatement.executeQuery();
        setBorrowBookData(resultSet, userID);
        borrowBook_TV.setItems(borrowBookList);
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

        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory
                = (TableColumn<User, String> param) -> new TableCell<>() {
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
                        User updatedUser = getTableView().getItems().get(getIndex());

                        String displayUserInfoQuery = "SELECT * FROM users WHERE id = ?";
                        Connection conn = connectDB();
                        PreparedStatement preparedStatement = null;
                        try {
                            preparedStatement = conn.prepareStatement(displayUserInfoQuery);
                            preparedStatement.setString(1, updatedUser.getId());
                            resultSet = preparedStatement.executeQuery();
                            questionField.getItems().addAll(questionList);

                            if (resultSet != null && resultSet.next()) {
                                userIdField.setText(resultSet.getString("id"));
                                usernameField.setText(resultSet.getString("username"));
                                fullNameField.setText(resultSet.getString("name"));
                                passwordField.setText(resultSet.getString("password"));
                                emailField.setText(resultSet.getString("email"));
                                phoneNumberField.setText(resultSet.getString("phoneNumber"));
                                questionField.setValue(resultSet.getString("question"));
                                answerField.setText(resultSet.getString("answer"));
                                if (resultSet.getString("avatarImg") == null) {
                                    Database.setImageByLink(userAvatar,
                                            getClass().getResource("/asset/img/user-avatar.png").toString());
                                } else {
                                    Database.setImageByLink(userAvatar, resultSet.getString("avatarImg"));
                                }

                                AtomicReference<File> file = new AtomicReference<>();
                                System.out.println(updatedUser.getUserType());
                                if (updatedUser.getHmCoin() > 0) {
                                    userAvatar.setOnMouseClicked(event1 -> {
                                        FileChooser fileChooser = new FileChooser();
                                        fileChooser.setTitle("Open Resource File");

                                        // Set the file extension filter
                                        FileChooser.ExtensionFilter extensionFilter =
                                                new FileChooser.ExtensionFilter("Image Files",
                                                        "*.png",
                                                        "*.jpg",
                                                        "*.jpeg",
                                                        "*.gif");
                                        fileChooser.getExtensionFilters().add(extensionFilter);

                                        // Show open file dialog
                                        file.set(fileChooser.showOpenDialog(viewUserDetailPane_HB.getScene().getWindow()));

                                        if (file.get() != null) {
                                            Image image = new Image(file.get().toURI().toString());
                                            userAvatar.setImage(image);
                                        }
                                    });
                                }

                                userManagement_TV.setVisible(false);
                                viewUserDetailPane_HB.setVisible(true);

                                saveButton1.setOnAction(event1 -> {
                                    try {
                                        String updateQuery = "UPDATE users " +
                                                "SET username = ?, " +
                                                "password = ?, " +
                                                "name = ?," +
                                                "email = ?, " +
                                                "phonenumber = ?, " +
                                                "question = ?, " +
                                                "answer = ?, " +
                                                "avatarImg = ?" +
                                                "WHERE id = ?";

                                        try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                                            AlertMessage alertMessage = new AlertMessage();
                                            if (usernameField.getText() == null
                                                    || passwordField.getText() == null
                                                    || emailField.getText() == null
                                                    || phoneNumberField.getText() == null
                                                    || fullNameField.getText() == null
                                                    || questionField.getValue() == null
                                                    || answerField.getText() == null) {
                                                alertMessage.errorMessage("Please fill in all fields.");
                                            } else if (!isValidUsername(usernameField.getText())) {
                                                alertMessage.errorMessage("Username must be between 6 and 12 characters.");
                                            } else if (!isValidPassword(passwordField.getText())) {
                                                alertMessage.errorMessage("Password must contain at least one uppercase letter, " +
                                                        "one lowercase letter, one digit, one special character, " +
                                                        "and is at least 8 characters long.");
                                            } else if (!isValidEmail(emailField.getText())) {
                                                alertMessage.errorMessage("Invalid email format.");
                                            } else if (!isValidPhoneNumber(phoneNumberField.getText())) {
                                                alertMessage.errorMessage("Invalid phone number format.");
                                            } else {
                                                updateStatement.setString(1, usernameField.getText());
                                                updateStatement.setString(2, passwordField.getText());
                                                updateStatement.setString(3, fullNameField.getText());
                                                updateStatement.setString(4, emailField.getText());
                                                updateStatement.setString(5, phoneNumberField.getText());
                                                updateStatement.setString(6, questionField.getValue());
                                                updateStatement.setString(7, answerField.getText());
                                                if (updatedUser.getHmCoin() > 0) {
                                                    updateStatement.setString(8, file.get().toURI().toString());
                                                }
                                                updateStatement.setString(9, updatedUser.getId());

                                                int updateSuccess = updateStatement.executeUpdate();
                                                if (updateSuccess > 0) {
                                                    alertMessage.successMessage("User information updated successfully!");
                                                } else {
                                                    alertMessage.errorMessage("Update failed");
                                                }
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        try {
                            loadBorrowBookTable(updatedUser.getId());
                        } catch (SQLException | LogicException e) {
                            e.printStackTrace();
                        }

                        try {
                            loadUserRequestTable(updatedUser.getId());
                        } catch (SQLException | LogicException e) {
                            e.printStackTrace();
                        }

                        backButton.setOnAction(event1 -> {
                            userManagement_TV.setVisible(true);
                            viewUserDetailPane_HB.setVisible(false);
                        });
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
            userManagement_TV.setVisible(true);
            updateUserPane.setVisible(false);
            viewUserDetailPane_HB.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}