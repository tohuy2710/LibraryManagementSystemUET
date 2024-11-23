package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookDetail {
    private final StringProperty name;
    private final StringProperty startDate;
    private final StringProperty returnDate;
    private final StringProperty dueDate;

    public BookDetail(String name, String startDate, String returnDate, String dueDate) {
        this.name = new SimpleStringProperty(name);
        this.startDate = new SimpleStringProperty(startDate);
        this.returnDate = new SimpleStringProperty(returnDate);
        this.dueDate = new SimpleStringProperty(dueDate);
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
}
