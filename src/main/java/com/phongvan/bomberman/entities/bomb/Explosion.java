package com.phongvan.bomberman.entities.bomb;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.entities.AnimatedEntities;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;

public class Explosion extends AnimatedEntities {
    private int type;
    private Bomber bomber;

    public static final int CENTER = 0;
    public static final int MIDDLE_HORIZONTAL = 1;
    public static final int MIDDLE_VERTICAL = 2;
    public static final int HEAD_UP = 3;
    public static final int HEAD_DOWN = 4;
    public static final int HEAD_RIGHT = 5;
    public static final int HEAD_LEFT = 6;

    public Explosion(Bomber bomber, double x, double y, int type) {
        super(x, y);
        this.type = type;
        setX(x * MapHandler.getInstance().getTileSize());
        setY(y * MapHandler.getInstance().getTileSize());
        isAlive = true;
        this.bomber = bomber;
    }

    @Override
    public void update() {
        Core.getInstance().checkExplosionCollision(this);
        animate();
    }

    @Override
    public void animate() {
        if (curFrame < 29) {
            ++curFrame;
        } else {
            isAlive = false;
        }

    }

    public void render(GraphicsContext gc) {
        int id;
        if (curFrame < 15) id = curFrame / 5;
        else id = (29 - curFrame) / 5;

        double renderX = x - Camera.getInstance().getOffsetX();
        double renderY = y - Camera.getInstance().getOffsetY();

        switch (type) {
            case CENTER -> gc.drawImage(SpriteHandler.getInstance().explodeCenter[id].getImage(), renderX, renderY);
            case MIDDLE_HORIZONTAL -> gc.drawImage(SpriteHandler.getInstance().explodeMiddleHorizontal[id].getImage(), renderX, renderY);
            case MIDDLE_VERTICAL -> gc.drawImage(SpriteHandler.getInstance().explodeMiddleVertical[id].getImage(), renderX, renderY);
            case HEAD_UP -> gc.drawImage(SpriteHandler.getInstance().explodeHeadUp[id].getImage(), renderX, renderY);
            case HEAD_RIGHT -> gc.drawImage(SpriteHandler.getInstance().explodeHeadRight[id].getImage(), renderX, renderY);
            case HEAD_DOWN -> gc.drawImage(SpriteHandler.getInstance().explodeHeadDown[id].getImage(), renderX, renderY);
            case HEAD_LEFT -> gc.drawImage(SpriteHandler.getInstance().explodeHeadLeft[id].getImage(), renderX, renderY);
        }
    }

    public Bomber getBomber() {
        return bomber;
    }
}
