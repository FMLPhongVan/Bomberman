package com.phongvan.bomberman.gui;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.HighScoreManager;
import com.phongvan.bomberman.SoundHandler;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class InGameController implements Initializable {
    private static InGameController instance;
    @FXML
    private AnchorPane pausePane;
    @FXML
    private AnchorPane pauseMenu;
    @FXML
    private ImageView inGameBackground;
    @FXML
    private AnchorPane gameOverPane;
    @FXML
    private AnchorPane gameOverMenu;
    @FXML
    private AnchorPane highScorePane;
    @FXML
    private AnchorPane highScoreMenu;
    @FXML
    private TextField name;
    @FXML
    private AnchorPane levelInfoPane;
    @FXML
    private Label levelInfoLabel;
    @FXML
    private Label livesInfoLabel;
    @FXML
    private Label bombsInfoLabel;
    @FXML
    private Label scoreInfoLabel;
    @FXML
    private Label bombLabel;
    @FXML
    private VBox infoVbox;
    @FXML
    private Label heartLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private AnchorPane storyModePane;
    @FXML
    private AnchorPane battleModePane;
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
        inGameBackground.setFitWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.DEFAULT_APP_SCALE_SIZE);
        inGameBackground.setFitHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.DEFAULT_APP_SCALE_SIZE);

        pauseMenu.setLayoutX(inGamePane.getPrefWidth() / 2 - pauseMenu.getPrefWidth() / 2);
        pauseMenu.setLayoutY(inGamePane.getPrefHeight() / 2 - pauseMenu.getPrefHeight() / 2);

        levelInfoPane.setLayoutX(inGamePane.getPrefWidth() / 2 - levelInfoPane.getPrefWidth() / 2);
        levelInfoPane.setLayoutY(inGamePane.getPrefHeight() / 2 - levelInfoPane.getPrefHeight() / 2);

        gameOverMenu.setLayoutX(inGamePane.getPrefWidth() / 2 - levelInfoPane.getPrefWidth() / 2);
        gameOverMenu.setLayoutY(inGamePane.getPrefHeight() / 2 - levelInfoPane.getPrefHeight() / 2);

        highScoreMenu.setLayoutX(inGamePane.getPrefWidth() / 2 - levelInfoPane.getPrefWidth() / 2);
        highScoreMenu.setLayoutY(inGamePane.getPrefHeight() / 2 - levelInfoPane.getPrefHeight() / 2);

        gameBoard.setWidth(15 * 16 * PaneController.DEFAULT_APP_SCALE_SIZE);
        gameBoard.setHeight(15 * 16 * PaneController.DEFAULT_APP_SCALE_SIZE);
        gameBoard.setLayoutX(inGamePane.getPrefWidth() / 2 - gameBoard.getWidth() / 2);
        gameBoard.setLayoutY(inGamePane.getPrefHeight() / 2 - gameBoard.getHeight() / 2);
        gameBoard.setFocusTraversable(true);

        Core.getInstance().initGC(gc);
    }

    public void resizePane() {
        inGamePane.setPrefWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.getInstance().getScaleSize());
        inGamePane.setPrefHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.getInstance().getScaleSize());
        inGameBackground.setFitWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.getInstance().getScaleSize());
        inGameBackground.setFitHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.getInstance().getScaleSize());

        pauseMenu.setLayoutX(inGamePane.getPrefWidth() / 2 - pauseMenu.getPrefWidth() / 2);
        pauseMenu.setLayoutY(inGamePane.getPrefHeight() / 2 - pauseMenu.getPrefHeight() / 2);

        levelInfoPane.setLayoutX(inGamePane.getPrefWidth() / 2 - levelInfoPane.getPrefWidth() / 2);
        levelInfoPane.setLayoutY(inGamePane.getPrefHeight() / 2 - levelInfoPane.getPrefHeight() / 2);

        gameOverMenu.setLayoutX(inGamePane.getPrefWidth() / 2 - levelInfoPane.getPrefWidth() / 2);
        gameOverMenu.setLayoutY(inGamePane.getPrefHeight() / 2 - levelInfoPane.getPrefHeight() / 2);

        highScoreMenu.setLayoutX(inGamePane.getPrefWidth() / 2 - levelInfoPane.getPrefWidth() / 2);
        highScoreMenu.setLayoutY(inGamePane.getPrefHeight() / 2 - levelInfoPane.getPrefHeight() / 2);

        gameBoard.setWidth(15 * 16 * PaneController.getInstance().getScaleSize());
        gameBoard.setHeight(15 * 16 * PaneController.getInstance().getScaleSize());
        gameBoard.setLayoutX(inGamePane.getPrefWidth() / 2 - gameBoard.getWidth() / 2);
        gameBoard.setLayoutY(inGamePane.getPrefHeight() / 2 - gameBoard.getHeight() / 2);

        Camera.getInstance().setCameraProperty();
    }

    public void earlyStage() {
        gameBoard.setVisible(false);
        infoVbox.setVisible(false);
        levelInfoPane.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), levelInfoPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(e -> {
            levelInfoPane.setVisible(false);
            infoVbox.setVisible(true);
            gameBoard.setVisible(true);
            gameBoard.setFocusTraversable(true);
            SoundHandler.getInstance().addMedia(SoundHandler.STAGE_STAGE_THEME, false);
            Core.getInstance().loop();
        });
        fadeTransition.play();
    }

    public void start(final int GAME_MODE) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.25));
        infoVbox.setVisible(false);
        levelInfoPane.setVisible(false);
        pausePane.setVisible(false);
        gameOverPane.setVisible(false);
        highScorePane.setVisible(false);
        pause.setOnFinished(e -> {
            Core.getInstance().initGameBoard(GAME_MODE);
            Core.getInstance().startLevel();
            earlyStage();
        });
        pause.play();
        //fadeTransition.play();
    }

    public void openPausePane() {
        pausePane.setVisible(true);
        pausePane.toFront();
    }

    public String getName() {
        if (name.getText().equals("")) {
            return "Anonymous";
        } else {
            return name.getText();
        }
    }

    public void onExitClicked() {
        Core.getInstance().exitToMenu();
        pausePane.setVisible(false);
        gameOverPane.setVisible(false);
        highScorePane.setVisible(false);
        gameBoard.setFocusTraversable(false);
        gameBoard.setVisible(false);
        infoVbox.setVisible(false);
        levelInfoPane.setVisible(false);
        SoundHandler.getInstance().reset();
        PaneController.getInstance().switchPane(PaneController.MENU_PANE_ID);
        SoundHandler.getInstance().addMedia(SoundHandler.STAGE_TITLE_SCREEN, true);
    }

    public void onRestartClicked() {
        pausePane.setVisible(false);
        gameOverPane.setVisible(false);
        highScorePane.setVisible(false);
        gameBoard.setFocusTraversable(true);
        gameBoard.setVisible(true);
        infoVbox.setVisible(true);
        levelInfoPane.setVisible(false);
        Core.getInstance().loadNewLevel(Core.getInstance().getLevel(), true);
    }

    public void onContinueClicked() {
        pausePane.setVisible(false);
        gameOverPane.setVisible(false);
        highScorePane.setVisible(false);
        gameBoard.setFocusTraversable(true);
        gameBoard.setVisible(true);
        infoVbox.setVisible(true);
        levelInfoPane.setVisible(false);
        Core.getInstance().loop();
        SoundHandler.getInstance().unpause();
    }

    public void openGameOverPane() {
        pausePane.setVisible(false);
        gameOverPane.setVisible(true);
        highScorePane.setVisible(false);
        gameBoard.setFocusTraversable(false);
        gameBoard.setVisible(true);
        infoVbox.setVisible(true);
        levelInfoPane.setVisible(false);
    }

    public void openHighScorePane() {
        pausePane.setVisible(false);
        gameOverPane.setVisible(false);
        highScorePane.setVisible(true);
        gameBoard.setFocusTraversable(false);
        gameBoard.setVisible(true);
        infoVbox.setVisible(true);
        levelInfoPane.setVisible(false);
    }

    public void onCheckClicked() {
        HighScoreManager.getInstance().saveHighScores();
        onExitClicked();
    }

    public static InGameController getInstance() {
        return instance;
    }

    public void onPlayerPressed(KeyEvent keyEvent) {
        Core.getInstance().addInputEvent(keyEvent);
    }

    public void onPlayerReleased(KeyEvent keyEvent) {
        Core.getInstance().removeInputEvent(keyEvent);
    }

    public Label getLevelLabel() {
        return levelLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getHeartLabel() {
        return heartLabel;
    }

    public Label getBombLabel() {
        return bombLabel;
    }

    public Label getLevelInfoLabel() {
        return levelInfoLabel;
    }

    public Label getLivesInfoLabel() {
        return livesInfoLabel;
    }

    public Label getBombsInfoLabel() {
        return bombsInfoLabel;
    }

    public Label getScoreInfoLabel() {
        return scoreInfoLabel;
    }

    public Canvas getGameBoard() {
        return gameBoard;
    }
}
