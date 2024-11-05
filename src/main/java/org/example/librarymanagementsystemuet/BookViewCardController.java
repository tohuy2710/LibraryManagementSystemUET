package org.example.librarymanagementsystemuet;

import com.google.api.services.books.model.Volume;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;


public class BookViewCardController implements Initializable {

    @FXML
    private Label bookAuthorAddBookResult;

    @FXML
    private HBox bookBox;

    @FXML
    private ImageView bookImageAddBookResult;

    @FXML
    private Label bookNameAddBookResult;

    @FXML
    private Label bookPublisherAddBookResult;

    private Volume volume;

    private String ID;

    public void setData(Volume volume) {
        this.volume = volume;

        if (volume.getVolumeInfo().getTitle() == null) {
            bookNameAddBookResult.setText("Unknown");
        } else {
            bookNameAddBookResult.setText(volume.getVolumeInfo().getTitle());
        }

        if (volume.getVolumeInfo().getAuthors() != null && !volume.getVolumeInfo().getAuthors().isEmpty()) {
            bookAuthorAddBookResult.setText(volume.getVolumeInfo().getAuthors().get(0));
        } else {
            bookAuthorAddBookResult.setText("Unknown");
        }

        if (volume.getVolumeInfo().getPublisher() != null && !volume.getVolumeInfo().getPublisher().isEmpty()) {
            bookPublisherAddBookResult.setText(volume.getVolumeInfo().getPublisher());
        } else {
            bookPublisherAddBookResult.setText("Unknown");
        }

        if (volume.getVolumeInfo().getImageLinks() != null) {
            Database.setImageByLink(bookImageAddBookResult, volume.getVolumeInfo().getImageLinks().getThumbnail());
        } else {

        }

        if (volume.getVolumeInfo().getPublisher() == null) {
            bookPublisherAddBookResult.setText("Unknown");
        } else {
            bookPublisherAddBookResult.setText(volume.getVolumeInfo().getPublisher());
        }
    }

    public void setData(ResultSet resultSet) {
        try {
            ID = resultSet.getString("id");
            bookNameAddBookResult.setText(resultSet.getString("name"));
            bookAuthorAddBookResult.setText(resultSet.getString("author"));
            bookPublisherAddBookResult.setText(resultSet.getString("publisher"));
            Database.setImageByLink(bookImageAddBookResult, resultSet.getString("linkCoverImage"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getID() {
        return ID;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookBox.setOnMouseClicked(event -> {
            bookBox.setStyle("-fx-background-color: #bfd8ff");
        });
    }
}