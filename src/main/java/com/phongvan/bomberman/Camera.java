package com.phongvan.bomberman;

import com.phongvan.bomberman.gui.InGameController;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;

public class Camera {
    private static Camera instance = null;
    private double offsetX;
    private double offsetY;
    private double camWidth;
    private double camHeight;

    private Camera() {
        offsetX = 0.0;
        offsetY = 0.0;
    }

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }

        return instance;
    }

    public void setCameraProperty() {
        camWidth = InGameController.getInstance().getGameBoard().getWidth();
        camHeight = InGameController.getInstance().getGameBoard().getHeight();
    }

    public void adjustCameraPosition() {
        double playerPosX = Core.getInstance().getPlayer().getX();
        double playerPosY = Core.getInstance().getPlayer().getY();

        double playerOffsetX = MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize();
        double playerOffsetY = MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize();

        offsetX = (playerPosX + playerOffsetX / 2) - InGameController.getInstance().getGameBoard().getWidth() / 2;
        offsetY = (playerPosY + playerOffsetY / 2) - InGameController.getInstance().getGameBoard().getHeight() / 2;

        if (offsetX < 0) {
            offsetX = 0;
        }

        if (offsetY < 0) {
            offsetY = 0;
        }

        if (offsetX > MapHandler.getInstance().getMapWidth() - camWidth) {
            offsetX = MapHandler.getInstance().getMapWidth() - camWidth;
        }

        if (offsetY > MapHandler.getInstance().getMapHeight() - camHeight) {
            offsetY = MapHandler.getInstance().getMapHeight() - camHeight;
        }
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }
}
