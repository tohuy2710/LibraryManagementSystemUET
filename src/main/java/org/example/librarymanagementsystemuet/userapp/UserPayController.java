package org.example.librarymanagementsystemuet.userapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.librarymanagementsystemuet.Database;
import org.example.librarymanagementsystemuet.PaymentAPI;
import org.example.librarymanagementsystemuet.obj.AlertMessage;
import org.example.librarymanagementsystemuet.userapp.obj.UserSession;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.librarymanagementsystemuet.PaymentAPI.*;

public class UserPayController implements Initializable {

    @FXML
    private ImageView decorImg;

    @FXML
    private ImageView donate;

    @FXML
    private ImageView pack1;

    @FXML
    private ImageView pack2;

    @FXML
    private ImageView pack3;

    @FXML
    private Label packNameLabel;

    @FXML
    private VBox panePay;

    @FXML
    private ImageView qrImg;

    @FXML
    private Button bankCheckbtn;

    List<PaymentAPI.Transaction> initTrans = new ArrayList<>();

    public void visiblePanePay() {
        panePay.setVisible(true);
        decorImg.setVisible(false);
    }

    public void setImageOnClick() {
        pack1.setOnMouseClicked(event -> {
            visiblePanePay();

            packNameLabel.setText("Coin Package");
            String transDesciption = String.valueOf(System.currentTimeMillis());
            PaymentAPI.Transaction tran
                    = new PaymentAPI.Transaction("20000", transDesciption, Database.getDateNow(), "Coin Package");
            initTrans.add(tran);
            String qrLink = PaymentAPI.getLinkQr(20000, transDesciption);
            Database.setImageByLink(qrImg, qrLink);
        });

        pack2.setOnMouseClicked(event -> {
            visiblePanePay();

            packNameLabel.setText("HM Member Pack");
            String transDesciption = String.valueOf(System.currentTimeMillis());
            PaymentAPI.Transaction tran
                    = new PaymentAPI.Transaction("50000", transDesciption, Database.getDateNow(), "HM Member Pack");
            initTrans.add(tran);
            String qrLink = PaymentAPI.getLinkQr(50000, transDesciption);
            Database.setImageByLink(qrImg, qrLink);
        });

        pack3.setOnMouseClicked(event -> {
            visiblePanePay();

            packNameLabel.setText("HM King");
            String transDesciption = String.valueOf(System.currentTimeMillis());
            PaymentAPI.Transaction tran
                    = new PaymentAPI.Transaction("999999", transDesciption, Database.getDateNow(), "HM King");
            initTrans.add(tran);
            String qrLink = PaymentAPI.getLinkQr(999999, transDesciption);
            Database.setImageByLink(qrImg, qrLink);
        });

        donate.setOnMouseClicked(event -> {
            visiblePanePay();

            packNameLabel.setText("Donate");
            String transDesciption = String.valueOf(System.currentTimeMillis());
            PaymentAPI.Transaction tran
                    = new PaymentAPI.Transaction("999999", transDesciption, Database.getDateNow(), DONATE);
            initTrans.add(tran);
            String qrLink = PaymentAPI.getLinkQr(0, transDesciption);
            Database.setImageByLink(qrImg, qrLink);
        });
    }

    public void checkBanked() {
        new Thread(this::checkBankedThr).start();
    }

    private void checkBankedThr() {
        if (initTrans.isEmpty()) {
            return;
        }
            for (Transaction transaction : initTrans) {
            System.out.println(transaction.getInitType() + " "
                    + transaction.getAmount() + " " + transaction.getDescription());
            if (check(transaction.getDescription(),
                    Integer.parseInt(transaction.getAmount()),
                    transaction.getDate())) {
                System.out.println(transaction.getInitType() + transaction.getAmount() + " " + transaction.getTime() + " " + transaction.getDescription());

                Platform.runLater(() -> {
                    if (transaction.getInitType().equals("Coin Package")) {
                        UserSession.getInstance().setHmCoin(UserSession.getInstance().getHmCoin() + 30);
                        setChangeToDB(1);
                    } else if (transaction.getInitType().equals("HM Member Pack")) {
                        UserSession.getInstance().setHmCoin(UserSession.getInstance().getHmCoin() + 50);
                        setChangeToDB(2);
                    } else if (transaction.getInitType().equals("HM King")) {
                    UserSession.getInstance().setHmCoin(UserSession.getInstance().getHmCoin() + 1999);
                        setChangeToDB(3);
                    } else if (transaction.getInitType().equals(DONATE)) {
                        UserSession.getInstance().setHmCoin(UserSession.getInstance().getHmCoin()
                                + Integer.parseInt(transaction.getAmount())%1000);
                    }
                    initTrans.remove(transaction);

                    AlertMessage alertMessage = new AlertMessage();
                    alertMessage.successMessage("Payment success!");
                });
            }
        }
    }

    private void setChangeToDB(int pack) {
        Database.connect = Database.connectDB();
        String query = "UPDATE users SET hmCoin = " + UserSession.getInstance().getHmCoin()
                + " WHERE id = " + UserSession.getInstance().getId();
        if (pack == 2) {
            query = "UPDATE users SET hmCoin = " + UserSession.getInstance().getHmCoin()
                    + ", expiredVipDate = NOW() + INTERVAL 1 MONTH"
                    + " WHERE id = " + UserSession.getInstance().getId();
        } else if (pack == 3) {
            query = "UPDATE users SET hmCoin = " + UserSession.getInstance().getHmCoin()
                    + ", expiredVipDate = NOW() + INTERVAL 999 MONTH"
                    + " WHERE id = " + UserSession.getInstance().getId();
        }
        try {
            Database.preparedStatement = Database.connect.prepareStatement(query);
            Database.preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImageOnClick();
        decorImg.setVisible(true);
        panePay.setVisible(false);
    }
}
