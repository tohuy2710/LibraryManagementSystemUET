package org.example.librarymanagementsystemuet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import package1.AlertMessage;
import package1.Book;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BookManagementController implements Initializable {

    @FXML
    private Button addBookButton11;

    @FXML
    private Button addBookButton111;

    @FXML
    private Button addBookButton1111;

    @FXML
    private TableColumn<?, ?> addedDateCol;

    @FXML
    private Circle adminAvatar;

    @FXML
    private HBox adminInfoBox;

    @FXML
    private Label adminName;

    @FXML
    private TableColumn<Book, String> authorsCol;

    @FXML
    private HBox bookManagementMainBox;

    @FXML
    private TableColumn<Book, String> bookNameCol;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private Button booksManagementButton;

    @FXML
    private Button booksManagementButtonMinimize;

    @FXML
    private Button changeMenuSize;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button dashboardButtonMinimize;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> isbnCol;

    @FXML
    private Button logoutButton;

    @FXML
    private Button logoutButtonMinimize;

    @FXML
    private AnchorPane paneMenuFull;

    @FXML
    private AnchorPane paneMenuMini;

    @FXML
    private TableColumn<?, ?> publisherCol;

    @FXML
    private HBox searchBookResultBox;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private ChoiceBox<String> selectTypeSearch;

    @FXML
    private Label topBorrowedBook1Author;

    @FXML
    private Label topBorrowedBook1BrrowedCount;

    @FXML
    private HBox topBorrowedBook1HBox;

    @FXML
    private Label topBorrowedBook1ISBN;

    @FXML
    private ImageView topBorrowedBook1Image;

    @FXML
    private Label topBorrowedBook1Name;

    @FXML
    private Label topBorrowedBook2Author;

    @FXML
    private Label topBorrowedBook2BrrowedCount;

    @FXML
    private HBox topBorrowedBook2HBox;

    @FXML
    private Label topBorrowedBook2ISBN;

    @FXML
    private ImageView topBorrowedBook2Image;

    @FXML
    private Label topBorrowedBook2Name;

    @FXML
    private Label topBorrowedBook3Author;

    @FXML
    private Label topBorrowedBook3BrrowedCount;

    @FXML
    private HBox topBorrowedBook3HBox;

    @FXML
    private Label topBorrowedBook3ISBN;

    @FXML
    private ImageView topBorrowedBook3Image;

    @FXML
    private Label topBorrowedBook3Name;

    @FXML
    private Button usersManagementButton;

    @FXML
    private Button usersManagementButtonMinimize;

    private String[] typeSearch = {"ID", "ISBN", "Name"};

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    public void showBookManagementMainBox(ActionEvent event) {
        bookManagementMainBox.setVisible(true);
        searchBookResultBox.setVisible(false);
    }

    public void searchBooks(ActionEvent event) {
        try {
            bookManagementMainBox.setVisible(false);
            searchBookResultBox.setVisible(true);


            Database.connect = Database.connectDB();
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
                    Database.prepare = Database.connect.prepareStatement(searchQuery);
                    Database.prepare.setString(1, searchValue);
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
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage("Please enter a search value!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Bind table columns to the properties of the Book class
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorsCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        addedDateCol.setCellValueFactory(new PropertyValueFactory<>("addedDate"));

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

    public void changeMenuSize(ActionEvent event) {
        if (paneMenuFull.isVisible()) {
            paneMenuFull.setVisible(false);
            paneMenuMini.setVisible(true);
        } else {
            paneMenuFull.setVisible(true);
            paneMenuMini.setVisible(false);
        }
    }

    public void getTopBorrowedBooks(ActionEvent event) {
        try {
            Database.connect = Database.connectDB();
            String query = "SELECT bookid, COUNT(userid) AS cnt, " +
                    "books.linkCoverImage, books.name, books.author, books.isbn " +
                    "FROM borrowbooks " +
                    "LEFT JOIN books ON books.id = borrowbooks.bookid " +
                    "GROUP BY bookid " +
                    "ORDER BY cnt DESC " +
                    "LIMIT 3;";
            Database.prepare = Database.connect.prepareStatement(query);
            Database.result = Database.prepare.executeQuery();

            int i = 1;
            while (Database.result != null && Database.result.next()) {
                if (i == 1) {
                    topBorrowedBook1Name.setText(Database.result.getString("name"));
                    topBorrowedBook1Author.setText(Database.result.getString("author"));
                    topBorrowedBook1ISBN.setText(Database.result.getString("isbn"));
                    topBorrowedBook1BrrowedCount.setText(Database.result.getString("cnt"));
                    topBorrowedBook1Image.setImage(new Image(Database.result.getString("linkCoverImage")));
                } else if (i == 2) {
                    topBorrowedBook2Name.setText(Database.result.getString("name"));
                    topBorrowedBook2Author.setText(Database.result.getString("author"));
                    topBorrowedBook2ISBN.setText(Database.result.getString("isbn"));
                    topBorrowedBook2BrrowedCount.setText(Database.result.getString("cnt"));
                    topBorrowedBook2Image.setImage(new Image(Database.result.getString("linkCoverImage")));
                } else if (i == 3) {
                    topBorrowedBook3Name.setText(Database.result.getString("name"));
                    topBorrowedBook3Author.setText(Database.result.getString("author"));
                    topBorrowedBook3ISBN.setText(Database.result.getString("isbn"));
                    topBorrowedBook3BrrowedCount.setText(Database.result.getString("cnt"));
                    topBorrowedBook3Image.setImage(new Image(Database.result.getString("linkCoverImage")));
                }
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectTypeSearch.getItems().addAll(typeSearch);
        selectTypeSearch.setValue("ID");

        getTopBorrowedBooks(null);

        paneMenuFull.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && paneMenuMini.isVisible()) {
                paneMenuMini.setVisible(false);
            }
            adjustContentSize();
        });

        paneMenuMini.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && paneMenuFull.isVisible()) {
                paneMenuFull.setVisible(false);
            }
            adjustContentSize();
        });

        contentPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            adjustHBoxSize();
        });
    }

    private void adjustContentSize() {
        if (paneMenuFull.isVisible() && !paneMenuMini.isVisible()) {
            AnchorPane.setLeftAnchor(contentPane, 240.0); // Giữ tỷ lệ kích thước cho menu lớn
        } else if (!paneMenuFull.isVisible() && paneMenuMini.isVisible()) {
            AnchorPane.setLeftAnchor(contentPane, 80.0); // Giữ tỷ lệ kích thước cho menu nhỏ
        }
        AnchorPane.setRightAnchor(contentPane, 0.0); // Cố định bên phải để tự điều chỉnh kích thước

        contentPane.applyCss();
        adjustHBoxSize();
    }

    private void adjustHBoxSize() {
        for (Node node : contentPane.getChildren()) {
            if (node instanceof HBox) {
                ((HBox) node).prefWidthProperty().bind(contentPane.widthProperty());
            }
        }
    }

}