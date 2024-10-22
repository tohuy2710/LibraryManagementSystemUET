package org.example.librarymanagementsystemuet;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class BookApiExampleByISBN {

    public static void main(String[] args) throws Exception {
        // ISBN of the book to search for
        String isbn = "9780132350884";  // Change this to the ISBN of the book you want to search for

        // URL to query Google Books API by ISBN
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        // Create an HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse the JSON response
        JSONObject jsonResponse = new JSONObject(response.body());

        // Check if there are any items in the response
        if (jsonResponse.has("items")) {
            JSONArray items = jsonResponse.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

                // Print book details
                System.out.println("Title: " + volumeInfo.getString("title"));
                if (volumeInfo.has("authors")) {
                    System.out.println("Author(s): " + volumeInfo.getJSONArray("authors").join(", "));
                }
                System.out.println("Publisher: " + volumeInfo.optString("publisher", "Unknown"));
                System.out.println("Published Date: " + volumeInfo.optString("publishedDate", "Unknown"));

                // Extract and print ISBNs (if available)
                if (volumeInfo.has("industryIdentifiers")) {
                    JSONArray identifiers = volumeInfo.getJSONArray("industryIdentifiers");
                    for (int j = 0; j < identifiers.length(); j++) {
                        JSONObject identifier = identifiers.getJSONObject(j);
                        String type = identifier.getString("type");
                        String identifierValue = identifier.getString("identifier");
                        System.out.println(type + ": " + identifierValue);
                    }
                }
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("No results found for ISBN: " + isbn);
        }
    }
}
