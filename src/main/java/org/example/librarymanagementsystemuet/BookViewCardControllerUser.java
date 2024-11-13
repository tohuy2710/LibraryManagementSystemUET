package org.example.librarymanagementsystemuet;

import com.google.api.services.books.model.Volume;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookViewCardControllerUser {

    @FXML
    private ImageView bookCoverImage;

    @FXML
    private Label bookName;

    @FXML
    private Label bookAuthor;

    @FXML
    private Label bookISBN;

    private String bookID;

    public void setData(ResultSet resultSet) throws SQLException {
        bookID = resultSet.getString("bookid");
        bookName.setText(resultSet.getString("name"));
        bookAuthor.setText(resultSet.getString("author"));
        bookISBN.setText("ISBN: " + resultSet.getString("isbn"));
        String imageUrl = resultSet.getString("linkCoverImage");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            bookCoverImage.setImage(new Image(imageUrl));
        } else {
            bookCoverImage.setImage(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemuet/unknowCover.jpg")));
        }
    }

    public void setData(Volume volume) {
        bookName.setText(volume.getVolumeInfo().getTitle());
        bookAuthor.setText(volume.getVolumeInfo().getAuthors() != null ? String.join(", ", volume.getVolumeInfo().getAuthors()) : "Unknown");
        bookISBN.setText("ISBN: " + (volume.getVolumeInfo().getIndustryIdentifiers() != null && !volume.getVolumeInfo().getIndustryIdentifiers().isEmpty() ? volume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier() : "Unknown"));
        String imageUrl = volume.getVolumeInfo().getImageLinks() != null ? volume.getVolumeInfo().getImageLinks().getThumbnail() : null;
        if (imageUrl != null && !imageUrl.isEmpty()) {
            bookCoverImage.setImage(new Image(imageUrl));
        } else {
            bookCoverImage.setImage(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemuet/unknowCover.jpg")));
        }
    }

    public String getID() {
        return bookID;
    }
}