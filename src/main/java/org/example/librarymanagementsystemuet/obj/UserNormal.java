package org.example.librarymanagementsystemuet.obj;

public class UserNormal extends User {
    public UserNormal(String id, String username, String name,
                      String password, String email, String registeredDate,
                      String phonenumber, String question, String answer) {
        super(id, username, name, password, email, registeredDate, phonenumber, question, answer);
    }

    @Override
    public String getUserType() {
        return "Normal";
    }
}
