package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;

public class Account {
    private  SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty email;

    public static final String VALID_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)" +
            "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String VALID_USER_NAME = "^[a-zA-Z0-9]+$";
    public static final String VALID_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
            "(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String VALID_PHONENUMBER = "^[0-9]{10}$";


    public Account() {
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public Account(String password, String email) {
        if (email.contains("@")) {
            this.username = new SimpleStringProperty(email.substring(0, email.indexOf("@")));
        } else {
            this.username = new SimpleStringProperty(email);
        }
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
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

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public SimpleStringProperty emailProperty() {
        return email;
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

    public static boolean isValidUsername(String username) {
        if (username.length() < 6 || username.length() > 12) {
            return false;
        }
        return username.matches(VALID_USER_NAME);
    }

    public static boolean isValidPassword(String password) {
        return password.matches(VALID_PASSWORD);
    }

    public static boolean isValidEmail(String email) {
        return email.matches(VALID_EMAIL_REGEX);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches(VALID_PHONENUMBER);
    }
}