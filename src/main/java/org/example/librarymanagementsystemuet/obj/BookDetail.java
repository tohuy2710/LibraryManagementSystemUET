package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.*;

public class BookDetail {
    private final IntegerProperty requestId;
    private final StringProperty name;
    private final StringProperty startDate;
    private final StringProperty returnDate;
    private final StringProperty dueDate;
    private final IntegerProperty noOfBooks;
    private final StringProperty daysLate;

    public BookDetail(int requestId, String name, String startDate, String returnDate, String dueDate, int noOfBooks, String daysLate) {
        this.requestId = new SimpleIntegerProperty(requestId);
        this.name = new SimpleStringProperty(name);
        this.startDate = new SimpleStringProperty(startDate);
        this.returnDate = new SimpleStringProperty(returnDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.noOfBooks = new SimpleIntegerProperty(noOfBooks);
        this.daysLate = new SimpleStringProperty(daysLate);
    }

    public int getRequestId() {
        return requestId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getStartDate() {
        return startDate.get();
    }

    public String getReturnDate() {
        return returnDate.get();
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public int getNoOfBooks() {
        return noOfBooks.get();
    }

    public String getDaysLate() {
        return daysLate.get();
    }

    public IntegerProperty requestIdProperty() {
        return requestId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public StringProperty returnDateProperty() {
        return returnDate;
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public IntegerProperty noOfBooksProperty() {
        return noOfBooks;
    }

    public StringProperty daysLateProperty() {
        return daysLate;
    }
}