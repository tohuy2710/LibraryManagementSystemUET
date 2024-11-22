package org.example.librarymanagementsystemuet;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.Book;

import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.example.librarymanagementsystemuet.Database.connectDB;

public class bookViewDetailPaneController {
    @FXML
    private Button backButton;

    @FXML
    private Text bookAuthorField;

    @FXML
    private Label bookAuthorLabel;

    @FXML
    private TextArea bookDescriptionField;

    @FXML
    private Label bookDescriptionLabel;

    @FXML
    private Text bookIdField;

    @FXML
    private Label bookIdLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Text bookIsbnField;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Text bookPublisherField;

    @FXML
    HBox book_detail_HB;

    @FXML
    private Button borrowBookButton1;

    public HBox getBookDetailHBox() {
        return book_detail_HB;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getBorrowBookButton1() {
        return borrowBookButton1;
    }

    // OKAY
    public void loadBookDetailByID(int bookId) throws SQLException {
        // Execute query to find data about book with provided id
        String query = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = connectDB();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, bookId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    Book selectedBook = new Book();
                    selectedBook.setId(resultSet.getString("id"));
                    selectedBook.setName(resultSet.getString("name"));
                    selectedBook.setAuthor(resultSet.getString("author"));
                    selectedBook.setDescription(resultSet.getString("description"));
                    selectedBook.setPublisher(resultSet.getString("publisher"));
                    selectedBook.setIsbn(resultSet.getString("isbn"));
                    selectedBook.setImageLink(resultSet.getString("linkCoverImage"));

                    // Display detail data about book
                    Platform.runLater(() -> {
                        bookNameLabel.setText(selectedBook.getName());
                        bookIdField.setText(String.valueOf(selectedBook.getId()));
                        bookAuthorField.setText(selectedBook.getAuthor());
                        bookIsbnField.setText(selectedBook.getIsbn());
                        bookPublisherField.setText(selectedBook.getPublisher());
                        bookDescriptionField.setText(selectedBook.getDescription());
                        Database.setImageByLink(bookImage, selectedBook.getImageLink());
                    });
                }
            }
        }
    }
}
