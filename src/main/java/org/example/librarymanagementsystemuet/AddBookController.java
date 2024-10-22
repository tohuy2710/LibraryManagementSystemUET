package org.example.librarymanagementsystemuet;

import com.google.api.services.books.model.Volumes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import package1.AlertMessage;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.GoogleBooksAPI.searchBookByISBN;

public class AddBookController extends Thread implements Initializable {

    @FXML
    private Label ISBNBook1;

    @FXML
    private Label ISBNBook2;

    @FXML
    private Label ISBNBook3;

    @FXML
    private Label PublishedDateBook1;

    @FXML
    private Label PublishedDateBook2;

    @FXML
    private Label PublishedDateBook3;

    @FXML
    private Button addBook1Button;

    @FXML
    private Button addBookButton;

    @FXML
    private Button addBookButton2;

    @FXML
    private Button addBookButton3;

    @FXML
    private Label bookAuthors;

    @FXML
    private ImageView bookCoverImage;

    @FXML
    private Text bookDescription;

    @FXML
    private Label bookISBN;

    @FXML
    private Label bookName;

    @FXML
    private Label bookPublisher;

    @FXML
    private HBox hboxBook1;

    @FXML
    private HBox hboxBook2;

    @FXML
    private HBox hboxBook3;

    @FXML
    private ImageView imageCoverBook1;

    @FXML
    private ImageView imageCoverBook2;

    @FXML
    private ImageView imageCoverBook3;

    @FXML
    private Label nameBook1;

    @FXML
    private Label nameBook2;

    @FXML
    private Label nameBook3;

    @FXML
    private Button nextBookButton;

    @FXML
    private Label publisherBook1;

    @FXML
    private Label publisherBook2;

    @FXML
    private Label publisherBook3;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private ChoiceBox<String> selectTypeSearch;

    @FXML
    private AnchorPane paneBookDetail;

    @FXML
    private AnchorPane paneSearchBook;

    private String[] typeSearch = {"ISBN", "Name"};

    public void searchBookByBooksAPI() throws GeneralSecurityException, IOException {

        paneBookDetail.setVisible(true);
        paneSearchBook.setVisible(false);

        if (selectTypeSearch.getValue().equals("ISBN")) {
            String ISBN = searchTextField.getText();

            if (ISBN.isEmpty()) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.errorMessage("Please enter ISBN to search!");
                return;
            }
            else {
                Volumes volumes = searchBookByISBN(ISBN);
                if (volumes.getTotalItems() == 0) {
                    AlertMessage alertMessage = new AlertMessage();
                    alertMessage.errorMessage("No book found with the given ISBN!");
                    return;
                }
                else {
                    // Display the book details
                    // ...
                }

            }
        } else {
            //search by name
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paneBookDetail.setVisible(false);
        selectTypeSearch.getItems().addAll(typeSearch);
    }
}
