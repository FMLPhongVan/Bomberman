module com.phongvan.bomberman {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens com.phongvan.bomberman to javafx.fxml;
    exports com.phongvan.bomberman;

    exports com.phongvan.bomberman.entities;
    opens com.phongvan.bomberman.entities to javafx.fxml;

    exports com.phongvan.bomberman.entities.mobs;
    opens com.phongvan.bomberman.entities.mobs to javafx.fxml;

    exports com.phongvan.bomberman.entities.bomb;
    opens com.phongvan.bomberman.entities.bomb to javafx.fxml;

    exports com.phongvan.bomberman.gui;
    opens com.phongvan.bomberman.gui to javafx.fxml;

    exports com.phongvan.bomberman.player;
    opens com.phongvan.bomberman.player to javafx.fxml;

    exports com.phongvan.bomberman.entities.powerups;
    opens com.phongvan.bomberman.entities.powerups to javafx.fxml;
}