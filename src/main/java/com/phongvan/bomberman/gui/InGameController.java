package com.phongvan.bomberman.gui;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.graphics.SpriteHandler;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ResourceBundle;

public class InGameController implements Initializable {
    private static InGameController instance;

    @FXML
    private Canvas gameBoard;

    @FXML
    private AnchorPane inGamePane;

    private GraphicsContext gc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        gc = gameBoard.getGraphicsContext2D();
        inGamePane.setPrefWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.DEFAULT_APP_SCALE_SIZE);
        inGamePane.setPrefHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.DEFAULT_APP_SCALE_SIZE);

        gameBoard.setWidth(15 * 16 * PaneController.DEFAULT_APP_SCALE_SIZE);
        gameBoard.setHeight(15 * 16 * PaneController.DEFAULT_APP_SCALE_SIZE);
        gameBoard.setFocusTraversable(true);

        Core.getInstance().initGC(gc);

        System.out.println(gameBoard.getHeight());
    }

    public static InGameController getInstance() {
        return instance;
    }

    public Canvas getGameBoard() {
        return gameBoard;
    }

    public void resizePane() {
        inGamePane.setPrefWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.getInstance().getScaleSize());
        inGamePane.setPrefHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.getInstance().getScaleSize());

        gameBoard.setWidth(15 * 16 * PaneController.getInstance().getScaleSize());
        gameBoard.setHeight(15 * 16 * PaneController.getInstance().getScaleSize());

        Camera.getInstance().setCameraProperty();
    }

    public void start() {
        PauseTransition pause = new PauseTransition(Duration.seconds(2 ));
        pause.setOnFinished(e -> {
            Core.getInstance().start();
        });
        pause.play();
    }

    public void onPlayerPressed(KeyEvent keyEvent) {
        Core.getInstance().addInputEvent(keyEvent);
    }

    public void onPlayerReleased(KeyEvent keyEvent) {
        Core.getInstance().removeInputEvent(keyEvent);
    }
}
