package package1;

public class Account {
    private String id;
    public String name;
    public String username;
    private String password;
    private String email;
    private String phoneNumber;

    public Account(String id, String name, String username, String password, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (isValidName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name must contain only letters and be no more than 28 characters long.");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (isValidUsername(username)) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Username must be 6-12 characters long and cannot consist only of numbers.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, " +
                    "one lowercase letter, one digit, one special character, " +
                    "and be at least 8 characters long.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid Vietnam Phone Number format.");
        }
    }

    private boolean isValidUsername(String username) {
        if (username.length() < 6 || username.length() > 12) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]+$");
    }

    private boolean isValidPassword(String password) {
        // Check if the password contains at least one uppercase letter, one lowercase letter, one digit, one special character, and is at least 8 characters long
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    private boolean isValidEmail(String email) {
        // Regular expression to validate email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidName(String name) {
        // Check if the name contains only letters and is not more than 28 characters long
        return name.matches("^[a-zA-Z]{1,28}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Check if the phone number contains exactly 10 digits and starts with 0
        return phoneNumber.matches("^0\\d{9}$");
    }
}