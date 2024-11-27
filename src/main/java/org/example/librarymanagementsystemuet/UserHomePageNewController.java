package org.example.librarymanagementsystemuet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.Database.connectDB;

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

    @FXML
    private HBox userHomePage_HB_1;

    @FXML
    private VBox highestRatingBook_VB;

    @FXML
    private Button borrowBookButton;

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

                    int bookID = Database.result.getInt("id");
                    topBorrowedBook1HBox.setOnMouseClicked(event1 -> {
                        loadBookDetailView(bookID);
                    });
                } else if (i == 2) {
                    topBorrowedBook2ID.setText("ID: " + Database.result.getInt("id"));
                    topBorrowedBook2Name.setText(Database.result.getString("name"));
                    topBorrowedBook2Author.setText(Database.result.getString("author"));
                    topBorrowedBook2ISBN.setText("ISBN: " + Database.result.getString("isbn"));
                    topBorrowedBook2BrrowedCount.setText("Requested by: "
                            + Database.result.getString("cnt") + " users");
                    Database.setImageByLink(topBorrowedBook2Image, Database.result.getString("linkCoverImage"));

                    int bookID = Database.result.getInt("id");
                    topBorrowedBook1HBox1.setOnMouseClicked(event1 -> {
                        loadBookDetailView(bookID);
                    });
                } else if (i == 3) {
                    topBorrowedBook3ID.setText("ID: " + Database.result.getInt("id"));
                    topBorrowedBook3Name.setText(Database.result.getString("name"));
                    topBorrowedBook3Author.setText(Database.result.getString("author"));
                    topBorrowedBook3ISBN.setText("ISBN: " + Database.result.getString("isbn"));
                    topBorrowedBook3BrrowedCount.setText("Requested by: "
                            + Database.result.getString("cnt") + " users");
                    Database.setImageByLink(topBorrowedBook3Image, Database.result.getString("linkCoverImage"));

                    int bookID = Database.result.getInt("id");
                    topBorrowedBook1HBox11.setOnMouseClicked(event1 -> {
                        loadBookDetailView(bookID);
                    });
                }
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadBookDetailView(int bookID) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("book-detail-view-pane.fxml"));
        try {
            HBox hbox = loader.load();

            bookViewDetailPaneController controller = loader.getController();

            // load book detail
            controller.loadBookDetailByID(bookID);

            // save content of previous page
            List<Node> previousContent = new ArrayList<>(userHomePage_HB_1.getChildren());

            // clear all current content of user home page hbox
            userHomePage_HB_1.getChildren().clear();
            // add book detail view hbox into user home page hbox
            userHomePage_HB_1.getChildren().add(hbox);

            // back button is clicked
            controller.getBackButton().getStyleClass().add("button6");
            controller.getBackButton().setOnAction(event2 -> {
                // book detail view is set not visible
                controller.getBookDetailHBox().setVisible(false);
                // show content of previous page
                userHomePage_HB_1.getChildren().setAll(previousContent);
            });

            // borrow book button is clicked
            controller.getBorrowBookButton1().getStyleClass().add("button6");
            controller.getBorrowBookButton1().setOnAction(event3 -> {
                try {
                    // send request borrowing book to admin
                    sendRequestToAdmin(UserSession.getInstance().getId(), bookID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequestToAdmin(String userId, int bookId) throws SQLException {
        // Execute query to send user request to admin
        try (Connection conn = connectDB();
             Statement statement = conn.createStatement()) {
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            String query = "INSERT INTO usersrequest (userId, bookId, createdTime, lastUpdatedTime, status) "
                    + "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {

                preparedStatement.setString(1, userId);
                preparedStatement.setInt(2, bookId);
                preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                preparedStatement.setString(5, "Pending");

                int insertQuery = preparedStatement.executeUpdate();
                AlertMessage alert = new AlertMessage();
                if (insertQuery > 0) {
                    alert.successMessage("Your request is sent to admin. Let's wait for approval to start reading this book");
                } else {
                    alert.errorMessage("Failed to send request to admin");
                }
            }

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
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

            highestRatingBook_VB.setOnMouseClicked(event1 -> {
                try {
                    loadBookDetailView(Database.result.getInt("id"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            borrowBookButton.setOnAction(event1 -> {
                try {
                    sendRequestToAdmin(UserSession.getInstance().getId(), Database.result.getInt("id"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
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

//org.example.librarymanagementsystemuet.UserHomePageNewController