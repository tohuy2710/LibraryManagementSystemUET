package org.example.librarymanagementsystemuet.adminapp.bookmanagement;

import com.google.api.client.util.Data;
import com.google.api.services.books.model.Volume;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagementsystemuet.adminapp.bookmanagement.bookviewcard.BookViewCardController;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.GoogleBooksAPI;
import org.example.librarymanagementsystemuet.exception.InvalidDatatype;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.obj.Book;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static org.example.librarymanagementsystemuet.obj.Book.*;

public class AddBookController implements Initializable {

    @FXML
    private Button addBookReloadBookInfoButton;

    @FXML
    private HBox addBookSearchBox;

    @FXML
    private Button addBookToDatabaseButton;

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
    private DatePicker bookDetailPublishDate;

    @FXML
    private TextField bookDetailPublisher;

    @FXML
    private AnchorPane bookDetailUnPickedPane;

    @FXML
    private VBox bookDetailVBox;

    @FXML
    private Button checkLinkCoverButton;

    @FXML
    private ChoiceBox<String> choiceBoxTypeSearchAddBook;

    @FXML
    private Button modifyBookDetailButton;

    @FXML
    private Label resultForBookNameLabel;

    @FXML
    private Button searchButtonAddBook;

    @FXML
    private TextField searchTextFieldAddBook;

    @FXML
    private TextField bookDetailQuantity;

    private ObservableList<Volume> volumesList = FXCollections.observableArrayList();
    private Volume volume = null;

    private String seachingText = null;

    @FXML
    public void addBookToDatabase(ActionEvent event) {
        Database.connect = Database.connectDB();
        String query = "INSERT INTO books " +
                "(isbn, name, author, publisher, category, location, quantity, " +
                "description, linkCoverImage, avgRate, language, publisherDate, pageCount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Database.prepare = Database.connect.prepareStatement(query);

            Book book = new Book();

            Database.prepare.setString(1, bookDetailISBN.getText().isEmpty()
                    ? BOOK_DEFAULT_ISBN : bookDetailISBN.getText());
            Database.prepare.setString(2, bookDetailName.getText().isEmpty()
                    ? BOOK_DEFAULT_NAME : bookDetailName.getText());
            Database.prepare.setString(3, bookDetailAuthor.getText().isEmpty()
                    ? BOOK_DEFAULT_AUTHOR : bookDetailAuthor.getText());
            Database.prepare.setString(4, bookDetailPublisher.getText().isEmpty()
                    ? BOOK_DEFAULT_PUBLISHER : bookDetailPublisher.getText());
            Database.prepare.setString(5, bookDetailCategory.getText().isEmpty()
                    ? BOOK_DEFAULT_CATEGORY : bookDetailCategory.getText());
            Database.prepare.setString(6, bookDetailLocation.getText().isEmpty()
                    ? BOOK_DEFAULT_LOCATION : bookDetailLocation.getText());

            book.setQuantity(bookDetailQuantity.getText());
            Database.prepare.setInt(7, bookDetailQuantity.getText().isEmpty()
                    ? 1 : Integer.parseInt(bookDetailQuantity.getText()));


            Database.prepare.setString(8, bookDetailDescription.getText().isEmpty()
                    ? BOOK_DEFAULT_DESCRIPTION : bookDetailDescription.getText());
            Database.prepare.setString(9, bookDetailCoverLink.getText().isEmpty() ?
                    BOOK_COVER_NOT_FOUND : bookDetailCoverLink.getText());

            book.setAvgRate(bookDetailAvgRate.getText());
            Database.prepare.setFloat(10, bookDetailAvgRate.getText().isEmpty()
                    ? (float) 0.00 : Float.parseFloat(bookDetailAvgRate.getText()));
            Database.prepare.setString(11, bookDetailLanguage.getText().isEmpty()
                    ? BOOK_DEFAULT_LANGUAGE : bookDetailLanguage.getText());
            Database.prepare.setString(12, bookDetailPublishDate.getValue().toString());
            Database.prepare.setInt(13, bookDetailPageCount.getText().isEmpty()
                    ? 0 : Integer.parseInt(bookDetailPageCount.getText()));

            Database.prepare.executeUpdate();
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.successMessage("Add book successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidDatatype e) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.errorMessage(e.getMessage());
        }
    }


