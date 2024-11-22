package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;

public class User extends Account {
    private SimpleStringProperty name;
    private SimpleStringProperty username;
    private SimpleStringProperty phonenumber;
    private SimpleStringProperty id;

    public User() {
        super();
        this.name = new SimpleStringProperty();
        this.phonenumber = new SimpleStringProperty();
        this.id = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
    }

    public User(String id, String name, String username, String password, String email, String phonenumber) {
        super(email, password);
        this.phonenumber = new SimpleStringProperty(phonenumber);
        this.name = new SimpleStringProperty(name);
        this.username = new SimpleStringProperty(username);
        this.id = new SimpleStringProperty(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty phonenumberProperty() {
        return phonenumber;
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getPhonenumber() {
        return phonenumber.get();
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber.set(phonenumber);
    }
}