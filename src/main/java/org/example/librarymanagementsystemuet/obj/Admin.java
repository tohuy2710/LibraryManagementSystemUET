package org.example.librarymanagementsystemuet.obj;

public final class Admin extends Account {

    private static Admin instance;

    public static final String DEFAULT_EMAIL = "tohuy.2710";
    public static final String DEFAULT_PASSWORD = "1";
    public static final String DEFAULT_AVATAR_PATH = "/asset/img/admin-avatar.png";

    public static Admin getInstance() {
        return getInstance(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    public static Admin getInstance(String email, String password) {
        if (instance == null) {
            instance = new Admin(email, password);
        }
        return instance;
    }

    private Admin(String email, String password) {
        super(email, password);
    }

    public String getName() {
        String name = getInstance().getName();
        return name.substring(0, name.indexOf('@'));
    }
}
