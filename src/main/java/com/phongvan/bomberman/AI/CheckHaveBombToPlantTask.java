package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Logger;

public class CheckHaveBombToPlantTask extends LeafTask {
    public CheckHaveBombToPlantTask(Data data) {
        super(data);
    }

    public CheckHaveBombToPlantTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return true;
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "CheckHaveBombToPlant", "Start checking if bomber has bomb to plant");
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "CheckHaveBombToPlant", "End checking: " + controller.succeeded());
    }

    @Override
    public void execute() {
        if (data.getPlayer().getPlantedBomb().size() < data.getPlayer().getBombs()) {
            controller.finishWithSuccess();
        } else {
            controller.finishWithFailure();
        }
    }
}
