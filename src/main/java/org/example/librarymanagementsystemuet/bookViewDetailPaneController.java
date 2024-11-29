package org.example.librarymanagementsystemuet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.librarymanagementsystemuet.exception.InvalidDatatype;
import org.example.librarymanagementsystemuet.obj.Book;

import java.sql.*;

import static org.example.librarymanagementsystemuet.Database.connectDB;

public class bookViewDetailPaneController {
    @FXML
    private Button backButton, borrowBookButton1;

    @FXML
    private TextArea bookDescriptionField;

    @FXML
    private Label bookNameLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Text bookIsbnField, bookPublisherField, bookIdField,
            bookAuthorField, bookQuantityField, bookCategoryField;

    @FXML
    private TextField borrowCountField;

    @FXML
    private  Button decreaseButton, increaseButton;

    @FXML
    HBox book_detail_HB;

    int quantityOfBook = 0;

    public int getQuantityOfBook() {
        return quantityOfBook;
    }

    public HBox getBookDetailHBox() {
        return book_detail_HB;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getBorrowBookButton1() {
        return borrowBookButton1;
    }

    public Button getDecreaseButton() {
        return decreaseButton;
    }

    public Button getIncreaseButton() {
        return increaseButton;
    }

    public TextField getBorrowCountField() {
        return borrowCountField;
    }

    public Book getSelectedBookByID(int bookId) throws SQLException {
        Book book = new Book();

        // Execute query to find data about book with provided id
        String query = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = connectDB();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, bookId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    book.setId(resultSet.getInt("id"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setName(resultSet.getString("name"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setPublisher(resultSet.getString("publisher"));
                    book.setCategory(resultSet.getString("category"));
                    book.setLocation(resultSet.getString("location"));
                    book.setQuantity(resultSet.getString("quantity"));
                    book.setAddedDate(resultSet.getString("addedDate"));
                    book.setDescription(resultSet.getString("description"));
                    book.setImageLink(resultSet.getString("linkCoverImage"));
                    book.setLastUpdateDate(resultSet.getString("lastUpdateDate"));
                    book.setAvgRate(String.valueOf(resultSet.getDouble("avgRate")));
                    book.setLanguage(resultSet.getString("language"));
                    book.setPublisherDate(resultSet.getString("publisherDate"));
                    book.setPageCount(resultSet.getString("pageCount"));
                    book.setViews(resultSet.getString("views"));
                }
            } catch (SQLException | InvalidDatatype e) {
                throw new RuntimeException(e);
            }
        }
        return book;
    }

    public void loadBookDetailByID(int bookId) throws SQLException {
        // Execute query to find data about book with provided id
        String query = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = connectDB();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, bookId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    Book selectedBook = getSelectedBookByID(bookId);

                    // Display detail data about book
                    bookNameLabel.setText(selectedBook.getName());
                    bookIdField.setText(String.valueOf(selectedBook.getId()));
                    bookAuthorField.setText(selectedBook.getAuthor());
                    bookIsbnField.setText(selectedBook.getIsbn());
                    bookPublisherField.setText(selectedBook.getPublisher());
                    bookDescriptionField.setText(selectedBook.getDescription());
                    bookCategoryField.setText(selectedBook.getCategory());
                    bookQuantityField.setText(String.valueOf(selectedBook.getQuantity()));
                    Database.setImageByLink(bookImage, selectedBook.getImageLink());
                }
            }
        }
    }
}
