package org.example.librarymanagementsystemuet.adminapp.bookmanagement.bookviewcard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.adminapp.bookmanagement.BookDetailController;
import org.example.librarymanagementsystemuet.adminapp.bookmanagement.BrowserBookController;
import org.example.librarymanagementsystemuet.obj.Book;

import java.net.URL;
import java.util.ResourceBundle;

public class BookViewCardVerticalController implements Initializable {

    @FXML
    private Label authorLabel;

    @FXML
    private AnchorPane bookBox;

    @FXML
    private VBox bookVBox;

    @FXML
    private Button detailButton;

    @FXML
    private ImageView imageView;

    @FXML
    private VBox infoVBox;

    @FXML
    private Label nameLabel;

    private BrowserBookController parentController;

    public void setParentController(BrowserBookController parentController) {
        this.parentController = parentController;
    }

    private Book book;

    public void setInfo(Book book) {
        this.book = book;
        if (book.getName() != null) {
            nameLabel.setText(book.getName());
        } else {
            nameLabel.setText("UNKNOWN");
        }
        if (book.getAuthor() != null) {
            authorLabel.setText(book.getAuthor());
        } else {
            authorLabel.setText("UNKNOWN");
        }
        Database.setImageByLink(imageView, book.getImageLink());
    }

    @FXML
    public void showDetail(ActionEvent event) {
        parentController.showBookDetailHBox(book);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        infoVBox.setOnMouseEntered(event -> {
            infoVBox.setVisible(false);
            detailButton.setVisible(true);
        });

        detailButton.setOnMouseExited(event -> {
            infoVBox.setVisible(true);
            detailButton.setVisible(false);
        });
    }
}
