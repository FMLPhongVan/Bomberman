package com.phongvan.bomberman.entities.powerups;

import com.phongvan.bomberman.entities.Entities;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;

public abstract class PowerUp extends Entities {
    public PowerUp(double x, double y) {
        super(x, y, null);
        setX(x * MapHandler.getInstance().getTileSize());
        setY(y * MapHandler.getInstance().getTileSize());
    }

    public void update() {

    }

    public abstract void update(Bomber bomber);
}
