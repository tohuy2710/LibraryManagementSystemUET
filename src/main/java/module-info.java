module org.example.librarymanagementsystemuet {
    // JavaFX modules
    requires javafx.controls;

    // External libraries and utilities
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // Java built-in modules
    requires java.desktop;
    requires java.sql;

    // MySQL connector for database
    requires mysql.connector.j;
    requires google.api.services.books.v1.rev114;
    requires google.http.client;
    requires google.http.client.jackson2;
    requires google.oauth.client;
    requires google.api.client;
    requires java.net.http;
    requires org.json;
    requires javafx.media;
    requires jdk.httpserver;
    requires okhttp3;
    requires nanohttpd;

    // Google API Client dependencies

    // Allow reflection-based access for FXML loading
    opens org.example.librarymanagementsystemuet to javafx.fxml;

    opens org.example.librarymanagementsystemuet.obj to javafx.base;

    // Export your base package to make it accessible for other modules
    exports org.example.librarymanagementsystemuet;
    exports org.example.librarymanagementsystemuet.adminapp.bookmanagement;
    opens org.example.librarymanagementsystemuet.adminapp.bookmanagement to javafx.fxml;
    exports org.example.librarymanagementsystemuet.adminapp.bookmanagement.bookviewcard;
    opens org.example.librarymanagementsystemuet.adminapp.bookmanagement.bookviewcard to javafx.fxml;
    exports org.example.librarymanagementsystemuet.adminapp;
    opens org.example.librarymanagementsystemuet.adminapp to javafx.fxml;
    exports org.example.librarymanagementsystemuet.adminapp.usermanagement;
    opens org.example.librarymanagementsystemuet.adminapp.usermanagement to javafx.fxml;
    exports org.example.librarymanagementsystemuet.adminapp.userrequestmanagement;
    opens org.example.librarymanagementsystemuet.adminapp.userrequestmanagement to javafx.fxml;
    exports org.example.librarymanagementsystemuet.userapp;
    opens org.example.librarymanagementsystemuet.userapp to javafx.fxml;
    opens org.example.librarymanagementsystemuet.exception to javafx.base;
    exports org.example.librarymanagementsystemuet.adminapp.penaltymanagement;
    opens org.example.librarymanagementsystemuet.adminapp.penaltymanagement to javafx.fxml;
}
