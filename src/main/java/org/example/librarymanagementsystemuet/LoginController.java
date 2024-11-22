package org.example.librarymanagementsystemuet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemuet.obj.Admin;
import org.example.librarymanagementsystemuet.obj.AlertMessage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.obj.Account.*;

public class LoginController implements Initializable {

    @FXML
    private TextField forgetPassword_answer;

    @FXML
    private Button forgetPassword_back;

    @FXML
    private TextField forgetPassword_email;

    @FXML
    private ImageView forgetPassword_imageView;

    @FXML
    private Label forgetPassword_labelForgetPassword;

    @FXML
    private TextField forgetPassword_phoneNumber;

    @FXML
    private Button forgetPassword_proceedButton;

    @FXML
    private ChoiceBox<String> forgetPassword_selectQuestion;

    @FXML
    private TextField forgetPassword_username;

    @FXML
    private AnchorPane forget_password;

    @FXML
    private Label label_haveAnAccount;

    @FXML
    private Button login_buttonLogin;

    @FXML
    private Button login_buttonSignUp;

    @FXML
    private Hyperlink login_forgotPassword;

    @FXML
    private ImageView login_imageView;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_passwordDisplay;

    @FXML
    private CheckBox login_selectShowPassword;

    @FXML
    private TextField login_username;

    @FXML
    private Button login_adminLoginButton;

    @FXML
    private Button resetPassword_backButton;

    @FXML
    private PasswordField resetPassword_confirmPassword;

    @FXML
    private TextField resetPassword_confirmPasswordDisplay;

    @FXML
    private ImageView resetPassword_imageView;

    @FXML
    private PasswordField resetPassword_password;

    @FXML
    private TextField resetPassword_passwordDisplay;

    @FXML
    private Button resetPassword_proceedButton;

    @FXML
    private CheckBox resetPassword_selectShowPassword;

    @FXML
    private Label resetPassword_username;

    @FXML
    private Label resetPassword_usernameLabel;

    @FXML
    private Label reset_labelResetPassword;

    @FXML
    private AnchorPane reset_password;

    @FXML
    private AnchorPane sign_in;

    @FXML
    private AnchorPane sign_up;

    @FXML
    private AnchorPane admin_sign_in;

    @FXML
    private TextField signup_answer;

    @FXML
    private PasswordField signup_confirmPassword;

    @FXML
    private TextField signup_confirmPasswordDisplay;

    @FXML
    private TextField signup_email;

    @FXML
    private ImageView signup_imageView;

    @FXML
    private Label signup_labelSignUp;

    @FXML
    private Button signup_loginButton;

    @FXML
    private PasswordField signup_password;

    @FXML
    private TextField signup_phoneNumber;

    @FXML
    private ChoiceBox<String> signup_questionList;

    @FXML
    private Button signup_signUpButton;

    @FXML
    private TextField signup_username;

    @FXML
    private TextField loginAdmin_email;

    @FXML
    private PasswordField loginAdmin_password;

    @FXML
    private TextField loginAdmin_passwordDisplay;

    @FXML
    private CheckBox loginAdmin_selectShowPassword;

    @FXML
    private Button loginAdmin_backButton;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    String[] questions = {"Who is your crush?", "How many of ex do you have?", "What is the name of your first pet?",
            "What is the name of your first school?", "What is your favorite movie?",
            "What is your favorite book?", "What is your favorite food?", "What is your favorite color?"};

