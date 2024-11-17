package org.example.librarymanagementsystemuet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import package1.Book;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.Database.connectDB;
import static org.example.librarymanagementsystemuet.Database.preparedStatement;

public class BrowserBookController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchTextField;

    private void showFullBooks() {
        gridPane.getChildren().clear();
        int row = 0;
        int col = 0;
        try {
            Database.connect = connectDB();
            String query = "SELECT * FROM books";

            preparedStatement = Database.connect.prepareStatement(query);
            Database.result = preparedStatement.executeQuery();

            while (Database.result != null && Database.result.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("book-view-card-vertical.fxml"));
                VBox bookBox = fxmlLoader.load();

                Book book = new Book();
                book.setId(Database.result.getInt("id"));
                book.setName(Database.result.getString("name"));
                book.setIsbn(Database.result.getString("isbn"));
                book.setAuthor(Database.result.getString("author"));
                book.setPublisher(Database.result.getString("publisher"));
                book.setCategory(Database.result.getString("category"));
                book.setLocation(Database.result.getString("location"));
                book.setQuantity(String.valueOf(Database.result.getInt("quantity")));
                book.setAddedDate(Database.result.getString("addedDate"));
                book.setDescription(Database.result.getString("description"));
                book.setImageLink(Database.result.getString("linkCoverImage"));
                book.setLastUpdateDate(Database.result.getString("lastUpdateDate"));
                book.setAvgRate(String.valueOf(Database.result.getFloat("avgRate")));
                book.setLanguage(Database.result.getString("language"));
                book.setPublisherDate(Database.result.getString("publisherDate"));
                book.setPageCount(String.valueOf(Database.result.getInt("pageCount")));
                book.setViews(String.valueOf(Database.result.getInt("views")));
                book.setBorrowCount(String.valueOf(Database.result.getInt("borrowCount")));

                BookViewCardVerticalController bookViewCardVerticalController = fxmlLoader.getController();
                bookViewCardVerticalController.setInfo(book);

                GridPane.setHalignment(bookBox, HPos.CENTER);
                GridPane.setValignment(bookBox, VPos.TOP);


                if (col == 5) {
                    col = 0;
                    ++row;
                }

                gridPane.add(bookBox, col++, row);
                gridPane.setAlignment(Pos.TOP_CENTER);
                gridPane.setPadding(new Insets(10));
                gridPane.setVgap(10);
                gridPane.setHgap(10);
                GridPane.setHalignment(gridPane, HPos.CENTER);
                GridPane.setValignment(gridPane, VPos.TOP);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showFullBooks();
    }
}
