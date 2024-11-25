package org.example.librarymanagementsystemuet.userapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.Book;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.Database.*;

public class UserBrowserBookController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private StackPane mainPane;

    private void showFullBooks() {
        gridPane.getChildren().clear();
        int row = 0;
        int col = 0;
        String lastCategory = "";

        try {
            Database.connect = connectDB();
            String query = "(\n" +
                    "    SELECT * \n" +
                    "    FROM books\n" +
                    "    WHERE category = \"Information Technology\"\n" +
                    "    LIMIT 5\n" +
                    ")\n" +
                    "UNION ALL\n" +
                    "(\n" +
                    "    SELECT * \n" +
                    "    FROM books\n" +
                    "    WHERE category = \"Economics\"\n" +
                    "    LIMIT 5\n" +
                    ");\n";

            preparedStatement = Database.connect.prepareStatement(query);
            Database.result = preparedStatement.executeQuery();

            while (Database.result != null && Database.result.next()) {
                String category = Database.result.getString("category");

                if (!category.equals(lastCategory)) {
                    lastCategory = category;

                    VBox labelBox = new VBox();
                    Label categoryLabel = new Label(category);
                    categoryLabel.getStyleClass().add("labelHeader");
                    labelBox.getChildren().add(categoryLabel);
                    labelBox.setPadding(new Insets(5, 0, 10, 0));

                    gridPane.add(labelBox, 0, row == 0 ? row : ++row, 5, 1);
                    GridPane.setHalignment(labelBox, HPos.LEFT);
                    row++;
                    col = 0;
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/org/example" +
                        "/librarymanagementsystemuet/borrow-book-view-card-vertical.fxml"));
                VBox bookBox = fxmlLoader.load();

                BorrowBookViewCardVerticalController
                        borrowBookViewCardVerticalController = fxmlLoader.getController();
                borrowBookViewCardVerticalController.setParentController(this);

                Book book = setBookInfo();
                borrowBookViewCardVerticalController.setInfo(book);

                GridPane.setHalignment(bookBox, HPos.CENTER);
                GridPane.setValignment(bookBox, VPos.TOP);

                if (col == 5) {
                    col = 0;
                    ++row;
                }

                gridPane.add(bookBox, col++, row);
                gridPane.setAlignment(Pos.TOP_CENTER);
                gridPane.setPadding(new Insets(10));
                gridPane.setVgap(10);
                gridPane.setHgap(10);
                GridPane.setHalignment(gridPane, HPos.CENTER);
                GridPane.setValignment(gridPane, VPos.TOP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showFullBooks();
    }
}
