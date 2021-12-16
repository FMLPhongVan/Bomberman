package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.entities.mobs.Mob;
import com.phongvan.bomberman.map.MapHandler;

import java.util.ArrayDeque;
import java.util.Queue;

public class CheckIfBomberReachEnemyTask extends LeafTask {
    public CheckIfBomberReachEnemyTask(Data data) {
        super(data);
    }

    public CheckIfBomberReachEnemyTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return !data.getBomberEnemies().isEmpty() && !data.getEnemies().isEmpty();
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "CheckIfBomberReachEnemy", "Start checking if bomber can reach closet enemy");
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "CheckIfBomberReachEnemy", "End checking" + controller.succeeded());
    }

    private Mob getMobAt(int tileX, int tileY) {
        int tileSize = MapHandler.getInstance().getTileSize();
        Mob target = null;

        if (!data.getBomberEnemies().isEmpty()) {
            for (Bomber enemy : data.getBomberEnemies()) {
                if (tileX == (int) ((enemy.getX() + tileSize / 2) / tileSize)
                        && tileY == (int) ((enemy.getY() + tileSize / 2) / tileSize)) {
                    target = (target == null) ? enemy : (enemy.getVelocity() > target.getVelocity() || enemy.canWallPass()) ? enemy : target;
                }
            }
        }

        if (!data.getEnemies().isEmpty()) {
            for (Mob enemy : data.getEnemies()) {
                if (tileX == (int) ((enemy.getX() + tileSize / 2) / tileSize)
                        && tileY == (int) ((enemy.getY() + tileSize / 2) / tileSize)) {
                    target = (target == null) ? enemy : (enemy.getVelocity() > target.getVelocity() || enemy.canWallPass()) ? enemy : target;
                }
            }
        }

        return target;
    }

    @Override
    public void execute() {
        data.setTargetMob(null);
        Bomber bomber = data.getPlayer();
        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX = (int) ((bomber.getX() + tileSize / 2) / tileSize);
        int tileY = (int) ((bomber.getY() + tileSize / 2) / tileSize);
        int rows = MapHandler.getInstance().getRows();
        int cols = MapHandler.getInstance().getCols();

        int[][] distance = new int[rows][cols];
        int[][] parent = new int[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                distance[i][j] = -1;
                parent[i][j] = -1;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(tileY * cols + tileX);
        distance[tileY][tileX] = 0;
        parent[tileY][tileX] = -1;
        int targetX = -1, targetY = -1;
        Mob closetMob = null;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            int x = node % cols, y = node / cols;

            for (int dir = 0; dir < 4; ++dir) {
                int u = x + MapHandler.dc[dir];
                int v = y + MapHandler.dh[dir];

                if (u < 1 || v < 1 || u > cols || v > rows
                        || MapHandler.getInstance().getTileType(u, v) == MapHandler.WALL_TILE
                        || MapHandler.getInstance().getTileType(u, v) == MapHandler.BRICKS_TILE
                        || Core.getInstance().thisTileHadBomb(u, v)
                        || distance[v][u] != -1) {
                    continue;
                }

                distance[v][u] = distance[y][x] + 1;
                parent[v][u] = node;
                if (targetX == -1 && targetY == -1) {
                    targetX = u;
                    targetY = v;
                    closetMob = getMobAt(u, v);
                    break;
                } else {
                    Mob mob = getMobAt(u, v);
                    if (mob != null) {
                        if (distance[targetY][targetX] > distance[v][u] ||
                                (distance[targetY][targetX] == distance[v][u]
                                    && mob.getVelocity() > closetMob.getVelocity())) {
                            targetX = u;
                            targetY = v;
                            closetMob = mob;
                        }
                    }
                }
                queue.add(v * cols + u);
            }
        }

        if (closetMob == null) {
            controller.finishWithFailure();
        } else {
            data.setTargetMob(closetMob);
            controller.finishWithSuccess();
        }
    }
}
