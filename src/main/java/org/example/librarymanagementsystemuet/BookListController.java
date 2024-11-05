package org.example.librarymanagementsystemuet;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class BookListController {

    @FXML
    private TableColumn<?, ?> addedDateCol;

    @FXML
    private TableColumn<?, ?> authorsCol;

    @FXML
    private TableColumn<?, ?> bookNameCol;

    @FXML
    private TableView<?> bookTable;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> isbnCol;

    @FXML
    private TableColumn<?, ?> publisherCol;

    @FXML
    private HBox searchBookResultBox;

}

//            Database.connect = Database.connectDB();
//            String searchQuery = null;
//            String searchValue = searchTextField.getText();
//
//            if (!searchValue.isEmpty()) {
//                if (selectTypeSearch.getValue().equals("ID")) {
//                    searchQuery = "SELECT * FROM books WHERE id = ?";
//                } else if (selectTypeSearch.getValue().equals("ISBN")) {
//                    searchQuery = "SELECT * FROM books WHERE isbn = ?";
//                } else if (selectTypeSearch.getValue().equals("Name")) {
//                    searchQuery = "SELECT * FROM books WHERE name LIKE ?";
//                    searchValue = "%" + searchValue + "%";
//                }
//
//                if (searchQuery != null) {
//                    Database.prepare = Database.connect.prepareStatement(searchQuery);
//                    Database.prepare.setString(1, searchValue);
//                    Database.result = Database.prepare.executeQuery();
//                }
//
//                bookList.clear();
//
//                while (Database.result != null && Database.result.next()) {
//                    Book book = new Book();
//                    book.setId(Database.result.getInt("id"));
//                    book.setIsbn(Database.result.getString("isbn"));
//                    book.setName(Database.result.getString("name"));
//                    book.setAuthor(Database.result.getString("author"));
//                    book.setPublisher(Database.result.getString("publisher"));
//                    book.setAddedDate(Database.result.getString("addedDate"));
//                    bookList.add(book);
//                }
//            } else {
//                AlertMessage alertMessage = new AlertMessage();
//                alertMessage.errorMessage("Please enter a search value!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
//        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        authorsCol.setCellValueFactory(new PropertyValueFactory<>("author"));
//        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
//        addedDateCol.setCellValueFactory(new PropertyValueFactory<>("addedDate"));
//
//        Callback<TableColumn<Book, String>, TableCell<Book, String>> cellFactory = (param) -> {
//            final TableCell<Book, String> cell = new TableCell<Book, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    if (empty) {
//                        setGraphic(null);
//                        setText(null);
//                    } else {
//                        final Button editButton = new Button("Edit");
//                        editButton.setOnAction(event -> {
//                            Book book = getTableView().getItems().get(getIndex());
//                            System.out.println(book.getId());
//                        });
//                        setGraphic(editButton);
//                        setText(null);
//                    }
//                }
//            };
//            return cell;
//        };
//
//        bookTable.setItems(bookList);
