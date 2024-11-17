package package1;

import javafx.beans.property.SimpleStringProperty;

public class Book {
    private SimpleStringProperty id;
    private SimpleStringProperty isbn;
    private SimpleStringProperty name;
    private SimpleStringProperty authors;
    private SimpleStringProperty publisher;
    private SimpleStringProperty addedDate;
    private SimpleStringProperty lastUpdateDate;
    private SimpleStringProperty typeOfLastUpdate;
    private SimpleStringProperty imageLink;
    private SimpleStringProperty category;
    private SimpleStringProperty location;
    private SimpleStringProperty quantity;
    private SimpleStringProperty avgRate;
    private SimpleStringProperty pageCount;
    private SimpleStringProperty description;
    private SimpleStringProperty views;
    private SimpleStringProperty borrowCount;
    private SimpleStringProperty language;
    private SimpleStringProperty publisherDate;

    public String getPublisherDate() {
        return publisherDate.get();
    }

    public SimpleStringProperty publisherDateProperty() {
        return publisherDate;
    }

    public void setPublisherDate(String publisherDate) {
        if (publisherDate == null) {
            publisherDate = "";
        } else {
            this.publisherDate.set(publisherDate);
        }
    }

    public Book() {
        this.id = new SimpleStringProperty();
        this.isbn = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.authors = new SimpleStringProperty();
        this.publisher = new SimpleStringProperty();
        this.addedDate = new SimpleStringProperty();
        this.lastUpdateDate = new SimpleStringProperty();
        this.typeOfLastUpdate = new SimpleStringProperty();
        this.imageLink = new SimpleStringProperty();
        this.category = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.quantity = new SimpleStringProperty();
        this.avgRate = new SimpleStringProperty();
        this.pageCount = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.views = new SimpleStringProperty();
        this.borrowCount = new SimpleStringProperty();
        this.language = new SimpleStringProperty();
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null){
            category = "";
        } else{
            this.category.set(category);
        }
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null){
            location = "";
        } else {
            this.location.set(location);
        }
    }

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        if (quantity == null){
            quantity = "";
        } else{
            this.quantity.set(quantity);
        }
    }

    public String getAvgRate() {
        return avgRate.get();
    }

    public SimpleStringProperty avgRateProperty() {
        return avgRate;
    }

    public void setAvgRate(String avgRate) {
        if (avgRate == null){
            avgRate = "";
        } else{
            this.avgRate.set(avgRate);
        }
    }

    public String getPageCount() {
        return pageCount.get();
    }

    public SimpleStringProperty pageCountProperty() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        if (pageCount == null){
            pageCount = "";
        } else{
            this.pageCount.set(pageCount);
        }
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null){
            description = "";
        } else {
            this.description.set(description);
        }
    }

    public String getViews() {
        return views.get();
    }

    public SimpleStringProperty viewsProperty() {
        return views;
    }

    public void setViews(String views) {
        if (views == null){
            views = "";
        } else {
            this.views.set(views);
        }
    }

    public String getBorrowCount() {
        return borrowCount.get();
    }

    public SimpleStringProperty borrowCountProperty() {
        return borrowCount;
    }

    public void setBorrowCount(String borrowCount) {
        if (borrowCount == null){
            borrowCount = "";
        } else {
            this.borrowCount.set(borrowCount);
        }
    }

    public String getLanguage() {
        return language.get();
    }

    public SimpleStringProperty languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        if (language == null){
            language = "";
        } else {
            this.language.set(language);
        }
    }

    public String getImageLink() {
        return imageLink.get();
    }

    public SimpleStringProperty imageLinkProperty() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        if (imageLink == null){
            imageLink = "";
        } else {
            this.imageLink.set(imageLink);
        }
    }

    public String getLastUpdateDate() {
        return lastUpdateDate.get();
    }

    public SimpleStringProperty lastUpdateDateProperty() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        if (lastUpdateDate == null){
            lastUpdateDate = "";
        } else {
            this.lastUpdateDate.set(lastUpdateDate);
        }
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public SimpleStringProperty isbnProperty() {
        return isbn;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getAuthors() {
        return authors.get();
    }

    public SimpleStringProperty authorsProperty() {
        return authors;
    }

    public void setAuthors(String authors) {
        if (authors == null){
            authors = "";
        } else {
            this.authors.set(authors);
        }
    }

    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    public SimpleStringProperty addedDateProperty() {
        return addedDate;
    }

    public String getTypeOfLastUpdate() {
        return typeOfLastUpdate.get();
    }

    public SimpleStringProperty typeOfLastUpdateProperty() {
        return typeOfLastUpdate;
    }

    public void setTypeOfLastUpdate(String typeOfLastUpdate) {
        if (typeOfLastUpdate == null){
            typeOfLastUpdate = "";
        } else {
            this.typeOfLastUpdate.set(typeOfLastUpdate);
        }
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
        if (publisher == null){
            publisher = "";
        } else {
            this.publisher.set(publisher);
        }
    }

    public String getAddedDate() {
        return addedDate.get();
    }

    public void setAddedDate(String addedDate) {
        if (addedDate == null){
            addedDate = "";
        } else {
            this.addedDate.set(addedDate);
        }
    }
}
