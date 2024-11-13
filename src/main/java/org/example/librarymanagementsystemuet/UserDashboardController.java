package org.example.librarymanagementsystemuet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDashboardController {

    @FXML
    private VBox topBorrowedBook1HBox;

    @FXML
    private VBox topBorrowedBook2HBox;

    @FXML
    private VBox topBorrowedBook3HBox;

    @FXML
    private VBox topBorrowedBook4HBox;

    @FXML
    private VBox topBorrowedBook5HBox;

    @FXML
    private VBox topBorrowedBook6HBox;

    @FXML
    private ImageView imageView;

    @FXML
    private Button playPauseButton;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;

    @FXML
    public void initialize() {
        loadTopBorrowedBooks();
        loadImages();
        startImageSlideshow();
        setupMediaPlayer();
    }

    private void loadTopBorrowedBooks() {
        try {
            Database.connect = Database.connectDB();
            String query = "SELECT bookId, COUNT(userId) AS cnt, " +
                    "books.linkCoverImage, books.name, books.author, books.isbn " +
                    "FROM usersrequest " +
                    "LEFT JOIN books ON books.id = usersrequest.bookId " +
                    "GROUP BY bookId " +
                    "ORDER BY cnt DESC " +
                    "LIMIT 6;";
            Database.prepare = Database.connect.prepareStatement(query);
            Database.result = Database.prepare.executeQuery();

            int i = 1;
            while (Database.result != null && Database.result.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("book-view-card-user.fxml"));
                VBox bookBox = fxmlLoader.load();

                BookViewCardControllerUser bookViewCardController = fxmlLoader.getController();
                bookViewCardController.setData(Database.result);

                switch (i) {
                    case 1 -> topBorrowedBook1HBox.getChildren().add(bookBox);
                    case 2 -> topBorrowedBook2HBox.getChildren().add(bookBox);
                    case 3 -> topBorrowedBook3HBox.getChildren().add(bookBox);
                    case 4 -> topBorrowedBook4HBox.getChildren().add(bookBox);
                    case 5 -> topBorrowedBook5HBox.getChildren().add(bookBox);
                    case 6 -> topBorrowedBook6HBox.getChildren().add(bookBox);
                }
                i++;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Database.result != null) Database.result.close();
                if (Database.prepare != null) Database.prepare.close();
                if (Database.connect != null) Database.connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadImages() {
        images.add(new Image(getClass().getResourceAsStream("/asset/img/nen1.png")));
        images.add(new Image(getClass().getResourceAsStream("/asset/img/nen2.png")));
        images.add(new Image(getClass().getResourceAsStream("/asset/img/nen3.png")));
    }

    private void startImageSlideshow() {
        imageView.setImage(images.get(currentImageIndex));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            imageView.setImage(images.get(currentImageIndex));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setupMediaPlayer() {
        String musicFile = getClass().getResource("/asset/img/music.mp3").toExternalForm();
        Media media = new Media(musicFile);
        mediaPlayer = new MediaPlayer(media);

        playPauseButton.setOnAction(event -> handlePlayPause());

        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    @FXML
    private void handlePlayPause() {
        if (isPlaying) {
            mediaPlayer.pause();
            playPauseButton.setText("Play");
        } else {
            mediaPlayer.play();
            playPauseButton.setText("Pause");
        }
        isPlaying = !isPlaying;
    }
}