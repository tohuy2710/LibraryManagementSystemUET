package org.example.librarymanagementsystemuet.userapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.UserHomePageController;
import org.example.librarymanagementsystemuet.bookViewDetailPaneController;
import org.example.librarymanagementsystemuet.obj.Book;
import org.example.librarymanagementsystemuet.obj.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.librarymanagementsystemuet.Database.connectDB;

public class UserAppBookshelfController {
    private final List<ImageView> bookImageViewList = new ArrayList<>();

    @FXML
    private ImageView imageBook1;
    @FXML
    private ImageView imageBook2;
    @FXML
    private ImageView imageBook3;
    @FXML
    private ImageView imageBook4;
    @FXML
    private ImageView imageBook5;
    @FXML
    private ImageView imageBook6;
    @FXML
    private ImageView imageBook7;
    @FXML
    private Label nameBook1;
    @FXML
    private Label nameBook2;
    @FXML
    private Label nameBook3;
    @FXML
    private Label nameBook4;
    @FXML
    private Label nameBook5;
    @FXML
    private Label nameBook6;
    @FXML
    private Label nameBook7;
    @FXML
    private Label authorBook1;
    @FXML
    private Label authorBook2;
    @FXML
    private Label authorBook3;
    @FXML
    private Label authorBook4;
    @FXML
    private Label authorBook5;
    @FXML
    private Label authorBook6;
    @FXML
    private Label authorBook7;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void addImageView() {
        bookImageViewList.add(imageBook1);
        bookImageViewList.add(imageBook2);
        bookImageViewList.add(imageBook3);
        bookImageViewList.add(imageBook4);
        bookImageViewList.add(imageBook5);
        bookImageViewList.add(imageBook6);
        bookImageViewList.add(imageBook7);
    }

    public void loadBooksByCategory(String category) {
        addImageView();

        executorService.submit(() -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/library_management_system_uet", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM books WHERE category = ? ORDER BY id LIMIT 7")) {
                stmt.setString(1, category);
                ResultSet rs = stmt.executeQuery();

                ImageView[] imageViews = {imageBook1, imageBook2, imageBook3, imageBook4, imageBook5, imageBook6, imageBook7};
                Label[] nameLabels = {nameBook1, nameBook2, nameBook3, nameBook4, nameBook5, nameBook6, nameBook7};
                Label[] authorLabels = {authorBook1, authorBook2, authorBook3, authorBook4, authorBook5, authorBook6, authorBook7};

                int i = 0;
                while (rs.next() && i < 7) {
                    String imageUrl = rs.getString("linkCoverImage");
                    String name = rs.getString("name");
                    String author = rs.getString("author");

                    Book selectedBook = new Book();
                    selectedBook.setId(rs.getInt("id"));
                    selectedBook.setName(rs.getString("name"));
                    selectedBook.setAuthor(rs.getString("author"));
                    selectedBook.setPublisher(rs.getString("publisher"));
                    selectedBook.setIsbn(rs.getString("isbn"));
                    selectedBook.setDescription(rs.getString("description"));
                    selectedBook.setAddedDate(rs.getString("addedDate"));
                    selectedBook.setLastUpdateDate(rs.getString("lastUpdateDate"));
                    selectedBook.setImageLink(rs.getString("linkCoverImage"));

                    int finalI = i;
                    bookImageViewList.get(finalI).setOnMouseClicked(event -> {
                        System.out.println("image view is clicked");
                    });

                    Platform.runLater(() -> {
                        Database.setImageByLink(imageViews[finalI], imageUrl);
                        nameLabels[finalI].setText(name);
                        authorLabels[finalI].setText(author);
                    });
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}