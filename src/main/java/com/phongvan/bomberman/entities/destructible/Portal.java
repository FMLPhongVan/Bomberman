package com.phongvan.bomberman.entities.destructible;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.entities.AnimatedEntities;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;

public class Portal extends AnimatedEntities {
    public Portal(double x, double y) {
        super(x, y, SpriteHandler.getInstance().portal.getImage());
        setX(x * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        setY(y * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
    }

    @Override
    public void update() {
        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX = (int) (Core.getInstance().getPlayer().getX() / tileSize);
        int tileY = (int) (Core.getInstance().getPlayer().getY() / tileSize);

        if (tileX == (int) (x / tileSize) && tileY == (int) (y / tileSize)) {
            if (Core.getInstance().killAllEnemies()) {
                Core.getInstance().loadNewStage(Core.getInstance().getLevel() + 1);
                System.out.println(1);
            }
        }
    }
}
