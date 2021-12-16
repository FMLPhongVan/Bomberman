package com.phongvan.bomberman.AI;

public class Selector extends ParentTask {
    public Selector(Data data) {
        super(data);
    }

    public Selector(Data data, String name) {
        super(data, name);
    }

    public Task chooseNewTask() {
        Task task = null;
        int curTaskPos = controller.subtasks.indexOf(controller.curTask);
        while (curTaskPos < controller.subtasks.size() - 1) {
            ++curTaskPos;
            if (controller.subtasks.elementAt(curTaskPos).checkCondition()) {
                task = controller.subtasks.get(curTaskPos);
                break;
            }
        }

        return task;
    }

    @Override
    public void childFinishWithSuccess() {
        controller.finishWithSuccess();
    }

    @Override
    public void childFinishWithFailure() {
        controller.curTask = chooseNewTask();
        if (controller.curTask == null) {
            controller.finishWithFailure();
        }
    }
}
