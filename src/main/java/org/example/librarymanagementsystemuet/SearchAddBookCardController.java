package org.example.librarymanagementsystemuet;

import com.google.api.services.books.model.Volume;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SearchAddBookCardController {

    @FXML
    private Label bookAuthor;

    @FXML
    private Button bookDetailButton;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookName;

    @FXML
    private ImageView bookRating;

    private Volume volume;

    public void setData(Volume volume) {
        this.volume = volume;

        if (volume.getVolumeInfo().getTitle() == null) {
            bookName.setText("Unknown");
        } else {
            bookName.setText(volume.getVolumeInfo().getTitle());
        }

        if (volume.getVolumeInfo().getAuthors() != null && !volume.getVolumeInfo().getAuthors().isEmpty()) {
            bookAuthor.setText(volume.getVolumeInfo().getAuthors().get(0));
        } else {
            bookAuthor.setText("Unknown");
        }

        if (volume.getVolumeInfo().getImageLinks() == null) {
            bookImage.setImage(new Image(getClass().getResourceAsStream("unknowCover.jpg")));
        } else {
            bookImage.setImage(new Image(volume.getVolumeInfo().getImageLinks().getThumbnail()));
        }
    }

    @FXML
    public void showBookDetail(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-book-book-detail.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);

            AddBookBookDetailController controller = loader.getController();
            controller.setBookDetails(volume);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}