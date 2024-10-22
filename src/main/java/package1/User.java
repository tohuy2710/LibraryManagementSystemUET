package package1;

import java.util.ArrayList;
import java.util.List;

public class User extends Account {
    public List<Book> history = new ArrayList<>();

    public User() {

    }

    public User(String id, String name, String username, String password, String email, String phoneNumber) {
        super(id, name, username, password, email, phoneNumber);
    }

    public void displayUserInfo() {
        System.out.println("User ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Username: " + getUsername());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone Number: " + getPhoneNumber());
    }

    public void displayHistoryOrderByBorrowedId() {
//        history.sort((Document d1, Document d2) -> d1.getId().compareTo(d2.getId()));
//        for (Document document : history) {
//            System.out.print("Document ID: " + document.getId());
//            System.out.print("Document Name: " + document.getName());
//            System.out.print("Document Author: " + document.getAuthor());
//            System.out.print("Document Publisher: " + document.getPublisher());
//            System.out.print("Document Publication Year: " + document.getPublicationYear());
//            System.out.print("Document Genre: " + document.getGenre());
//            System.out.print("Document Language: " + document.getLanguage());
//            System.out.print("Document Number of Document: " + document.getNumberOfDocument());
//            System.out.println();
//        }
    }
}
