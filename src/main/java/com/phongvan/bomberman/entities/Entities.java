package com.phongvan.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Entities {
    protected int x;
    protected int y;
    protected Image image = null;

    public Entities(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Entities(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void render(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(image, x, y);
    }
}
