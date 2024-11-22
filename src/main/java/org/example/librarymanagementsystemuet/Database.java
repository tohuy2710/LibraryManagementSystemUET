package org.example.librarymanagementsystemuet;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.librarymanagementsystemuet.exception.InvalidDatatype;
import org.example.librarymanagementsystemuet.obj.Book;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.*;

public class Database {

    public static Connection connect;
    public static PreparedStatement prepare;
    public static ResultSet result;
    public static PreparedStatement preparedStatement;

    public static final String VALID_DATE_FORMAT_REGEX = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$\n";
    public static final String VALID_YEAR_REGEX = "^(19|20)\\d{2}$";
    public static final String VALID_YEAR_MONTH_REGEX = "^(19|20)\\d{2}-(0[1-9]|1[0-2])$\n";

    public static final String DEFAULT_DATE = "1970-01-01";


    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3307/library_management_system_uet",
                            "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void setImageByLink(ImageView imageView, String link) {

        imageView.setImage(new Image(Objects.requireNonNull(Database.class
                .getResourceAsStream("/asset/img/unknowCover.jpg"))));

        Callable<Image> loadImageTask = () -> {
            try {
                return new Image(link, true);
            } catch (Exception e) {
                return new Image(Objects.requireNonNull(Database.class
                        .getResourceAsStream("/asset/img/unknowCover.jpg")));
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

    public static String validateDate(String date) {
        if (date.matches(VALID_DATE_FORMAT_REGEX)) {
            return date;
        } else {
            if (date.matches(VALID_YEAR_MONTH_REGEX)) {
                return date + "-01";
            } else if (date.matches(VALID_YEAR_REGEX)) {
                return date + "-01-01";
            }
        }
        return DEFAULT_DATE;
    }

    public static Book setBookInfo() throws SQLException, InvalidDatatype {
        Book book = new Book();
        book.setId(Database.result.getInt("id"));
        book.setName(Database.result.getString("name"));
        book.setIsbn(Database.result.getString("isbn"));
        book.setAuthor(Database.result.getString("author"));
        book.setPublisher(Database.result.getString("publisher"));
        book.setCategory(Database.result.getString("category"));
        book.setLocation(Database.result.getString("location"));
        book.setQuantity(String.valueOf(Database.result.getInt("quantity")));
        book.setAddedDate(Database.result.getString("addedDate"));
        book.setDescription(Database.result.getString("description"));
        book.setImageLink(Database.result.getString("linkCoverImage"));
        book.setLastUpdateDate(Database.result.getString("lastUpdateDate"));
        book.setAvgRate(String.valueOf(Database.result.getFloat("avgRate")));
        book.setLanguage(Database.result.getString("language"));
        book.setPublisherDate(Database.result.getString("publisherDate"));
        book.setPageCount(String.valueOf(Database.result.getInt("pageCount")));
        book.setViews(String.valueOf(Database.result.getInt("views")));
        book.setBorrowCount(String.valueOf(Database.result.getInt("borrowCount")));
        return book;
    }
}
