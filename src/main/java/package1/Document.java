package package1;
import java.util.*;

public class Document {
    private String id;
    private String name;
    private String author;
    private String publisher;
    private int publicationYear;
    private List<String> genre;
    private String language;
    private int numberOfDocument;

    public Document(String id, String name, String author, String publisher, int publicationYear, List<String> genre, String language, int numberOfDocument) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.language = language;
        this.numberOfDocument = numberOfDocument;
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getNumberOfDocument() {
        return numberOfDocument;
    }

    public void setNumberOfDocument(int numberOfDocument) {
        this.numberOfDocument = numberOfDocument;
    }

    public void display() {
        System.out.println("Document ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Author: " + author);
        System.out.println("Publisher: " + publisher);
        System.out.println("Publication Year: " + publicationYear);
        System.out.println("Genres: " + String.join(", ", genre));
        System.out.println("Language: " + language);
        System.out.println("Number of Documents: " + numberOfDocument);
    }
}
