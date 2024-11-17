package org.example.librarymanagementsystemuet;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import package1.Book;

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
    private Text descriptionText;

    @FXML
    private TableColumn<?, ?> dueDateCol;

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
    private TableColumn<?, ?> returnDateCol;

    @FXML
    private TableColumn<?, ?> startDateCol;

    @FXML
    private TableColumn<?, ?> userIDCol;

    Book book;

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
    }

    public void getDescription(Book book) {
        descriptionText.setWrappingWidth(400);
        descriptionText.setText(book.getDescription());
    }

    public void getBorrowInfo(Book book) {

    }
}
