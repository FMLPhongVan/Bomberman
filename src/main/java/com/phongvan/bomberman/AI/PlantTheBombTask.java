package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.map.MapHandler;

public class PlantTheBombTask extends LeafTask {
    public PlantTheBombTask(Data data) {
        super(data);
    }

    public PlantTheBombTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX = (int) ((data.getPlayer().getX() + tileSize / 2) / tileSize);
        int tileY = (int) ((data.getPlayer().getY() + tileSize / 2) / tileSize);
        return !Core.getInstance().thisTileHadBomb(tileX, tileY);
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "PlantTheBomb",
                "Plant the bomb at " + data.getPlayer().getX() + " " + data.getPlayer().getY());
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "PlantTheBomb", "Finish");
    }

    @Override
    public void execute() {
        data.getPlayer().setPlantBomb(true);
        controller.finishWithSuccess();
    }
}
