package org.example.librarymanagementsystemuet;

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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import package1.AlertMessage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

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
    private TextField bookDetailDescription;

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
    private TextField bookDetailPublishDate;

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

    private ObservableList<Volume> volumesList = FXCollections.observableArrayList();
    private Volume volume = null;

    private String seachingText = null;

    @FXML
    public void addBookToDatabase(ActionEvent event) {
        Database.connect = Database.connectDB();
        String query = "INSERT INTO books " +
                "(isbn, name, author, publisher, description, linkCoverImage, " +
                "avgRate, language, pageCount, location) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Database.prepare = Database.connect.prepareStatement(query);

            // Kiểm tra và gán giá trị cho các trường
            Database.prepare.setString(1, bookDetailISBN.getText().isEmpty()
                    ? "Unknown" : bookDetailISBN.getText());
            Database.prepare.setString(2, bookDetailName.getText().isEmpty()
                    ? "Unknown" : bookDetailName.getText());
            Database.prepare.setString(3, bookDetailAuthor.getText().isEmpty()
                    ? "Unknown" : bookDetailAuthor.getText());
            Database.prepare.setString(4, bookDetailPublisher.getText().isEmpty()
                    ? "Unknown" : bookDetailPublisher.getText());
            Database.prepare.setString(5, bookDetailDescription.getText().isEmpty()
                    ? "Unknown" : bookDetailDescription.getText());
            Database.prepare.setString(6, bookDetailCoverLink.getText().isEmpty()
                    ? "Unknown" : bookDetailCoverLink.getText());
            // Kiểm tra và gán giá trị cho avgRate và pageCount
            try {
                double avgRate = bookDetailAvgRate.getText().isEmpty()
                        ? 0 : Double.parseDouble(bookDetailAvgRate.getText());
                Database.prepare.setDouble(7, avgRate);
            } catch (NumberFormatException e) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage("Average rate is not a valid number");
                return;
            }

            Database.prepare.setString(8, bookDetailLanguage.getText().isEmpty() ? "Unknown" : bookDetailLanguage.getText());

            try {
                int pageCount = bookDetailPageCount.getText().isEmpty() ? 0 : Integer.parseInt(bookDetailPageCount.getText());
                Database.prepare.setInt(9, pageCount);
            } catch (NumberFormatException e) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage("Page count is not a valid number");
                return;
            }
            Database.prepare.setString(10, bookDetailLocation.getText().isEmpty()
                    ? "Unknown" : bookDetailLocation.getText());

            // Thực thi truy vấn
            Database.prepare.executeUpdate();

            // Thông báo thành công
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.successMessage("Add Book To Database Successfully!");

        } catch (SQLException e) {
            // Xử lý lỗi SQL
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void delAllBookDetailInfoInAddBook(ActionEvent event) {
        volume = null;
        setBookDetailsInfo();
    }

    @FXML
    void reloadBookImage(ActionEvent event) {
        bookDetailCover.setImage(new Image(getClass().getResourceAsStream("unknowCover.jpg")));
        String imageLink = bookDetailCoverLink.getText();
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
            bookDetailPublishDate.setText("");
            bookDetailPageCount.setText("");
            bookDetailISBN.setText("");
            bookDetailLocation.setText("");
            bookDetailCoverLink.setText("");
            bookDetailCover.setImage(new Image(getClass().getResourceAsStream("unknowCover.jpg")));
            return;
        }

        bookDetailName.setText(volume.getVolumeInfo().getTitle() != null
                ? volume.getVolumeInfo().getTitle() : "Unknown");
        bookDetailCategory.setText(volume.getVolumeInfo().getCategories() != null
                && !volume.getVolumeInfo().getCategories().isEmpty()
                ? volume.getVolumeInfo().getCategories().get(0) : "Unknown");
        bookDetailAuthor.setText(volume.getVolumeInfo().getAuthors() != null
                && !volume.getVolumeInfo().getAuthors().isEmpty()
                ? volume.getVolumeInfo().getAuthors().get(0) : "Unknown");
        bookDetailPublisher.setText(volume.getVolumeInfo().getPublisher() != null
                ? volume.getVolumeInfo().getPublisher() : "Unknown");
        bookDetailDescription.setText(volume.getVolumeInfo().getDescription() != null
                ? volume.getVolumeInfo().getDescription() : "Unknown");
        bookDetailCover.setImage(volume.getVolumeInfo().getImageLinks() != null
                ? new Image(volume.getVolumeInfo().getImageLinks().getThumbnail())
                : new Image(getClass().getResourceAsStream("unknowCover.jpg")));
        bookDetailAvgRate.setText(volume.getVolumeInfo().getAverageRating() != null
                ? String.valueOf(volume.getVolumeInfo().getAverageRating()) : "Unknown");
        bookDetailLanguage.setText(volume.getVolumeInfo().getLanguage() != null
                ? volume.getVolumeInfo().getLanguage() : "Unknown");
        bookDetailPublishDate.setText(volume.getVolumeInfo().getPublishedDate() != null
                ? volume.getVolumeInfo().getPublishedDate() : "Unknown");
        bookDetailPageCount.setText(volume.getVolumeInfo().getPageCount() != null
                ? String.valueOf(volume.getVolumeInfo().getPageCount()) : "Unknown");
        bookDetailISBN.setText(volume.getVolumeInfo().getIndustryIdentifiers() != null
                && !volume.getVolumeInfo().getIndustryIdentifiers().isEmpty()
                ? volume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier() : "Unknown");
        bookDetailLocation.setText("Unknown");
        bookDetailCoverLink.setText(volume.getVolumeInfo().getImageLinks() != null
                ? volume.getVolumeInfo().getImageLinks().getThumbnail() : "Unknown");
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
                fxmlLoader.setLocation(getClass().getResource("book-view-card.fxml"));
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