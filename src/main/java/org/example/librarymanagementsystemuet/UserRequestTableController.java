package org.example.librarymanagementsystemuet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagementsystemuet.exception.LogicException;
import org.example.librarymanagementsystemuet.obj.UserRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.librarymanagementsystemuet.Database.connectDB;

public class UserRequestTableController {
    @FXML
    private HBox userRequest_HB;

    @FXML
    private TableColumn<UserRequest, String> userRequest_IdCol, userRequest_createdTimeCol,
            userRequest_lastUpdatedTimeCol, userRequest_statusCol, userRequest_BookIdCol;

    @FXML
    private TableView<UserRequest> userRequest_TV;

    private final ObservableList<UserRequest> userRequestList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        userRequest_HB.setVisible(true);
    }

    public void addUserRequestIntoList(ResultSet resultSet) throws SQLException, LogicException {
        while (resultSet != null && resultSet.next()) {
            UserRequest userRequest = new UserRequest();
            userRequest.setRequestID(resultSet.getString("id"));
            userRequest.setBookID(resultSet.getString("bookId"));
            userRequest.setCreatedTime(resultSet.getString("createdTime"));
            userRequest.setLastUpdatedTime(resultSet.getString("lastUpdatedTime"));
            userRequest.setStatus(resultSet.getString("status"));
            System.out.println(userRequest.getRequestID() + " " + userRequest.getBookID() + " " +
                    userRequest.getStatus() + " " + userRequest.getCreatedTime() + " " + userRequest.getLastUpdatedTime());
            userRequestList.add(userRequest);
        }
    }

    public void loadUserRequestIntoTable(String userId) throws SQLException, LogicException {
        userRequest_IdCol.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        userRequest_BookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        userRequest_createdTimeCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        userRequest_lastUpdatedTimeCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedTime"));
        userRequest_statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        userRequestList.clear();

        String query = "SELECT * FROM usersrequest WHERE userId = ?";
        try (Connection conn = connectDB();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            addUserRequestIntoList(resultSet);
        }
        userRequest_TV.setItems(userRequestList);
    }
}
