package org.example.librarymanagementsystemuet.obj;

import org.example.librarymanagementsystemuet.Database;

public class UserFactory {
    private UserFactory() {

    }

    public static User createUser(String id, String username, String name, String password,
                                  String email, String registeredDate, String phonenumber,
                                  String question, String answer, int hmCoin, String expiredVipDate) {
        if (expiredVipDate == null || expiredVipDate.compareTo(Database.getDateNow()) < 0) {
            return new UserClassic(id, username, name, password, email, registeredDate, phonenumber, question, answer, hmCoin);
        } else {
            return new UserVip(id, username, name,
                    password, email, registeredDate,
                    phonenumber, question, answer, hmCoin, expiredVipDate);
        }
    }
}