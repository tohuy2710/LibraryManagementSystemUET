package org.example.librarymanagementsystemuet.obj;

import javafx.beans.property.SimpleStringProperty;

public class User extends Account {
    private SimpleStringProperty name;
    private SimpleStringProperty phonenumber;
    private SimpleStringProperty id;
    private SimpleStringProperty question;
    private SimpleStringProperty answer;

    public User() {
        super();
        this.name = new SimpleStringProperty();
        this.phonenumber = new SimpleStringProperty();
        this.id = new SimpleStringProperty();
        this.question = new SimpleStringProperty();
        this.answer = new SimpleStringProperty();
    }

    public User(String id, String name, String username,
                String password, String email, String phonenumber,
                String question, String answer) {
        super(password, email);
        this.phonenumber = new SimpleStringProperty(phonenumber);
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty(answer);
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

    public SimpleStringProperty questionProperty() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public String getQuestion() {
        return question.get();
    }

    public SimpleStringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public String getAnswer() {
        return answer.get();
    }
}