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

    public static final String PENALTLY = "PENALTY";
    public static final String VIP = "VIP";
    public static final String DONATE = "DONATE";

    public static String getLinkQr(String amount, String description) {
        return LINK_QR_PREFIX +
                LINK_QR_AMOUNT + amount + LINK_QR_ADD_INFO + description;
    }

    public static String getLinkQr(int amount, String description) {
        return LINK_QR_PREFIX +
                LINK_QR_AMOUNT + amount + LINK_QR_ADD_INFO + description;
    }

    public static String getLinkQr(long amount, String description) {
        return LINK_QR_PREFIX +
                LINK_QR_AMOUNT + amount + LINK_QR_ADD_INFO + description;
    }

    public static boolean check(String descriptionContent,int paymentAmount, String date) {
        OkHttpClient client = new OkHttpClient();
        LocalDate today = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        Request request = new Request.Builder()
                .url("https://oauth.casso.vn/v2/transactions?fromDate=" + formattedDate + "&sort=DESC")
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

                            if (description.contains(descriptionContent)
                                    && (amount.compareTo(String.valueOf(paymentAmount)) >= 0)) {
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

    public static List<Transaction> getTransactions() {
        OkHttpClient client = new OkHttpClient();
        List<Transaction> transactions = new ArrayList<>();
        //https://oauth.casso.vn/v2/transactions?fromDate=2021-04-01&page=4&pageSize=20&sort=ASC
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        Request request = new Request.Builder()
                .url("https://oauth.casso.vn/v2/transactions?fromDate=" + formattedDate + "&sort=DESC")
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

    public static class Transaction {
        private String id;
        private String tid;
        private String description;
        private String amount;
        private String when;
        private String date;
        private String time;
        private String initType;

        public Transaction(String id, String tid, String description, String amount, String when) {
            this.id = id;
            this.tid = tid;
            this.description = description;
            this.amount = amount;
            this.when = when;
            date = when.substring(0, 10);
            time = when.substring(11, 19);
        }

        public Transaction(String amount, String description, String date, String initType) {
            this.amount = amount;
            this.description = description;
            this.date = date;
            this.initType = initType;
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

        public String getInitType() {
            return initType;
        }

        public void setInitType(String initType) {
            this.initType = initType;
        }
    }

    public static void main(String[] args) {
        boolean t = check("HMVip1732911357883", 20000, Database.getDateNow());
    }

    //Coin Package 20000 HMVip1732911357883

}
