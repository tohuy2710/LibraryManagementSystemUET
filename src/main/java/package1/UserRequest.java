package package1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserRequest {
    private StringProperty requestID;
    private StringProperty bookID;
    private StringProperty userID;
    private StringProperty createdTime;
    private StringProperty lastUpdatedTime;
    private StringProperty status;

    public UserRequest() {
        this.requestID = new SimpleStringProperty();
        this.bookID = new SimpleStringProperty();
        this.userID = new SimpleStringProperty();
        this.createdTime = new SimpleStringProperty();
        this.lastUpdatedTime = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
    }

    public UserRequest(String requestID, String bookID, String userID, String createdTime, String lastUpdatedTime, String status) {
        this.requestID = new SimpleStringProperty(requestID);
        this.bookID = new SimpleStringProperty(bookID);
        this.userID = new SimpleStringProperty(userID);
        this.createdTime = new SimpleStringProperty(createdTime);
        this.lastUpdatedTime = new SimpleStringProperty(lastUpdatedTime);
        this.status = new SimpleStringProperty(status);
    }

    public String getRequestID() { return requestID.get(); }
    public void setRequestID(String requestID) { this.requestID.set(requestID); }
    public StringProperty requestIDProperty() { return requestID; }

    public String getBookID() { return bookID.get(); }
    public void setBookID(String bookID) { this.bookID.set(bookID); }
    public StringProperty bookIDProperty() { return bookID; }

    public String getUserID() { return userID.get(); }
    public void setUserID(String userID) { this.userID.set(userID); }
    public StringProperty userIDProperty() { return userID; }

    public String getCreatedTime() { return createdTime.get(); }
    public void setCreatedTime(String createdTime) { this.createdTime.set(createdTime); }
    public StringProperty createdTimeProperty() { return createdTime; }

    public String getLastUpdatedTime() { return lastUpdatedTime.get(); }
    public void setLastUpdatedTime(String lastUpdatedTime) { this.lastUpdatedTime.set(lastUpdatedTime); }
    public StringProperty lastUpdatedTimeProperty() { return lastUpdatedTime; }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }
}
