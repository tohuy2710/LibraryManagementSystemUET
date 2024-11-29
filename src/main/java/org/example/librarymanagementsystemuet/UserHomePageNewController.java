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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagementsystemuet.exception.InvalidDatatype;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.Book;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.Database.connectDB;
import static org.example.librarymanagementsystemuet.obj.UserRequest.PENDING;

public class UserHomePageNewController implements Initializable {

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
    private Button borrowBookButton, decreaseButton1, increaseButton1;

    @FXML
    private TextField borrowCountField1;

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

    public void handleDecreaseAction(Button decreaseButton, TextField borrowCountField) {
        decreaseButton.setOnAction(event -> {
            int currentBorrowCount = Integer.parseInt(borrowCountField.getText());
            if (currentBorrowCount > 0) {
                currentBorrowCount--;
                borrowCountField.setText(String.valueOf(currentBorrowCount));
            }
        });
    }

    public void handleIncreaseAction(Button increaseButton, TextField borrowCountField) {
        increaseButton.setOnAction(event -> {
            int currentBorrowCount = Integer.parseInt(borrowCountField.getText());
            currentBorrowCount++;
            borrowCountField.setText(String.valueOf(currentBorrowCount));
        });
    }

    public void handleBorrowAction(Button borrowBookButton, TextField borrowCountField, Book book) {
        borrowBookButton.setOnAction(event -> {
            try {
                // Check if the number of requests exceeds the number of books available
                if (Integer.parseInt(borrowCountField.getText()) > Integer.parseInt(book.getQuantity()) ||
                        Integer.parseInt(borrowCountField.getText()) <= 0) {
                    // Give error message if violated
                    AlertMessage alertMessage = new AlertMessage();
                    alertMessage.errorMessage("The number of books requested to borrow is invalid. Please type again.");
                } else {
                    // Check if user account is classic or vip to continue processing
                    if (Objects.equals(UserSession.getInstance().getUserType(), "Classic")) {
                        // MySQL query to return number of requests that user created in current month
                        String CheckConditionquery = "SELECT COUNT(*) AS requestCount " +
                                "FROM usersrequest WHERE userId = ? ";

                        try (Connection conn = connectDB();
                             PreparedStatement preparedStatement = conn.prepareStatement(CheckConditionquery)) {
                            preparedStatement.setString(1, UserSession.getInstance().getId());

                            ResultSet rs = preparedStatement.executeQuery();
                            // save the number of user requests
                            int userRequestCount = 0;
                            if (rs.next()) {
                                userRequestCount = rs.getInt("requestCount");
                            }

                            // Classic user can only create 10 requests per month
                            // if amount of request = 10 in current month then classic user is not allowed to send any request in current month
                            if (userRequestCount >= 10) {
                                AlertMessage alertMessage = new AlertMessage();
                                alertMessage.errorMessage("You have run out of books to borrow this month. " +
                                        "Please wait until next month or upgrade to VIP for unlimited borrowing.");
                            } else {
                                // if amount of request < 10 in current month then classic user still can send request in current month
                                // send request borrowing book to admin
                                sendRequestToAdmin(UserSession.getInstance().getId(), book.getId(),
                                        Integer.parseInt(borrowCountField.getText()));
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // VIP users can send unlimited requests per month
                        // send request borrowing book to admin
                        sendRequestToAdmin(UserSession.getInstance().getId(), book.getId(), Integer.parseInt(borrowCountField.getText()));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void loadBookDetailView(int bookID) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("book-detail-view-pane.fxml"));
        try {
            HBox hbox = loader.load();

            bookViewDetailPaneController controller = loader.getController();

            // load book detail
            controller.loadBookDetailByID(bookID);
            Book book = controller.getSelectedBookByID(bookID);

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
            controller.getDecreaseButton().getStyleClass().add("button6");
            controller.getIncreaseButton().getStyleClass().add("button6");
            controller.getBorrowCountField().setText("0");

            handleDecreaseAction(controller.getDecreaseButton(), controller.getBorrowCountField());
            handleIncreaseAction(controller.getIncreaseButton(), controller.getBorrowCountField());
            handleBorrowAction(controller.getBorrowBookButton1(), controller.getBorrowCountField(), book);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequestToAdmin(String userId, int bookId, int i) throws SQLException {
        // Execute query to send user request to admin
        try (Connection conn = connectDB();
             Statement statement = conn.createStatement()) {
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            String query = "INSERT INTO usersrequest (userId, bookId, noOfBooks) "
                    + "VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {

                preparedStatement.setString(1, userId);
                preparedStatement.setInt(2, bookId);
                preparedStatement.setInt(3, i);

                int insertQuery = preparedStatement.executeUpdate();
                AlertMessage alert = new AlertMessage();
                if (insertQuery > 0) {
                    alert.successMessage("Your request is sent to admin. " +
                            "Let's wait for approval to start reading this book");
                } else {
                    alert.errorMessage("Failed to send request to admin");
                }
            }

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    public void getHighestRatingBook(ActionEvent event) {
        Connection conn = connectDB();
        try {
            String query = "SELECT * FROM books " +
                    "ORDER BY avgRate " +
                    "DESC LIMIT 1";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Book book = new Book();
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
                book.setAvgRate(resultSet.getString("avgRate"));
                book.setLanguage(resultSet.getString("language"));
                book.setPublisherDate(resultSet.getString("publisherDate"));
                book.setPageCount(resultSet.getString("pageCount"));
                book.setViews(resultSet.getString("views"));

                BookHRName.setText(book.getName());
                bookHRAuthor.setText(book.getAuthor());
                bookHRDescription.setText(book.getDescription());
                Database.setImageByLink(BookHRImg, book.getImageLink());
            }

            highestRatingBook_VB.setOnMouseClicked(event1 -> {
                loadBookDetailView(book.getId());
            });

            borrowCountField1.setText("0");

            handleDecreaseAction(decreaseButton1, borrowCountField1);
            handleIncreaseAction(increaseButton1, borrowCountField1);
            handleBorrowAction(borrowBookButton, borrowCountField1, book);
        } catch (SQLException | InvalidDatatype e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTopRequestedBooks(null);
        getHighestRatingBook(null);
    }
}

//org.example.librarymanagementsystemuet.UserHomePageNewController