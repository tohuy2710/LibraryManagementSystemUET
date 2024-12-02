package org.example.librarymanagementsystemuet.obj;

public class TopicComment {
    private String nameTopic;
    private int userId;
    private String comment;
    private String commentTime;
    private String userName;

    public TopicComment(String nameTopic, int userId, String comment, String commentTime, String userName) {
        this.nameTopic = nameTopic;
        this.userId = userId;
        this.comment = comment;
        this.commentTime = commentTime;
        this.userName = userName;
    }

    public String getNameTopic() {
        return nameTopic;
    }

    public int getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public String getUserName() {
        return userName;
    }
}