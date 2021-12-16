package com.phongvan.bomberman.AI;

public abstract class LeafTask extends Task{
    protected TaskController controller;

    public LeafTask(Data data) {
        super(data);
        controller = new TaskController(this);
    }

    public LeafTask(Data data, String name) {
        super(data, name);
        controller = new TaskController(this);
    }

    public TaskController getController() {
        return controller;
    }
}
