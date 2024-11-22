package org.example.librarymanagementsystemuet.adminapp.bookmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.example.librarymanagementsystemuet.Controller;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.adminapp.AdminAppController;
import org.example.librarymanagementsystemuet.exception.InvalidDatatype;
import org.example.librarymanagementsystemuet.obj.Book;

import java.io.IOException;
import java.net.URL;
import java.security.cert.PolicyNode;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BookManagementDashboardController extends Controller implements Initializable {
 //org.example.librarymanagementsystemuet.adminapp.bookmanagement.BookManagementDashboardController.java
    @FXML
    private StackPane bookManagementMainBox;

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
    private TableView<Book> recentlyUpdateTable;

    @FXML
    private TableColumn<Book, String> timeCol;

    @FXML
    private TableColumn<Book, String> bookIDCol;

    @FXML
    private TableColumn<Book, String> bookNameCol;

    @FXML
    private TableColumn<Book, String> typeCol;

    private ObservableList<Book> recentlyUpdateBookList = FXCollections.observableArrayList();

    private AdminAppController parentController;

    private Book book1;
    private Book book2;
    private Book book3;

    @FXML
    private HBox topRequestedBookBox;

    public void setParentController(AdminAppController parentController) {
        this.parentController = parentController;
    }

    public void getRecentlyUpdatedBooks() {
        recentlyUpdateTable.getItems().clear();
        recentlyUpdateBookList.clear();

        try {
            Database.connect = Database.connectDB();
            String query = "SELECT id, name, lastUpdateDate, " +
                    "IF(lastUpdateDate = addedDate, 'ADD', 'MODIFY') AS type " +
                    "FROM books " +
                    "ORDER BY lastUpdateDate DESC " +
                    "LIMIT 10;";
            Database.prepare = Database.connect.prepareStatement(query);
            Database.result = Database.prepare.executeQuery();

            while (Database.result != null && Database.result.next()) {
                Book book = new Book();
                book.setId(Database.result.getInt("id"));
                book.setName(Database.result.getString("name"));
                book.setLastUpdateDate(Database.result.getString("lastUpdateDate"));
                book.setTypeOfLastUpdate(Database.result.getString("type"));
                recentlyUpdateBookList.add(book);
            }

            bookIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            bookNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            timeCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdateDate"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("typeOfLastUpdate"));

            recentlyUpdateTable.setItems(recentlyUpdateBookList);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Database.result != null) Database.result.close();
                if (Database.prepare != null) Database.prepare.close();
                if (Database.connect != null) Database.connect.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void getTopBorrowedBooks(ActionEvent event) {
        try {
            Database.connect = Database.connectDB();
            String query = "SELECT \n" +
                    "    books.id, \n" +
                    "    books.name AS name, \n" +
                    "    books.isbn AS isbn, \n" +
                    "    books.linkCoverImage AS linkCoverImage, \n" +
                    "    books.author AS author, \n" +
                    "    getCountRequestBook.cnt AS cnt \n" +
                    "FROM \n" +
                    "    books \n" +
                    "INNER JOIN \n" +
                    "    (SELECT \n" +
                    "        bookId, \n" +
                    "        COUNT(*) AS cnt \n" +
                    "     FROM \n" +
                    "        usersrequest \n" +
                    "     GROUP BY \n" +
                    "        bookId) AS getCountRequestBook \n" +
                    "ON \n" +
                    "    books.id = getCountRequestBook.bookId " +
                    "LIMIT 3;\n";
            Database.prepare = Database.connect.prepareStatement(query);
            Database.result = Database.prepare.executeQuery();
            int i = 1;
            while (Database.result != null && Database.result.next()) {
                if (i == 1) {
                    topBorrowedBook1Name.setText(Database.result.getString("name"));
                    topBorrowedBook1Author.setText(Database.result.getString("author"));
                    topBorrowedBook1ISBN.setText("ISBN: " + Database.result.getString("isbn"));
                    topBorrowedBook1BrrowedCount.setText("Borrowed By: "
                            + Database.result.getString("cnt") + " users");
                    Database.setImageByLink(topBorrowedBook1Image, Database.result.getString("linkCoverImage"));
                } else if (i == 2) {
                    topBorrowedBook2Name.setText(Database.result.getString("name"));
                    topBorrowedBook2Author.setText(Database.result.getString("author"));
                    topBorrowedBook2ISBN.setText("ISBN: " + Database.result.getString("isbn"));
                    topBorrowedBook2BrrowedCount.setText("Borrowed By: "
                            + Database.result.getString("cnt") + " users");
                    Database.setImageByLink(topBorrowedBook2Image, Database.result.getString("linkCoverImage"));
                } else if (i == 3) {
                    topBorrowedBook3Name.setText(Database.result.getString("name"));
                    topBorrowedBook3Author.setText(Database.result.getString("author"));
                    topBorrowedBook3ISBN.setText("ISBN: " + Database.result.getString("isbn"));
                    topBorrowedBook3BrrowedCount.setText("Borrowed By: "
                            + Database.result.getString("cnt") + " users");
                    Database.setImageByLink(topBorrowedBook3Image, Database.result.getString("linkCoverImage"));
                }
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getDetailTopBorrowedBooks(ActionEvent event) {
        topBorrowedBook1HBox.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/org/example/librarymanagementsystemuet/book-detail.fxml"));
                AnchorPane bookDetailBox = loader.load();
                BookDetailController bookDetailController = loader.getController();
                bookDetailController.setParentController(this);

                Database.connect = Database.connectDB();
                String query = "SELECT * FROM books WHERE name = '" + topBorrowedBook1Name.getText() + "';";
                Database.prepare = Database.connect.prepareStatement(query);
                Database.result = Database.prepare.executeQuery();
                if (Database.result.next()) {
                    book1 = Database.setBookInfo();
                }
                bookDetailController.setDetail(book1);
                bookManagementMainBox.getChildren().add(bookDetailBox);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        topBorrowedBook2HBox.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/org/example/librarymanagementsystemuet/book-detail.fxml"));
                AnchorPane bookDetailBox = loader.load();
                BookDetailController bookDetailController = loader.getController();
                bookDetailController.setParentController(this);

                Database.connect = Database.connectDB();
                String query = "SELECT * FROM books WHERE name = '" + topBorrowedBook2Name.getText() + "';";
                Database.prepare = Database.connect.prepareStatement(query);
                Database.result = Database.prepare.executeQuery();
                if (Database.result.next()) {
                    book2 = Database.setBookInfo();
                }
                bookDetailController.setDetail(book2);
                bookManagementMainBox.getChildren().add(bookDetailBox);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        topBorrowedBook3HBox.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/org/example/librarymanagementsystemuet/book-detail.fxml"));
                AnchorPane bookDetailBox = loader.load();
                BookDetailController bookDetailController = loader.getController();
                bookDetailController.setParentController(this);

                Database.connect = Database.connectDB();
                String query = "SELECT * FROM books WHERE name = '" + topBorrowedBook3Name.getText() + "';";
                Database.prepare = Database.connect.prepareStatement(query);
                Database.result = Database.prepare.executeQuery();
                if (Database.result.next()) {
                    book3 = Database.setBookInfo();
                }
                bookDetailController.setDetail(book3);
                bookManagementMainBox.getChildren().add(bookDetailBox);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void showAddBookSearchBox(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystemuet/book-management-add-book.fxml"));
            HBox addBookSearchBox = loader.load();
            bookManagementMainBox.getChildren().clear();
            bookManagementMainBox.getChildren().add(addBookSearchBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showModifyDeleteBookBox(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystemuet/book-management-modify-delete-book.fxml"));
            HBox modifyDeleteBookBox = loader.load();
            bookManagementMainBox.getChildren().clear();
            bookManagementMainBox.getChildren().add(modifyDeleteBookBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBrowserBookBox(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/browser-book.fxml"));
            HBox browserBookBox = loader.load();

            parentController.getMainBox().getChildren().clear();
            parentController.getMainBox().getChildren().add(browserBookBox);
            parentController.showPaneMenuMini();
            parentController.disableChangeMenuSizeButton();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void visibleTopRequestedBookBox() {
        topRequestedBookBox.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && parentController.isShowingPaneMenuFull()) {
                topRequestedBookBox.setVisible(false);
            } else if (newValue && !parentController.isShowingPaneMenuFull()) {
                topRequestedBookBox.setVisible(true);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTopBorrowedBooks(null);
        getRecentlyUpdatedBooks();
        getDetailTopBorrowedBooks(null);
        visibleTopRequestedBookBox();
    }

    public StackPane getMainPane() {
        return bookManagementMainBox;
    }
}
