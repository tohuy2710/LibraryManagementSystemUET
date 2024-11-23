package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserPenaltyRecord {
    private final StringProperty userId;
    private final StringProperty userName;
    private final StringProperty booksNotReturned;
    private final StringProperty fineAmount;

    public UserPenaltyRecord(int userId, String userName, int booksNotReturned, double fineAmount) {
        this.userId = new SimpleStringProperty(String.valueOf(userId));
        this.userName = new SimpleStringProperty(userName);
        this.booksNotReturned = new SimpleStringProperty(String.valueOf(booksNotReturned));
        this.fineAmount = new SimpleStringProperty(String.valueOf(fineAmount));
    }

    public String getUserId() {
        return userId.get();
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public String getBooksNotReturned() {
        return booksNotReturned.get();
    }

    public StringProperty booksNotReturnedProperty() {
        return booksNotReturned;
    }

    public String getFineAmount() {
        return fineAmount.get();
    }

    public StringProperty fineAmountProperty() {
        return fineAmount;
    }
}
