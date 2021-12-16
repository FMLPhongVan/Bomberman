package com.phongvan.bomberman;

import com.phongvan.bomberman.gui.PaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Bomberman extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        Bomberman.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Bomberman.class.getResource("gui/app.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                PaneController.DEFAULT_APP_WIDTH * PaneController.DEFAULT_APP_SCALE_SIZE,
                PaneController.DEFAULT_APP_HEIGHT * PaneController.DEFAULT_APP_SCALE_SIZE);

        System.out.println(Screen.getPrimary().getBounds().getHeight());

        Logger.log(Logger.INFO, "Bomberman", "Starting game");

        Bomberman.stage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        Logger.log(Logger.INFO, "Bomberman", "Current size of Game is : " + stage.getWidth() + "x" + stage.getHeight());

        Bomberman.stage.initStyle(StageStyle.TRANSPARENT);
        Bomberman.stage.setResizable(false);
        Bomberman.stage.setTitle("Super Bomberman");
        Bomberman.stage.setScene(scene);
        Bomberman.stage.sizeToScene();
        Bomberman.stage.show();
    }

    public static void changeSize() throws IOException {
        double newWidth = PaneController.DEFAULT_APP_WIDTH * PaneController.getInstance().getScaleSize();
        double newHeight = PaneController.DEFAULT_APP_HEIGHT * PaneController.getInstance().getScaleSize();

        stage.setWidth(newWidth);
        stage.setHeight(newHeight);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

        Logger.log(Logger.INFO, "Bomberman", "Current size of Game is : " + newWidth + "x" + newHeight);
    }

    public static void main(String[] args) {
        launch();
    }
}