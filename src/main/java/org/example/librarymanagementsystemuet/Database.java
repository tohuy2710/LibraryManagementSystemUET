package org.example.librarymanagementsystemuet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.librarymanagementsystemuet.exception.InvalidDatatype;
import org.example.librarymanagementsystemuet.obj.Book;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.*;

public class Database {

    public static Connection connect = connectDB();
    public static PreparedStatement prepare;
    public static ResultSet result;
    public static PreparedStatement preparedStatement;

    public static final String VALID_DATE_FORMAT_REGEX = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$\n";

    public static final String DEFAULT_DATE = "1970-01-01";

    public static final String NUMBER_REGEX = "^-?\\d+$";

    private static String today = null;

    public static String getDateNow() {
        if (today != null) {
            return today;
        }
        LocalDate todayLocalDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        today = todayLocalDate.format(formatter);
        return today;
    }

    public static String getTimeNow() {
        LocalTime time = LocalTime.now();
        return time.toString();
    }



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

    private static final ExecutorService executorService = Executors.newFixedThreadPool(12);

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
                Image image = future.get(100, TimeUnit.MILLISECONDS);
                imageView.setImage(image);
            } catch (Exception e) {
                future.cancel(true);
                e.printStackTrace();
            }
        });
    }

    public static String validateDate(String date) {
        if (date.matches(VALID_DATE_FORMAT_REGEX)) {
            return date;
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
        return book;
    }
}
