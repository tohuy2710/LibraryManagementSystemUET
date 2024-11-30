package org.example.librarymanagementsystemuet;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class WebhookReceiver extends NanoHTTPD {

    public WebhookReceiver() throws IOException {
        super(80); // Lắng nghe trên cổng 8080
        start(SOCKET_READ_TIMEOUT, false); // Bắt đầu server
        System.out.println("Server is running on http://localhost:8080");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String responseText = "Hello, NanoHTTPD!";
        return newFixedLengthResponse(Response.Status.OK, "text/plain", responseText);
    }

    public static void main(String[] args) {
        try {
            new WebhookReceiver();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
