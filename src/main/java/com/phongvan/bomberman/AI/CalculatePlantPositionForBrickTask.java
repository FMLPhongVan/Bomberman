package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.map.MapHandler;

import java.util.ArrayDeque;
import java.util.Queue;

public class CalculatePlantPositionForBrickTask extends  LeafTask {
    public CalculatePlantPositionForBrickTask(Data data) {
        super(data);
    }

    public CalculatePlantPositionForBrickTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return data.getPlayer().getPlantedBomb().size() < data.getPlayer().getBombs();
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "CalculatePlantPositionForBrick", "Start calculating plant position");
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "CalculatePlantPositionForBrick",
                "Finish calculating: " + data.getPlayer().getTargetX() + " " + data.getPlayer().getTargetY());
    }

    private int countBricks(int x, int y) {
        int cnt = 0;

        for (int dir = 0; dir < 4; ++dir) {
            for (int k = 1; k <= data.getPlayer().getFlames() + 1; ++k) {
                int u = x + MapHandler.dc[dir] * k;
                int v = y + MapHandler.dh[dir] * k;

                if (u < 1 || v < 1 || u > MapHandler.getInstance().getCols() || v > MapHandler.getInstance().getRows()
                        || MapHandler.getInstance().getTileType(u, v) == MapHandler.WALL_TILE) {
                    break;
                }

                if (MapHandler.getInstance().getTileType(u, v) == MapHandler.BRICKS_TILE) {
                    ++cnt;
                    break;
                }
            }

        }

        return cnt;
    }

    @Override
    public void execute() {
        Bomber bomber = data.getPlayer();
        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX = (int) ((bomber.getX() + tileSize / 2) / tileSize);
        int tileY = (int) ((bomber.getY() + tileSize / 2) / tileSize);
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

        int bestPos = -1;
        int bestW = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            int x = node % cols, y = node / cols;

            int cnt = countBricks(x, y);
            if (cnt > 0 && data.haveAWayToHide(x, y)) {
                System.out.println(x + " " + y + " " + cnt + " " + (distance[y][x] - cnt));
                if (bestPos == -1) {
                    bestPos = y * cols + x;
                    bestW = distance[y][x] - cnt;
                } else if (bestW > distance[y][x] - cnt) {
                    bestW = distance[y][x] - cnt;
                    bestPos = y * cols + x;
                }
            }

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
                if (data.bNodes[v][u].status == Data.NodeStatus.DANGEROUS) {
                    if (16 * distance[v][u] / bomber.getVelocity() < data.bNodes[v][u].remainingTime - 5)
                        queue.add(v * cols + u);
                } else if (data.bNodes[v][u].status == Data.NodeStatus.SAFE) {
                    queue.add(v * cols + u);
                }
            }
        }

        if (bestPos == -1) {
            controller.finishWithFailure();
        } else {
            bomber.setTargetX((bestPos % cols) * tileSize);
            bomber.setTargetY((bestPos / cols) * tileSize);
            controller.finishWithSuccess();
        }
    }
}
