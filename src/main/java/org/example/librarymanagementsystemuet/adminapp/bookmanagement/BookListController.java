package org.example.librarymanagementsystemuet.adminapp.bookmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.Book;

import java.sql.SQLException;

public class BookListController {

    @FXML
    private TableColumn<Book, String> addedDateCol;

    @FXML
    private TableColumn<Book, String> authorsCol;

    @FXML
    private TableColumn<Book, String> bookNameCol;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Integer> idCol;

    @FXML
    private TableColumn<Book, String> isbnCol;

    @FXML
    private TableColumn<Book, String> publisherCol;

    @FXML
    private HBox searchBookResultBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private ChoiceBox<String> selectTypeSearch;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    public void searchBooks(String searchValue) {
        try {
            Database.connect = Database.connectDB();
            String searchQuery = null;

            if (!searchValue.isEmpty()) {
                searchQuery = "SELECT * FROM books WHERE id LIKE ? OR isbn LIKE ? OR name LIKE ?";

                if (searchQuery != null) {
                    Database.prepare = Database.connect.prepareStatement(searchQuery);
                    for (int i = 1; i <= 3; i++) {
                        Database.prepare.setString(i, "%" + searchValue + "%");
                    }
                    Database.result = Database.prepare.executeQuery();
                }

                bookList.clear();

                while (Database.result != null && Database.result.next()) {
                    Book book = new Book();
                    book.setId(Database.result.getInt("id"));
                    book.setIsbn(Database.result.getString("isbn"));
                    book.setName(Database.result.getString("name"));
                    book.setAuthor(Database.result.getString("author"));
                    book.setPublisher(Database.result.getString("publisher"));
                    book.setAddedDate(Database.result.getString("addedDate"));
                    bookList.add(book);
                }
            } else {
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorsCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        addedDateCol.setCellValueFactory(new PropertyValueFactory<>("addedDate"));

        bookTable.setItems(bookList);
    }
}