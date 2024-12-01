package org.example.librarymanagementsystemuet.adminapp.penaltymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.BookDetail;
import org.example.librarymanagementsystemuet.obj.UserPenaltyRecord;

import java.sql.Connection;
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
    private TableColumn<UserPenaltyRecord, Void> colDetail;
    @FXML
    private TableColumn<UserPenaltyRecord, Double> paidAmountColumn;

    @FXML
    private TableColumn<UserPenaltyRecord, Double> unpaidAmountColumn;

    @FXML
    private TextField searchField;

    private ObservableList<UserPenaltyRecord> masterData = FXCollections.observableArrayList();

    //done
    @FXML
    public void initialize() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBooksNotReturned.setCellValueFactory(new PropertyValueFactory<>("booksNotReturned"));
        colFineAmount.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));
        paidAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        unpaidAmountColumn.setCellValueFactory(new PropertyValueFactory<>("unpaidAmount"));

        addDetailButtonToTable();
        loadPenaltyData();

        FilteredList<UserPenaltyRecord> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return record.getUserName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        userTable.setItems(filteredData);
    }

    private void addDetailButtonToTable() {
        colDetail.setCellFactory(param -> new TableCell<>() {
            private final Button detailButton = new Button("Detail");
            private final HBox hbox = new HBox(detailButton);

            {
                hbox.setAlignment(Pos.CENTER);
                detailButton.getStyleClass().add("button6");
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

    // dk pay date >= 0 -> view
    private void showBookDetails(String userId) {
        try (Connection conn = Database.connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT ur.id AS requestId, b.name, br.start_date, br.return_date, br.due_date, ur.noOfBooks " +
                             "FROM borrowbooks br " +
                             "JOIN usersrequest ur ON br.request_id = ur.id " +
                             "JOIN books b ON b.id = ur.bookId " +
                             "WHERE ur.userId = " + userId)) {

            TableView<BookDetail> bookDetailTable = new TableView<>();
            bookDetailTable.setPrefWidth(1000);

            TableColumn<BookDetail, Integer> colRequestId = new TableColumn<>("Request ID");
            colRequestId.setPrefWidth(100);
            TableColumn<BookDetail, String> colName = new TableColumn<>("Name");
            colName.setPrefWidth(200);
            TableColumn<BookDetail, String> colStartDate = new TableColumn<>("Start Date");
            colStartDate.setPrefWidth(150);
            TableColumn<BookDetail, String> colReturnDate = new TableColumn<>("Return Date");
            colReturnDate.setPrefWidth(150);
            TableColumn<BookDetail, String> colDueDate = new TableColumn<>("Due Date");
            colDueDate.setPrefWidth(150);
            TableColumn<BookDetail, Integer> colNoOfBooks = new TableColumn<>("No of Books");
            colNoOfBooks.setPrefWidth(100);
            TableColumn<BookDetail, String> colDaysLate = new TableColumn<>("Days Late");
            colDaysLate.setPrefWidth(150);

            colRequestId.setCellValueFactory(new PropertyValueFactory<>("requestId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
            colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
            colNoOfBooks.setCellValueFactory(new PropertyValueFactory<>("noOfBooks"));
            colDaysLate.setCellValueFactory(new PropertyValueFactory<>("daysLate"));

            bookDetailTable.getStyleClass().add("table-view");
            bookDetailTable.getColumns().addAll(colRequestId, colName, colStartDate, colReturnDate, colDueDate, colNoOfBooks, colDaysLate);

            while (rs.next()) {
                String returnDate = rs.getString("return_date");
                long daysLate;
                if (returnDate == null) {
                    returnDate = "Not returned";
                    daysLate = java.time.Duration.between(rs.getTimestamp("due_date").toInstant(), java.time.Instant.now()).toDays();
                } else {
                    daysLate = java.time.Duration.between(rs.getTimestamp("due_date").toInstant(), rs.getTimestamp("return_date").toInstant()).toDays();
                }
                //ok
                if (daysLate >= 0) {
                    bookDetailTable.getItems().add(new BookDetail(
                            rs.getInt("requestId"),
                            rs.getString("name"),
                            rs.getString("start_date"),
                            returnDate,
                            rs.getString("due_date"),
                            rs.getInt("noOfBooks"),
                            String.valueOf(daysLate)
                    ));
                }
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(new HBox(bookDetailTable)));
            stage.setTitle("Book Details");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //check query
    private void loadPenaltyData() {
        try (Connection conn = Database.connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT u.id, u.username, COUNT(ur.id) AS booksNotReturned, " +
                             "(SUM(DATEDIFF(IFNULL(br.return_date, NOW()), br.start_date) * 1000 + b.pageCount * 5) * ur.noOfbooks) AS fineAmount, " +
                             "IFNULL(p.money, 0) AS paidAmount, " +
                             "((SUM(DATEDIFF(IFNULL(br.return_date, NOW()), br.start_date) * 1000 + b.pageCount * 5) * ur.noOfbooks) - IFNULL(p.money, 0)) AS unpaidAmount " +
                             "FROM users u " +
                             "JOIN usersrequest ur ON u.id = ur.userId " +
                             "JOIN borrowbooks br ON ur.id = br.request_id " +
                             "JOIN books b ON ur.bookId = b.id " +
                             "LEFT JOIN penaltypayments p ON p.userId = u.id " +
                             "WHERE IFNULL(br.return_date, NOW()) > br.due_date " +
                             "GROUP BY u.id, u.username;")) {

            while (rs.next()) {
                UserPenaltyRecord record = new UserPenaltyRecord(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("booksNotReturned"),
                        rs.getDouble("fineAmount"),
                        rs.getDouble("paidAmount"),
                        rs.getDouble("unpaidAmount")
                );
                masterData.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}