package package1;

import javafx.beans.property.SimpleStringProperty;

public class User extends Account {
    private SimpleStringProperty phonenumber;

    public User() {
        super();
        this.phonenumber = new SimpleStringProperty();
    }

    public User(String id, String name, String username, String password, String email, String phoneNumber) {
        super(id, name, username, password, email, phoneNumber);
        this.phonenumber = new SimpleStringProperty(phoneNumber);
    }

    public String getPhonenumber() {
        return phonenumber.get();
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber.set(phonenumber);
    }

}