package org.example.librarymanagementsystemuet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.example.librarymanagementsystemuet.exception.LogicException;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.Book;
import org.example.librarymanagementsystemuet.obj.BorrowRecord;
import org.example.librarymanagementsystemuet.obj.UserRequest;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.example.librarymanagementsystemuet.Database.connectDB;
import static org.example.librarymanagementsystemuet.obj.Account.*;
import static org.example.librarymanagementsystemuet.obj.Account.isValidPhoneNumber;

public class UserDetailViewPaneController {

    @FXML
    private TextField answerField, emailField, fullNameField,
            passwordField, phoneNumberField, userIdField, usernameField, expiredVIPDateField;

    @FXML
    private Button saveButton;

    @FXML
    private TableColumn<UserRequest, String> bookIdCol_UserRequestTable;

    @FXML
    private TableView<BorrowRecord> borrowBook_TV;

    @FXML
    private TableColumn<UserRequest, String> lastUpdateTimeCol_UserRequestTable,
            requestIdCol_UserRequestTable, statusCol_UserRequestTable, createdTimeCol_UserRequestTable,
            numberOfRequestsCol_UserRequestTable;

    @FXML
    private TableColumn<Book, String> bookNameCol_UserRequestTable;

    @FXML
    private ChoiceBox<String> questionField;

    @FXML
    private TableColumn<BorrowRecord, String> requestIdCol_BorrowBookTable,
            returnDateCol_borrowBookTable, startDateCol_borrowBookTable, dueDateCol_borrowBookTable;

    @FXML
    private TableView<UserRequest> userRequest_TV;

    @FXML
    private HBox viewUserDetailPane_HB;

    @FXML
    private ImageView userAvatar;


    private final ObservableList<UserRequest> userRequestList = FXCollections.observableArrayList();
    private final ObservableList<BorrowRecord> borrowBookList = FXCollections.observableArrayList();
    private ResultSet resultSet;

    private final String[] questionList = {
            "Who is your crush?",
            "How many of ex do you have?",
            "What is the name of your first pet?",
            "What is the name of your first school?",
            "What is your favorite movie?",
            "What is your favorite book?",
            "What is your favorite food?",
            "What is your favorite color?"};

    public void initialize() throws SQLException, LogicException, IOException {
        viewUserDetailPane_HB.setVisible(true);
        handleAllAction();
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

    public void handleUpdateAction(String userId) throws SQLException {
        String displayUserInfoQuery = "SELECT * FROM users WHERE id = ?";
        Connection conn = connectDB();
        PreparedStatement preparedStatement = conn.prepareStatement(displayUserInfoQuery);
        preparedStatement.setString(1, userId);
        resultSet = preparedStatement.executeQuery();
        questionField.getItems().addAll(questionList);
        if (resultSet != null && resultSet.next()) {
            userIdField.setText(resultSet.getString("id") == null ? "" : resultSet.getString("id"));
            usernameField.setText(resultSet.getString("username") == null ? "" : resultSet.getString("username"));
            fullNameField.setText(resultSet.getString("name") == null ? "" : resultSet.getString("name"));
            passwordField.setText(resultSet.getString("password") == null ? "" : resultSet.getString("password"));
            emailField.setText(resultSet.getString("email") == null ? "" : resultSet.getString("email"));
            phoneNumberField.setText(resultSet.getString("phoneNumber") == null ? "" : resultSet.getString("phoneNumber"));
            questionField.setValue(resultSet.getString("question") == null ? "" : resultSet.getString("question"));
            answerField.setText(resultSet.getString("answer") == null ? "" : resultSet.getString("answer"));
            if (resultSet.getString("avatarImg") == null) {
                Database.setImageByLink(userAvatar,
                        getClass().getResource("/asset/img/user-avatar.png").toString());
            } else {
                Database.setImageByLink(userAvatar, resultSet.getString("avatarImg"));
            }

            AtomicReference<File> file = new AtomicReference<>();
            if (Objects.equals(UserSession.getInstance().getUserType(), "VIP")) {
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

            saveButton.setOnAction(event -> {
                try {
                    String updateQuery = "UPDATE users " +
                            "SET username = ?, " +
                            "password = ?, " +
                            "name = ?," +
                            "email = ?, " +
                            "phonenumber = ?, " +
                            "question = ?, " +
                            "answer = ? ," +
                            "avatarImg = ? " +
                            "WHERE id = ?";

                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                        AlertMessage alertMessage = new AlertMessage();
                        if (usernameField.getText().isEmpty()
                                || passwordField.getText().isEmpty()
                                || emailField.getText().isEmpty()
                                || phoneNumberField.getText().isEmpty()
                                || fullNameField.getText().isEmpty()
                                || questionField.getValue().isEmpty()
                                || answerField.getText().isEmpty()) {
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
                            if (UserSession.getInstance().getUserType() == "VIP") {
                                updateStatement.setString(8, file.get().toURI().toString());
                            }
                            updateStatement.setString(9, userId);

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
    }

    public void handleAllAction() {
        try {
            handleUpdateAction(UserSession.getInstance().getId());
            loadUserRequestTable(UserSession.getInstance().getId());
            loadBorrowBookTable(UserSession.getInstance().getId());
            viewUserDetailPane_HB.setVisible(true);
        } catch (SQLException | LogicException e) {
            e.printStackTrace();
        }
    }
}
