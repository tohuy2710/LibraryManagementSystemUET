package org.example.librarymanagementsystemuet;

import com.google.api.services.books.model.Volume;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddBookController extends Thread implements Initializable {

    @FXML
    private GridPane bookContainer;

    @FXML
    private ChoiceBox<String> choiceBoxTypeSearch;

    @FXML
    private Label resultForBookNameLabel;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    private List<Volume> volumesList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxTypeSearch.getItems().addAll("ISBN", "Name");
        choiceBoxTypeSearch.setValue("ISBN");
    }

    public void showSearchResult(ActionEvent event) {

        bookContainer.getChildren().clear();
        int column = 0;
        int row = 1;

        try {
            if (choiceBoxTypeSearch.getValue().equals("ISBN")) {
                volumesList = GoogleBooksAPI.searchBookByISBN(searchTextField.getText()).getItems();
            } else if (choiceBoxTypeSearch.getValue().equals("Name")) {
                volumesList = GoogleBooksAPI.searchBookByName(searchTextField.getText()).getItems();
            }

            if (volumesList == null || volumesList.isEmpty()) {
                resultForBookNameLabel.setText("No result found for " + searchTextField.getText());
                return;
            }
        } catch (Exception e) {
            resultForBookNameLabel.setText("Error fetching data: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        resultForBookNameLabel.setText("Result for " + searchTextField.getText());
        try {
            for (Volume volume : volumesList) {
                if (volume == null) {
                    continue;
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("book-view-card.fxml"));
                HBox bookBox = fxmlLoader.load();

                SearchAddBookCardController searchAddBookCardController = fxmlLoader.getController();
                searchAddBookCardController.setData(volume);

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                bookContainer.add(bookBox, column++, row);
                GridPane.setMargin(bookBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}