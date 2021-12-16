package com.phongvan.bomberman.entities.mobs;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.SoundHandler;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Doll extends Mob {
    public Doll(double x, double y, Image image) {
        super(x, y, image);
        setX(x * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        setY(y * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        velocity = 1;
        direction = RIGHT_DIR + ((rand.nextInt(2) == 0) ? 0 : 2);
        point = 400;
        isAlive = true;
    }

    @Override
    public void update() {
        velX = velY = 0;
        if (isAlive) {
            switch (direction) {
                case UP_DIR -> velY = (-velocity) * PaneController.getInstance().getScaleSize();
                case RIGHT_DIR -> velX = velocity * PaneController.getInstance().getScaleSize();
                case DOWN_DIR -> velY = velocity * PaneController.getInstance().getScaleSize();
                case LEFT_DIR -> velX = (-velocity) * PaneController.getInstance().getScaleSize();
            }

            int tileSize = MapHandler.getInstance().getTileSize();
            int tileX, tileY;
            if (MapHandler.getInstance().canMoveTo(direction, x + velX, y + velY, wallPass, bombPass, false)) {
                x += velX;
                y += velY;
            } else {
                if (x % tileSize == 0 && y % tileSize == 0) {
                    if (rand.nextInt(10) == 0) {
                        direction = rand.nextInt(TOTAL_DIR);
                    } else {
                        direction = (direction + 2) % TOTAL_DIR;
                    }
                } else {
                    direction = (direction + 2) % TOTAL_DIR;
                }
            }

            Core.getInstance().checkCollideBomber(this);
        }

        animate();
    }

    @Override
    public void animate() {
        if (isAlive) {
            curFrame = (curFrame < 44) ? curFrame + 1 : 0;
        } else {
            ++curFrame;
            if (curFrame == 59) {
                Core.getInstance().deleteMob(this);
            }
        }
    }

    @Override
    public void kill() {
        isAlive = false;
        curFrame = 0;
        SoundHandler.getInstance().addMedia(SoundHandler.EFFECT_DEATH, false);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isAlive) {
            if (direction == 1 || direction == 2) {
                gc.drawImage(SpriteHandler.getInstance().dollDownRight[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            } else {
                gc.drawImage(SpriteHandler.getInstance().dollUpLeft[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            }
        } else {
            gc.drawImage(SpriteHandler.getInstance().dollDead[curFrame / 15].getImage(),
                    x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
        }
    }
}
