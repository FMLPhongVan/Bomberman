package com.phongvan.bomberman.entities;

import javafx.scene.image.Image;

public abstract class AnimatedEntities extends Entities {
    protected int curFrame = 0;
    public static final int MAX_FRAME = 29;

    public AnimatedEntities(double x, double y) {
        super(x, y);
    }

    public AnimatedEntities(double x, double y, Image image) {
        super(x, y, image);
    }

    public void animate() {
        if (curFrame < MAX_FRAME)
            ++curFrame;
        else
            curFrame = 0;
    }

    public abstract void update();
}
