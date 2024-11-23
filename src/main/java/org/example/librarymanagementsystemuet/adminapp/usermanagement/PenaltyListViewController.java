package org.example.librarymanagementsystemuet.adminapp.usermanagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.librarymanagementsystemuet.obj.BookDetail;
import org.example.librarymanagementsystemuet.obj.UserPenaltyRecord;

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
    //
    @FXML
    private TableColumn<UserPenaltyRecord, Void> colDetail;

    private void addDetailButtonToTable() {
        colDetail.setCellFactory(param -> new TableCell<>() {
            private final Button detailButton = new Button("Detail");
            private final HBox hbox = new HBox(detailButton);

            {
                hbox.setAlignment(Pos.CENTER);
                detailButton.getStyleClass().add("button-detail");
                detailButton.setOnAction(event -> {
                    UserPenaltyRecord record = getTableView().getItems().get(getIndex());
                    showBookDetails(record.getUserId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        });
    }

    private void showBookDetails(String userId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/library_management_system_uet", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT b.name, br.start_date, br.return_date, br.due_date " +
                             "FROM borrowbooks br " +
                             "JOIN usersrequest ur ON br.request_id = ur.id " +
                             "JOIN books b ON b.id = ur.bookId " +
                             "WHERE ur.userId = " + userId)) {

            TableView<BookDetail> bookDetailTable = new TableView<>();
            bookDetailTable.setPrefWidth(800); // Set preferred width for the TableView

            TableColumn<BookDetail, String> colName = new TableColumn<>("Name");
            colName.setPrefWidth(200); // Set preferred width for the column
            TableColumn<BookDetail, String> colStartDate = new TableColumn<>("Start Date");
            colStartDate.setPrefWidth(200); // Set preferred width for the column
            TableColumn<BookDetail, String> colReturnDate = new TableColumn<>("Return Date");
            colReturnDate.setPrefWidth(200); // Set preferred width for the column
            TableColumn<BookDetail, String> colDueDate = new TableColumn<>("Due Date");
            colDueDate.setPrefWidth(200); // Set preferred width for the column

            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
            colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

            bookDetailTable.getColumns().addAll(colName, colStartDate, colReturnDate, colDueDate);

            while (rs.next()) {
                bookDetailTable.getItems().add(new BookDetail(
                        rs.getString("name"),
                        rs.getString("start_date"),
                        rs.getString("return_date"),
                        rs.getString("due_date")
                ));
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(new HBox(bookDetailTable)));
            stage.setTitle("Book Details");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBooksNotReturned.setCellValueFactory(new PropertyValueFactory<>("booksNotReturned"));
        colFineAmount.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));

        addDetailButtonToTable();
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
}