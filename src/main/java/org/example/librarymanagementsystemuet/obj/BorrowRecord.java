package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BorrowRecord {
    private final StringProperty userId;
    private final StringProperty startDate;
    private final StringProperty dueDate;
    private final StringProperty returnDate;

    public BorrowRecord(int userId, String startDate, String dueDate, String returnDate) {
        this.userId = new SimpleStringProperty(String.valueOf(userId));
        this.startDate = new SimpleStringProperty(startDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.returnDate = new SimpleStringProperty(returnDate);
    }

    public String getUserId() {
        return userId.get();
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public String getStartDate() {
        return startDate.get();
    }
    public StringProperty startDateProperty() {
        return startDate;
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public String getReturnDate() {
        return returnDate.get();
    }

    public StringProperty returnDateProperty() {
        return returnDate;
    }
}