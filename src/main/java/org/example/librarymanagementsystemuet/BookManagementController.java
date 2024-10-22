package org.example.librarymanagementsystemuet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import package1.AlertMessage;
import package1.Book;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BookManagementController implements Initializable {

    @FXML
    private Button addBookButton;

    @FXML
    private TableColumn<Book, String> addedDateCol;

    @FXML
    private TableColumn<Book, String> authorsCol;

    @FXML
    private AnchorPane bookManagementPant;

    @FXML
    private TableColumn<Book, String> bookNameCol;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> isbnCol;

    @FXML
    private TableColumn<Book, String> publisherCol;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private ChoiceBox<String> selectTypeSearch;

    private String[] typeSearch = {"ID", "ISBN", "Name"};

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    public Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3307/library_management_system_uet",
                            "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchBooks(ActionEvent event) {
        try {
            connect = connectDB();
            String searchQuery = null;
            String searchValue = searchTextField.getText();

            if (!searchValue.isEmpty()) {
                if (selectTypeSearch.getValue().equals("ID")) {
                    searchQuery = "SELECT * FROM books WHERE id = ?";
                } else if (selectTypeSearch.getValue().equals("ISBN")) {
                    searchQuery = "SELECT * FROM books WHERE isbn = ?";
                } else if (selectTypeSearch.getValue().equals("Name")) {
                    searchQuery = "SELECT * FROM books WHERE name LIKE ?";
                    searchValue = "%" + searchValue + "%"; // For partial matching
                }

                if (searchQuery != null) {
                    prepare = connect.prepareStatement(searchQuery);
                    prepare.setString(1, searchValue);
                    result = prepare.executeQuery();
                }

                bookList.clear();

                while (result != null && result.next()) {
                    Book book = new Book();
                    book.setId(result.getInt("id"));
                    book.setIsbn(result.getString("isbn"));
                    book.setName(result.getString("name"));
                    book.setAuthor(result.getString("author"));
                    book.setPublisher(result.getString("publisher"));
                    book.setAddedDate(result.getString("addedDate"));
                    bookList.add(book);
                }
            }

            else {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage("Please enter a search value!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Bind table columns to the properties of the Book class
        idCol.setCellValueFactory(new PropertyValueFactory<Book, String>("id"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        authorsCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
        addedDateCol.setCellValueFactory(new PropertyValueFactory<Book, String>("addedDate"));

        Callback<TableColumn<Book, String>, TableCell<Book, String>> cellFactory = (param) -> {
            final TableCell<Book, String> cell = new TableCell<Book, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button editButton = new Button("Edit");
                        editButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            System.out.println(book.getId());
                        });
                        setGraphic(editButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };

        bookTable.setItems(bookList);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectTypeSearch.getItems().addAll(typeSearch);
        selectTypeSearch.setValue("ID");
    }
}
