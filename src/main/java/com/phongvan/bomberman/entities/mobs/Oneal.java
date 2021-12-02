package com.phongvan.bomberman.entities.mobs;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
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
        System.out.println(x + " " + y);
        if (isAlive) {
            if (x % MapHandler.getInstance().getTileSize() == 0 && y % MapHandler.getInstance().getTileSize() == 0) {
                MapHandler.getInstance().findShortestPath(this, Core.getInstance().getPlayer());
            }
            System.out.println(direction);
            move(false);
        }
        animate();
    }

    @Override
    public void kill() {

    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(SpriteHandler.getInstance().onealDownRight[curFrame / 10].getImage(), x, y);
    }
}
