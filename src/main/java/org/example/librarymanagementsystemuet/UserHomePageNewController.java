package org.example.librarymanagementsystemuet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagementsystemuet.exception.InvalidDatatype;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.Book;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.Database.*;
import static org.example.librarymanagementsystemuet.obj.UserRequest.PENDING;

public class UserHomePageNewController extends Controller implements Initializable {

    @FXML
    private Label topBorrowedBook1Author, topBorrowedBook1BrrowedCount, topBorrowedBook1ID,
            topBorrowedBook1ISBN, topBorrowedBook1Name, topBorrowedBook2Author,
            topBorrowedBook2BrrowedCount, topBorrowedBook2ID, topBorrowedBook2ISBN,
            topBorrowedBook2Name, topBorrowedBook3Author, topBorrowedBook3BrrowedCount,
            topBorrowedBook3ID, topBorrowedBook3ISBN, topBorrowedBook3Name,
            BookHRName, bookHRAuthor;

    @FXML
    private HBox topBorrowedBook1HBox, topBorrowedBook1HBox1, topBorrowedBook1HBox11,
            userHomePage_HB_1;

    @FXML
    private ImageView topBorrowedBook1Image, topBorrowedBook2Image, topBorrowedBook3Image,
            BookHRImg;

    @FXML
    private TextArea bookHRDescription;

    @FXML
    private VBox highestRatingBook_VB;

    @FXML
    private Button borrowBookButton;

    public void loadTopBorrowBook(Label topBorrowedBookID, Label topBorrowedBookName,
                                  Label topBorrowedBookAuthor, Label topBorrowedBookISBN,
                                  Label topBorrowedBookBrrowedCount, ImageView topBorrowedBookImage,
                                  ResultSet resultSet) throws SQLException {
        topBorrowedBookID.setText("ID: " + resultSet.getInt("id"));
        topBorrowedBookName.setText(resultSet.getString("name"));
        topBorrowedBookAuthor.setText(resultSet.getString("author"));
        topBorrowedBookISBN.setText("ISBN: " + resultSet.getString("isbn"));
        topBorrowedBookBrrowedCount.setText("Requested by: " +
                resultSet.getString("cnt") + " users");
        Database.setImageByLink(topBorrowedBookImage, resultSet.getString("linkCoverImage"));
    }

    public void getTopRequestedBooks(ActionEvent event) {
        try {
            Connection conn = connectDB();
            String query = "SELECT \n" +
                    "    books.id as id, \n" +
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
                    "        bookId ORDER BY cnt DESC LIMIT 3) AS getCountRequestBook \n" +
                    "ON \n" +
                    "    books.id = getCountRequestBook.bookId " +
                    "LIMIT 3;\n";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 1;
            while (resultSet != null && resultSet.next()) {
                if (i == 1) {
                    loadTopBorrowBook(topBorrowedBook1ID, topBorrowedBook1Name, topBorrowedBook1Author,
                            topBorrowedBook1ISBN, topBorrowedBook1BrrowedCount, topBorrowedBook1Image,
                            resultSet);
                    int bookID = resultSet.getInt("id");
                    topBorrowedBook1HBox.setOnMouseClicked(event1 -> loadBookDetailView(bookID));
                } else if (i == 2) {
                    loadTopBorrowBook(topBorrowedBook2ID, topBorrowedBook2Name, topBorrowedBook2Author,
                            topBorrowedBook2ISBN, topBorrowedBook2BrrowedCount, topBorrowedBook2Image,
                            resultSet);
                    int bookID = resultSet.getInt("id");
                    topBorrowedBook1HBox1.setOnMouseClicked(event1 -> loadBookDetailView(bookID));
                } else if (i == 3) {
                    loadTopBorrowBook(topBorrowedBook3ID, topBorrowedBook3Name, topBorrowedBook3Author,
                            topBorrowedBook3ISBN, topBorrowedBook3BrrowedCount, topBorrowedBook3Image,
                            resultSet);
                    int bookID = resultSet.getInt("id");
                    topBorrowedBook1HBox11.setOnMouseClicked(event1 -> loadBookDetailView(bookID));
                }
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public AnchorPane mainPane;

    public void loadBookDetailView(int bookID) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/librarymanagementsystemuet/book-detail-view-pane.fxml"));
            AnchorPane homePageBox = loader.load();
            mainPane.getChildren().add(homePageBox);
            BookViewDetailPaneController controller = loader.getController();
            controller.loadBookDetailByID(bookID);
            controller.setParentController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHighestRatingBook(ActionEvent event) {
        try {
            connect = connectDB();
            String query = "SELECT * FROM books " +
                    "ORDER BY avgRate " +
                    "DESC LIMIT 1";

            preparedStatement = Database.connect.prepareStatement(query);
            Database.result = preparedStatement.executeQuery();

            if (result != null && result.next()) {
                Book book = setBookInfo();
                BookHRName.setText(book.getName());
                bookHRAuthor.setText(book.getAuthor());
                bookHRDescription.setText(book.getDescription());
                Database.setImageByLink(BookHRImg, book.getImageLink());

                borrowBookButton.setOnAction(event1 ->
                        loadBookDetailView(book.getId()));
            }
        } catch (SQLException | InvalidDatatype e) {
            throw new RuntimeException(e);
        }
    }

    public AnchorPane getMainPane() {
        return mainPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTopRequestedBooks(null);
        getHighestRatingBook(null);
    }
}

//org.example.librarymanagementsystemuet.UserHomePageNewController