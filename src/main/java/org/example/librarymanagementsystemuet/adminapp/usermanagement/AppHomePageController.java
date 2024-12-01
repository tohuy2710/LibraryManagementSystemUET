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
import javafx.scene.text.Font;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.obj.Account;
import org.example.librarymanagementsystemuet.obj.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class AppHomePageController {

    @FXML
    private VBox userBox1, userBox2, userBox3, userBox4, userBox5;
    @FXML
    private ImageView userImage1, userImage2, userImage3, userImage4, userImage5;
    @FXML
    private Label userName1, userHmPoint1, userRegisterDate1, userExpiredVipDate1;
    @FXML
    private Label userName2, userHmPoint2, userRegisterDate2, userExpiredVipDate2;
    @FXML
    private Label userName3, userHmPoint3, userRegisterDate3, userExpiredVipDate3;
    @FXML
    private Label userName4, userHmPoint4, userRegisterDate4, userExpiredVipDate4;
    @FXML
    private Label userName5, userHmPoint5, userRegisterDate5, userExpiredVipDate5;
    @FXML
    private AnchorPane totalBooksPane;
    @FXML
    private AnchorPane booksOnLoanPane;
    @FXML
    private AnchorPane overdueBooksPane;

    @FXML
    private VBox pieChartContainer;
    @FXML
    private VBox barChartContainer;

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
    private Label adminEmailLabel;

    private Connection connection;
    //
    public void initialize() {
        connection = Database.connectDB();
        if (connection != null) {
            displayTopUsers();
        } else {
            System.out.println("Failed to connect to the database.");
        }
        updateStatistics();
        createPieChart();
        createBarChart();
        displayAdminEmail();
    }

    private void displayTopUsers() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users ORDER BY hmCoin DESC LIMIT 5");
            int index = 1;
            while (rs.next()) {
                String userName = rs.getString("username");
                int hmCoin = rs.getInt("hmCoin");
                String registerDate = rs.getString("registered_date");
                String expiredVipDate = rs.getString("expiredVipDate");
                String avatarImg = rs.getString("avatarImg");

                switch (index) {
                    case 1:
                        updateUserBox(userName, hmCoin, registerDate, expiredVipDate, avatarImg, userName1, userHmPoint1, userRegisterDate1, userExpiredVipDate1, userImage1);
                        break;
                    case 2:
                        updateUserBox(userName, hmCoin, registerDate, expiredVipDate, avatarImg, userName2, userHmPoint2, userRegisterDate2, userExpiredVipDate2, userImage2);
                        break;
                    case 3:
                        updateUserBox(userName, hmCoin, registerDate, expiredVipDate, avatarImg, userName3, userHmPoint3, userRegisterDate3, userExpiredVipDate3, userImage3);
                        break;
                    case 4:
                        updateUserBox(userName, hmCoin, registerDate, expiredVipDate, avatarImg, userName4, userHmPoint4, userRegisterDate4, userExpiredVipDate4, userImage4);
                        break;
                    case 5:
                        updateUserBox(userName, hmCoin, registerDate, expiredVipDate, avatarImg, userName5, userHmPoint5, userRegisterDate5, userExpiredVipDate5, userImage5);
                        break;
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserBox(String userName, int hmCoin, String registerDate, String expiredVipDate, String avatarImg, Label userNameLabel, Label hmPointLabel, Label registerDateLabel, Label expiredVipDateLabel, ImageView userImageView) {
        userNameLabel.setText(userName);
        hmPointLabel.setText("hmPoint: " + hmCoin);
        registerDateLabel.setText(registerDate);
        expiredVipDateLabel.setText("expiredVipDate: " + expiredVipDate);
        if (avatarImg != null && !avatarImg.isEmpty()) {
            userImageView.setImage(new Image(avatarImg));
        }
    }

    //hbox 3 anchorpane -> hiện count
    private void updateStatistics() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS totalBooks FROM books");
            if (rs.next()) {
                updateStatisticPane(totalBooksPane, rs.getInt("totalBooks"), "Total Books");
            }

            rs = stmt.executeQuery("SELECT SUM(noOfBooks) AS booksOnLoan FROM usersrequest WHERE status = 'Book is currently on loan'");
            if (rs.next()) {
                updateStatisticPane(booksOnLoanPane, rs.getInt("booksOnLoan"), "Books on Loan");
            }

            rs = stmt.executeQuery("SELECT SUM(noOfBooks) AS overdueBooks FROM usersrequest WHERE status = 'Overdue for return book'");
            if (rs.next()) {
                updateStatisticPane(overdueBooksPane, rs.getInt("overdueBooks"), "Overdue Books");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatisticPane(AnchorPane pane, int count, String label) {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-alignment: center; -fx-spacing: 5px;");

        Label countLabel = new Label(String.valueOf(count));
        countLabel.setFont(new Font(30));
        countLabel.setStyle("-fx-text-fill: #ffeb3b; -fx-font-weight: bold; -fx-alignment: center;");

        Label textLabel = new Label(label);
        textLabel.setFont(new Font(20));
        textLabel.setStyle("-fx-text-fill: #ffffff; -fx-alignment: center;");

        vbox.getChildren().addAll(countLabel, textLabel);
        vbox.setAlignment(Pos.CENTER);
        pane.getChildren().clear();
        pane.getChildren().add(vbox);
        AnchorPane.setTopAnchor(vbox, 0.0);
        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);
    }

    // bd 25.11.2024
    private void createPieChart() {
        Map<String, Integer> data = fetchDataForPieChart();
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Books by Category");

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(slice);
            Tooltip tooltip = new Tooltip(entry.getKey() + ": " + entry.getValue());
            Tooltip.install(slice.getNode(), tooltip);
        }

        pieChartContainer.getChildren().add(pieChart);
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


    private void createBarChart() {
        Map<String, Integer> data = fetchDataForBarChart();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Books Borrowed");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Books Borrowed by Month");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(dataPoint);
            Tooltip tooltip = new Tooltip(entry.getKey() + ": " + entry.getValue());
            Tooltip.install(dataPoint.getNode(), tooltip);
        }

        barChart.getData().add(series);
        barChartContainer.getChildren().add(barChart);
    }

    //modify info
    private void displayAdminEmail() {
        Admin admin = Admin.getInstance();
        adminEmailLabel.setText(admin.getEmail());
    }

    @FXML
    private void showModifyInfo() {
        adminInfoVBox.setVisible(false);
        modifyInfoVBox.setVisible(true);
    }

    //check currentPass, email <-> pass ???
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

    //ok
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