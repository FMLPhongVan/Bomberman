package com.phongvan.bomberman.gui;

import com.phongvan.bomberman.Logger;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private static MenuController instance;
    @FXML
    private AnchorPane menuPane;

    @FXML
    public ChoiceBox<String> choiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        menuPane.setPrefWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.DEFAULT_APP_SCALE_SIZE);
        menuPane.setPrefHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.DEFAULT_APP_SCALE_SIZE);

        ObservableList<String> list = FXCollections.observableArrayList("1", "2", "3", "4");
        choiceBox.setItems(list);

        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newVal) {
                if (newVal != null) {
                    PaneController.getInstance().setScaleSize(Integer.parseInt(newVal));
                    PaneController.getInstance().resizePane();
                    System.out.println(newVal);
                }
            }
        };

        choiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public static MenuController getInstance() {
        return instance;
    }

    public void resizePane() {
        menuPane.setPrefWidth(PaneController.DEFAULT_APP_WIDTH * PaneController.getInstance().getScaleSize());
        menuPane.setPrefHeight(PaneController.DEFAULT_APP_HEIGHT * PaneController.getInstance().getScaleSize());
    }

    public void onNewGameClicked(MouseEvent mouseEvent) {
        PaneController.getInstance().switchPane(PaneController.IN_GAME_PANE_ID);
        InGameController.getInstance().start();
        Logger.log(Logger.INFO, "MenuController", "Create a new game");
    }
}
