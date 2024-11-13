package package1;

import javafx.beans.property.SimpleStringProperty;

public class Book {
    private SimpleStringProperty id;
    private SimpleStringProperty isbn;
    private SimpleStringProperty name;
    private SimpleStringProperty authors;
    private SimpleStringProperty publisher;
    private SimpleStringProperty addedDate;
    private SimpleStringProperty imageUrl;

    public Book() {
        this.id = new SimpleStringProperty();
        this.isbn = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.authors = new SimpleStringProperty();
        this.publisher = new SimpleStringProperty();
        this.addedDate = new SimpleStringProperty();
        this.imageUrl = new SimpleStringProperty();
    }

    public int getId() {
        return Integer.parseInt(id.get());
    }

    public void setId(int id) {
        this.id.set(String.valueOf(id));
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAuthor() {
        return authors.get();
    }

    public void setAuthor(String author) {
        this.authors.set(author);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getAddedDate() {
        return addedDate.get();
    }

    public void setAddedDate(String addedDate) {
        this.addedDate.set(addedDate);
    }

    public String getImageUrl() {
        return imageUrl.get();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }
}