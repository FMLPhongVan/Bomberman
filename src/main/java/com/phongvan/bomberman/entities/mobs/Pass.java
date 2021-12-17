package com.phongvan.bomberman.entities.mobs;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.SoundHandler;
import com.phongvan.bomberman.entities.bomb.Bomb;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Pass extends Mob {
    private int chasingTime = 0;
    public static final int DEFAULT_CHASE_TIME = 240;

    public Pass(double x, double y, Image image) {
        super(x, y, image);
        setX(x * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        setY(y * MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize());
        velocity = 1.5;
        direction = rand.nextInt(TOTAL_DIR);
        point = 4000;
        wallPass = false;
        bombPass = true;
        isAlive = true;
    }

    @Override
    public void update() {
        velX = velY = 0;
        if (isAlive) {
            int tileSize = MapHandler.getInstance().getTileSize();
            adjustFixedPosition();
            if (chasingTime > 0) {
                --chasingTime;

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
                if (x % tileSize == 0 && y % tileSize == 0 && rand.nextInt(10) < 2) {
                    chasingTime = DEFAULT_CHASE_TIME;
                } else {
                    makeRandomMove();
                }
            }

            int tileX = (int) ((x + tileSize / 2) / tileSize);
            int tileY = (int) ((y + tileSize / 2) / tileSize);

            for (int i = 0; i < Core.getInstance().getPlayer().getPlantedBomb().size(); ++i) {
                Bomb copy = Core.getInstance().getPlayer().getPlantedBomb().get(i);
                if ((int) (copy.getX() / tileSize) == tileX && (int) (copy.getY() / tileSize) == tileY) {
                    Core.getInstance().getPlayer().getPlantedBomb().remove(i);
                    break;
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
                gc.drawImage(SpriteHandler.getInstance().passDownRight[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            } else {
                gc.drawImage(SpriteHandler.getInstance().passUpLeft[curFrame / 15].getImage(),
                        x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
            }
        } else {
            gc.drawImage(SpriteHandler.getInstance().passDead[curFrame / 15].getImage(),
                    x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
        }
    }
}
