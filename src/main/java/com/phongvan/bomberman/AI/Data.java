package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.entities.mobs.Mob;
import com.phongvan.bomberman.entities.powerups.PowerUp;
import com.phongvan.bomberman.map.MapHandler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Data {
    private Bomber player;
    private Mob targetMob = null;
    private List<Bomber> bomberEnemies;
    private List<Mob> enemies;
    private List<PowerUp> powerUps;
    public BNode[][] bNodes;
    public boolean safe = false;

    enum NodeStatus {
        WALL,
        BRICK,
        SAFE,
        DANGEROUS,
        VERY_DANGEROUS
    }

    static class BNode {
        public NodeStatus status;
        public int remainingTime = Integer.MAX_VALUE;
    }

    public Data(Bomber player) {
        this.player = player;
        bomberEnemies = Core.getInstance().getBomberEnemies();
        enemies = Core.getInstance().getEnemies();
        powerUps = Core.getInstance().getPowerUps();
        bNodes = new BNode[MapHandler.getInstance().getRows()][MapHandler.getInstance().getCols()];
    }

    private boolean checkInExplodeArea(int tileX, int tileY, int u, int v) {
        for (int dir = 0; dir < 4; ++dir) {
            for (int k = 0; k <= player.getFlames() + 1; ++k) {
                if (tileX + k * MapHandler.dc[dir] == u && tileY + k * MapHandler.dh[dir] == v) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean haveAWayToHide(int tileX, int tileY) {
        int tileSize = MapHandler.getInstance().getTileSize();
        int rows = MapHandler.getInstance().getRows();
        int cols = MapHandler.getInstance().getCols();

        int[][] distance = new int[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                distance[i][j] = -1;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(tileY * cols + tileX);
        distance[tileY][tileX] = 0;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            int x = node % cols, y = node / cols;

            if (bNodes[y][x].status == NodeStatus.SAFE && !checkInExplodeArea(tileX, tileY, x, y)) {
                return true;
            }

            for (int dir = 0; dir < 4; ++dir) {
                int u = x + MapHandler.dc[dir];
                int v = y + MapHandler.dh[dir];

                if (u < 1 || v < 1 || u > cols || v > rows
                        || bNodes[v][u].status == Data.NodeStatus.WALL
                        || bNodes[v][u].status == Data.NodeStatus.BRICK
                        || bNodes[v][u].status == Data.NodeStatus.VERY_DANGEROUS
                        || (Core.getInstance().thisTileHadBomb(u, v) && !player.isStepOnBomb())
                        || (Core.getInstance().thisTileHadBomb(u, v) && player.isStepOnBomb() && !Core.getInstance().thisTileHadBomb(x, y))
                        || distance[v][u] != -1) {
                    continue;
                }

                distance[v][u] = distance[y][x] + 1;
                if (bNodes[v][u].status == Data.NodeStatus.DANGEROUS) {
                    if (16.0 * distance[v][u] / (1.0 * player.getVelocity()) < bNodes[v][u].remainingTime - 5)
                        queue.add(v * cols + u);
                } else if (bNodes[v][u].status == Data.NodeStatus.SAFE) {
                    queue.add(v * cols + u);
                }
            }
        }

        return false;
    }

    public void setTargetMob(Mob targetMob) {
        this.targetMob = targetMob;
    }

    public Bomber getPlayer() {
        return player;
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

    public Mob getTargetMob() {
        return targetMob;
    }
}
