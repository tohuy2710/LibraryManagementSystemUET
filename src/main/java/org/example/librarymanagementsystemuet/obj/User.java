package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public abstract class User extends Account {

    private SimpleStringProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty name;
    private SimpleStringProperty password;
    private SimpleStringProperty email;
    private SimpleStringProperty registeredDate;
    private SimpleStringProperty phonenumber;
    private SimpleStringProperty question;
    private SimpleStringProperty answer;
    private SimpleIntegerProperty hmCoin;

    public abstract String getUserType();

    public User() {
        super();
        this.id = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.registeredDate = new SimpleStringProperty();
        this.phonenumber = new SimpleStringProperty();
        this.question = new SimpleStringProperty();
        this.answer = new SimpleStringProperty();
        this.hmCoin = new SimpleIntegerProperty();
    }

    public User(User user) {
        super.setEmail(user.getEmail());
        super.setPassword(user.getPassword());
        this.id = user.id;
        this.username = user.username;
        this.name = user.name;
        this.password = user.password;
        this.email = user.email;
        this.registeredDate = user.registeredDate;
        this.phonenumber = user.phonenumber;
        this.question = user.question;
        this.answer = user.answer;
        this.hmCoin = user.hmCoin;
    }

    public User(SimpleStringProperty id, SimpleStringProperty username,
                SimpleStringProperty name, SimpleStringProperty password,
                SimpleStringProperty email, SimpleStringProperty registeredDate,
                SimpleStringProperty phonenumber, SimpleStringProperty question,
                SimpleStringProperty answer, SimpleIntegerProperty hmCoin) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.registeredDate = registeredDate;
        this.phonenumber = phonenumber;
        this.question = question;
        this.answer = answer;
        this.hmCoin = hmCoin;
    }

    public User(String id, String username, String name, String password,
                String email, String registeredDate, String phonenumber,
                String question, String answer, int hmCoin) {
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.name = new SimpleStringProperty(name);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
        this.registeredDate = new SimpleStringProperty(registeredDate);
        this.phonenumber = new SimpleStringProperty(phonenumber);
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty(answer);
        this.hmCoin = new SimpleIntegerProperty(hmCoin);
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

    public String getUserName() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUserName(String name) {
        this.username.set(name);
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

    @Override
    public String getPassword() {
        return password.get();
    }

    @Override
    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    @Override
    public String getEmail() {
        return email.get();
    }

    @Override
    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getRegisteredDate() {
        return registeredDate.get();
    }

    public SimpleStringProperty registeredDateProperty() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate.set(registeredDate);
    }

    public String getPhonenumber() {
        return phonenumber.get();
    }

    public SimpleStringProperty phonenumberProperty() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber.set(phonenumber);
    }

    public String getQuestion() {
        return question.get();
    }

    public SimpleStringProperty questionProperty() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public String getAnswer() {
        return answer.get();
    }

    public SimpleStringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public int getHmCoin() {
        return hmCoin.get();
    }

    public SimpleIntegerProperty hmCoinProperty() {
        return hmCoin;
    }

    public void setHmCoin(int hmCoin) {
        this.hmCoin.set(hmCoin);
    }

    public double getPenaltyCoefficient() {
        if (this.getUserType().equals("VIP")) {
            return 0.8;
        } else {
            return 1.0;
        }
    }
}