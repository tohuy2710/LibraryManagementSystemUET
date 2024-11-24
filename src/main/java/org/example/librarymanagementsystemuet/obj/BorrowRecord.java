package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BorrowRecord {
    private final StringProperty requestId;
    private final StringProperty startDate;
    private final StringProperty dueDate;
    private final StringProperty returnDate;

    public BorrowRecord() {
        this.requestId = new SimpleStringProperty();
        this.startDate = new SimpleStringProperty();
        this.dueDate = new SimpleStringProperty();
        this.returnDate = new SimpleStringProperty();
    }

    public BorrowRecord(int requestId, String startDate, String dueDate, String returnDate) {
        this.requestId = new SimpleStringProperty(String.valueOf(requestId));
        this.startDate = new SimpleStringProperty(startDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.returnDate = new SimpleStringProperty(returnDate);
    }

    public void setRequestId(String requestId) {
        this.requestId.set(requestId);
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    public void setReturnDate(String returnDate) {
        this.returnDate.set(returnDate);
    }

    public String getRequestId() {
        return requestId.get();
    }

    public StringProperty requestIdProperty() {
        return requestId;
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