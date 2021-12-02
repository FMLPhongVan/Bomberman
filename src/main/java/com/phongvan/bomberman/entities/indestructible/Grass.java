package com.phongvan.bomberman.entities.indestructible;

import com.phongvan.bomberman.entities.Entities;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.image.Image;

public class Grass extends Entities {
    public Grass(double x, double y) {
        super(x, y, SpriteHandler.getInstance().grass.getImage());
        setX(x * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        setY(y * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
    }

    @Override
    public void update() {

    }
}
