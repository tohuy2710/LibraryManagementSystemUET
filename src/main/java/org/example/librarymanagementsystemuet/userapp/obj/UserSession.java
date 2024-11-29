package org.example.librarymanagementsystemuet.userapp.obj;

import org.example.librarymanagementsystemuet.obj.User;
import org.example.librarymanagementsystemuet.obj.UserFactory;

public class UserSession {

    private static User user;

    public static User getInstance() {
        if (user == null) {
            user = UserFactory.createUser(
                    "14",
                    "humami_test",
                    "Humami Reader",
                    "1",
                    "humami@hmm.com",
                    "2021-01-01",
                    "0123456789",
                    "Who is your crush?",
                    "you",
                    5,
                    "2025-10-10"
            );
        }
        return user;
    }

    public static User getInstance(String id, String username, String name,
                                   String password, String email, String registeredDate,
                                   String phonenumber, String question, String answer,
                                   int hmCoin, String expiredVipDate) {
        if (user == null) {
            user = UserFactory.createUser(
                    id,
                    username,
                    name,
                    password,
                    email,
                    registeredDate,
                    phonenumber,
                    question,
                    answer,
                    hmCoin,
                    expiredVipDate
            );
        }
        return user;
    }
}