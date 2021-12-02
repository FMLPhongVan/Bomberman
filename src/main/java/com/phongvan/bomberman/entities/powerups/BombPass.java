package com.phongvan.bomberman.entities.powerups;

import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.graphics.SpriteHandler;
import javafx.scene.canvas.GraphicsContext;

public class BombPass extends PowerUp {
    public BombPass(double x, double y) {
        super(x, y);
    }

    @Override
    public void update(Bomber bomber) {

    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(SpriteHandler.getInstance().bombPass.getImage(), x, y);
    }
}
