package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.entities.powerups.PowerUp;
import com.phongvan.bomberman.map.MapHandler;

public class CheckItemExistTask extends LeafTask {
    public CheckItemExistTask(Data data) {
        super(data);
    }

    public CheckItemExistTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return true;
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "CheckItemExistTask", "Start checking if item exists");
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "CheckItemExistTask", "End checking :" + controller.succeeded());
    }

    @Override
    public void execute() {
        if (data.getPowerUps().isEmpty()) {
            controller.finishWithFailure();
            return;
        }

        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX, tileY;
        for (PowerUp powerUp : data.getPowerUps()) {
            tileX = (int) (powerUp.getX() / tileSize);
            tileY = (int) (powerUp.getY() / tileSize);

            if (MapHandler.getInstance().getTileType(tileX, tileY) != MapHandler.BRICKS_TILE) {
                controller.finishWithSuccess();
                return;
            }
        }

        controller.finishWithFailure();
    }
}
