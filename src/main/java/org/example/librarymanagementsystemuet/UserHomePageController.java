// UserHomePageController.java
package org.example.librarymanagementsystemuet;

import com.google.api.client.util.Data;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class UserHomePageController {
    @FXML
    private ImageView featuredImage;

    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    @FXML
    private ImageView imageViewDetail;
    @FXML
    private Label bookNameDetail;
    @FXML
    private Text authorDetail;
    @FXML
    private Text publisherDetail;
    @FXML
    private Text viewDetail;
    @FXML
    private Text countBorrowDetail;

    @FXML
    private VBox bookBox1;
    @FXML
    private VBox bookBox2;
    @FXML
    private VBox bookBox3;
    @FXML
    private VBox bookBox4;
    @FXML
    private VBox bookBox5;

    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private ImageView imageView4;
    @FXML
    private ImageView imageView5;

    @FXML
    private Label nameLabel1;
    @FXML
    private Label nameLabel2;
    @FXML
    private Label nameLabel3;
    @FXML
    private Label nameLabel4;
    @FXML
    private Label nameLabel5;

    @FXML
    private Label authorLabel1;
    @FXML
    private Label authorLabel2;
    @FXML
    private Label authorLabel3;
    @FXML
    private Label authorLabel4;
    @FXML
    private Label authorLabel5;

    @FXML
    private ImageView borrowImageView1;
    @FXML
    private ImageView borrowImageView2;
    @FXML
    private ImageView borrowImageView3;
    @FXML
    private Label borrowNameLabel1;
    @FXML
    private Label borrowNameLabel2;
    @FXML
    private Label borrowNameLabel3;
    @FXML
    private Label borrowAuthorLabel1;
    @FXML
    private Label borrowAuthorLabel2;
    @FXML
    private Label borrowAuthorLabel3;
    //
    @FXML
    private HBox technologyBookshelfHBox;
    @FXML
    private HBox literatureBookshelfHBox;
    @FXML
    private HBox economicsBookshelfHBox;
    @FXML
    private HBox foreignLanguageBookshelfHBox;

    // ExecutorService to manage threads
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void initialize() {
        loadImages();
        startImageSlideshow();
        loadRecommendedBooks();
        loadBookDetails();
        loadTopBorrowedBooks();
        loadBookshelf("Technology", technologyBookshelfHBox);
        loadBookshelf("Literature", literatureBookshelfHBox);
        loadBookshelf("Economics", economicsBookshelfHBox);
        loadBookshelf("Foreign Language", foreignLanguageBookshelfHBox);
    }
    private void loadImages() {
        images.add(new Image(getClass().getResourceAsStream("/asset/img/nen1.png")));
        images.add(new Image(getClass().getResourceAsStream("/asset/img/nen2.png")));
        images.add(new Image(getClass().getResourceAsStream("/asset/img/nen3.png")));
    }

    private void startImageSlideshow() {
        featuredImage.setImage(images.get(currentImageIndex));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            featuredImage.setImage(images.get(currentImageIndex));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void loadRecommendedBooks() {
        Callable<VBox[]> loadBooksTask = () -> {
            VBox[] books = new VBox[5];
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/library_management_system_uet", "root", "");
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM books ORDER BY id DESC LIMIT 5")) {

                int i = 0;
                while (rs.next() && i < 5) {
                    VBox bookBox = new VBox();
                    String imageUrl = rs.getString("linkCoverImage");
                    if (imageUrl == null || imageUrl.isEmpty()) {
                        imageUrl = "path/to/default/image.png"; // Default image path
                    }
                    ImageView imageView = new ImageView(new Image(imageUrl, true));
                    Label nameLabel = new Label(rs.getString("name"));
                    Label authorLabel = new Label(rs.getString("author"));
                    bookBox.getChildren().addAll(imageView, nameLabel, authorLabel);
                    books[i] = bookBox;
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return books;
        };

        Future<VBox[]> future = executorService.submit(loadBooksTask);

        executorService.submit(() -> {
            try {
                VBox[] books = future.get(10, TimeUnit.SECONDS);
                updateUI(books);
            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Loading books timed out.");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadBookDetails() {
        Callable<Void> loadDetailsTask = () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/library_management_system_uet", "root", "");
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM books ORDER BY id DESC LIMIT 1")) {

                if (rs.next()) {
                    String imageUrl = rs.getString("linkCoverImage");
//                    if (imageUrl == null || imageUrl.isEmpty()) {
//                        imageUrl = "path/to/default/image.png"; // Default image path
//                    }
//                    Image image = new Image(imageUrl, true);
                    String name = rs.getString("name");
                    String author = rs.getString("author");
                    String publisher = rs.getString("publisher");
                    int views = rs.getInt("views");
                    int borrowCount = rs.getInt("borrowCount");

                    // Update UI elements on the JavaFX Application Thread
                    javafx.application.Platform.runLater(() -> {
                        Database.setImageByLink(imageViewDetail, imageUrl);
                        bookNameDetail.setText(name);
                        authorDetail.setText(author);
                        publisherDetail.setText(publisher);
                        viewDetail.setText(String.valueOf(views));
                        countBorrowDetail.setText(String.valueOf(borrowCount));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };

        executorService.submit(loadDetailsTask);
    }

    private void loadTopBorrowedBooks() {
        Callable<Void> loadTopBorrowedBooksTask = () -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/library_management_system_uet", "root", "");
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM books ORDER BY borrowCount DESC LIMIT 3")) {

                int i = 0;
                while (rs.next() && i < 3) {
                    String imageUrl = rs.getString("linkCoverImage");
//                    if (imageUrl == null || imageUrl.isEmpty()) {
//                        imageUrl = "asset/img/cover-not-found-img.png"; // Default image path
//                    }
//                    Image image = new Image(imageUrl, true);
                    String name = rs.getString("name");
                    String author = rs.getString("author");

                    // Update UI elements on the JavaFX Application Thread
                    int finalI = i;
                    javafx.application.Platform.runLater(() -> {
                        switch (finalI) {
                            case 0 -> {
                                Database.setImageByLink(borrowImageView1, imageUrl);
                                borrowNameLabel1.setText(name);
                                borrowAuthorLabel1.setText(author);
                            }
                            case 1 -> {
                                Database.setImageByLink(borrowImageView2, imageUrl);
                                borrowNameLabel2.setText(name);
                                borrowAuthorLabel2.setText(author);
                            }
                            case 2 -> {
                                Database.setImageByLink(borrowImageView3, imageUrl);
                                borrowNameLabel3.setText(name);
                                borrowAuthorLabel3.setText(author);
                            }
                        }
                    });
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };

        executorService.submit(loadTopBorrowedBooksTask);
    }

    private void updateUI(VBox[] books) {
        if (books == null) return;

        // Run this code on the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            for (int i = 0; i < books.length; i++) {
                VBox bookBox = books[i];
                if (bookBox != null) {
                    switch (i) {
                        case 0 -> updateBookBox(bookBox1, imageView1, nameLabel1, authorLabel1, bookBox);
                        case 1 -> updateBookBox(bookBox2, imageView2, nameLabel2, authorLabel2, bookBox);
                        case 2 -> updateBookBox(bookBox3, imageView3, nameLabel3, authorLabel3, bookBox);
                        case 3 -> updateBookBox(bookBox4, imageView4, nameLabel4, authorLabel4, bookBox);
                        case 4 -> updateBookBox(bookBox5, imageView5, nameLabel5, authorLabel5, bookBox);
                    }
                }
            }
        });
    }

    private void updateBookBox(VBox targetBox, ImageView targetImageView, Label targetNameLabel, Label targetAuthorLabel, VBox sourceBox) {
        ImageView sourceImageView = (ImageView) sourceBox.getChildren().get(0);
        Label sourceNameLabel = (Label) sourceBox.getChildren().get(1);
        Label sourceAuthorLabel = (Label) sourceBox.getChildren().get(2);

        targetImageView.setImage(sourceImageView.getImage());
        targetNameLabel.setText(sourceNameLabel.getText());
        targetAuthorLabel.setText(sourceAuthorLabel.getText());
    }
    private void loadBookshelf(String category, HBox targetHBox) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass()
                            .getResource("/org/example/librarymanagementsystemuet/user-app-bookshelf.fxml"));
            HBox bookshelf = loader.load();
            UserAppBookshelfController controller = loader.getController();
            controller.loadBooksByCategory(category);
            targetHBox.getChildren().add(bookshelf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ensure to shut down the ExecutorService when no longer needed
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}