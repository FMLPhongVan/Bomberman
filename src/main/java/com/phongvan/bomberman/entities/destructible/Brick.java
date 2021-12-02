package com.phongvan.bomberman.entities.destructible;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.entities.AnimatedEntities;
import com.phongvan.bomberman.entities.indestructible.Grass;
import com.phongvan.bomberman.graphics.Sprite;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;

import java.util.Map;

public class Brick extends AnimatedEntities {
    public Brick(int x, int y) {
        super(x, y, null);
        setX(x * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        setY(y * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        setAlive(true);
    }

    @Override
    public void update() {
        if (!isAlive)
            animate();

        if (curFrame == 35) {
            int tileSize = MapHandler.getInstance().getTileSize();
            if (MapHandler.getInstance().isPortalPosition((int) x / tileSize, (int) y / tileSize)) {
                MapHandler.getInstance().setTile((int) x / tileSize, (int) y / tileSize, MapHandler.PORTAL_TILE);
                Core.getInstance().addNewTile(new Portal((int) x / tileSize, (int) y / tileSize));
            } else {
                MapHandler.getInstance().setTile((int) x / tileSize, (int) y / tileSize, MapHandler.GRASS_TILE);
                Core.getInstance().addNewTile(new Grass((int) x / tileSize, (int) y / tileSize));
            }
        }
    }

    @Override
    public void animate() {
        if (curFrame < 35) {
            ++curFrame;
        }
    }

    public void render(GraphicsContext gc) {
        double renderX = x - Camera.getInstance().getOffsetX();
        double renderY = y - Camera.getInstance().getOffsetY();

        if (isAlive) {
            gc.drawImage(SpriteHandler.getInstance().bricks[0].getImage(), renderX, renderY);
        } else {
            gc.drawImage(SpriteHandler.getInstance().bricks[(curFrame / 12) + 1].getImage(), renderX, renderY);
        }
    }
}
