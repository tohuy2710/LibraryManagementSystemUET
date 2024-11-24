// UserHomePageController.java
package org.example.librarymanagementsystemuet.userapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.UserRequestTableController;
import org.example.librarymanagementsystemuet.bookViewDetailPaneController;
import org.example.librarymanagementsystemuet.exception.LogicException;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.Book;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.example.librarymanagementsystemuet.Database.connectDB;

public class UserHomePageController {
    public Text recommendBooksHBox, descriptionBook;
    public ImageView imageBook;

    private final List<ImageView> recommendBookImageViewList = new ArrayList<>();
    private final List<ImageView> topBorrowBookImageViewList = new ArrayList<>();
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;

    @FXML
    private Text authorDetail, publisherDetail, viewDetail, countBorrowDetail;

    @FXML
    private VBox bookBox1, bookBox2, bookBox3, bookBox4, bookBox5;

    @FXML
    private ImageView featuredImage, imageViewDetail,
            imageView1, imageView2, imageView3, imageView4, imageView5,
            borrowImageView1, borrowImageView2, borrowImageView3;

    @FXML
    private Label bookNameDetail, nameLabel1, nameLabel2, nameLabel3, nameLabel4, nameLabel5,
            authorLabel1, authorLabel2, authorLabel3, authorLabel4, authorLabel5,
            borrowNameLabel1, borrowNameLabel2, borrowNameLabel3,
            borrowAuthorLabel1, borrowAuthorLabel2, borrowAuthorLabel3;

    @FXML
    private HBox technologyBookshelfHBox, literatureBookshelfHBox,
            economicsBookshelfHBox, foreignLanguageBookshelfHBox, bookDetailView_HB;

    @FXML
    private Button borrowBookButton;

    @FXML
    private ScrollPane userHomePage_ScrollPane;

    public ScrollPane getUserHomePageScrollPane() {
        return userHomePage_ScrollPane;
    }

    public HBox getBookDetailViewHB() {
        return bookDetailView_HB;
    }

    // ExecutorService to manage threads
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void addImageView() {
        recommendBookImageViewList.add(imageView1);
        recommendBookImageViewList.add(imageView2);
        recommendBookImageViewList.add(imageView3);
        recommendBookImageViewList.add(imageView4);
        recommendBookImageViewList.add(imageView5);
        topBorrowBookImageViewList.add(borrowImageView1);
        topBorrowBookImageViewList.add(borrowImageView2);
        topBorrowBookImageViewList.add(borrowImageView3);
    }

    public void initialize() throws SQLException, LogicException, IOException {
        addImageView();
        loadImages();
        startImageSlideshow();
        loadRecommendedBooks();
        loadBookDetails();
        loadTopBorrowedBooks();
        loadBookshelf("Technology", technologyBookshelfHBox);
        loadBookshelf("Literature", literatureBookshelfHBox);
        loadBookshelf("Economics", economicsBookshelfHBox);
        loadBookshelf("Foreign Language", foreignLanguageBookshelfHBox);
        userHomePage_ScrollPane.setVisible(true);
        bookDetailView_HB.setVisible(false);
//        loadUserRequestTableView("6", requestTable_HB);
//        requestTable_HB.setVisible(false);
//        handleAction();
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
                    recommendBookImageViewList.get(finalI).setOnMouseClicked(event -> {
                        System.out.println("recommend book + " + finalI + " is clicked");
                        try {
                            loadBookDetailView(selectedBook.getId(), bookDetailView_HB);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

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

                    imageViewDetail.setOnMouseClicked(event -> {
                        System.out.println("book detail is clicked");
                        try {
                            loadBookDetailView(selectedBook.getId(), bookDetailView_HB);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

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
                    topBorrowBookImageViewList.get(finalI).setOnMouseClicked(event -> {
                        System.out.println("top borrow book is clicked");
                        try {
                            loadBookDetailView(selectedBook.getId(), bookDetailView_HB);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    // Update UI elements on the JavaFX Application Thread
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

    //void showBook(Book book)
    //void sendrequest -> userId, BookId, -> ....


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

    public void loadBookDetailView(int bookId, HBox targetHBox) throws IOException, SQLException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystemuet/book-detail-view-pane.fxml"));
        HBox bookDetailView = loader.load();
        bookViewDetailPaneController controller = loader.getController();
        targetHBox.getChildren().add(bookDetailView);

        controller.loadBookDetailByID(bookId);

        userHomePage_ScrollPane.setVisible(false);
        bookDetailView_HB.setVisible(true);

        controller.getBackButton().setOnMouseClicked(event -> {
            userHomePage_ScrollPane.setVisible(true);
            bookDetailView_HB.setVisible(false);
        });

        controller.getBorrowBookButton1().setOnMouseClicked(event -> {
            try {
                sendRequestToAdmin("6", bookId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
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

    // OKAY
    public void loadUserRequestTableView(String userId, HBox targetHbox) throws IOException, SQLException, LogicException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-request-view-pane.fxml"));
        HBox tableView = loader.load();
        UserRequestTableController controller = loader.getController();
        controller.loadUserRequestIntoTable(userId);
        targetHbox.getChildren().add(tableView);
    }

    // OKAY
    public void sendRequestToAdmin(String userId, int bookId) throws SQLException {
        // Execute query to send user request to admin
        String query = "INSERT INTO usersrequest (userId, bookId, createdTime, lastUpdatedTime, status)"
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectDB();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

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
    }

    // OKAY
    public void handleAction() {
        borrowBookButton.setOnAction(event -> {
           try {
               sendRequestToAdmin("6", 100011);
           } catch (Exception e) {
               e.printStackTrace();
           }
        });
    }
}