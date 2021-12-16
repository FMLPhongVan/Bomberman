package com.phongvan.bomberman.entities.mobs;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.SoundHandler;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Kondoria extends Mob {
    public Kondoria(double x, double y, Image image) {
        super(x, y, image);
        setX(x * MapHandler.getInstance().getTileSize());
        setY(y * MapHandler.getInstance().getTileSize());
        velocity = 0.25;
        direction = rand.nextInt(TOTAL_DIR);
        point = 200;
        wallPass = true;
    }

    @Override
    public void update() {
        if (isAlive) {
            int tileSize = MapHandler.getInstance().getTileSize();
            if (x % tileSize == 0 && y % tileSize == 0) {
                MapHandler.getInstance().findShortestPath(this, Core.getInstance().getPlayer());
            }
            if (direction == UNSET) {
                direction = rand.nextInt(TOTAL_DIR);
                makeRandomMove();
            } else {
                move(false);
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
                gc.drawImage(SpriteHandler.getInstance().kondoriaDownRight[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            } else {
                gc.drawImage(SpriteHandler.getInstance().kondoriaUpLeft[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            }
        } else {
            gc.drawImage(SpriteHandler.getInstance().kondoriaDead[curFrame / 15].getImage(),
                    x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
        }
    }
}