    @FXML
    void delAllBookDetailInfoInAddBook(ActionEvent event) {
        volume = null;
        setBookDetailsInfo();
    }

    @FXML
    void reloadBookImage(ActionEvent event) {
        String imageLink = bookDetailCoverLink.getText();
        Database.setImageByLink(bookDetailCover, imageLink);
        AlertMessage alertMessage = new AlertMessage();

        if (imageLink.isEmpty()) {
            alertMessage.errorMessage("Please enter the link to the cover image!");
            return;
        }
        else if (!((Predicate<String>) link -> {
            String urlRegex = "^(http|https)://.*$";
            return link.matches(urlRegex);
        }).test(imageLink)) {
            alertMessage.errorMessage("Invalid link format! Please enter a valid URL.");
            return;
        }
        new Thread(() -> {
            try {
                bookDetailCover.setImage(new Image(imageLink));
            } catch (IllegalArgumentException e) {
                alertMessage.errorMessage("Failed to load image. Check if the link is correct.");
            }
        }).start();
    }


    @FXML
    public void searchVolumesAddBook(ActionEvent event) {
        volumesList.clear();
        seachingText = searchTextFieldAddBook.getText();
        if (choiceBoxTypeSearchAddBook.getValue().equals("ISBN")) {
            resultForBookNameLabel.setText("Searching for ISBN: " + seachingText + "...");
            new Thread(() -> {
                GoogleBooksAPI.SearchByISBN isbnThread = new GoogleBooksAPI.SearchByISBN(seachingText);
                isbnThread.run();
                if (isbnThread.getVolumes().getItems() == null) {
                    Platform.runLater(() -> {
                        resultForBookNameLabel.setText("No result found!");
                    });
                } else {
                    Platform.runLater(() ->
                            volumesList.addAll(isbnThread.getVolumes().getItems()));
                }
            }).start();
        } else if (choiceBoxTypeSearchAddBook.getValue().equals("Name")) {
            resultForBookNameLabel.setText("Searching for Name: " + seachingText + "...");
            new Thread(() -> {
                GoogleBooksAPI.SearchByName nameThread = new GoogleBooksAPI.SearchByName(seachingText);
                nameThread.run();
                if (nameThread.getVolumes().getItems() == null) {
                    Platform.runLater(() -> resultForBookNameLabel.setText("No result found!"));
                } else {
                    Platform.runLater(() -> volumesList.addAll(nameThread.getVolumes().getItems()));
                }
            }).start();
        }
    }

