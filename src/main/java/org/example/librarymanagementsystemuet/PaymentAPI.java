package org.example.librarymanagementsystemuet;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentAPI {

    private static final String API_KEY = "AK_CS.687b1090ad9611ef8f20077fb9828c57.roJCbYj97ZZnD" +
            "FrFBOPoyGWgT0WaEW8fO63lTUGKHSngV6aO30azSex8Z6ICSBNXq4nFkDu7";

    private static final String LINK_QR_PREFIX = "https://api.vietqr.io/image/970422-0001019624993-JXVtmO3.jpg?accountName=TO%20QUANG%20HUY";
    private static final String LINK_QR_AMOUNT = "&amount=";
    private static final String LINK_QR_ADD_INFO = "&addInfo=";

    private static final String PENATLY = "PENALTY";
    private static final String VIP = "VIP";
    private static final String DONATE = "DONATE";

    public static String getLinkQr(int amount, String addInfo) {
        return LINK_QR_PREFIX +
                LINK_QR_AMOUNT + amount + LINK_QR_ADD_INFO + addInfo;
    }

    public static String getAddInfoKey(String type) {
        if (type.equals(PENATLY)) {
            return "HMPen" + System.currentTimeMillis();
        }
        if (type.equals(VIP)) {
            return "HMVip" + System.currentTimeMillis();
        }
        return "HMDonate" + System.currentTimeMillis();
    }

    public static List<Transaction> getTransactions() {
        OkHttpClient client = new OkHttpClient();
        List<Transaction> transactions = new ArrayList<>();
        //https://oauth.casso.vn/v2/transactions?fromDate=2021-04-01&page=4&pageSize=20&sort=ASC
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        Request request = new Request.Builder()
                .url("https://oauth.casso.vn/v2/transactions?fromDate=" + formattedDate)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Apikey " + API_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();

                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.has("data")) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    if (dataObject.has("records")) {
                        JSONArray recordsArray = dataObject.getJSONArray("records");

                        for (int i = 0; i < recordsArray.length(); i++) {
                            JSONObject record = recordsArray.getJSONObject(i);

                            String id = String.valueOf(record.optInt("id", 0));
                            String tid = record.optString("tid", "");
                            String description = record.optString("description", "");
                            String amount = String.valueOf(record.optInt("amount", 0));
                            String when = record.optString("when", "");

                            Transaction transaction = new Transaction(id, tid, description, amount, when);
                            transactions.add(transaction);
                        }
                    }
                }
            } else {
                System.err.println("Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private static class Transaction {
        private String id;
        private String tid;
        private String description;
        private String amount;
        private String when;
        private String date;
        private String time;

        public Transaction(String id, String tid, String description, String amount, String when) {
            this.id = id;
            this.tid = tid;
            this.description = description;
            this.amount = amount;
            this.when = when;
            date = when.substring(0, 10);
            time = when.substring(11, 19);
        }

        public String getId() {
            return id;
        }

        public String getTid() {
            return tid;
        }

        public String getDescription() {
            return description;
        }

        public String getAmount() {
            return amount;
        }

        public String getWhen() {
            return when;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }
    }

    public static boolean paymentCheck(String descriptionContent,int paymentAmount, String timeConfirm) {
        OkHttpClient client = new OkHttpClient();
        //https://oauth.casso.vn/v2/transactions?fromDate=2021-04-01&page=4&pageSize=20&sort=ASC
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        Request request = new Request.Builder()
                .url("https://oauth.casso.vn/v2/transactions?fromDate=" + formattedDate)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Apikey " + API_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();

                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.has("data")) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    if (dataObject.has("records")) {
                        JSONArray recordsArray = dataObject.getJSONArray("records");

                        for (int i = 0; i < recordsArray.length(); i++) {
                            JSONObject record = recordsArray.getJSONObject(i);

                            String description = record.optString("description", "");
                            String amount = String.valueOf(record.optInt("amount", 0));
                            String when = record.optString("when", "");
                            String time = when.substring(11, 19);

                            if (description.contains(descriptionContent)
                                    && (Integer.parseInt(amount) >= paymentAmount)
                                    && time.compareTo(timeConfirm) < 0) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                System.err.println("Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        List<Transaction> transactions = getTransactions();
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getId()
                    + " - " + transaction.getTid()
                    + " - " + transaction.getDescription()
                    + " - " + transaction.getAmount()
                    + " - " + transaction.getWhen()
                    + " - " + transaction.getDate()
                    + " - " + transaction.getTime());
        }

        System.out.println(paymentCheck("TO QUANG HUY CHUYEN TIEN",
                5000, "09:55:48"));
    }

}
