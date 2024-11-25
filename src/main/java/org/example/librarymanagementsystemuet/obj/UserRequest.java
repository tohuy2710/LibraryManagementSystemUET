package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.librarymanagementsystemuet.exception.LogicException;

public class UserRequest {
    public static final String APPROVED_FOR_BORROWING = "Approved for borrowing";
    public static final String DENIED_FOR_BORROWING = "Denied for borrowing";
    public static final String CANCELLED_BY_ADMIN = "Cancelled by admin";
    public static final String OVERDUE_FOR_RETURN = "Overdue for return book";
    public static final String BOOK_RETURNED = "Book has been returned";
    public static final String PENDING = "Pending";
    public static final String ON_LOAN = "Book is currently on loan";

    private StringProperty requestID;
    private StringProperty bookID;
    private StringProperty bookName;
    private StringProperty noOfBooks;
    private StringProperty userID;
    private StringProperty createdTime;
    private StringProperty lastUpdatedTime;
    private StringProperty status;
    private StringProperty previousStatus;

    public UserRequest() {
        this.requestID = new SimpleStringProperty();
        this.bookID = new SimpleStringProperty();
        this.bookName = new SimpleStringProperty();
        this.userID = new SimpleStringProperty();
        this.createdTime = new SimpleStringProperty();
        this.lastUpdatedTime = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
        this.previousStatus = new SimpleStringProperty();
        this.noOfBooks = new SimpleStringProperty();
    }

    public UserRequest(String requestID, String bookID, String userID,
                       String createdTime, String lastUpdatedTime, String status, String noOfBooks) {
        this.requestID = new SimpleStringProperty(requestID);
        this.bookID = new SimpleStringProperty(bookID);
        this.bookName = new SimpleStringProperty();
        this.userID = new SimpleStringProperty(userID);
        this.createdTime = new SimpleStringProperty(createdTime);
        this.lastUpdatedTime = new SimpleStringProperty(lastUpdatedTime);
        this.status = new SimpleStringProperty(status);
        this.previousStatus = new SimpleStringProperty(status);
        this.noOfBooks = new SimpleStringProperty(noOfBooks);
    }

    public String getNoOfBooks() {
        return noOfBooks.get();
    }

    public StringProperty noOfBooksProperty() {
        return noOfBooks;
    }

    public void setNoOfBooks(String noOfBooks) {
        this.noOfBooks.set(noOfBooks);
    }

    public String getRequestID() {
        return requestID.get();
    }

    public StringProperty requestIDProperty() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID.set(requestID);
    }

    public String getBookID() {
        return bookID.get();
    }

    public StringProperty bookIDProperty() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID.set(bookID);
    }

    public String getBookName() {
        return bookName.get();
    }

    public StringProperty bookNameProperty() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName.set(bookName);
    }

    public String getUserID() {
        return userID.get();
    }

    public StringProperty userIDProperty() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID.set(userID);
    }

    public String getCreatedTime() {
        return createdTime.get();
    }

    public StringProperty createdTimeProperty() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime.set(createdTime);
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime.get();
    }

    public StringProperty lastUpdatedTimeProperty() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime.set(lastUpdatedTime);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) throws LogicException {
        if (previousStatus.get() != null) {
            if (previousStatus.get().equals(status)) {
                this.status.set(status);
            } else if (previousStatus.get().equals(PENDING)) {
                if (status.equals(APPROVED_FOR_BORROWING)
                        || status.equals(DENIED_FOR_BORROWING)
                        || status.equals(CANCELLED_BY_ADMIN)) {
                    this.status.set(status);
                } else {
                    throw new LogicException("Invalid status change");
                }
            } else if (previousStatus.get().equals(APPROVED_FOR_BORROWING)) {
                if (status.equals(ON_LOAN) || status.equals(CANCELLED_BY_ADMIN)) {
                    this.status.set(status);
                } else {
                    throw new LogicException("Invalid status change");
                }
            } else if (previousStatus.get().equals(ON_LOAN)) {
                if (status.equals(OVERDUE_FOR_RETURN) || status.equals(BOOK_RETURNED)) {
                    this.status.set(status);
                } else {
                    throw new LogicException("Invalid status change");
                }
            } else if (previousStatus.get().equals(OVERDUE_FOR_RETURN)) {
                if (status.equals(BOOK_RETURNED)) {
                    this.status.set(status);
                } else {
                    throw new LogicException("Invalid status change");
                }
            } else {
                throw new LogicException("Invalid status change");
            }
        }
    }

    public String getPreviousStatus() {
        return previousStatus.get();
    }

    public StringProperty previousStatusProperty() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus.set(previousStatus);
    }
}
