package com.phongvan.bomberman;

import com.phongvan.bomberman.entities.Entities;
import com.phongvan.bomberman.entities.bomb.Explosion;
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

public class Core {
    private static Core instance = null;
    private boolean running = false;
    private boolean pause = true;
    private boolean levelPass = false;
    private boolean auto = false;

    private GraphicsContext gc;
    private AnimationTimer animationTimer = null;

    private Bomber player;
    private final List<Bomber> bomberEnemies = new ArrayList<>();
    private final List<Mob> enemies = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private Entities[][] tiles;

    private int level = 1;
    private int mode;
    private int timePerLevel = 200 * 60;
    private int timeWait = 0;

    public static final double FPS = 60;
    public static final int DEFAULT_TIME_PER_LEVEL = 12000; /// 200 seconds
    public static final int STORY_MODE = 0;
    public static final int BATTLE_MODE = 1;

    private Core() {
        SpriteHandler.getInstance().loadAllSprite();
        Camera.getInstance().setCameraProperty();
    }

    public void initGC(GraphicsContext gc) {
        this.gc = gc;
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
        }

        return instance;
    }


    public void initGameBoard(final int GAME_MODE) {
        mode = GAME_MODE;
        player = null;
        bomberEnemies.clear();
        enemies.clear();
        powerUps.clear();
        levelPass = false;

        int tileSize = MapHandler.getInstance().getTileSize();
        switch (mode) {
            case STORY_MODE -> {
                level = 1;
                timePerLevel = DEFAULT_TIME_PER_LEVEL;
                player = new Bomber(1 * tileSize, 1 * tileSize, null);
                MapHandler.getInstance().loadMap(level);
            }
            case BATTLE_MODE -> {
                timePerLevel = DEFAULT_TIME_PER_LEVEL;
                player = new Bomber(1 * tileSize, 1 * tileSize, null);
                for (int i = 0; i < 3; ++i) {
                    bomberEnemies.add(new Bomber(1 * tileSize, 1 * tileSize, null));
                }

                bomberEnemies.get(0).resetToDefaultPosition(Bomber.DEFAULT_UP_RIGHT_CORNER);
                bomberEnemies.get(1).resetToDefaultPosition(Bomber.DEFAULT_DOWN_LEFT_CORNER);
                bomberEnemies.get(2).resetToDefaultPosition(Bomber.DEFAULT_DOWN_RIGHT_CORNER);

                MapHandler.getInstance().loadMap(0);
            }
        }

        tiles = new Entities[MapHandler.getInstance().getRows()][MapHandler.getInstance().getCols()];
        MapHandler.getInstance().processNewBoard();
        SoundHandler.getInstance().reset();
        InGameController.getInstance().getLevelLabel().setText("Level " + level);
        InGameController.getInstance().getTimeLabel().setText("" + timePerLevel / 60);
        InGameController.getInstance().getBombLabel().setText("" + player.getBombs());

        player.AI().initAI();
        player.AI().start();
    }

    public void startLevel() {
        switch (mode) {
            case STORY_MODE -> {
                InGameController.getInstance().getLevelInfoLabel().setText("Level " + level);
                InGameController.getInstance().getLivesInfoLabel().setText("" + player.getLives());
                InGameController.getInstance().getBombsInfoLabel().setText("" + player.getPreBombs());
                InGameController.getInstance().getScoreInfoLabel().setText("" + player.getPreScores());

                SoundHandler.getInstance().addMedia(SoundHandler.STAGE_LEVEL_START, false);
            }
            case BATTLE_MODE -> {

            }
        }
    }

    public void start() {
        running = true;
        pause = false;
        loop();
    }

    public void loadNewLevel(int level, boolean reload) {
        timePerLevel = DEFAULT_TIME_PER_LEVEL;
        MapHandler.getInstance().loadMap(level);
        tiles = new Entities[MapHandler.getInstance().getRows()][MapHandler.getInstance().getCols()];
        ControlHandler.getInstance().reset();
        SoundHandler.getInstance().reset();
        InGameController.getInstance().getLevelLabel().setText("Level " + level);
        InGameController.getInstance().getTimeLabel().setText("" + timePerLevel / 60);
        levelPass = false;

        switch (mode) {
            case STORY_MODE -> {
                player.resetToDefaultPosition(Bomber.DEFAULT_UP_LEFT_CORNER);
                enemies.clear();
                powerUps.clear();
            }
            case BATTLE_MODE -> {
                player.resetToDefaultPosition(Bomber.DEFAULT_UP_LEFT_CORNER);
                bomberEnemies.get(0).resetToDefaultPosition(Bomber.DEFAULT_UP_RIGHT_CORNER);
                bomberEnemies.get(1).resetToDefaultPosition(Bomber.DEFAULT_DOWN_LEFT_CORNER);
                bomberEnemies.get(2).resetToDefaultPosition(Bomber.DEFAULT_DOWN_RIGHT_CORNER);
            }
        }
        MapHandler.getInstance().processNewBoard();
        player.AI().initAI();
        player.AI().start();

        if (reload) {
            pause = true;
            if (animationTimer != null) {
                animationTimer.stop();
            }
            startLevel();
            InGameController.getInstance().earlyStage();
        } else {
            SoundHandler.getInstance().addMedia(SoundHandler.STAGE_STAGE_THEME, true);
        }
    }

    public void loop() {
        if (animationTimer == null) {
            animationTimer = new AnimationTimer() {
                private final double timePerFrame = 1000000000.0 / FPS;
                private double timer = 0.0;
                private double lastTime = System.nanoTime();

                @Override
                public void handle(long now) {
                    now = System.nanoTime();
                    timer += (now - lastTime) / timePerFrame;
                    lastTime = System.nanoTime();

                    while (timer >= 1) {
                        --timePerLevel;
                        update();
                        --timer;
                    }
                    render();
                }
            };

            pause = false;
            animationTimer.start();
        }

        if (pause) {
            animationTimer.start();
            pause = false;
        }
    }

    public void update() {
        if (levelPass) {
            --timeWait;
            if (timeWait == 0) {
                level += 1;
                loadNewLevel(level, true);
            }
        }

        for (int i = 0; i < MapHandler.getInstance().getRows(); ++i) {
            for (int j = 0; j < MapHandler.getInstance().getCols(); ++j) {
                tiles[i][j].update();
            }
        }
        ControlHandler.getInstance().handleInput();

        if (!auto) {
            player.update(0);
        } else {
            player.AI().update();
            player.update();
        }

        for (int i = 0; i < enemies.size(); ++i) {
            enemies.get(i).update();
        }
    }

    public void render() {
        InGameController.getInstance().getTimeLabel().setText("" + timePerLevel / 60);
        InGameController.getInstance().getScoreLabel().setText("Score: " + player.getScores());
        InGameController.getInstance().getHeartLabel().setText(" " + player.getLives());
        InGameController.getInstance().getBombLabel().setText(" " + player.getBombs());
        Camera.getInstance().adjustCameraPosition();
        resetCanvas();

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

        SoundHandler.getInstance().play();
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
        if (!pause) {
            ControlHandler.getInstance().add(event);
        }
    }

    public void removeInputEvent(KeyEvent event) {
        if (!pause) {
            ControlHandler.getInstance().remove(event);
        }
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
                SoundHandler.getInstance().addMedia(SoundHandler.EFFECT_POWER_UP, false);
            }
        }
    }

    public void exitToMenu() {
        running = false;
        pause = false;
        if (animationTimer != null) {
            animationTimer.stop();
        }
        animationTimer = null;
        player = null;
        bomberEnemies.clear();
        enemies.clear();
        powerUps.clear();
        tiles = null;
        level = 1;
        timeWait = 0;
    }

    public void levelComplete() {
        if (!levelPass) {
            SoundHandler.getInstance().reset();
            SoundHandler.getInstance().addMedia(SoundHandler.STAGE_LEVEL_COMPLETE, false );
            levelPass = true;
            player.saveBomberIndicator();
            timeWait = 240;
        }
    }

    public void setPause(boolean state) {
        pause = state;

        if (pause) {
            animationTimer.stop();
            animationTimer = null;
        }

        SoundHandler.getInstance().pause();
        ControlHandler.getInstance().reset();
    }

    public void checkHighScore(int scores) {
        animationTimer.stop();
        animationTimer = null;
        if (!HighScoreManager.getInstance().aNewHighScore(scores)) {
            InGameController.getInstance().openGameOverPane();
        } else {
            InGameController.getInstance().openHighScorePane();
        }
    }

    public void checkCollideBomber(Mob enemy) {
        int tileSize = MapHandler.getInstance().getTileSize();
        if ((int) (enemy.getX() + tileSize / 2) / tileSize == (int) (player.getX() + tileSize / 2) / tileSize
                && (int) (enemy.getY() + tileSize / 2) / tileSize == (int) (player.getY() + tileSize / 2) / tileSize) {
            player.kill();
        }
    }

    public void toggleAIMode() {
        auto = !auto;
    }

    public void checkExplosionCollision(Explosion explosion) {
        int tileSize = MapHandler.getInstance().getTileSize();
        if ((int) ((player.getX() + tileSize / 2) / tileSize) == (int) (explosion.getX() / tileSize)
                && (int) ((player.getY() + tileSize / 2) / tileSize) == (int) (explosion.getY() / tileSize)) {
            //player.kill();
        }

        if (!bomberEnemies.isEmpty()) {
            for (Bomber enemy : bomberEnemies) {
                if ((int) ((enemy.getX() + tileSize / 2) / tileSize) == (int) (explosion.getX() / tileSize)
                        && (int) ((enemy.getY() + tileSize / 2) / tileSize) == (int) (explosion.getY() / tileSize)) {
                    if (enemy.isAlive()) {
                        enemy.kill();
                        explosion.getBomber().addScore(enemy.getPoint());
                    }
                }
            }
        }

        if (!enemies.isEmpty()) {
            for (Mob mob : enemies) {
                if ((int) ((mob.getX() + tileSize / 2) / tileSize) == (int) (explosion.getX() / tileSize)
                        && (int) ((mob.getY() + tileSize / 2) / tileSize) == (int) (explosion.getY() / tileSize)) {
                    if (mob.isAlive()) {
                        mob.kill();
                        explosion.getBomber().addScore(mob.getPoint());
                    }
                }
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getPauseState() {
        return pause;
    }

    public List<Bomber> getBomberEnemies() {
        return bomberEnemies;
    }

    public List<Mob> getEnemies() {
        return enemies;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public AnimationTimer getAnimationTimer() {
        return animationTimer;
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
}
