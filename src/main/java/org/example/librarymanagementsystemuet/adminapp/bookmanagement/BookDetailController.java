package org.example.librarymanagementsystemuet.adminapp.bookmanagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.librarymanagementsystemuet.Controller;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.Book;
import org.example.librarymanagementsystemuet.obj.BorrowRecord;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.example.librarymanagementsystemuet.Database.connectDB;

public class BookDetailController extends Controller {

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

    private ObservableList<BorrowRecord> borrowRecordObservableList = FXCollections.observableArrayList();

    public Controller getParentController() {
        return parentController;
    }

    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    public void setDetail(Book book) {
        if (book == null) {
            return;
        }
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
        if (parentController instanceof BrowserBookController) {
            ((BrowserBookController) parentController).getMainPane().getChildren().remove(1);
            ((BrowserBookController) parentController).getSearchBox().setVisible(true);
        } else if (parentController instanceof BookManagementDashboardController) {
            ((BookManagementDashboardController) parentController).getMainPane().getChildren().remove(1);
        }
    }
}