package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Logger;

public class CheckAllEnemyDied extends LeafTask {
    public CheckAllEnemyDied(Data data) {
        super(data);
    }

    public CheckAllEnemyDied(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return true;
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "CheckAllEnemyDied", "Start checking if all enemy died");
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "CheckAllEnemyDied", "End checking: " + controller.succeeded());
    }

    @Override
    public void execute() {
        if (data.getBomberEnemies().isEmpty() && data.getEnemies().isEmpty()) {
            controller.finishWithSuccess();
        } else {
            controller.finishWithFailure();
        }
    }
}
