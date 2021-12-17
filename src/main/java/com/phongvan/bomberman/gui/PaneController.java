package com.phongvan.bomberman.gui;

import com.phongvan.bomberman.Bomberman;
import com.phongvan.bomberman.HighScoreManager;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.SoundHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaneController implements Initializable {
    private static PaneController instance;
    @FXML
    private AnchorPane root;
    private int scaleSize = 2;

    public static final int DEFAULT_APP_WIDTH = 512,
                            DEFAULT_APP_HEIGHT = 288;
    public static final int DEFAULT_APP_SCALE_SIZE = 2;

    public static final String MENU_PANE_ID    = "menuPane",
                               IN_GAME_PANE_ID = "inGamePane",
                               PAUSE_PANE_ID   = "pausePane";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        root.setPrefWidth(DEFAULT_APP_WIDTH * DEFAULT_APP_SCALE_SIZE);
        root.setPrefHeight(DEFAULT_APP_HEIGHT * DEFAULT_APP_SCALE_SIZE);
        switchPane(MENU_PANE_ID);
        SoundHandler.getInstance().addMedia(SoundHandler.STAGE_TITLE_SCREEN, true);
        HighScoreManager.getInstance().loadSavedScores();
    }

    public void setScaleSize(int scaleSize) {
        this.scaleSize = scaleSize;
    }

    public void switchPane(final String paneID) {
        for (Node node : root.getChildren()) {
            if (node.getId() != null && node.getId().equals(paneID))
                node.visibleProperty().set(true);
            else node.visibleProperty().set(false);
        }
    }

    public void resizePane() {
        try {
            Bomberman.changeSize();
        } catch (IOException e) {
            Logger.log(Logger.ERROR, "PaneController", e.getMessage());
            e.printStackTrace();
        }
        root.setPrefWidth(DEFAULT_APP_WIDTH * scaleSize);
        root.setPrefHeight(DEFAULT_APP_HEIGHT * scaleSize);
        MenuController.getInstance().resizePane();
        InGameController.getInstance().resizePane();
    }

    public static PaneController getInstance() {
        return instance;
    }

    public int getScaleSize() {
        return scaleSize;
    }
}
