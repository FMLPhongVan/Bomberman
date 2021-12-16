package com.phongvan.bomberman.AI;

import java.util.Vector;

public class ParentTaskController extends TaskController {
    protected Task curTask;
    protected Vector<Task> subtasks;

    public ParentTaskController(Task task) {
        super(task);
        curTask = null;
        subtasks = new Vector<>();
    }

    public void addTask(Task task) {
        subtasks.add(task);
    }

    public void reset() {
        super.reset();
        curTask = subtasks.firstElement();
    }

    public void setCurTask(Task task) {
        curTask = task;
    }

    public Task getCurTask() {
        return curTask;
    }
}
