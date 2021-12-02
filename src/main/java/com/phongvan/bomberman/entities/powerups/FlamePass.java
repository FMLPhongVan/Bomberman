package com.phongvan.bomberman.entities.powerups;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.graphics.SpriteHandler;
import javafx.scene.canvas.GraphicsContext;

public class FlamePass extends PowerUp {
    public FlamePass(double x, double y) {
        super(x, y);
    }

    @Override
    public void update(Bomber bomber) {
        bomber.setFlamePass(true);
    }

    @Override
    public void render(GraphicsContext gc) {
        double renderX = x - Camera.getInstance().getOffsetX();
        double renderY = y - Camera.getInstance().getOffsetY();

        gc.drawImage(SpriteHandler.getInstance().flamePass.getImage(), renderX, renderY);
    }
}
