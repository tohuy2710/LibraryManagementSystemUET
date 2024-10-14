package org.example.librarymanagementsystemuet;

import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;

public class GoogleBooksAPIExample {
    private static final String API_KEY = "AIzaSyAgKSfHFYAt6XR9-DLjTCWGAzwVF65ekzU";

    public static void main(String[] args) throws IOException {
        // Khởi tạo dịch vụ Books
        Books books = new Books.Builder(new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.jackson2.JacksonFactory(), null)
                .setApplicationName("Your Application Name")
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
                .build();

        // Tìm kiếm sách theo từ khóa
        String query = "Object-oriented programming";
        Books.Volumes.List volumesList = books.volumes().list(query);
        Volumes volumes = volumesList.execute();

        // In kết quả ra màn hình
        if (volumes.getItems() != null && !volumes.getItems().isEmpty()) {
            volumes.getItems().forEach(volume -> {
                System.out.println("Title: " + volume.getVolumeInfo().getTitle());
                System.out.println("Authors: " + volume.getVolumeInfo().getAuthors());
                System.out.println("Description: " + volume.getVolumeInfo().getDescription());
                System.out.println();
            });
        } else {
            System.out.println("No books found.");
        }
    }
}

