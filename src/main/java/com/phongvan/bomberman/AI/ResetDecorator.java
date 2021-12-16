package com.phongvan.bomberman.AI;

public class ResetDecorator extends TaskDecorator {

    public ResetDecorator(Data data, Task task) {
        super(data, task);
    }

    public ResetDecorator(Data data, Task task, String name) {
        super(data, task, name);
    }

    @Override
    public void execute() {
        task.execute();
        if (task.getController().finished()) {
            task.getController().reset();
        }
    }
}
