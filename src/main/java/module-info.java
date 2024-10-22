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

    // Google API Client dependencies

    // Allow reflection-based access for FXML loading
    opens org.example.librarymanagementsystemuet to javafx.fxml;

    opens package1 to javafx.base;

    // Export your base package to make it accessible for other modules
    exports org.example.librarymanagementsystemuet;
}
