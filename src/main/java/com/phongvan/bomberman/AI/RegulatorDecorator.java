package com.phongvan.bomberman.AI;

public class RegulatorDecorator extends TaskDecorator {
    private double updateTime;
    private double lastUpdateTime;

    public RegulatorDecorator(Data data, Task task, String name, double updateTime) {
        super(data, task, name);
        this.updateTime = updateTime;
        lastUpdateTime = System.nanoTime();
    }

    @Override
    public void start() {
        super.start();
        lastUpdateTime = System.nanoTime();
    }

    @Override
    public void execute() {
        if (System.nanoTime() - lastUpdateTime > updateTime * 1000000000) {
            task.execute();
            lastUpdateTime = System.nanoTime();
        }
    }
}
