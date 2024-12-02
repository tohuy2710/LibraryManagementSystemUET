package org.example.librarymanagementsystemuet.userapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.example.librarymanagementsystemuet.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DiscussionPageController {

    @FXML
    private ListView<String> topicListView;

    @FXML
    private ListView<String> commentListView;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private TextArea newTopicTextArea;

    private ObservableList<String> topics = FXCollections.observableArrayList();
    private ObservableList<String> comments = FXCollections.observableArrayList();
    private Timeline timeline;

    @FXML
    public void initialize() {
        topicListView.setItems(topics);
        commentListView.setItems(comments);

        topicListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadCommentsForTopic(newValue);
            }
        });

        loadTopics();

        timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            String selectedTopic = topicListView.getSelectionModel().getSelectedItem();
            if (selectedTopic != null) {
                loadCommentsForTopic(selectedTopic);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void loadTopics() {
        Task<ObservableList<String>> task = new Task<>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                ObservableList<String> newTopics = FXCollections.observableArrayList();
                try (Connection conn = Database.connectDB();
                     PreparedStatement pstmt = conn.prepareStatement("SELECT DISTINCT nameTopic FROM topicComment")) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        newTopics.add(rs.getString("nameTopic"));
                    }
                }
                return newTopics;
            }
        };

        task.setOnSucceeded(e -> topics.setAll(task.getValue()));
        new Thread(task).start();
    }

    private void loadCommentsForTopic(String topic) {
        Task<ObservableList<String>> task = new Task<>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                ObservableList<String> newComments = FXCollections.observableArrayList();
                try (Connection conn = Database.connectDB();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "SELECT tc.comment, tc.commentTime, u.username " +
                                     "FROM topicComment tc " +
                                     "JOIN users u ON tc.userId = u.id " +
                                     "WHERE tc.nameTopic = ?")) {
                    pstmt.setString(1, topic);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        String comment = rs.getString("comment");
                        String commentTime = rs.getString("commentTime");
                        String userName = rs.getString("username");
                        newComments.add(userName + " (" + commentTime + "): " + comment);
                    }
                }
                return newComments;
            }
        };

        task.setOnSucceeded(e -> comments.setAll(task.getValue()));
        new Thread(task).start();
    }

    @FXML
    private void handleAddComment() {
        String content = commentTextArea.getText();
        if (content.isEmpty()) {
            return;
        }

        String selectedTopic = topicListView.getSelectionModel().getSelectedItem();
        if (selectedTopic == null) {
            return;
        }

        int userId = getCurrentUserID();

        try (Connection conn = Database.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO topicComment (nameTopic, userId, comment) VALUES (?, ?, ?)")) {
            pstmt.setString(1, selectedTopic);
            pstmt.setInt(2, userId);
            pstmt.setString(3, content);
            pstmt.executeUpdate();

            loadCommentsForTopic(selectedTopic);
            commentTextArea.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTopic() {
        String newTopic = newTopicTextArea.getText();
        if (newTopic.isEmpty()) {
            return;
        }

        topics.add(newTopic);
        newTopicTextArea.clear();
    }

    private int getCurrentUserID() {
        return 5; // Replace with actual logic to get the current user's ID
    }
}