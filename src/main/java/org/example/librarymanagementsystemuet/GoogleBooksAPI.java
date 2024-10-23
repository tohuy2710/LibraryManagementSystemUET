package org.example.librarymanagementsystemuet;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleBooksAPI extends Thread {

    private static final String APPLICATION_NAME = "Google Books API Test";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/org/example/librarymanagementsystemuet/credentials.json";

    public static Volumes searchBookByISBN(String ISBN) throws GeneralSecurityException, IOException {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        InputStream credentialsStream = GoogleBooksAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

        if (credentialsStream == null) {
            throw new IOException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/books"));

        Books books = new Books.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN);
        Books.Volumes.List volumesList = books.volumes().list(ISBN).setMaxResults(6L);
        Volumes volumes = volumesList.execute();
        return volumes;
    }

    public static Volumes searchBookByName(String bookName) throws GeneralSecurityException, IOException {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        InputStream credentialsStream = GoogleBooksAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

        if (credentialsStream == null) {
            throw new IOException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/books"));

        Books books = new Books.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=" + bookName);
        Books.Volumes.List volumesList = books.volumes().list(bookName).setMaxResults(12L);
        Volumes volumes = volumesList.execute();
        return volumes;
    }

}