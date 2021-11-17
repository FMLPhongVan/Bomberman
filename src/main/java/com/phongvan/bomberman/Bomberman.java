package com.phongvan.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.io.IOException;

public class Bomberman extends Application {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    private GraphicsContext graphicsContext;

    @Override
    public void start(Stage stage) throws IOException {
        Canvas canvas = new Canvas(HEIGHT, WIDTH);
        graphicsContext = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}