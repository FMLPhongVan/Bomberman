package com.phongvan.bomberman.entities.mobs;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.SoundHandler;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Oneal extends Mob {
    public Oneal(double x, double y, Image image) {
        super(x, y, image);
        setX(x * MapHandler.getInstance().getTileSize());
        setY(y * MapHandler.getInstance().getTileSize());
        velocity = 1;
        direction = rand.nextInt(TOTAL_DIR);
        point = 200;
    }

    @Override
    public void update() {
        if (isAlive) {
            int tileSize = MapHandler.getInstance().getTileSize();
            if (getManhattanDistance(Core.getInstance().getPlayer()) <= 5) {
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
                gc.drawImage(SpriteHandler.getInstance().onealDownRight[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            } else {
                gc.drawImage(SpriteHandler.getInstance().onealUpLeft[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            }
        } else {
            gc.drawImage(SpriteHandler.getInstance().onealDead[curFrame / 15].getImage(),
                    x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
        }
    }
}
