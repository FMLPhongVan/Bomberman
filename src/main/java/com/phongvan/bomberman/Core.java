package com.phongvan.bomberman;

import com.phongvan.bomberman.entities.Entities;
import com.phongvan.bomberman.entities.bomb.Explosion;
import com.phongvan.bomberman.entities.mobs.Ballooms;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.entities.mobs.Mob;
import com.phongvan.bomberman.entities.powerups.PowerUp;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.InGameController;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import com.phongvan.bomberman.player.ControlHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Core {
    class ThreadHandle implements Runnable {

        @Override
        public void run() {

        }
    }

    private static Core instance = null;
    private boolean running = false;
    private boolean pause = true;

    private GraphicsContext gc;
    private final ControlHandler controlHandler = new ControlHandler();

    private Bomber player;
    private final List<Mob> enemies = new ArrayList<>();
    private Entities[][] tiles;
    private final List<PowerUp> powerUps = new ArrayList<>();

    private int level = 1;
    private int mode;

    public static final double FPS = 60;
    public static final int STORY_MODE = 0;
    public static final int PVE_MODE = 1;

    private Core() {
        SpriteHandler.getInstance().loadAllSprite();
        Camera.getInstance().setCameraProperty();
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
        }

        return instance;
    }

    public void initGC(GraphicsContext gc) {
        this.gc = gc;
    }

    public void start() {
        running = true;
        pause = false;
        newGame();
        loop();
    }

    public void newGame() {
        level = 2;
        mode = STORY_MODE;
        player = new Bomber(MapHandler.getInstance().getTileSize(), MapHandler.getInstance().getTileSize(), null);
        enemies.clear();
        powerUps.clear();
        MapHandler.getInstance().loadMap(level);
        tiles = new Entities[MapHandler.getInstance().getRows()][MapHandler.getInstance().getCols()];
        MapHandler.getInstance().processNewBoard();
    }

    public void loadNewStage(int level) {
        mode = STORY_MODE;
        player.resetToDefaultPosition(Bomber.DEFAULT_UP_LEFT_CORNER);
        enemies.clear();
        powerUps.clear();
        MapHandler.getInstance().loadMap(level);
        MapHandler.getInstance().processNewBoard();
    }

    public void loop() {

        AnimationTimer animationTimer = new AnimationTimer() {
            private final double timePerFrame = 1000000000.0 / FPS;
            private double timer = 0.0;
            private double lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                now = System.nanoTime();
                timer += (now - lastTime) / timePerFrame;
                lastTime = System.nanoTime();

                while (timer >= 1) {
                    update();
                    --timer;
                }

                resetCanvas();
                Camera.getInstance().adjustCameraPosition();

                for (int i = 0; i < MapHandler.getInstance().getRows(); ++i) {
                    for (int j = 0; j < MapHandler.getInstance().getCols(); ++j) {
                        tiles[i][j].render(gc);
                    }
                }

                for (PowerUp powerUp : powerUps) {
                    powerUp.render(gc);
                    int tileSize = MapHandler.getInstance().getTileSize();
                    int tileX = (int) (powerUp.getX() / tileSize);
                    int tileY = (int) (powerUp.getY() / tileSize);

                    if (MapHandler.getInstance().getTileType(tileX, tileY) == MapHandler.BRICKS_TILE)
                        tiles[tileY][tileX].render(gc);
                }

                player.render(gc);

                for (Entities mob : enemies) {
                    mob.render(gc);
                }
            }
        };

        animationTimer.start();
    }

    public void update() {
        for (int i = 0; i < MapHandler.getInstance().getRows(); ++i) {
            for (int j = 0; j < MapHandler.getInstance().getCols(); ++j) {
                tiles[i][j].update();
            }
        }
        controlHandler.handleInput();

        player.update(0);

        for (int i = 0; i < enemies.size(); ++i) {
            enemies.get(i).update();
        }
    }

    private void resetCanvas() {
        gc.clearRect(0, 0,
                InGameController.getInstance().getGameBoard().getWidth(),
                InGameController.getInstance().getGameBoard().getHeight());
        gc.fillRect(0, 0,
                InGameController.getInstance().getGameBoard().getWidth(),
                InGameController.getInstance().getGameBoard().getHeight());
        gc.setFill(Color.rgb(80, 160, 0));
    }

    public boolean thisTileHadBomb(int tileX, int tileY) {
        return player.checkHadPlantedBomb(tileX, tileY);
    }

    public void addInputEvent(KeyEvent event) {
        controlHandler.add(event);
    }

    public void removeInputEvent(KeyEvent event) {
        controlHandler.remove(event);
    }

    public void addNewMob(Mob mob) {
        enemies.add(mob);
    }

    public void deleteMob(Mob mob) {
        enemies.remove(mob);
    }

    public void addNewTile(Entities tile) {
        int tileSize = MapHandler.DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize();
        tiles[(int) (tile.getY() / tileSize)][(int) (tile.getX() / tileSize)] = tile;
    }

    public void addNewPowerUps(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public void checkEatPowerUp(Bomber bomber) {
        for (int i = 0; i < powerUps.size(); ++i) {
            if (bomber.checkCollision(powerUps.get(i))) {
                powerUps.get(i).update(bomber);
                powerUps.remove(i);
            }
        }
    }

    public void checkExplosionCollision(Explosion explosion) {
        int tileSize = MapHandler.getInstance().getTileSize();
        if ((int) ((player.getX() + tileSize / 2) / tileSize) == (int) (explosion.getX() / tileSize)
                && (int) ((player.getY() + tileSize / 2) / tileSize) == (int) (explosion.getY() / tileSize)) {
            player.kill();
        }

        if (!enemies.isEmpty()) {
            for (Mob mob : enemies) {
                if ((int) ((mob.getX() + tileSize / 2) / tileSize) == (int) (explosion.getX() / tileSize)
                        && (int) ((mob.getY() + tileSize / 2) / tileSize) == (int) (explosion.getY() / tileSize)) {
                    mob.kill();
                }
            }
        }
    }

    public boolean killAllEnemies() {
        return enemies.isEmpty();
    }


    public GraphicsContext getGC() {
        return gc;
    }

    public Entities getTile(int tileX, int tileY) {
        return tiles[tileY][tileX];
    }

    public int getMode() {
        return mode;
    }

    public Bomber getPlayer() {
        return player;
    }

    public int getLevel() {
        return level;
    }
}
