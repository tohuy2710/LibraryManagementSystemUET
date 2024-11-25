package org.example.librarymanagementsystemuet.userapp.obj;

import org.example.librarymanagementsystemuet.obj.User;

public class UserSession {
    private static User user;

    public static User getInstance() {
        if (user == null) {
            return User.DEFAULT_USER;
        }
        return user;
    }

    public static User getInstance(User user) {
        if (user == null) {
            user = new User(user);
        }
        return user;
    }
}