    public Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3307/library_management_system_uet",
                            "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchForm(ActionEvent e) {
        if (e.getSource() == login_buttonSignUp) {
            loginClearFields();
            sign_up.setVisible(true);
            sign_in.setVisible(false);
            forget_password.setVisible(false);
            reset_password.setVisible(false);
            admin_sign_in.setVisible(false);
        } else if (e.getSource() == signup_loginButton || e.getSource() == loginAdmin_backButton) {
            registerClearFields();
            sign_up.setVisible(false);
            sign_in.setVisible(true);
            forget_password.setVisible(false);
            reset_password.setVisible(false);
            admin_sign_in.setVisible(false);
        } else if (e.getSource() == login_forgotPassword) {
            forgetPasswordClearFields();
            sign_up.setVisible(false);
            sign_in.setVisible(false);
            forget_password.setVisible(true);
            reset_password.setVisible(false);
            admin_sign_in.setVisible(false);
        } else if (e.getSource() == forgetPassword_back) {
            forgetPasswordClearFields();
            sign_up.setVisible(false);
            sign_in.setVisible(true);
            forget_password.setVisible(false);
            reset_password.setVisible(false);
            admin_sign_in.setVisible(false);
        } else if (e.getSource() == resetPassword_backButton) {
            resetPasswordClearFields();
            sign_up.setVisible(false);
            sign_in.setVisible(false);
            forget_password.setVisible(true);
            reset_password.setVisible(false);
            admin_sign_in.setVisible(false);
        } else if (e.getSource() == login_adminLoginButton) {
            loginClearFields();
            sign_up.setVisible(false);
            sign_in.setVisible(false);
            forget_password.setVisible(false);
            reset_password.setVisible(false);
            admin_sign_in.setVisible(true);
        }
    }

    public void showPassword(ActionEvent e) {
        if (e.getSource() == resetPassword_selectShowPassword) {
            if (resetPassword_selectShowPassword.isSelected()) {
                resetPassword_passwordDisplay.setText(resetPassword_password.getText());
                resetPassword_passwordDisplay.setVisible(true);
                resetPassword_password.setVisible(false);
            } else {
                resetPassword_password.setText(resetPassword_passwordDisplay.getText());
                resetPassword_password.setVisible(true);
                resetPassword_passwordDisplay.setVisible(false);
            }
        } else if (e.getSource() == login_selectShowPassword) {
            if (login_selectShowPassword.isSelected()) {
                login_passwordDisplay.setText(login_password.getText());
                login_passwordDisplay.setVisible(true);
                login_password.setVisible(false);
            } else {
                login_password.setText(login_passwordDisplay.getText());
                login_password.setVisible(true);
                login_passwordDisplay.setVisible(false);
            }
        } else if (e.getSource() == loginAdmin_selectShowPassword) {
            if (loginAdmin_selectShowPassword.isSelected()) {
                loginAdmin_passwordDisplay.setText(loginAdmin_password.getText());
                loginAdmin_passwordDisplay.setVisible(true);
                loginAdmin_password.setVisible(false);
            } else {
                loginAdmin_password.setText(loginAdmin_passwordDisplay.getText());
                loginAdmin_password.setVisible(true);
                loginAdmin_passwordDisplay.setVisible(false);
            }
        } else if (e.getSource() == signup_confirmPasswordDisplay) {
            if (signup_confirmPasswordDisplay.isVisible()) {
                signup_confirmPassword.setText(signup_confirmPasswordDisplay.getText());
                signup_confirmPassword.setVisible(true);
                signup_confirmPasswordDisplay.setVisible(false);
            } else {
                signup_confirmPasswordDisplay.setText(signup_confirmPassword.getText());
                signup_confirmPasswordDisplay.setVisible(true);
                signup_confirmPassword.setVisible(false);
            }
        }
    }

    public void loginUser() {
        AlertMessage alertMessage = new AlertMessage();

        if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
            alertMessage.errorMessage("Please fill in all fields.");
        } else {
            try {
                String checkLogin = "SELECT * FROM users WHERE username = ? AND password = ?;";

                connect = connectDB();
                prepare = connect.prepareStatement(checkLogin);
                prepare.setString(1, login_username.getText());
                prepare.setString(2, login_password.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alertMessage.successMessage("Login successful.");
                } else {
                    alertMessage.errorMessage("Invalid username or password.");
                    loginClearFields();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alertMessage.errorMessage("Database error: " + e.getMessage());
            } finally {
                try {
                    if (result != null) result.close();
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void register() {
        AlertMessage alertMessage = new AlertMessage();

        if (signup_username.getText().isEmpty() || signup_password.getText().isEmpty()
                || signup_confirmPassword.getText().isEmpty() || signup_email.getText().isEmpty()
                || signup_phoneNumber.getText().isEmpty()) {
            alertMessage.errorMessage("Please fill in all fields.");
        } else if (!signup_password.getText().equals(signup_confirmPassword.getText())) {
            alertMessage.errorMessage("Passwords do not match.");
        } else if (!isValidUsername(signup_username.getText())) {
            alertMessage.errorMessage("Username must be between 6 and 12 characters.");
        } else if (!isValidEmail(signup_email.getText())) {
            alertMessage.errorMessage("Invalid email format.");
        } else if (!isValidPhoneNumber(signup_phoneNumber.getText())) {
            alertMessage.errorMessage("Invalid phone number format.");
        } else if (!isValidPassword(signup_password.getText())) {
            alertMessage.errorMessage("Password must contain at least one uppercase letter, " +
                    "one lowercase letter, one digit, one special character, and is at least 8 characters long.");
        } else if (signup_questionList.getValue() == null) {
            alertMessage.errorMessage("Please select a security question.");
        } else if (signup_answer.getText().isEmpty()) {
            alertMessage.errorMessage("Please provide an answer to the security question.");
        } else {
            try {
                String checkUsernameEmailPhoneNumber
                        = "SELECT * FROM users WHERE username = ? OR email = ? OR phonenumber = ?;";
                connect = connectDB();

                prepare = connect.prepareStatement(checkUsernameEmailPhoneNumber);
                prepare.setString(1, signup_username.getText());
                prepare.setString(2, signup_email.getText());
                prepare.setString(3, signup_phoneNumber.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alertMessage
                            .errorMessage("Username already exists or Email/Phone Number has used to create account.");
                } else {
                    String insertData
                            = "INSERT INTO users (username, password, email, phoneNumber, question, answer) " +
                            "VALUES (?, ?, ?, ?, ?, ?);";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, signup_username.getText());
                    prepare.setString(2, signup_password.getText());
                    prepare.setString(3, signup_email.getText());
                    prepare.setString(4, signup_phoneNumber.getText());
                    prepare.setString(5, signup_questionList.getValue());
                    prepare.setString(6, signup_answer.getText());

                    int rowsAffected = prepare.executeUpdate();

                    if (rowsAffected > 0) {
                        alertMessage.successMessage("Registration successful.");
                        registerClearFields();
                    } else {
                        alertMessage.errorMessage("Failed to register. Please try again.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alertMessage.errorMessage("Database error: " + e.getMessage());
            } finally {
                try {
                    if (result != null) result.close();
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void proceedForgetPassword() {
        AlertMessage alertMessage = new AlertMessage();

        if (forgetPassword_username.getText().isEmpty() || forgetPassword_email.getText().isEmpty()
                || forgetPassword_phoneNumber.getText().isEmpty() || forgetPassword_selectQuestion.getValue() == null
        || forgetPassword_answer.getText().isEmpty()) {
            alertMessage.errorMessage("Please fill in all fields.");
        }
        else {
            try {
                String checkForgetPassword
                        = "SELECT * FROM users WHERE username = ? AND email = ? AND phoneNumber = ? " +
                        "AND question = ? AND answer = ?";

                connect = connectDB();

                prepare = connect.prepareStatement(checkForgetPassword);
                prepare.setString(1, forgetPassword_username.getText());
                prepare.setString(2, forgetPassword_email.getText());
                prepare.setString(3, forgetPassword_phoneNumber.getText());
                prepare.setString(4, forgetPassword_selectQuestion.getValue());
                prepare.setString(5, forgetPassword_answer.getText());

                result= prepare.executeQuery();

                if (result.next()) {
                    reset_password.setVisible(true);
                    sign_up.setVisible(false);
                    sign_in.setVisible(false);
                    forget_password.setVisible(false);

                    resetPassword_username.setText(forgetPassword_username.getText());
                } else {
                    alertMessage.errorMessage("Invalid credentials.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void proceedResetPassword() {
        AlertMessage alertMessage = new AlertMessage();
        if (resetPassword_password.getText().isEmpty() || resetPassword_confirmPassword.getText().isEmpty()) {
            alertMessage.errorMessage("Please fill in all fields.");
        } else if (!resetPassword_password.getText().equals(resetPassword_confirmPassword.getText())) {
            alertMessage.errorMessage("Passwords do not match.");
        } else {
            try {
                String resetPassword = "UPDATE users SET password = ? WHERE username = ?";

                connect = connectDB();
                prepare = connect.prepareStatement(resetPassword);
                prepare.setString(1, resetPassword_password.getText());
                prepare.setString(2, resetPassword_username.getText());

                int rowsAffected = prepare.executeUpdate();

                if (rowsAffected > 0) {
                    alertMessage.successMessage("Reset Password successful.");
                    resetPasswordClearFields();
                    sign_in.setVisible(true);
                    reset_password.setVisible(false);
                } else {
                    alertMessage.errorMessage("Failed to register. Please try again.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void adminLogin() {

        AlertMessage alertMessage = new AlertMessage();

        if (loginAdmin_email.getText().isEmpty() || loginAdmin_password.getText().isEmpty()) {
            alertMessage.errorMessage("Please fill in all fields.");
        }

        else {
            try {
                String checkLogin = "SELECT * FROM admins WHERE email = ? AND password = ?;";

                connect = connectDB();
                prepare = connect.prepareStatement(checkLogin);
                prepare.setString(1, loginAdmin_email.getText());
                prepare.setString(2, loginAdmin_password.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alertMessage.successMessage("Login successful.");
                    Admin admin = Admin.getInstance(result.getString("email"),
                            result.getString("password"));
                    OpenAdminApp();
                    Stage stage = (Stage) loginAdmin_email.getScene().getWindow();
                    stage.close();
                } else {
                    alertMessage.errorMessage("Invalid email or password.");
                    adminLoginClearFields();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alertMessage.errorMessage("Database error: " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (result != null) result.close();
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void OpenAdminApp() throws IOException {
        AdminApplication adminApplication = new AdminApplication();
        adminApplication.start(new Stage());
    }

    public void loginClearFields() {
        login_username.clear();
        login_password.clear();
    }

    public void forgetPasswordClearFields() {
        forgetPassword_username.clear();
        forgetPassword_email.clear();
        forgetPassword_phoneNumber.clear();
        forgetPassword_answer.clear();
    }

    public void registerClearFields() {
        signup_username.clear();
        signup_password.clear();
        signup_confirmPassword.clear();
        signup_email.clear();
        signup_phoneNumber.clear();
    }

    public void resetPasswordClearFields() {
        resetPassword_password.clear();
        resetPassword_confirmPassword.clear();
    }

    public void adminLoginClearFields() {
        loginAdmin_email.clear();
        loginAdmin_password.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sign_up.setVisible(false);
        sign_in.setVisible(true);
        forget_password.setVisible(false);
        reset_password.setVisible(false);
        admin_sign_in.setVisible(false);
        // Initialization logic
        signup_questionList.getItems().addAll(questions);
        forgetPassword_selectQuestion.getItems().addAll(questions);
    }
}
