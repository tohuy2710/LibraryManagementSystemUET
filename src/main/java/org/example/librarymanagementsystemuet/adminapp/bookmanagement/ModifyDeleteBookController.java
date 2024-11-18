package org.example.librarymanagementsystemuet.adminapp.bookmanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.adminapp.bookmanagement.bookviewcard.BookViewCardController;
import org.example.librarymanagementsystemuet.exception.InvalidDatatype;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.Book;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.librarymanagementsystemuet.Database.preparedStatement;

public class ModifyDeleteBookController {

    @FXML
    private GridPane bookContainer;

    @FXML
    private TextField bookDetailAuthor;

    @FXML
    private TextField bookDetailAvgRate;

    @FXML
    private TextField bookDetailCategory;

    @FXML
    private ImageView bookDetailCover;

    @FXML
    private TextField bookDetailCoverLink;

    @FXML
    private TextArea bookDetailDescription;

    @FXML
    private TextField bookDetailISBN;

    @FXML
    private TextField bookDetailLanguage;

    @FXML
    private TextField bookDetailName;

    @FXML
    private TextField bookDetailPageCount;

    @FXML
    private TextField bookDetailLocation;

    @FXML
    private TextField bookDetailPublisher;

    @FXML
    private DatePicker publisherDatePicker;

    @FXML
    private AnchorPane bookDetailUnPickedPane;

    @FXML
    private VBox bookDetailVBox;

    @FXML
    private ChoiceBox<?> choiceBoxTypeSearch;

    @FXML
    private Button modifyBookDetailButton;

    @FXML
    private Label resultForBookNameLabel;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private HBox selectedBookBox;

    @FXML
    private Label idLabel;

    @FXML
    private TextField bookDetailQuantity;

    private String workingBookID = null;

    private Book book;

