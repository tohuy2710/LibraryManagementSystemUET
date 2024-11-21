package org.example.librarymanagementsystemuet.adminapp.usermanagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PenaltyListViewController {

    @FXML
    private TableView<UserPenaltyRecord> userTable;

    @FXML
    private TableColumn<UserPenaltyRecord, String> colUserId;

    @FXML
    private TableColumn<UserPenaltyRecord, String> colUserName;

    @FXML
    private TableColumn<UserPenaltyRecord, String> colBooksNotReturned;

    @FXML
    private TableColumn<UserPenaltyRecord, String> colFineAmount;

    @FXML
    public void initialize() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBooksNotReturned.setCellValueFactory(new PropertyValueFactory<>("booksNotReturned"));
        colFineAmount.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));

        loadPenaltyData();
    }

    private void loadPenaltyData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/library_management_system_uet", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT u.id, u.username, COUNT(ur.id) AS booksNotReturned, " +
                             "SUM(DATEDIFF(br.return_date, br.start_date) * 1000 + b.pageCount * 5) AS fineAmount " +
                             "FROM users u " +
                             "JOIN usersrequest ur ON u.id = ur.userId " +
                             "JOIN borrowbooks br ON ur.id = br.request_id " +
                             "JOIN books b ON ur.bookId = b.id " +
                             "WHERE br.return_date > br.due_date " +
                             "GROUP BY u.id, u.username")) {

            while (rs.next()) {
                UserPenaltyRecord record = new UserPenaltyRecord(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("booksNotReturned"),
                        rs.getDouble("fineAmount")
                );
                userTable.getItems().add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class UserPenaltyRecord {
        private final StringProperty userId;
        private final StringProperty userName;
        private final StringProperty booksNotReturned;
        private final StringProperty fineAmount;

        public UserPenaltyRecord(int userId, String userName, int booksNotReturned, double fineAmount) {
            this.userId = new SimpleStringProperty(String.valueOf(userId));
            this.userName = new SimpleStringProperty(userName);
            this.booksNotReturned = new SimpleStringProperty(String.valueOf(booksNotReturned));
            this.fineAmount = new SimpleStringProperty(String.valueOf(fineAmount));
        }

        public String getUserId() {
            return userId.get();
        }

        public StringProperty userIdProperty() {
            return userId;
        }

        public String getUserName() {
            return userName.get();
        }

        public StringProperty userNameProperty() {
            return userName;
        }

        public String getBooksNotReturned() {
            return booksNotReturned.get();
        }

        public StringProperty booksNotReturnedProperty() {
            return booksNotReturned;
        }

        public String getFineAmount() {
            return fineAmount.get();
        }

        public StringProperty fineAmountProperty() {
            return fineAmount;
        }
    }
}