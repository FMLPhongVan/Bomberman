package com.phongvan.bomberman.AI;

public class Sequence extends ParentTask {
    public Sequence(Data data) {
        super(data);
    }

    public Sequence(Data data, String name) {
        super(data, name);
    }

    @Override
    public void childFinishWithSuccess() {
        int curPos = controller.subtasks.indexOf(controller.curTask);
        if (curPos == controller.subtasks.size() - 1) {
            controller.finishWithSuccess();
        } else {
            controller.curTask = controller.subtasks.get(curPos + 1);
            if (!controller.curTask.checkCondition()) {
                controller.finishWithFailure();
            }
        }
    }

    @Override
    public void childFinishWithFailure() {
        controller.finishWithFailure();
    }
}
