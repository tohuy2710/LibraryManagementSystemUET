package org.example.librarymanagementsystemuet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import package1.AlertMessage;
import package1.LogicException;
import package1.UserRequest;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static package1.UserRequest.*;

public class UserRequestManagementController implements Initializable {

    @FXML
    private TableColumn<UserRequest, String> bookIDCol;

    @FXML
    private TableColumn<UserRequest, String> bookNameCol;

    @FXML
    private TableColumn<UserRequest, String> createdTimeCol;

    @FXML
    private TableColumn<UserRequest, String> lastUpdatedTimeCol;

    @FXML
    private TableColumn<UserRequest, String> requestIdCol;

    @FXML
    private TableColumn<UserRequest, String> statusCol;

    @FXML
    private TableColumn<UserRequest, String> userIDCol;

    @FXML
    private TableView<UserRequest> userRequestTableView;

    @FXML
    private CheckBox pendingCheckBox;

    @FXML
    private CheckBox approvedCheckBox;

    @FXML
    private CheckBox deniedCheckBox;

    @FXML
    private CheckBox overdueCheckBox;

    @FXML
    private CheckBox returnedCheckBox;

    @FXML
    private CheckBox cancelledCheckBox;

    @FXML
    private CheckBox onLoanCheckBox;

    @FXML
    private TextField searchTextField;

    private ObservableList<UserRequest> userRequestList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupDatabaseAndFetchData("SELECT * FROM usersrequest " +
                "LEFT JOIN books ON usersrequest.bookId = books.id" +
                " WHERE status in (" +
                "'Pending', 'Approved for borrowing', 'Overdue for return book')");

