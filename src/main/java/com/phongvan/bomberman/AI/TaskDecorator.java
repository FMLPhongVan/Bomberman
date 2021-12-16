package com.phongvan.bomberman.AI;

public abstract class TaskDecorator extends Task {
    protected Task task;

    public TaskDecorator(Data data, Task task) {
        super(data);
        setTask(task);
    }

    public TaskDecorator(Data data, Task task, String name) {
        super(data, name);
        setTask(task);
    }

    public void setTask(Task task) {
        this.task = task;
        this.task.getController().setTask(this);
    }

    @Override
    public void start() {
        task.start();
    }

    @Override
    public void end() {
        task.end();
    }

    @Override
    public boolean checkCondition() {
        return task.checkCondition();
    }

    public TaskController getController() {
        return task.getController();
    }
}
