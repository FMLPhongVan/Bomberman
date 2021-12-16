package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Logger;

public abstract class ParentTask extends Task {
    protected ParentTaskController controller;

    public ParentTask(Data data) {
        super(data);
        controller = new ParentTaskController(this);
    }

    public ParentTask(Data data, String name) {
        super(data, name);
        controller = new ParentTaskController(this);
    }

    public abstract void childFinishWithSuccess();

    public abstract void childFinishWithFailure();

    @Override
    public boolean checkCondition() {
        return !controller.subtasks.isEmpty();
    }

    @Override
    public void execute() {
        if (controller.finished()) return;

        if (controller.getCurTask() == null) {
            Logger.log(Logger.WARN, "ParentTaskController: " + name, "Null current task");
            return;
        }

        if (!controller.getCurTask().getController().isStarted()) {
            controller.getCurTask().getController().start();
        } else if (controller.getCurTask().getController().finished()) {
            controller.getCurTask().getController().end();

            if (controller.getCurTask().getController().succeeded()) {
                childFinishWithSuccess();
            } else {
                childFinishWithFailure();
            }
        } else {
            controller.getCurTask().execute();
        }
    }

    @Override
    public void start() {
        controller.curTask = controller.subtasks.firstElement();
        if (controller.curTask == null) {
            Logger.log(Logger.WARN, "ParentTaskController: " + name, "Null current task");
        }
    }

    @Override
    public void end() {

    }

    @Override
    public TaskController getController() {
        return controller;
    }
}
