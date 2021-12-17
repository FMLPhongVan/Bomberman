package com.phongvan.bomberman.gui;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.HighScoreManager;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.SoundHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private static MenuController instance;
    public ListView<String> storyModeHighScoreList;
    public ListView<String> battleModeHighScoreList;

    @FXML
    private Slider volumeSlider;
    @FXML
    private ImageView background;
    @FXML
    private AnchorPane optionPane;

    @FXML
    private AnchorPane highScorePane;

    @FXML
    private AnchorPane selectPane;

    @FXML
    private AnchorPane menuPane;

    @FXML
    private ChoiceBox<String> choiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        menuPane.setPrefWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.DEFAULT_APP_SCALE_SIZE);
        menuPane.setPrefHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.DEFAULT_APP_SCALE_SIZE);
        background.setFitWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.DEFAULT_APP_SCALE_SIZE);
        background.setFitHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.DEFAULT_APP_SCALE_SIZE);

        ObservableList<String> list = FXCollections.observableArrayList("1024 x 576", "1536 x 864");
        choiceBox.setItems(list);

        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newVal) {
                if (newVal != null) {
                    String[] size = newVal.split("x");
                    for (int i = 0; i < size.length; ++i) {
                        size[i] = size[i].trim();
                    }
                    PaneController.getInstance().setScaleSize(Integer.parseInt(size[0]) / PaneController.DEFAULT_APP_WIDTH);
                    PaneController.getInstance().resizePane();
                    System.out.println(newVal);
                }
            }
        };

        choiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);

        volumeSlider.valueProperty().addListener((observableValue, oldVal, newVal) -> {
            SoundHandler.getInstance().changeVolume(newVal);
        });

        openPane(selectPane);
    }

    public void resizePane() {
        menuPane.setPrefWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.getInstance().getScaleSize());
        menuPane.setPrefHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.getInstance().getScaleSize());
        background.setFitWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.getInstance().getScaleSize());
        background.setFitHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.getInstance().getScaleSize());

        selectPane.setLayoutX(menuPane.getPrefWidth() / 2 - selectPane.getPrefWidth() / 2);
        selectPane.setLayoutY(105 * PaneController.getInstance().getScaleSize());

        optionPane.setLayoutX(menuPane.getPrefWidth() / 2 - optionPane.getPrefWidth() / 2);
        optionPane.setLayoutY(105 * PaneController.getInstance().getScaleSize());

        highScorePane.setLayoutX(menuPane.getPrefWidth() / 2 - highScorePane.getPrefWidth() / 2);
        highScorePane.setLayoutY(105 * PaneController.getInstance().getScaleSize());
    }

    public void openPane(AnchorPane pane) {
        for (Node node : menuPane.getChildren()) {
            if (node.getId().contains("Pane")) {
                node.visibleProperty().set(false);
            }
        }

        pane.visibleProperty().set(true);
    }

    public void onExitGameClicked() {
        HighScoreManager.getInstance().printScoreData();
        Logger.log(Logger.INFO, "MenuController", "Exiting game");
        System.exit(0);
    }

    public void onHighScoreClicked() {
        HighScoreManager.getInstance().getHighScores(storyModeHighScoreList);
        openPane(highScorePane);
    }

    public void onOptionClicked() {
        openPane(optionPane);
    }

    public void onCloseClicked() {
        openPane(selectPane);
    }

    public void onBattleModeClicked() {
        PaneController.getInstance().switchPane(PaneController.IN_GAME_PANE_ID);
        InGameController.getInstance().start(Core.BATTLE_MODE);
        Logger.log(Logger.INFO, "MenuController", "Battle Mode: new game");
    }

    public void onStoryModeClicked() {
        PaneController.getInstance().switchPane(PaneController.IN_GAME_PANE_ID);
        InGameController.getInstance().start(Core.STORY_MODE);
        Logger.log(Logger.INFO, "MenuController", "Story Mode: new game");
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public static MenuController getInstance() {
        return instance;
    }
}
