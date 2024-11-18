package org.example.librarymanagementsystemuet.adminapp.bookmanagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.Book;
import org.example.librarymanagementsystemuet.obj.BorrowRecord;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class BookDetailController {

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
    private BrowserBookController parentController;

    public BrowserBookController getParentController() {
        return parentController;
    }

    public void setParentController(BrowserBookController parentController) {
        this.parentController = parentController;
    }

    private Book book;
    public void setDetail(Book book) {
        this.book = book;
        idLabel.setText(String.valueOf(book.getId()));
        nameLabel.setText(book.getName());
        authorLabel.setText(book.getAuthors());
        isbnLabel.setText(book.getIsbn());
        publisherLabel.setText(book.getPublisher());
        addedDateLabel.setText(book.getAddedDate());
        lastUpdatedTimeLabel.setText(book.getLastUpdateDate());
        categoryLabel.setText(book.getCategory());
        locationLabel.setText(book.getLocation());
        quantityLabel.setText(book.getQuantity());
        avgRateLabel.setText(book.getAvgRate());
        pageCountLabel.setText(book.getPageCount());
        descriptionLabel.setText(book.getDescription());
        laguageLabel.setText(book.getLanguage());
        publisherDateLabel.setText(book.getPublisherDate());

        descriptionText.setText(book.getDescription());
        // Load book cover image
        Database.setImageByLink(bookCoverImageView, book.getImageLink());
    }
    public void getBorrowInfo(int bookId) {
        borrowHistoryTable.getItems().clear();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/library_management_system_uet", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT ur.userId, br.start_date, br.due_date, br.return_date " +
                             "FROM borrowbooks br " +
                             "JOIN usersrequest ur ON br.request_id = ur.id " +
                             "WHERE ur.bookId = " + bookId)) {

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                        rs.getInt("userId"),
                        rs.getString("start_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date")
                );
                borrowHistoryTable.getItems().add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToBrowser(ActionEvent event) {

    }
}