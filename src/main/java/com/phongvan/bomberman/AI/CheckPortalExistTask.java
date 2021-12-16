package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.entities.destructible.Portal;
import com.phongvan.bomberman.map.MapHandler;

public class CheckPortalExistTask extends LeafTask {
    public CheckPortalExistTask(Data data) {
        super(data);
    }

    public CheckPortalExistTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return Core.getInstance().getMode() == Core.STORY_MODE;
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "CheckPortalExist", "Start checking if the portal exists");
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "CheckPortalExist", "End checking :" + controller.succeeded());
    }

    @Override
    public void execute() {
        int tileX = MapHandler.getInstance().getPortalX();
        int tileY = MapHandler.getInstance().getPortalY();

        if (Core.getInstance().getTile(tileY, tileX) instanceof Portal) {
            controller.finishWithSuccess();
            return;
        }

        controller.finishWithFailure();
    }
}