    @FXML
    public void setBookDetailsInfo() {
        if (volume == null) {
            bookDetailName.setText("");
            bookDetailCategory.setText("");
            bookDetailAuthor.setText("");
            bookDetailPublisher.setText("");
            bookDetailDescription.setText("");
            bookDetailAvgRate.setText("");
            bookDetailLanguage.setText("");
            bookDetailPublishDate.setValue(LocalDate.now());
            bookDetailPageCount.setText("");
            bookDetailISBN.setText("");
            bookDetailLocation.setText("");
            bookDetailCoverLink.setText("");
            bookDetailCover.setImage(new Image(getClass().getResourceAsStream("unknowCover.jpg")));
            return;
        }

        bookDetailName.setText(volume.getVolumeInfo().getTitle() != null
                ? volume.getVolumeInfo().getTitle() : BOOK_DEFAULT_NAME);
        bookDetailCategory.setText(volume.getVolumeInfo().getCategories() != null
                && !volume.getVolumeInfo().getCategories().isEmpty()
                ? volume.getVolumeInfo().getCategories().get(0) : BOOK_DEFAULT_CATEGORY);
        bookDetailAuthor.setText(volume.getVolumeInfo().getAuthors() != null
                && !volume.getVolumeInfo().getAuthors().isEmpty()
                ? volume.getVolumeInfo().getAuthors().get(0) : BOOK_DEFAULT_AUTHOR);
        bookDetailPublisher.setText(volume.getVolumeInfo().getPublisher() != null
                ? volume.getVolumeInfo().getPublisher() : BOOK_DEFAULT_PUBLISHER);
        bookDetailDescription.setText(volume.getVolumeInfo().getDescription() != null
                ? volume.getVolumeInfo().getDescription() : BOOK_DEFAULT_DESCRIPTION);
        Database.setImageByLink(bookDetailCover, volume.getVolumeInfo().getImageLinks() != null
                ? volume.getVolumeInfo().getImageLinks().getThumbnail() : BOOK_DEFAULT_IMAGE_LINK);
        bookDetailAvgRate.setText(volume.getVolumeInfo().getAverageRating() != null
                ? String.valueOf(volume.getVolumeInfo().getAverageRating()) : BOOK_DEFAULT_AVG_RATE);
        bookDetailLanguage.setText(volume.getVolumeInfo().getLanguage() != null
                ? volume.getVolumeInfo().getLanguage() : BOOK_DEFAULT_LANGUAGE);

        String publishedDate = Database.validateDate(volume.getVolumeInfo().getPublishedDate());
        bookDetailPublishDate.setValue(LocalDate.parse(publishedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        bookDetailPageCount.setText(volume.getVolumeInfo().getPageCount() != null
                ? String.valueOf(volume.getVolumeInfo().getPageCount()) : BOOK_DEFAULT_PAGE_COUNT);
        bookDetailISBN.setText(volume.getVolumeInfo().getIndustryIdentifiers() != null
                && !volume.getVolumeInfo().getIndustryIdentifiers().isEmpty()
                ? volume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier() : BOOK_DEFAULT_ISBN);
        bookDetailLocation.setText(BOOK_DEFAULT_LOCATION);
        bookDetailCoverLink.setText(volume.getVolumeInfo().getImageLinks() != null
                ? volume.getVolumeInfo().getImageLinks().getThumbnail() : BOOK_DEFAULT_IMAGE_LINK);
        bookDetailQuantity.setText("1");
    }

    @FXML
    private HBox selectedBookBox = null;

    private void showBooksAddBookResults() {
        bookContainer.getChildren().clear();
        int column = 0;
        int row = 1;

        try {
            for (Volume volume : volumesList) {
                if (volume == null) {
                    continue;
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/org/example" +
                        "/librarymanagementsystemuet/book-view-card.fxml"));
                HBox bookBox = fxmlLoader.load();

                BookViewCardController bookViewCardController = fxmlLoader.getController();
                bookViewCardController.setData(volume);

                // Set default style
                bookBox.setStyle("-fx-background-color: #ffffff");

                // Set up click event to change background color
                bookBox.setOnMouseClicked(ev -> {
                    // Reset the background color of the previously selected bookBox
                    if (selectedBookBox != null) {
                        selectedBookBox.setStyle("-fx-background-color: #ffffff");
                    }
                    // Set the new color for the currently selected bookBox
                    bookBox.setStyle("-fx-background-color: #bfd8ff");
                    selectedBookBox = bookBox; // Update the selected bookBox

                    // Optionally, store the selected volume for further processing
                    this.volume = volume;
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxTypeSearchAddBook.getItems().addAll("ISBN", "Name");
        choiceBoxTypeSearchAddBook.setValue("ISBN");

        volumesList.addListener((ListChangeListener<Volume>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    showBooksAddBookResults();
                }
            }
        });
    }
}