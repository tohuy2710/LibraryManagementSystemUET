package org.example.librarymanagementsystemuet;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import package1.Book;

import java.net.URL;
import java.util.ResourceBundle;

public class BookViewCardVerticalController implements Initializable {

    @FXML
    private Label authorLabel;

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
