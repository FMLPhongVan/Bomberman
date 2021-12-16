package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.entities.bomb.Bomb;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.entities.mobs.Mob;
import com.phongvan.bomberman.map.MapHandler;

import java.util.ArrayList;
import java.util.List;

public class CheckBomberSafetyTask extends LeafTask {
    public CheckBomberSafetyTask(Data data) {
        super(data);
    }

    public CheckBomberSafetyTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return true;
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "CheckBomberSafety", "Start checking bomber's safety");
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "CheckBomberSafety", "End checking:" + controller.succeeded());
    }

    @Override
    public void execute() {
        data.safe = true;
        for (int i = 0; i < MapHandler.getInstance().getRows(); ++i) {
            for (int j = 0; j < MapHandler.getInstance().getCols(); ++j) {
                data.bNodes[i][j] = new Data.BNode();
                switch (MapHandler.getInstance().getTileType(j, i)) {
                    case MapHandler.WALL_TILE -> data.bNodes[i][j].status = Data.NodeStatus.WALL;
                    case MapHandler.BRICKS_TILE -> {
                        data.bNodes[i][j].status = (data.getPlayer().canWallPass()) ? Data.NodeStatus.SAFE : Data.NodeStatus.BRICK;
                    }
                    case MapHandler.PORTAL_TILE, MapHandler.GRASS_TILE -> data.bNodes[i][j].status = Data.NodeStatus.SAFE;
                }
            }
        }

        int tileSize = MapHandler.getInstance().getTileSize();
        Bomber bomber = data.getPlayer();
        List<Bomb> bombList = new ArrayList<>(bomber.getPlantedBomb());

        if (!data.getBomberEnemies().isEmpty()) {
            for (Bomber enemy : data.getBomberEnemies()) {
                bombList.addAll(enemy.getPlantedBomb());
            }
        }

        if (!data.getEnemies().isEmpty()) {
            for (Mob enemy : data.getEnemies()) {
                int tileX = (int) ((enemy.getX() + tileSize / 2) / tileSize);
                int tileY = (int) ((enemy.getY() + tileSize / 2) / tileSize);
                data.bNodes[tileY][tileX].status = Data.NodeStatus.VERY_DANGEROUS;
            }
        }

        if (bombList.isEmpty()) {
            controller.finishWithSuccess();
        } else {
            int tileX = (int) ((bomber.getX() + tileSize / 2) / tileSize);
            int tileY = (int) ((bomber.getY() + tileSize / 2) / tileSize);

            boolean check = false;
            for (Bomb bomb : bombList) {
                if (bomberInExplosionArea(tileX, tileY, bomb)) {
                    check = true;
                }
            }

            if (check) {
                data.safe = false;
                controller.finishWithFailure();
            } else {
                data.safe = true;
                controller.finishWithSuccess();
            }
        }
    }

    private boolean bomberInExplosionArea(int tileX, int tileY, Bomb bomb) {
        int tileSize = MapHandler.getInstance().getTileSize();
        int x = (int) (bomb.getX() / tileSize);
        int y = (int) (bomb.getY() / tileSize);
        boolean check = false;
        data.bNodes[y][x].status = (bomb.getTime() == 0) ? Data.NodeStatus.VERY_DANGEROUS : Data.NodeStatus.DANGEROUS;
        data.bNodes[y][x].remainingTime = Math.min(data.bNodes[y][x].remainingTime, bomb.getTime());
        if (tileX == x && tileY == y) check = true;

        for (int dir = 0; dir < 4; ++dir) {
            for (int k = 1; k <= bomb.getFlameLength(); ++k) {
                int u = x + MapHandler.dc[dir] * k;
                int v = y + MapHandler.dh[dir] * k;

                if (u < 1 || v < 1 || u > MapHandler.getInstance().getCols() || v > MapHandler.getInstance().getRows()
                        || MapHandler.getInstance().getTileType(u, v) == MapHandler.WALL_TILE
                        || MapHandler.getInstance().getTileType(u, v) == MapHandler.BRICKS_TILE) {
                    if (MapHandler.getInstance().getTileType(u, v) == MapHandler.BRICKS_TILE && data.getPlayer().canWallPass()) {
                        data.bNodes[v][u].status = (bomb.getTime() == 0) ? Data.NodeStatus.VERY_DANGEROUS : Data.NodeStatus.DANGEROUS;
                        data.bNodes[v][u].remainingTime = Math.min(data.bNodes[v][u].remainingTime, bomb.getTime());
                    }
                    break;
                }

                data.bNodes[v][u].status = (bomb.getTime() == 0) ? Data.NodeStatus.VERY_DANGEROUS : Data.NodeStatus.DANGEROUS;
                data.bNodes[v][u].remainingTime = Math.min(data.bNodes[v][u].remainingTime, bomb.getTime());

                if (tileX == u && tileY == v) {
                    check = true;
                }
            }
        }

        return check;
    }
}
