package com.phongvan.bomberman.entities.bomb;

import com.phongvan.bomberman.Camera;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.SoundHandler;
import com.phongvan.bomberman.entities.AnimatedEntities;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.graphics.SpriteHandler;
import com.phongvan.bomberman.map.MapHandler;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends AnimatedEntities {
    private int flameLength = 1;
    private int time = 120; // 2 second until explode.
    private List<Explosion> explosions = new ArrayList<>();
    private Bomber bomber;

    public Bomb(Bomber bomber, int x, int y, int flameLength) {
        super(x, y, null);
        this.flameLength = flameLength;
        time = 120;
        this.bomber = bomber;
    }

    public void render(GraphicsContext gc) {
        if (time != 0) {
            gc.drawImage(SpriteHandler.getInstance().bomb[curFrame / 10].getImage(),
                    x - Camera.getInstance().getOffsetX(), y - Camera.getInstance().getOffsetY());
        } else if (!explosions.isEmpty()){
            for (Explosion explosion : explosions) {
                explosion.render(gc);
            }
        }
    }

    private void createExplosionRange() {
        int tileSize = MapHandler.getInstance().getTileSize();
        int centerTileX = (int) (x / tileSize);
        int centerTileY = (int) (y / tileSize);

        explosions.add(new Explosion(bomber, centerTileX, centerTileY, Explosion.CENTER));
        if (MapHandler.getInstance().getTileType(centerTileX, centerTileY) == MapHandler.BRICKS_TILE) {
            Core.getInstance().getTile(centerTileX, centerTileY).setAlive(false);
        }

        for (int d = 1; d <= flameLength; ++d) {
            int tile = MapHandler.getInstance().getTileType(centerTileX + d, centerTileY);
            if (tile == MapHandler.WALL_TILE) {
                break;
            } else if (tile == MapHandler.BRICKS_TILE) {
                Core.getInstance().getTile(centerTileX + d, centerTileY).setAlive(false);
                break;
            }
            if (d < flameLength) {
                explosions.add(new Explosion(bomber,centerTileX + d, centerTileY, Explosion.MIDDLE_HORIZONTAL));
            } else {
                explosions.add(new Explosion(bomber,centerTileX + d, centerTileY, Explosion.HEAD_RIGHT));
            }
        }

        for (int d = 1; d <= flameLength; ++d) {
            int tile = MapHandler.getInstance().getTileType(centerTileX - d, centerTileY);
            if (tile == MapHandler.WALL_TILE) {
                break;
            } else if (tile == MapHandler.BRICKS_TILE) {
                Core.getInstance().getTile(centerTileX - d, centerTileY).setAlive(false);
                break;
            }
            if (d < flameLength) {
                explosions.add(new Explosion(bomber,centerTileX - d, centerTileY, Explosion.MIDDLE_HORIZONTAL));
            } else {
                explosions.add(new Explosion(bomber,centerTileX - d, centerTileY, Explosion.HEAD_LEFT));
            }
        }

        for (int d = 1; d <= flameLength; ++d) {
            int tile = MapHandler.getInstance().getTileType(centerTileX, centerTileY + d);
            if (tile == MapHandler.WALL_TILE) {
                break;
            } else if (tile == MapHandler.BRICKS_TILE) {
                Core.getInstance().getTile(centerTileX, centerTileY + d).setAlive(false);
                break;
            }
            if (d < flameLength) {
                explosions.add(new Explosion(bomber, centerTileX, centerTileY + d, Explosion.MIDDLE_VERTICAL));
            } else {
                explosions.add(new Explosion(bomber, centerTileX, centerTileY + d, Explosion.HEAD_DOWN));
            }
        }

        for (int d = 1; d <= flameLength; ++d) {
            int tile = MapHandler.getInstance().getTileType(centerTileX, centerTileY - d);
            if (tile == MapHandler.WALL_TILE) {
                break;
            } else if (tile == MapHandler.BRICKS_TILE) {
                Core.getInstance().getTile(centerTileX, centerTileY - d).setAlive(false);
                break;
            }
            if (d < flameLength) {
                explosions.add(new Explosion(bomber, centerTileX, centerTileY - d, Explosion.MIDDLE_VERTICAL));
            } else {
                explosions.add(new Explosion(bomber, centerTileX, centerTileY - d, Explosion.HEAD_UP));
            }
        }
    }

    public void update() {
        if (time > 0) {
            --time;
        }

        if (time == 0) {
            if (explosions.isEmpty()) {
                createExplosionRange();
                SoundHandler.getInstance().addMedia(SoundHandler.EFFECT_EXPLOSION, false);
            } else {
                for (Explosion explosion : explosions) {
                    explosion.update();
                }
            }
        }
        animate();
    }

    public void update(int flames) {
        flameLength = flames + 1;

        update();
    }

    public boolean explodeToBomb(Bomb other) {
        for (Explosion explosion : explosions) {
            if (explosion.checkCollision(other)) return true;
        }

        return false;
    }

    public boolean isExploded() {
        if (time != 0) {
            return false;
        }

        if (explosions.isEmpty()) return false;
        for (Explosion explosion : explosions) {
            if (explosion.isAlive()) return false;
        }

        return true;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public int getFlameLength() {
        return flameLength;
    }

    public List<Explosion> getExplosions() {
        return explosions;
    }
}
