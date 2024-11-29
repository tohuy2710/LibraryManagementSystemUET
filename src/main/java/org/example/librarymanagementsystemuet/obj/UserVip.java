package org.example.librarymanagementsystemuet.obj;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserVip extends User {
    private String expiredVipDate;

    public UserVip(String id, String username, String name,
                   String password, String email,
                   String registeredDate, String phonenumber,
                   String question, String answer, int hmCoin, String expiredVipDate) {
        super(id, username, name, password, email, registeredDate, phonenumber, question, answer, hmCoin);
        this.expiredVipDate = expiredVipDate;
    }

    @Override
    public String getUserType() {
        return "VIP";
    }

    public String getExpiredVipDate() {
        return expiredVipDate;
    }
}
