package org.example.librarymanagementsystemuet.userapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.Admin;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DiscussionPageController {

    @FXML
    private Label topicTitleLabel;

    @FXML
    private Label topicCreatorLabel;

    @FXML
    private Label topicCreationTimeLabel;
    @FXML
    private ListView<String> topicListView;

    @FXML
    private ListView<HBox> commentListView;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private TextArea newTopicTextArea;

    private ObservableList<String> topics = FXCollections.observableArrayList();
    private ObservableList<HBox> comments = FXCollections.observableArrayList();
    private Timeline timeline;

    @FXML
    public void initialize() {
        topicListView.setItems(topics);
        commentListView.setItems(comments);

        topicListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadCommentsForTopic(newValue);
                loadTopicDetails(newValue);
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

    private void loadTopicDetails(String topic) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Connection conn = Database.connectDB();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "SELECT ti.topicTitle, ti.createTime, u.username, a.email, ti.role " +
                                     "FROM topicinfo ti " +
                                     "LEFT JOIN users u ON ti.id = u.id " +
                                     "LEFT JOIN admins a ON ti.id = a.id " +
                                     "WHERE ti.topicTitle = ?")) {
                    pstmt.setString(1, topic);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String topicTitle = rs.getString("topicTitle");
                        String createTime = rs.getString("createTime");
                        String role = rs.getString("role");
                        String creator = role.equals("ADMIN") ? "ADMIN " + rs.getString("email") : rs.getString("username");

                        Platform.runLater(() -> {
                            topicTitleLabel.setText("Title: " + topicTitle);
                            topicCreatorLabel.setText("Created By: " + creator);
                            topicCreationTimeLabel.setText("Creation Time: " + createTime);
                        });
                    }
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void loadTopics() {
        Task<ObservableList<String>> task = new Task<>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                ObservableList<String> newTopics = FXCollections.observableArrayList();
                try (Connection conn = Database.connectDB();
                     PreparedStatement pstmt = conn.prepareStatement("SELECT DISTINCT topicTitle FROM topicinfo")) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        newTopics.add(rs.getString("topicTitle"));
                    }
                }
                return newTopics;
            }
        };

        task.setOnSucceeded(e -> topics.setAll(task.getValue()));
        new Thread(task).start();
    }

    private void loadCommentsForTopic(String topic) {
        Task<ObservableList<HBox>> task = new Task<>() {
            @Override
            protected ObservableList<HBox> call() throws Exception {
                ObservableList<HBox> newComments = FXCollections.observableArrayList();
                try (Connection conn = Database.connectDB();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "SELECT tc.comment, tc.commentTime, u.username, a.email, tc.role " +
                                     "FROM topiccomment tc " +
                                     "LEFT JOIN users u ON tc.id = u.id " +
                                     "LEFT JOIN admins a ON tc.id = a.id " +
                                     "JOIN topicinfo ti ON tc.topicID = ti.topicID " +
                                     "WHERE ti.topicTitle = ?")) {
                    pstmt.setString(1, topic);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        String comment = rs.getString("comment");
                        String commentTime = rs.getString("commentTime");
                        String userName = rs.getString("role").equals("ADMIN") ? rs.getString("email") : rs.getString("username");
                        String role = rs.getString("role");
                        String displayComment = (role.equals("ADMIN") ? "[Admin] " : "[User] ") + userName + ": " + comment;

                        Label commentLabel = new Label(displayComment);
                        Label timeLabel = new Label(commentTime);
                        HBox commentBox = new HBox(commentLabel, timeLabel);
                        HBox.setHgrow(commentLabel, Priority.ALWAYS);
                        commentBox.setSpacing(10);
                        commentBox.setAlignment(Pos.CENTER_LEFT);
                        timeLabel.setMaxWidth(Double.MAX_VALUE);
                        HBox.setHgrow(timeLabel, Priority.ALWAYS);
                        timeLabel.setAlignment(Pos.CENTER_RIGHT);

                        if (role.equals("ADMIN")) {
                            commentLabel.getStyleClass().add("admin-comment");
                            timeLabel.getStyleClass().add("admin-time");
                        } else {
                            commentLabel.getStyleClass().add("user-comment");
                            timeLabel.getStyleClass().add("user-time");
                        }

                        newComments.add(commentBox);
                    }
                }
                return newComments;
            }
        };

        task.setOnSucceeded(e -> commentListView.setItems(task.getValue()));
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
        if (userId == -1) {
            System.err.println("Failed to retrieve user ID.");
            return;
        }

        String role = Admin.getInstance() != null ? "ADMIN" : "USER";

        try (Connection conn = Database.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO topiccomment (topicID, id, comment, role) VALUES ((SELECT topicID FROM topicinfo WHERE topicTitle = ? LIMIT 1), ?, ?, ?)")) {
            pstmt.setString(1, selectedTopic);
            pstmt.setInt(2, userId);
            pstmt.setString(3, content);
            pstmt.setString(4, role);
            pstmt.executeUpdate();

            // load lai
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

        try (Connection conn = Database.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO topicinfo (topicTitle, id, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, newTopic);
            pstmt.setInt(2, getCurrentUserID());
            pstmt.setString(3, Admin.getInstance() != null ? "ADMIN" : "USER");
            pstmt.executeUpdate();

            topics.add(newTopic);
            newTopicTextArea.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCurrentUserID() {
        if (Admin.getInstance() != null) {
            try (Connection conn = Database.connectDB();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM admins WHERE email = ?")) {
                pstmt.setString(1, Admin.getInstance().getEmail());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (Connection conn = Database.connectDB();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM users WHERE email = ?")) {
                pstmt.setString(1, UserSession.getInstance().getEmail());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}