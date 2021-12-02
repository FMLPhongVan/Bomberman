package com.phongvan.bomberman.entities;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Entities {
    protected double x;
    protected double y;
    protected boolean isAlive;
    protected Image image = null;

    public Entities(double x, double y) {
        this.x = x;
        this.y = y;
        isAlive = true;
    }

    public Entities(double x, double y, Image image) {
        this.x = x;
        this.y = y;
        isAlive = true;
        this.image = image;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setAlive(boolean state) {
        this.isAlive = state;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Image getImage() {
        return image;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
    }

    public abstract void update();

    public boolean checkCollision(Entities other) {
        double leftA = x, rightA = x + MapHandler.getInstance().getTileSize();
        double topA = y, bottomA = y + MapHandler.getInstance().getTileSize();

        double leftB = other.x, rightB = other.x + MapHandler.getInstance().getTileSize();
        double topB = other.y, bottomB = other.y + MapHandler.getInstance().getTileSize();

        if (bottomA <= topB) {
            return false;
        }

        if (topA >= bottomB) {
            return false;
        }

        if (rightA <= leftB) {
            return false;
        }

        if (leftA >= rightB) {
            return false;
        }

        return true;
    }
}