    @FXML
    private void showSearchBooksResults(ActionEvent event) {
        try {
            bookContainer.getChildren().clear();
            int column = 0;
            int row = 1;

            Database.connect = Database.connectDB();
            String query = "SELECT * FROM books WHERE name LIKE ? OR ISBN LIKE ?";

            preparedStatement = Database.connect.prepareStatement(query);

            String searchText = "%" + searchTextField.getText() + "%";
            for (int i = 1; i <= 2; i++) {
                preparedStatement.setString(i, searchText);
            }

            Database.result = preparedStatement.executeQuery();
            if (!Database.result.next()) {
                resultForBookNameLabel.setText("No book found!");
            } else {
                resultForBookNameLabel.setText("Search results for: " + searchTextField.getText());
            }

            try {
                while (Database.result != null && Database.result.next()) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/org/example" +
                            "/librarymanagementsystemuet/book-view-card.fxml"));
                    HBox bookBox = fxmlLoader.load();

                    BookViewCardController bookViewCardController = fxmlLoader.getController();
                    bookViewCardController.setData(Database.result);

                    bookBox.setStyle("-fx-background-color: #ffffff");

                    bookBox.setOnMouseClicked(ev -> {
                        if (selectedBookBox != null) {
                            selectedBookBox.setStyle("-fx-background-color: #ffffff");
                        }
                        bookBox.setStyle("-fx-background-color: #bfd8ff");
                        selectedBookBox = bookBox;
                        workingBookID = bookViewCardController.getID();
                        setBookDetailInfo(null);
                    });

                    GridPane.setHalignment(bookBox, HPos.CENTER);
                    GridPane.setValignment(bookBox, VPos.TOP);

                    if (column == 1) {
                        column = 0;
                        ++row;
                    }

                    bookContainer.add(bookBox, column++, row);
                    bookContainer.setAlignment(Pos.TOP_CENTER);
                    GridPane.setMargin(bookBox, new Insets(10));
                    GridPane.setHalignment(bookContainer, HPos.CENTER);
                    GridPane.setValignment(bookContainer, VPos.TOP);
                }
            } catch (IOException e) {
            } finally {
                if (Database.result != null) try {
                    Database.result.close();
                } catch (SQLException e) {
                }
                if (preparedStatement != null) try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (Database.connect != null) try {
                    Database.connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setBookDetailInfo(ActionEvent event) {
        try {
            Database.connect = Database.connectDB();
            String query = "SELECT * FROM books WHERE id = ?";
            preparedStatement = Database.connect.prepareStatement(query);
            preparedStatement.setString(1, workingBookID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            idLabel.setText(workingBookID);
            bookDetailName.setText(resultSet.getString("name") != null
                    ? resultSet.getString("name") : "NULL");
            bookDetailAuthor.setText(resultSet.getString("author") != null
                    ? resultSet.getString("author") : "NULL");
//            bookDetailPublisher.setText(resultSet.getString("publisher") != null
//                    ? resultSet.getString("publisher") : "NULL");
            publisherDatePicker.setValue(resultSet.getDate("publisherDate") != null
                    ? resultSet.getDate("publisherDate").toLocalDate() : null);
            Database.setImageByLink(bookDetailCover, resultSet.getString("linkCoverImage") != null
                    ? resultSet.getString("linkCoverImage") : "NULL");
            bookDetailCoverLink.setText(resultSet.getString("linkCoverImage") != null
                    ? resultSet.getString("linkCoverImage") : "NULL");
            bookDetailDescription.setText(resultSet.getString("description") != null
                    ? resultSet.getString("description") : "NULL");
            bookDetailISBN.setText(resultSet.getString("isbn") != null
                    ? resultSet.getString("isbn") : "NULL");
            bookDetailLanguage.setText(resultSet.getString("language") != null
                    ? resultSet.getString("language") : "NULL");
            bookDetailPageCount.setText(resultSet.getString("pageCount") != null
                    ? resultSet.getString("pageCount") : "NULL");
            bookDetailAvgRate.setText(resultSet.getString("avgRate") != null
                    ? resultSet.getString("avgRate") : "NULL");
            bookDetailLocation.setText(resultSet.getString("location") != null
                    ? resultSet.getString("location") : "NULL");

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UploadToDatabase(ActionEvent event) {
        if (workingBookID == null) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage("Please select a book to modify!");
            return;
        }
        try {
            Database.connect = Database.connectDB();
            String query = "UPDATE books SET name = ?, author = ?, publisher = ?, linkCoverImage = ?, " +
                    "description = ?, isbn = ?, language = ?, pageCount = ?, publisherDate = ?, avgRate = ?, " +
                    "location = ?, lastUpdateDate = NOW(), quantity = ? WHERE id = ?";
            preparedStatement = Database.connect.prepareStatement(query);

            book = new Book();
            book.setName(bookDetailName.getText());
            book.setAuthor(bookDetailAuthor.getText());
            book.setPublisher(bookDetailPublisher.getText());
            book.setImageLink(bookDetailCoverLink.getText());
            book.setDescription(bookDetailDescription.getText());
            book.setIsbn(bookDetailISBN.getText());
            book.setLanguage(bookDetailLanguage.getText());
            book.setPageCount(bookDetailPageCount.getText());
            book.setAvgRate(bookDetailAvgRate.getText());
            book.setLocation(bookDetailLocation.getText());
            book.setQuantity(bookDetailQuantity.getText());

            preparedStatement.setString(1, bookDetailName.getText());
            preparedStatement.setString(2, bookDetailAuthor.getText());
            preparedStatement.setString(3, bookDetailPublisher.getText());
            preparedStatement.setString(4, bookDetailCoverLink.getText());
            preparedStatement.setString(5, bookDetailDescription.getText());
            preparedStatement.setString(6, bookDetailISBN.getText());
            preparedStatement.setString(7, bookDetailLanguage.getText());
            preparedStatement.setString(8, bookDetailPageCount.getText());
            String date = publisherDatePicker.getValue().toString();
            preparedStatement.setString(9, publisherDatePicker.getValue().toString());
            preparedStatement.setString(10, bookDetailAvgRate.getText());
            preparedStatement.setString(11, bookDetailLocation.getText());
            preparedStatement.setString(12, bookDetailQuantity.getText());
            preparedStatement.setString(13, workingBookID);
            preparedStatement.executeUpdate();
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.successMessage("Update book successfully!");
        }
        catch (InvalidDatatype e) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteBook(ActionEvent event) {
        if (workingBookID == null) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage("Please select a book to delete!");
            return;
        }
        try {
            Database.connect = Database.connectDB();
            String query = "DELETE FROM books WHERE id = ?";
            preparedStatement = Database.connect.prepareStatement(query);
            preparedStatement.setString(1, workingBookID);
            preparedStatement.executeUpdate();
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.successMessage("Delete book successfully!");
            bookContainer.getChildren().clear();
            bookDetailInfoClearAllField();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bookDetailInfoClearAllField() {
        bookDetailName.setText("");
        bookDetailAuthor.setText("");
        bookDetailPublisher.setText("");
        bookDetailCoverLink.setText("");
        bookDetailDescription.setText("");
        bookDetailISBN.setText("");
        bookDetailLanguage.setText("");
        bookDetailPageCount.setText("");
        publisherDatePicker.setValue(null);
        bookDetailAvgRate.setText("");
        bookDetailLocation.setText("");
        Database.setImageByLink(bookDetailCover, "NULL");
    }

    @FXML
    public void reloadBookImage(ActionEvent event) {
        book.setImageLink(bookDetailCoverLink.getText());
        Database.setImageByLink(bookDetailCover, book.getImageLink());
    }
}