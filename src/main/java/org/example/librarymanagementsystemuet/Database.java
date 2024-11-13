package org.example.librarymanagementsystemuet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import package1.AlertMessage;

import java.sql.*;
import java.util.Timer;
import java.util.concurrent.*;

public class Database {

    protected static Connection connect;
    protected static PreparedStatement prepare;
    protected static ResultSet result;
    protected static PreparedStatement preparedStatement;

    protected static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3308/library_management_system_uet",
                            "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    protected static void setImageByLink(ImageView imageView, String link) {

        imageView.setImage(new Image(Database.class.getResourceAsStream("unknowCover.jpg")));

        Callable<Image> loadImageTask = () -> {
            try {
                return new Image(link, true);
            } catch (Exception e) {
                return new Image(Database.class.getResourceAsStream("unknowCover.jpg"));
            }
        };

        Future<Image> future = executorService.submit(loadImageTask);

        executorService.submit(() -> {
            try {
                Image image = future.get(5, TimeUnit.SECONDS);
                imageView.setImage(image);
            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Image loading timed out.");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
