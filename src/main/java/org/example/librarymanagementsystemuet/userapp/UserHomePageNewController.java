package org.example.librarymanagementsystemuet.userapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.librarymanagementsystemuet.Database;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserHomePageNewController implements Initializable {

    @FXML
    private Label topBorrowedBook1Author;

    @FXML
    private Label topBorrowedBook1BrrowedCount;

    @FXML
    private HBox topBorrowedBook1HBox;

    @FXML
    private HBox topBorrowedBook1HBox1;

    @FXML
    private HBox topBorrowedBook1HBox11;

    @FXML
    private Label topBorrowedBook1ID;

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
    private Label topBorrowedBook2ID;

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
    private Label topBorrowedBook3ID;

    @FXML
    private Label topBorrowedBook3ISBN;

    @FXML
    private ImageView topBorrowedBook3Image;

    @FXML
    private Label topBorrowedBook3Name;


    @FXML
    private ImageView BookHRImg;

    @FXML
    private Label BookHRName;

    @FXML
    private Label bookHRAuthor;

    @FXML
    private TextArea bookHRDescription;

    public void getTopRequestedBooks(ActionEvent event) {
        try {
            Database.connect = Database.connectDB();
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
            Database.prepare = Database.connect.prepareStatement(query);
            Database.result = Database.prepare.executeQuery();
            int i = 1;
            while (Database.result != null && Database.result.next()) {
                if (i == 1) {
                    topBorrowedBook1ID.setText("ID: " + Database.result.getInt("id"));
                    topBorrowedBook1Name.setText(Database.result.getString("name"));
                    topBorrowedBook1Author.setText(Database.result.getString("author"));
                    topBorrowedBook1ISBN.setText("ISBN: " + Database.result.getString("isbn"));
                    topBorrowedBook1BrrowedCount.setText("Requested by: "
                            + Database.result.getString("cnt") + " users");
                    Database.setImageByLink(topBorrowedBook1Image, Database.result.getString("linkCoverImage"));
                } else if (i == 2) {
                    topBorrowedBook2ID.setText("ID: " + Database.result.getInt("id"));
                    topBorrowedBook2Name.setText(Database.result.getString("name"));
                    topBorrowedBook2Author.setText(Database.result.getString("author"));
                    topBorrowedBook2ISBN.setText("ISBN: " + Database.result.getString("isbn"));
                    topBorrowedBook2BrrowedCount.setText("Requested by: "
                            + Database.result.getString("cnt") + " users");
                    Database.setImageByLink(topBorrowedBook2Image, Database.result.getString("linkCoverImage"));
                } else if (i == 3) {
                    topBorrowedBook3ID.setText("ID: " + Database.result.getInt("id"));
                    topBorrowedBook3Name.setText(Database.result.getString("name"));
                    topBorrowedBook3Author.setText(Database.result.getString("author"));
                    topBorrowedBook3ISBN.setText("ISBN: " + Database.result.getString("isbn"));
                    topBorrowedBook3BrrowedCount.setText("Requested by: "
                            + Database.result.getString("cnt") + " users");
                    Database.setImageByLink(topBorrowedBook3Image, Database.result.getString("linkCoverImage"));
                }
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getHighestRatingBook(ActionEvent event) {
        Database.connect = Database.connectDB();
        try {
            String query = "SELECT id, name, author, description, " +
                    "linkCoverImage FROM books ORDER BY avgRate DESC LIMIT 1";
            Database.prepare = Database.connect.prepareStatement(query);
            Database.result = Database.prepare.executeQuery();
            if (Database.result != null && Database.result.next()) {
                BookHRName.setText(Database.result.getString("name"));
                bookHRAuthor.setText(Database.result.getString("author"));
                bookHRDescription.setText(Database.result.getString("description"));
                Database.setImageByLink(BookHRImg, Database.result.getString("linkCoverImage"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTopRequestedBooks(null);
        getHighestRatingBook(null);
    }
}

//org.example.librarymanagementsystemuet.userapp.UserHomePageNewController