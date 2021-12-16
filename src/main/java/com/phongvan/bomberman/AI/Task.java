package com.phongvan.bomberman.AI;

public abstract class Task {
    protected String name;
    protected Data data;

    public Task(Data data) {
        this.data = data;
    }

    public Task(Data data, String name) {
        this.name = name;
        this.data = data;
    }

    public abstract boolean checkCondition();

    public abstract void start();

    public abstract void end();

    public abstract void execute();

    public abstract TaskController getController();
}
