package com.phongvan.bomberman.AI;

import java.util.Random;

public class RandomDecorator extends TaskDecorator {
    private double rate;
    private Random rand;

    public RandomDecorator(Data data, Task task, String name, double rate) {
        super(data, task, name);
        this.rate = rate;
        rand = new Random();
    }

    @Override
    public boolean checkCondition() {
        return super.checkCondition() && (rand.nextDouble() % 100 < rate);
    }

    @Override
    public void execute() {
        task.execute();
    }
}
