package org.example.librarymanagementsystemuet;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleBooksAPI {

    private static final String APPLICATION_NAME = "Google Books API Test";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/org/example/librarymanagementsystemuet/credentials.json";
    //please get your own credentials file from Google Books API.
    private static Books getBooksService() throws GeneralSecurityException, IOException {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        InputStream credentialsStream = GoogleBooksAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

        if (credentialsStream == null) {
            throw new IOException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/books"));

        return new Books.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static class SearchByISBN {

        private final String ISBN;
        private Volumes volumes;

        public SearchByISBN(String ISBN) {
            this.ISBN = ISBN;
        }


        public String getISBN() {
            return ISBN;
        }

        public void setVolumes(Volumes volumes) {
            this.volumes = volumes;
        }

        public Volumes getVolumes() {
            return volumes;
        }

        public void run() {
            try {
                Books books = getBooksService();
                Books.Volumes.List volumesList = books.volumes()
                        .list("isbn:" + ISBN)
                        .setMaxResults(40L);
                this.volumes = volumesList.execute();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class SearchByName {
        private final String bookName;
        private Volumes volumes;

        public SearchByName(String bookName) {
            this.bookName = bookName;
        }

        public Volumes getVolumes() {
            return volumes;
        }
        public void run() {
            try {
                Books books = getBooksService();
                Books.Volumes.List volumesList = books.volumes().list(bookName).setMaxResults(40L);
                this.volumes = volumesList.execute();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SearchByName searchByName = new SearchByName("Java Programming");
        searchByName.run();
        Volumes volumes = searchByName.getVolumes();
        System.out.println(volumes);
    }
}