        initializeColumns();
    }

    public void refreshData(ActionEvent event) {
        String query = "SELECT * FROM usersrequest LEFT JOIN books ON usersrequest.bookId = books.id WHERE";

        if (pendingCheckBox.isSelected()) {
            query += " status = 'Pending' OR";
        }

        if (approvedCheckBox.isSelected()) {
            query += " status = 'Approved for borrowing' OR";
        }

        if (deniedCheckBox.isSelected()) {
            query += " status = 'Denied for borrowing' OR";
        }

        if (overdueCheckBox.isSelected()) {
            query += " status = 'Overdue for return book' OR";
        }

        if (returnedCheckBox.isSelected()) {
            query += " status = 'Book has been returned' OR";
        }

        if (cancelledCheckBox.isSelected()) {
            query += " status = 'Cancelled by admin' OR";
        }

        if (onLoanCheckBox.isSelected()) {
            query += " status = 'Book is currently on loan' OR";
        }

        if (returnedCheckBox.isSelected()) {
            query += " status = 'Book has been returned' OR";
        }

        if (cancelledCheckBox.isSelected()) {
            query += " status = 'Cancelled by admin' OR";
        }

        if (query.endsWith("WHERE")) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage("Please select at least one status to filter the data.");
        } else {
            setupDatabaseAndFetchData(query.substring(0, query.length() - 2));
        }
    }

    public void search(ActionEvent event) {
        if (searchTextField.getText().isEmpty() || searchTextField.getText().isBlank()) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage("Please enter a search query.");
        } else {
            String searchQuery = searchTextField.getText();
            setupDatabaseAndFetchData("SELECT * FROM usersrequest " +
                    "LEFT JOIN books ON usersrequest.bookId = books.id " +
                    "WHERE bookId LIKE '%" + searchQuery + "%'" +
                    "OR userId LIKE '%" + searchQuery + "%'" +
                    "OR usersrequest.id LIKE '%" + searchQuery+ "%'" +
                    "OR books.name LIKE '%" + searchQuery + "%'");
        }

    }

    private void setupDatabaseAndFetchData(String query) {
        Database.connect = Database.connectDB();

        userRequestList.clear();
        userRequestTableView.getItems().clear();

        try {
            Database.prepare = Database.connect.prepareStatement(query);
            Database.result = Database.prepare.executeQuery();

            while (Database.result != null && Database.result.next()) {
                UserRequest userRequest = new UserRequest();
                userRequest.setRequestID(Database.result.getString("id"));
                userRequest.setBookID(Database.result.getString("bookId"));
                userRequest.setBookName(Database.result.getString("name"));
                userRequest.setUserID(Database.result.getString("userId"));
                userRequest.setCreatedTime(Database.result.getString("createdTime"));
                userRequest.setLastUpdatedTime(Database.result.getString("lastUpdatedTime"));
                userRequest.setPreviousStatus(Database.result.getString("status"));
                userRequest.setStatus(Database.result.getString("status"));
                userRequestList.add(userRequest);
            }
        } catch (SQLException | LogicException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (Database.result != null) Database.result.close();
                if (Database.prepare != null) Database.prepare.close();
                if (Database.connect != null) Database.connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        userRequestTableView.setItems(userRequestList);
    }

    private void initializeColumns() {
        requestIdCol.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        createdTimeCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        lastUpdatedTimeCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedTime"));

        // Update status options to match the new descriptive statuses
        List<String> statusOptions = Arrays.asList(
                APPROVED_FOR_BORROWING,
                DENIED_FOR_BORROWING,
                CANCELLED_BY_ADMIN,
                OVERDUE_FOR_RETURN,
                BOOK_RETURNED,
                PENDING,
                ON_LOAN
        );

        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> {
            ComboBoxTableCell<UserRequest, String> cell =
                    new ComboBoxTableCell<>(FXCollections.observableArrayList(statusOptions)) {
                        @Override
                        public void updateItem(String status, boolean empty) {
                            super.updateItem(status, empty);
                            if (status == null || empty) {
                                setText(null);
                                setStyle("");
                            } else {
                                setText(status);

                                getStyleClass().clear();  // Clear previous styles
                                getStyleClass().add("status-common");  // Apply common style

                                // Apply CSS based on the updated status values
                                switch (status) {
                                    case APPROVED_FOR_BORROWING:
                                        getStyleClass().add("status-active");
                                        break;
                                    case DENIED_FOR_BORROWING:
                                        getStyleClass().add("status-denied");
                                        break;
                                    case CANCELLED_BY_ADMIN:
                                        getStyleClass().add("status-canceled");
                                        break;
                                    case OVERDUE_FOR_RETURN:
                                        getStyleClass().add("status-overdue");
                                        break;
                                    case BOOK_RETURNED:
                                        getStyleClass().add("status-completed");
                                        break;
                                    case PENDING:
                                        getStyleClass().add("status-in-progress");
                                        break;
                                    case ON_LOAN:
                                        getStyleClass().add("status-reserved");
                                        break;
                                    default:
                                        getStyleClass().clear(); // Remove any previous styles
                                        break;
                                }
                            }
                        }
                    };
            return cell;
        });

        // Allow editing the status column
        statusCol.setOnEditCommit(event -> {
            UserRequest userRequest = event.getRowValue();
            try {
                userRequest.setStatus(event.getNewValue());
            } catch (LogicException e) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage(e.getMessage());
            }
            userRequestTableView.refresh();
            if (!userRequest.getStatus().equals(userRequest.getPreviousStatus())) {
                updateStatus();
            }
        });

        userRequestTableView.setEditable(true);
    }



    public void updateStatus() {
        Database.connect = Database.connectDB();
        String queryUpdateStatus = "UPDATE usersrequest SET status = ? WHERE id = ?";
        String queryAddToBorrowBooks = "INSERT INTO borrowbooks (request_id) VALUES (?)";

        try {
            for (UserRequest userRequest : userRequestList) {
                Database.prepare = Database.connect.prepareStatement(queryUpdateStatus);
                Database.prepare.setString(1, userRequest.getStatus());
                Database.prepare.setString(2, userRequest.getRequestID());
                Database.prepare.executeUpdate();

                if (userRequest.getStatus().equals(ON_LOAN)) {
                    Database.prepare = Database.connect.prepareStatement(queryAddToBorrowBooks);
                    Database.prepare.setString(1, userRequest.getRequestID());
                    Database.prepare.executeUpdate();
                } else if (userRequest.getStatus().equals(BOOK_RETURNED)) {
                    Database.prepare = Database.connect
                                    .prepareStatement("UPDATE borrowbooks " +
                                            "SET return_date = NOW() WHERE request_id = ?");
                    Database.prepare.setString(1, userRequest.getRequestID());
                    Database.prepare.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (Database.prepare != null) Database.prepare.close();
                if (Database.connect != null) Database.connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
