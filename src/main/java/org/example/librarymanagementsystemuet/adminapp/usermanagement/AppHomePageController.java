package org.example.librarymanagementsystemuet.adminapp.usermanagement;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.Account;
import org.example.librarymanagementsystemuet.obj.Admin;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AppHomePageController {

    @FXML
    private Label userName1, userHmPoint1;
    @FXML
    private Label userName2, userHmPoint2;
    @FXML
    private Label userName3, userHmPoint3;
    @FXML
    private Label userName4, userHmPoint4;
    @FXML
    private Label userName5, userHmPoint5;
    @FXML
    private Circle adminProfileCircle;

    @FXML
    private Circle userCircle1, userCircle2, userCircle3, userCircle4, userCircle5;
    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private VBox adminInfoVBox;
    @FXML
    private VBox modifyInfoVBox;

    @FXML
    private TextField currentPasswordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Label adminEmailLabel, adminNameLabel;
    @FXML

    private Connection connection;

    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label booksOnLoanLabel;
    @FXML
    private Label overdueBooksLabel;

    private void displayBookStatistics() {
        try {
            Connection connection = Database.connectDB();
            Statement statement = connection.createStatement();

            ResultSet totalBooksResult = statement.executeQuery("SELECT COUNT(*) AS totalBooks FROM books");
            if (totalBooksResult.next()) {
                int totalBooks = totalBooksResult.getInt("totalBooks");
                totalBooksLabel.setText(String.valueOf(totalBooks));
            }

            ResultSet booksOnLoanResult = statement.executeQuery("SELECT COUNT(*) AS booksOnLoan FROM borrowbooks WHERE return_date IS NULL");
            if (booksOnLoanResult.next()) {
                int booksOnLoan = booksOnLoanResult.getInt("booksOnLoan");
                booksOnLoanLabel.setText(String.valueOf(booksOnLoan));
            }

            ResultSet overdueBooksResult = statement.executeQuery("SELECT COUNT(*) AS overdueBooks FROM borrowbooks WHERE return_date IS NULL AND due_date < NOW()");
            if (overdueBooksResult.next()) {
                int overdueBooks = overdueBooksResult.getInt("overdueBooks");
                overdueBooksLabel.setText(String.valueOf(overdueBooks));
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        connection = Database.connectDB();
        if (connection != null) {
            displayTopUsers();
        } else {
            System.out.println("Failed to connect to the database.");
        }
        displayBookStatistics();
        createPieChart();
        createBarChart();
        displayAdminInfo();
    }

    private void displayTopUsers() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users ORDER BY hmCoin DESC LIMIT 5");
            int index = 1;
            while (rs.next()) {
                String userName = rs.getString("username");
                int hmCoin = rs.getInt("hmCoin");
                String avatarImg = rs.getString("avatarImg");

                switch (index) {
                    case 1:
                        updateUserBox(userName, hmCoin, avatarImg, userName1, userHmPoint1, userCircle1);
                        break;
                    case 2:
                        updateUserBox(userName, hmCoin, avatarImg, userName2, userHmPoint2, userCircle2);
                        break;
                    case 3:
                        updateUserBox(userName, hmCoin, avatarImg, userName3, userHmPoint3, userCircle3);
                        break;
                    case 4:
                        updateUserBox(userName, hmCoin, avatarImg, userName4, userHmPoint4, userCircle4);
                        break;
                    case 5:
                        updateUserBox(userName, hmCoin, avatarImg, userName5, userHmPoint5, userCircle5);
                        break;
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserBox(String userName, int hmCoin, String avatarImg, Label userNameLabel, Label hmPointLabel, Circle userCircle) {
        userNameLabel.setText(userName);
        hmPointLabel.setText("hmPoint: " + hmCoin);
        if (avatarImg != null && !avatarImg.isEmpty()) {
            Image image = new Image(avatarImg);
            userCircle.setFill(new ImagePattern(image));
        }
    }

    private void displayAdminInfo() {
        Admin admin = Admin.getInstance();
        adminEmailLabel.setText(admin.getEmail());
        String avatarPath = Admin.DEFAULT_AVATAR_PATH;
        adminNameLabel.setText(admin.getName());
        try {
            Image image = new Image(getClass().getResource(avatarPath).toExternalForm());
            adminProfileCircle.setFill(new ImagePattern(image));
        } catch (NullPointerException e) {
            System.err.println("Resource not found: " + avatarPath);
            e.printStackTrace();
        }
    }

    private void createPieChart() {
        Map<String, Integer> data = fetchDataForPieChart();
        pieChart.setTitle("Books by Category");

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(slice);
        }
    }

    private Map<String, Integer> fetchDataForPieChart() {
        Map<String, Integer> data = new HashMap<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT category, COUNT(*) AS count FROM books GROUP BY category");
            while (rs.next()) {
                data.put(rs.getString("category"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void createBarChart() {
        Map<String, Integer> data = fetchDataForBarChart();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Books Borrowed");

        barChart.setTitle("Books Borrowed by Month");
        barChart.getXAxis().setLabel("Month");
        barChart.getYAxis().setLabel("Books Borrowed");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
    }

    private Map<String, Integer> fetchDataForBarChart() {
        Map<String, Integer> data = new HashMap<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT DATE_FORMAT(start_date, '%b') AS month, COUNT(*) AS count " +
                            "FROM borrowbooks " +
                            "WHERE start_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) " +
                            "GROUP BY DATE_FORMAT(start_date, '%b') " +
                            "ORDER BY start_date DESC " +
                            "LIMIT 3"
            );
            while (rs.next()) {
                data.put(rs.getString("month"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @FXML
    private void showModifyInfo() {
        adminInfoVBox.setVisible(false);
        modifyInfoVBox.setVisible(true);
    }

    @FXML
    private void hideModifyInfo() {
        adminInfoVBox.setVisible(true);
        modifyInfoVBox.setVisible(false);
    }

    @FXML
    private void saveAdminInfo() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        Admin admin = Admin.getInstance();

        if (!admin.getPassword().equals(currentPassword)) {
            showAlert("Incorrect Password", "The current password is incorrect.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Password Mismatch", "The new password and confirm password do not match.");
            return;
        }

        if (!Account.isValidPassword(newPassword)) {
            showAlert("Invalid Password", "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
            return;
        }

        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE admins SET password = ? WHERE email = ?")) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, admin.getEmail());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Admin information updated successfully.");
            } else {
                showAlert("Error", "Failed to update admin information.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating admin information.");
        }

        modifyInfoVBox.setVisible(false);
        adminInfoVBox.setVisible(true);
    }

    private void showAlert(String title, String message) {
        connection = Database.connectDB();
        displayTopUsers();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}