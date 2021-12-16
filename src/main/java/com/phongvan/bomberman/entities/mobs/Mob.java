package com.phongvan.bomberman.entities.mobs;

import com.phongvan.bomberman.entities.AnimatedEntities;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.image.Image;

import java.util.Random;

public abstract class Mob extends AnimatedEntities {
    protected double velocity;
    protected double velX;
    protected double velY;
    protected double targetX;
    protected double targetY;
    protected int direction;
    protected int point;
    protected boolean wallPass;
    protected boolean bombPass;
    protected Random rand = new Random();

    public static final int UNSET = -1;
    public static final int UP_DIR = 0;
    public static final int RIGHT_DIR = 1;
    public static final int DOWN_DIR = 2;
    public static final int LEFT_DIR = 3;
    public static final int TOTAL_DIR = 4;

    public Mob(double x, double y, Image image) {
        super(x, y, image);
        velocity = 1.0;
        velX = velY = 0.0;
        targetX = UNSET;
        targetY = UNSET;
        direction = UNSET;
        point = 0;
        wallPass = false;
        bombPass = false;
    }

    /**
     * This method adjusts x, y if mob's velocity is 1.5.
     */
    public void adjustFixedPosition() {
        if (velocity != 1.5) return;

        int tileSize = MapHandler.getInstance().getTileSize();
        int scale = PaneController.getInstance().getScaleSize();
        int tileX = (int) ((x + (double) tileSize / 2) / tileSize);
        int tileY = (int) ((y + (double) tileSize / 2) / tileSize);

        if (Math.abs(x / (1.0 * scale) - tileX * 16.0) <= 1) {
            x = tileSize * tileX;
        }

        if (Math.abs(y / (1.0 * scale) - tileY * 16.0) <= 1) {
            y = tileSize * tileY;
        }
    }

    public void move(boolean isBomber) {
        velX = velY = 0;
        if (direction == UNSET) return;
        switch (direction) {
            case UP_DIR -> velY = (-velocity) * PaneController.getInstance().getScaleSize();
            case RIGHT_DIR -> velX = velocity * PaneController.getInstance().getScaleSize();
            case DOWN_DIR -> velY = velocity * PaneController.getInstance().getScaleSize();
            case LEFT_DIR -> velX = (-velocity) * PaneController.getInstance().getScaleSize();
        }

        if (MapHandler.getInstance().canMoveTo(direction, x + velX, y + velY, wallPass, bombPass, isBomber)) {
            x += velX;
            y += velY;
        } else {
            int tileSize = MapHandler.getInstance().getTileSize();
            x = ((int) ((x + (double) tileSize / 2) / tileSize)) * tileSize;
            y = ((int) ((y + (double) tileSize / 2) / tileSize)) * tileSize;
        }
    }

    public void makeRandomMove() {
        velX = velY = 0;
        switch (direction) {
            case UP_DIR -> velY = (-velocity) * PaneController.getInstance().getScaleSize();
            case RIGHT_DIR -> velX = velocity * PaneController.getInstance().getScaleSize();
            case DOWN_DIR -> velY = velocity * PaneController.getInstance().getScaleSize();
            case LEFT_DIR -> velX = (-velocity) * PaneController.getInstance().getScaleSize();
        }

        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX = (int) ((x + (double) tileSize / 2) / tileSize);
        int tileY = (int) ((y + (double) tileSize / 2) / tileSize);

        if (MapHandler.getInstance().canMoveTo(direction, x + velX, y + velY, wallPass, bombPass, false)) {
            x += velX;
            y += velY;
            adjustFixedPosition();

            if (x % tileSize == 0 && y % tileSize == 0) {
                tileX = (int) (x / tileSize);
                tileY = (int) (y / tileSize);
                if (MapHandler.getInstance().isForkTile(tileX, tileY)
                        || MapHandler.getInstance().isCrossTile(tileX, tileY)) {
                    if (rand.nextInt(2) == 1) {
                        direction = rand.nextInt(TOTAL_DIR);
                    }
                }
            }
        } else {
            adjustFixedPosition();

            if (x % tileSize == 0 && y % tileSize == 0) {
                tileX = (int) (x / tileSize);
                tileY = (int) (y / tileSize);
                if (MapHandler.getInstance().countPossibleWays(tileX, tileY) > 1) {
                    direction = (direction + 1) % TOTAL_DIR;
                    boolean way1 = MapHandler.getInstance().canTurn(tileX, tileY, direction, wallPass);
                    boolean way2 = MapHandler.getInstance().canTurn(tileX, tileY, (direction + 2) % TOTAL_DIR, wallPass);
                    int random;
                    if (way1 && way2) {
                        random = rand.nextInt(3);
                        switch (random) {
                            case 1 -> direction = (direction + 2) % TOTAL_DIR;
                            case 2 -> direction = (direction + 1) % TOTAL_DIR;
                        }
                    } else if (way1 && !way2) {
                        random = rand.nextInt(2);
                        if (random == 1) {
                            direction = (direction + 1) % TOTAL_DIR;
                        }
                    } else if (!way1 && way2) {
                        random = rand.nextInt(2);
                        switch (random) {
                            case 0 -> direction = (direction + 2) % TOTAL_DIR;
                            case 1 -> direction = (direction + 1) % TOTAL_DIR;
                        }
                    } else {
                        direction = (direction + 1) % TOTAL_DIR;
                    }
                } else {
                    direction = (direction + 1) % TOTAL_DIR;
                }
            } else {
                direction = (direction + 2) % TOTAL_DIR;
            }
        }
    }

    public int getManhattanDistance(Mob other) {
        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX = (int) (x + tileSize / 2) / tileSize;
        int tileY = (int) (y + tileSize / 2) / tileSize;
        int otherTileX = (int) (other.x + tileSize / 2) / tileSize;
        int otherTileY = (int) (other.y + tileSize / 2) / tileSize;
        return Math.abs(tileX - otherTileX) + Math.abs(tileY - otherTileY);
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void setVel(double velX, double velY) {
        this.velX = velX;
        this.velY = velY;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setTargetPosition(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void setWallPass(boolean state) {
        wallPass = state;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public int getDirection() {
        return direction;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public int getPoint() {
        return point;
    }

    public boolean canWallPass() {
        return wallPass;
    }

    public boolean canBombPass() {
        return bombPass;
    }

    public abstract void update();

    public abstract void kill();

}
