package org.example.librarymanagementsystemuet.obj;

public class UserFactory {
    private UserFactory() {
    }

    public static User createUser(String id, String username, String name, String password,
                                  String email, String registeredDate, String phonenumber,
                                  String question, String answer, int vipPoint) {
        if (vipPoint == 0) {
            return new UserNormal(id, username, name,
                    password, email, registeredDate,
                    phonenumber, question, answer);
        } else {
            return new UserVip(id, username, name,
                    password, email, registeredDate,
                    phonenumber, question, answer,
                    vipPoint);
        }
    }
}
