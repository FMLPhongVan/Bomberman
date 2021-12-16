package com.phongvan.bomberman.entities.powerups;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
import javafx.scene.canvas.GraphicsContext;

import java.util.Map;

public class Speed extends PowerUp {
    public Speed(double x, double y) {
        super(x, y);
    }

    @Override
    public void update(Bomber bomber) {
        int scale = PaneController.getInstance().getScaleSize();
        if (bomber.getDirection() % 2 == 0) {
            double y = bomber.getY() / scale;
            if (y % 2 == 1) y -= 1;
            y = y * scale;
            bomber.setY(y);
        } else {
            double x = bomber.getX() / scale;
            if (x % 2 == 1) x -= 1;
            x = x * scale;
            bomber.setX(x);
        }
        bomber.setVelocity(1.5);
    }

    @Override
    public void render(GraphicsContext gc) {
        double renderX = x - Camera.getInstance().getOffsetX();
        double renderY = y - Camera.getInstance().getOffsetY();

        gc.drawImage(SpriteHandler.getInstance().speed.getImage(), renderX, renderY);
    }
}
