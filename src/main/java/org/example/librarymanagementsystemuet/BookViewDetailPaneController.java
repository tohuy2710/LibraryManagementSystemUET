package org.example.librarymanagementsystemuet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.librarymanagementsystemuet.Controller;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.Book;
import org.example.librarymanagementsystemuet.obj.BorrowRecord;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.example.librarymanagementsystemuet.Database.*;
import static org.example.librarymanagementsystemuet.Database.preparedStatement;

public class BookViewDetailPaneController extends Controller {

    @FXML
    private Label addedDateLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label avgRateLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextArea descriptionText;
    @FXML
    private TableColumn<BorrowRecord, String> dueDateCol;

    @FXML
    private Label idLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label laguageLabel;

    @FXML
    private Label lastUpdatedTimeLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label pageCountLabel;
    @FXML
    private Label publisherDateLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private TableColumn<BorrowRecord, String> returnDateCol;

    @FXML
    private TableColumn<BorrowRecord, String> startDateCol;

    @FXML
    private TableColumn<BorrowRecord, String> userIDCol;

    @FXML
    private TableView<BorrowRecord> borrowHistoryTable;

    @FXML
    private ImageView bookCoverImageView;

    @FXML
    private Controller parentController;

    @FXML
    private TextField noWantBorrowTextField;

    private ObservableList<BorrowRecord> borrowRecordObservableList = FXCollections.observableArrayList();

    public Controller getParentController() {
        return parentController;
    }

    public void BorrowBook(ActionEvent event) {
        if (!noWantBorrowTextField.getText().matches(NUMBER_REGEX)
                || noWantBorrowTextField.getText().isEmpty()
                || noWantBorrowTextField.getText().isBlank()) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage("Please enter a valid number!");
            noWantBorrowTextField.setText("1");
            return;
        }
        else {
            int num = Integer.parseInt(noWantBorrowTextField.getText());
            if (num <= 0) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage("Please enter a valid number!");
                noWantBorrowTextField.setText("1");
                return;
            } else if (num > Integer.parseInt(quantityLabel.getText().substring(10))) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage("Your request is over the current quantity of this book in library!");
                noWantBorrowTextField.setText("1");
                return;
            } else if (num > UserSession.getInstance().getHmCoin()) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage("You don't have enough HmCoin to sent request! Please buy HmCoin to request!");
                noWantBorrowTextField.setText("1");
                return;
            }
            UserSession.getInstance().setHmCoin(UserSession.getInstance().getHmCoin() - num);
            sentRequest(UserSession.getInstance().getId(), idLabel.getText().substring(4), num);
        }
    }

    public void sentRequest(String userId, String bookId, int quantity) {
        String query1 = "INSERT INTO usersrequest (userId, bookId, noOfBooks) VALUES ("
                + userId + ", " + bookId + ", " + quantity + ")";
        String query2 = "UPDATE users SET hmCoin = " + UserSession.getInstance().getHmCoin()
                + " WHERE id = " + userId;
        connect = connectDB();
        try {
            preparedStatement = connect.prepareStatement(query1);
            preparedStatement = connect.prepareStatement(query2);
            preparedStatement.executeUpdate();
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.successMessage("Sent request successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    public void loadBookDetailByID(int BookId) {
        String query = "SELECT * FROM books WHERE id = " + BookId;
        Database.connect = connectDB();
        try {
            preparedStatement = Database.connect.prepareStatement(query);
            Database.result = preparedStatement.executeQuery();

            if (Database.result != null && Database.result.next()) {
                Book book = setBookInfo();
                setDetail(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setDetail(Book book) {
        idLabel.setText("ID: " + book.getId());
        nameLabel.setText(book.getName());
        authorLabel.setText("Author: " + book.getAuthors());
        isbnLabel.setText("ISBN: " + book.getIsbn());
        publisherLabel.setText("Publisher: " + book.getPublisher());
        addedDateLabel.setText("Added Date: " + book.getAddedDate());
        lastUpdatedTimeLabel.setText("Last Updated Time: " + book.getLastUpdateDate());
        categoryLabel.setText("Category: " + book.getCategory());
        locationLabel.setText("Location: " + book.getLocation());
        quantityLabel.setText("Quantity: " + book.getQuantity());
        avgRateLabel.setText("Average Rate: " + book.getAvgRate());
        pageCountLabel.setText("Page Count: " + book.getPageCount());
        laguageLabel.setText("Language: " + book.getLanguage());
        publisherDateLabel.setText("Publisher Date: " + book.getPublisherDate());
        descriptionText.setText(book.getDescription());
        Database.setImageByLink(bookCoverImageView, book.getImageLink());

        getBorrowInfo(book.getId());
    }

    public void getBorrowInfo(int bookId) {
        borrowHistoryTable.getItems().clear();
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT ur.id, ur.userId, br.start_date, br.due_date, br.return_date " +
                             "FROM borrowbooks br " +
                             "JOIN usersrequest ur ON br.request_id = ur.id " +
                             "WHERE ur.bookId = " + bookId)) {

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                        rs.getString("Id"),
                        rs.getString("userId"),
                        rs.getString("start_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date")
                );
                borrowRecordObservableList.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        borrowHistoryTable.setItems(borrowRecordObservableList);
    }

    @FXML
    public void backToBrowser(ActionEvent event) {
        if (parentController instanceof UserHomePageNewController) {
            ((UserHomePageNewController) parentController).getMainPane().getChildren().remove(1);
        }
    }
}