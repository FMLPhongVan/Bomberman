package com.phongvan.bomberman.entities.mobs;

import com.phongvan.bomberman.AI.BomberAI;
import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.SoundHandler;
import com.phongvan.bomberman.entities.bomb.Bomb;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.gui.PaneController;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Bomber extends Mob {
    private int lives = 0;
    private int preScores = 0;
    private int scores = 0;
    private boolean plantBomb = false;
    private boolean isStepOnBomb = false;
    private final List<Bomb> plantedBomb = new ArrayList<>();

    private int preBombs = 0;
    private int bombs = 0;
    private int preFlames = 0;
    private int flames = 0;
    private boolean detonator = false;

    private BomberAI bomberAI;

    public static final int DEFAULT_LIVES = 3;
    public static final int DEFAULT_BOMBS = 1;
    public static final int DEFAULT_UP_LEFT_CORNER = 0;
    public static final int DEFAULT_UP_RIGHT_CORNER = 1;
    public static final int DEFAULT_DOWN_RIGHT_CORNER = 2;
    public static final int DEFAULT_DOWN_LEFT_CORNER = 3;

    public Bomber(double x, double y, Image image) {
        super(x, y, image);
        lives = DEFAULT_LIVES;
        bombs = DEFAULT_BOMBS;
        direction = RIGHT_DIR;
        scores = 0;
        velocity = 1.0;
        velX = velY = 0;
        flames = 0;
        preFlames = flames;
        preBombs = bombs;
        preScores = scores;
        bomberAI = new BomberAI(this);
    }

    public Bomber(double x, double y, Image image, int lives, int bombs) {
        super(x, y, image);
        this.lives = lives;
        this.bombs = bombs;
        scores = 0;
        velX = velY = 0;
        bomberAI = new BomberAI(this);
    }

    public void resetVelocity() {
        velX = velY = 0;
    }

    public void changeVelX(boolean positive) {
        velX = velX + ((positive) ? velocity : -velocity) * PaneController.getInstance().getScaleSize();
    }

    public void changeVelY(boolean positive) {
        velY = velY + ((positive) ? velocity : -velocity) * PaneController.getInstance().getScaleSize();
    }

    public boolean checkHadPlantedBomb(int tileX, int tileY) {
        int posX = tileX * MapHandler.getInstance().getTileSize();
        int posY = tileY * MapHandler.getInstance().getTileSize();
        for (Bomb bomb : plantedBomb) {
            if (bomb.getX() == posX && bomb.getY() == posY) {
                return true;
            }
        }

        return false;
    }

    public void update(int keyCode) {
        System.out.println(x + " " + y);
        if (isAlive) {
            int tileSize = MapHandler.getInstance().getTileSize();
            int tileX = (int) (x + tileSize / 2) / tileSize;
            int tileY = (int) (y + tileSize / 2) / tileSize;

            if (isStepOnBomb && !Core.getInstance().thisTileHadBomb(tileX, tileY)) {
                isStepOnBomb = false;
            }

            if (velX != 0 && velY == 0) {
                int newDirection = (velX > 0) ? RIGHT_DIR : LEFT_DIR;
                if (direction % 2 != 1) {
                    if (MapHandler.getInstance().canTurn(tileX, tileY, newDirection, wallPass)) {
                        if (targetX == UNSET && targetY == UNSET) {
                            targetX = tileX * tileSize;
                            targetY = tileY * tileSize;
                        }
                        if (targetX != UNSET && targetY != UNSET) {
                            adjustFixedPosition();
                            if (x == targetX && y == targetY) {
                                x += velX;
                                direction = newDirection;
                                targetX = targetY = UNSET;
                                //SoundHandler.getInstance().addMedia(SoundHandler.EFFECT_RIGHT, false);
                            } else {
                                if (targetY < y) {
                                    y -= velocity * PaneController.getInstance().getScaleSize();
                                } else if (targetY > y) {
                                    y += velocity * PaneController.getInstance().getScaleSize();
                                }
                                x = targetX;
                                //SoundHandler.getInstance().playMoveSound(direction);
                            }
                        }
                    }
                } else {
                    if (MapHandler.getInstance().canMoveTo(newDirection, x + velX, y, wallPass, bombPass, true)) {
                        x += velX;
                        //SoundHandler.getInstance().addMedia(SoundHandler.EFFECT_RIGHT, false);
                    } else {
                        x = tileX * tileSize;
                    }
                    direction = newDirection;
                }
            } else if (velX == 0 && velY != 0) {
                int newDirection = (velY > 0) ? DOWN_DIR : UP_DIR;
                if (direction % 2 != 0) {
                    if (MapHandler.getInstance().canTurn(tileX, tileY, newDirection, wallPass)) {
                        if (targetX == UNSET && targetY == UNSET) {
                            targetX = tileX * tileSize;
                            targetY = tileY * tileSize;
                        }
                        if (targetX != UNSET && targetY != UNSET) {
                            adjustFixedPosition();
                            if (x == targetX && y == targetY) {
                                y += velY;
                                direction = newDirection;
                                targetX = targetY = UNSET;
                                //SoundHandler.getInstance().playMoveSound(direction);
                            } else {
                                if (targetX < x) {
                                    x -= velocity * PaneController.getInstance().getScaleSize();
                                } else if (targetX > x) {
                                    x += velocity * PaneController.getInstance().getScaleSize();
                                }
                                y = targetY;
                                //SoundHandler.getInstance().playMoveSound(direction);
                            }
                        }
                    }
                } else {
                    if (MapHandler.getInstance().canMoveTo(newDirection, x, y + velY, wallPass, bombPass, true)) {
                        y += velY;
                        //SoundHandler.getInstance().playMoveSound(direction);
                    } else {
                        y = tileY * tileSize;
                    }
                    direction = newDirection;
                }
            } else if (velX != 0 && velY != 0) {
                if (MapHandler.getInstance().isTurnTile(tileX, tileY)) {
                    int firstDirection = (velX > 0) ? RIGHT_DIR : LEFT_DIR;
                    int secondDirection = (velY > 0) ? DOWN_DIR : UP_DIR;
                    boolean way1 = MapHandler.getInstance().canMoveTo(firstDirection, x + velX, y, wallPass, bombPass, true);
                    boolean way2 = MapHandler.getInstance().canMoveTo(secondDirection, x, y + velY, wallPass, bombPass, true);

                    if (way1 && !way2) {
                        x += velX;
                        direction = firstDirection;
                    } else if (!way1 && way2) {
                        y += velY;
                        direction = secondDirection;
                    }

                    //SoundHandler.getInstance().playMoveSound(direction);
                }
            } /*else {
                SoundHandler.getInstance().playMoveSound(UNSET);
            }*/
        }

        //animate();
        update();
    }

    public void update() {
        animate();
        if (isAlive && velX == 0 && velY == 0) {
            curFrame = 0;
        }

        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX = (int) (x + tileSize / 2) / tileSize;
        int tileY = (int) (y + tileSize / 2) / tileSize;

        if (plantBomb) {
            if (plantedBomb.size() < bombs && !Core.getInstance().thisTileHadBomb(tileX, tileY)) {
                plantedBomb.add(new Bomb(this,tileX * tileSize, tileY * tileSize, flames));
                isStepOnBomb = true;
                SoundHandler.getInstance().addMedia(SoundHandler.EFFECT_BOMB_PLANT, false);
            }
        }

        Core.getInstance().checkEatPowerUp(this);

        for (int i = 0; i < plantedBomb.size(); ++i) {
            plantedBomb.get(i).update(flames);
            if (plantedBomb.get(i).getTime() == 0) {
                for (int j = 0; j < plantedBomb.size(); ++j) {
                    if (j != i && plantedBomb.get(j).getTime() != 0 && plantedBomb.get(i).explodeToBomb(plantedBomb.get(j))) {
                        plantedBomb.get(j).setTime(0);
                    }
                }
            }

            if (plantedBomb.get(i).isExploded()) {
                plantedBomb.remove(i);
            }
        }

        plantBomb = false;
    }

    @Override
    public void animate() {
        if (isAlive) {
            if (curFrame < 29) {
                ++curFrame;
            } else {
                curFrame = 0;
            }
        } else {
            ++curFrame;

            if (curFrame == 30) {
                if (Core.getInstance().getMode() == Core.STORY_MODE) {
                    SoundHandler.getInstance().reset();
                    SoundHandler.getInstance().addMedia(SoundHandler.STAGE_JUST_DIED, false);
                }
            }

            if (lives == 0) {
                if (curFrame == 149) {
                    SoundHandler.getInstance().reset();
                    //Core.getInstance().getAnimationTimer().stop();
                    SoundHandler.getInstance().addMedia(SoundHandler.STAGE_GAME_OVER, false);
                    Core.getInstance().checkHighScore(scores);
                }
            } else {
                if (curFrame == 149) {
                    if (Core.getInstance().getMode() == Core.STORY_MODE) {
                        reloadBomberIndicator();
                        Core.getInstance().loadNewLevel(Core.getInstance().getLevel(), false);
                    } else {
                        resetToDefaultPosition(DEFAULT_UP_LEFT_CORNER);
                    }
                }
            }
        }
    }

    public void kill() {
        if (isAlive && lives > 0) {
            --lives;
            isAlive = false;
            curFrame = 0;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        double renderPosX = x - Camera.getInstance().getOffsetX();
        double renderPosY = y - Camera.getInstance().getOffsetY();

        if (isAlive()) {
            switch (direction) {
                case UP_DIR -> gc.drawImage(SpriteHandler.getInstance().bombermanUp[curFrame / 10].getImage(), renderPosX, renderPosY);
                case RIGHT_DIR -> gc.drawImage(SpriteHandler.getInstance().bombermanRight[curFrame / 10].getImage(), renderPosX, renderPosY);
                case DOWN_DIR -> gc.drawImage(SpriteHandler.getInstance().bombermanDown[curFrame / 10].getImage(), renderPosX, renderPosY);
                case LEFT_DIR -> gc.drawImage(SpriteHandler.getInstance().bombermanLeft[curFrame / 10].getImage(), renderPosX, renderPosY);
            }
        } else {
            gc.drawImage(SpriteHandler.getInstance().bombermanDead[curFrame / 50].getImage(), renderPosX, renderPosY);
        }

        for (Bomb bomb : plantedBomb) {
            bomb.render(gc);
        }
    }

    public void detonateOldestBomb() {
        if (!detonator) return;

        for (int i = 0; i < plantedBomb.size(); ++i) {
            if (plantedBomb.get(i).getTime() > 0) {
                plantedBomb.get(i).setTime(0);
                break;
            }
        }
    }

    public void setDetonator(boolean state) {
        detonator = state;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public int getScores() {
        return scores;
    }

    public int getLives() {
        return lives;
    }

    public int getBombs() {
        return bombs;
    }

    public void increaseBombs() {
        ++bombs;
    }

    public void increaseLives() {
        ++lives;
    }

    public void setPlantBomb(boolean state) {
        plantBomb = state;
    }

    public boolean isStepOnBomb() {
        return isStepOnBomb;
    }

    public void setFlames(int flames) {
        this.flames = flames;
    }

    public int getFlames() {
        return flames;
    }

    public void addScore(int point) {
        scores += point;
    }

    public void resetToDefaultPosition(final int CORNER) {
        plantedBomb.clear();
        velX = velY = 0;
        curFrame = 0;
        isAlive = true;
        int tileSize = MapHandler.getInstance().getTileSize();
        switch (CORNER) {
            case DEFAULT_UP_LEFT_CORNER -> {
                x = 1;
                y = 1;
                direction = RIGHT_DIR;
            }
            case DEFAULT_UP_RIGHT_CORNER -> {
                x = 14;
                y = 1;
                direction = DOWN_DIR;
            }
            case DEFAULT_DOWN_RIGHT_CORNER -> {
                x = 14;
                y = 14;
                direction = LEFT_DIR;
            }
            case DEFAULT_DOWN_LEFT_CORNER -> {
                x = 1;
                y = 14;
                direction = UP_DIR;
            }
        }

        x = x * tileSize;
        y = y * tileSize;
    }

    public void reloadBomberIndicator() {
        scores = preScores;
        bombs = preBombs;
        flames = preFlames;
        switch (Core.getInstance().getLevel()) {
            case 3 -> detonator = false;
            case 4 -> velocity = 1.0;
            case 6 -> wallPass = false;
        }
    }

    public void saveBomberIndicator() {
        preScores = scores;
        preFlames = flames;
        preBombs = bombs;
    }

    public int getPreScores() {
        return scores;
    }

    public int getPreBombs() {
        return preBombs;
    }

    public List<Bomb> getPlantedBomb() {
        return plantedBomb;
    }

    public BomberAI AI() {
        return bomberAI;
    }
}
