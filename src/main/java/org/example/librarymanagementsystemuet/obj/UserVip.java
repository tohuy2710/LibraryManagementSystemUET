package org.example.librarymanagementsystemuet.obj;

public class UserVip extends User {
    private int vipPoint;

    public UserVip(String id, String username, String name,
                   String password, String email,
                   String registeredDate, String phonenumber,
                   String question, String answer, int vipPoint) {
        super(id, username, name, password, email, registeredDate, phonenumber, question, answer);
        this.vipPoint = vipPoint;
    }

    @Override
    public String getUserType() {
        return "VIP";
    }
}
