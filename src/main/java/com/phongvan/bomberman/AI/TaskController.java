package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Logger;

public class TaskController {
    protected boolean finish;
    protected boolean success;
    protected boolean started;
    protected Task task;

    public TaskController(Task task) {
        this.task = task;
        finish = false;
        success = true;
        started = false;
    }

    public void start() {
        started = true;
        task.start();
    }

    public void end() {
        finish = false;
        started = false;
        task.end();
    }

    public void finishWithSuccess() {
        success = true;
        finish = true;
        //Logger.log(Logger.INFO, "TaskController", "Finished with success");
    }

    public void finishWithFailure() {
        success = false;
        finish = true;
        //Logger.log(Logger.INFO, "TaskController", "Finished with failure");
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void reset() {
        success = true;
        finish = false;
    }

    public boolean finished() {
        return finish;
    }

    public boolean succeeded() {
        return success;
    }

    public boolean failed() {
        return !success;
    }

    public boolean isStarted() {
        return started;
    }
}
