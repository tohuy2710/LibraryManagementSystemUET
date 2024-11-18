package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;

public class Account {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty email;
    private SimpleStringProperty phonenumber;

    public Account() {
        this.id = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.phonenumber = new SimpleStringProperty();
    }

    public Account(String id, String name, String username, String password, String email, String phonenumber) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
        this.phonenumber = new SimpleStringProperty(phonenumber);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPhoneNumber() {
        return phonenumber.get();
    }

    public void setPhoneNumber(String phonenumber) {
        this.phonenumber.set(phonenumber);
    }

    public static boolean isValidUsername(String username) {
        if (username.length() < 6 || username.length() > 12) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z]{1,28}$");
    }

    public static boolean isValidPhoneNumber(String phonenumber) {
        return phonenumber.matches("^0\\d{9}$");
    }
}