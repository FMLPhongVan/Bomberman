package com.phongvan.bomberman.entities.mobs;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.SoundHandler;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Minvo extends Mob {
    public Minvo(double x, double y, Image image) {
        super(x, y, image);
        setX(x * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        setY(y * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        velocity = 1.5;
        direction = RIGHT_DIR + ((rand.nextInt(2) == 0) ? 0 : 2);
        point = 800;
        isAlive = true;
    }

    @Override
    public void update() {
        velX = velY = 0;

        if (isAlive) {
            int tileSize = MapHandler.getInstance().getTileSize();
            if (getManhattanDistance(Core.getInstance().getPlayer()) <= 5) {
                adjustFixedPosition();

                if (x % tileSize == 0 && y % tileSize == 0) {
                    MapHandler.getInstance().findShortestPath(this, Core.getInstance().getPlayer());
                }
                if (direction == UNSET) {
                    direction = rand.nextInt(TOTAL_DIR);
                    makeRandomMove();
                } else {
                    move(false);
                }
            } else {
                if (direction == UNSET) {
                    direction = rand.nextInt(TOTAL_DIR);
                }
                makeRandomMove();
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
                gc.drawImage(SpriteHandler.getInstance().minvoDownRight[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            } else {
                gc.drawImage(SpriteHandler.getInstance().minvoUpLeft[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            }
        } else {
            gc.drawImage(SpriteHandler.getInstance().minvoDead[curFrame / 15].getImage(),
                    x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
        }
    }
}
