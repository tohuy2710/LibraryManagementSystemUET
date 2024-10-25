package org.example.librarymanagementsystemuet;

import com.google.api.client.util.Data;
import com.google.api.services.books.model.Volume;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import package1.AlertMessage;

import java.sql.*;

import static org.example.librarymanagementsystemuet.Database.connectDB;

public class AddBookBookDetailController {

    @FXML
    private Label bookDetailAvgRate;

    @FXML
    private Label bookDetailAuthor;

    @FXML
    private Label bookDetailCategory;

    @FXML
    private ImageView bookDetailCover;

    @FXML
    private Text bookDetailDescription;

    @FXML
    private Label bookDetailLanguage;

    @FXML
    private Text bookDetailName;

    @FXML
    private Label bookDetailPageCount;

    @FXML
    private Hyperlink bookDetailPreviewLink;

    @FXML
    private Label bookDetailPublishDate;

    @FXML
    private Label bookDetailPublisher;

    Volume volume;

    public void setBookDetails(Volume volume) {

        this.volume = volume;

        if (volume.getVolumeInfo().getTitle() != null) {
            bookDetailName.setText(volume.getVolumeInfo().getTitle());
        } else {
            bookDetailName.setText("Unknown");
        }

        if (volume.getVolumeInfo().getCategories() != null && !volume.getVolumeInfo().getCategories().isEmpty()) {
            bookDetailCategory.setText(volume.getVolumeInfo().getCategories().get(0));
        } else {
            bookDetailCategory.setText("Unknown");
        }

        if (volume.getVolumeInfo().getAuthors() != null && !volume.getVolumeInfo().getAuthors().isEmpty()) {
            bookDetailAuthor.setText(volume.getVolumeInfo().getAuthors().get(0));
        } else {
            bookDetailAuthor.setText("Unknown");
        }

        if (volume.getVolumeInfo().getPublisher() != null) {
            bookDetailPublisher.setText(volume.getVolumeInfo().getPublisher());
        } else {
            bookDetailPublisher.setText("Unknown");
        }

        if (volume.getVolumeInfo().getDescription() != null) {
            bookDetailDescription.setText(volume.getVolumeInfo().getDescription());
        } else {
            bookDetailDescription.setText("Unknown");
        }

        if (volume.getVolumeInfo().getImageLinks() != null) {
            bookDetailCover.setImage(new Image(volume.getVolumeInfo().getImageLinks().getThumbnail()));
        } else {
            bookDetailCover.setImage(new Image(getClass().getResourceAsStream("unknowCover.jpg")));
        }

        if (volume.getVolumeInfo().getAverageRating() != null) {
            bookDetailAvgRate.setText(String.valueOf(volume.getVolumeInfo().getAverageRating()));
        } else {
            bookDetailAvgRate.setText("Unknown");
        }

        if (volume.getVolumeInfo().getLanguage() != null) {
            bookDetailLanguage.setText(volume.getVolumeInfo().getLanguage());
        } else {
            bookDetailLanguage.setText("Unknown");
        }

        if (volume.getVolumeInfo().getPublishedDate() != null) {
            bookDetailPublishDate.setText(volume.getVolumeInfo().getPublishedDate());
        } else {
            bookDetailPublishDate.setText("Unknown");
        }

        if (volume.getVolumeInfo().getPageCount() != null) {
            bookDetailPageCount.setText(String.valueOf(volume.getVolumeInfo().getPageCount()));
        } else {
            bookDetailPageCount.setText("Unknown");
        }

        if (volume.getVolumeInfo().getPreviewLink() != null) {
            bookDetailPreviewLink.setText(volume.getVolumeInfo().getPreviewLink());
            bookDetailPreviewLink.setOnAction(event -> {
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(volume.getVolumeInfo().getPreviewLink()));
                } catch (java.io.IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        } else {
            bookDetailPreviewLink.setText("Unknown");
        }
    }

    public void previewLinkOnclick(ActionEvent event) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(bookDetailPreviewLink.getText()));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBookToDatabase(ActionEvent event) {
        Database.connect = connectDB();
        String query = "INSERT INTO books " +
                "(isbn, name, author, publisher, description, linkCoverImage, " +
                "avgRate, language, pageCount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Database.prepare = Database.connect.prepareStatement(query);
            if (volume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier() == null) {
                Database.prepare.setString(1, "Unknown");
            } else {
                Database.prepare.setString(1, volume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier());
            }

            if (volume.getVolumeInfo().getTitle() == null) {
                Database.prepare.setString(2, "Unknown");
            } else {
                Database.prepare.setString(2, volume.getVolumeInfo().getTitle());
            }

            if (volume.getVolumeInfo().getAuthors() == null) {
                Database.prepare.setString(3, "Unknown");
            } else {
                Database.prepare.setString(3, volume.getVolumeInfo().getAuthors().get(0));
            }

            if (volume.getVolumeInfo().getPublisher() == null) {
                Database.prepare.setString(4, "Unknown");
            } else {
                Database.prepare.setString(4, volume.getVolumeInfo().getPublisher());
            }

            if (volume.getVolumeInfo().getDescription() == null) {
                Database.prepare.setString(5, "Unknown");
            } else {
                Database.prepare.setString(5, volume.getVolumeInfo().getDescription());
            }

            if (volume.getVolumeInfo().getImageLinks() == null) {
                Database.prepare.setString(6, "Unknown");
            } else {
                Database.prepare.setString(6, volume.getVolumeInfo().getImageLinks().getThumbnail());
            }

            if (volume.getVolumeInfo().getAverageRating() == null) {
                Database.prepare.setDouble(7, 0);
            } else {
                Database.prepare.setDouble(7, volume.getVolumeInfo().getAverageRating());
            }

            if (volume.getVolumeInfo().getLanguage() == null) {
                Database.prepare.setString(8, "Unknown");
            } else {
                Database.prepare.setString(8, volume.getVolumeInfo().getLanguage());
            }

            if (volume.getVolumeInfo().getPageCount() == null) {
                Database.prepare.setInt(9, 0);
            } else {
                Database.prepare.setInt(9, volume.getVolumeInfo().getPageCount());
            }

            Database.prepare.executeUpdate();

            AlertMessage alertMessage = new AlertMessage();
            alertMessage.successMessage("Add Book To Database Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}