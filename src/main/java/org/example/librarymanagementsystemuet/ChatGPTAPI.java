package org.example.librarymanagementsystemuet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPTAPI {
    public static String chatGPT(String question) {
        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-3.5-turbo";
        String apiKey = "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";
        // The prompt is the question that the user asks
        String knowledge =
                "You are a helpful assistant of Humami Library that can answer questions from user." +
                "This is a conversation between a user and a library assistant. If user question not in your knowledge," +
                        "you will reply: " +
                        // reply question not in the knowledge base
                        "'I'm sorry, but as the Humami Library assistant, " +
                        "I can only answer questions related to the Humami Library, " +
                        "such as borrowing books, using HMCoins, " +
                        "or the topics our library specializes in " +
                        "(information technology, economics, and literature). " +
                        "Let me know if you have any library-related questions!' " +

                        "The user is asking a question to the assistant." +

                        // Knowledge base
                        "This is your knowledge base. You can use it to answer the user's question: " +
                        "Our Humami library mainly provides books on " +
                        "information technology, economics and literature. " +
                        "To borrow books from the Humami library, you need HMCoins. " +
                        "With each HMCoin, you can send a request to borrow a book to the admin. " +
                        "If the request is accepted, you will be able to go to the library to borrow the book. " +
                        "If not, you will be returned the coins used. " +

                        "Now, the user is asking a question to the assistant: ";

        String prompt = knowledge + question;

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }

    public static void main(String[] args) {

        System.out.println(chatGPT("who are you?"));

    }
}
