package package1;

public class Account {
    private String id;
    public String name;
    public String username;
    private String password;
    private String email;
    private String phoneNumber;

    public Account() {

    }

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
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

    }

    public static boolean isValidUsername(String username) {
        if (username.length() < 6 || username.length() > 12) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean isValidPassword(String password) {
        // Check if the password contains at least one uppercase letter, one lowercase letter, one digit, one special character, and is at least 8 characters long
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    public static boolean isValidEmail(String email) {
        // Regular expression to validate email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidName(String name) {
        // Check if the name contains only letters and is not more than 28 characters long
        return name.matches("^[a-zA-Z]{1,28}$");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Check if the phone number contains exactly 10 digits and starts with 0
        return phoneNumber.matches("^0\\d{9}$");
    }
}