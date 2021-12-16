package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.map.MapHandler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FindBestSafePositionTask extends LeafTask{
    public FindBestSafePositionTask(Data data) {
        super(data);
    }

    public FindBestSafePositionTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return true;
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "FindBestSafePosition", "Start finding best position");
    }

    @Override
    public void end() {
        if (controller.succeeded()) {
            Logger.log(Logger.INFO, "FindBestSafePosition",
                    "Finish finding. Safe location is: " + data.getPlayer().getTargetX() + " " + data.getPlayer().getTargetY());
        } else {
            Logger.log(Logger.INFO, "FindBestSafePosition", "Not found");
        }
    }

    @Override
    public void execute() {
        if (data.safe) {
            controller.finishWithFailure();
            return;
        }

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
        List<Integer> safeLocationList = new ArrayList<>();
        queue.add(tileY * cols + tileX);
        distance[tileY][tileX] = 0;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            int x = node % cols, y = node / cols;

            if (data.bNodes[y][x].status == Data.NodeStatus.SAFE) {
                safeLocationList.add(node);
            }

            for (int dir = 0; dir < 4; ++dir) {
                int u = x + MapHandler.dc[dir];
                int v = y + MapHandler.dh[dir];

                if (u < 1 || v < 1 || u > cols || v > rows
                        || data.bNodes[v][u].status == Data.NodeStatus.WALL
                        || data.bNodes[v][u].status == Data.NodeStatus.BRICK
                        || data.bNodes[v][u].status == Data.NodeStatus.VERY_DANGEROUS
                        || (Core.getInstance().thisTileHadBomb(u, v) && !data.getPlayer().isStepOnBomb())
                        || (Core.getInstance().thisTileHadBomb(u, v) && data.getPlayer().isStepOnBomb() && !Core.getInstance().thisTileHadBomb(x, y))
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

        if (safeLocationList.isEmpty()) {
            controller.finishWithFailure();
        } else {
            bomber.setTargetX((safeLocationList.get(0) % cols) * tileSize);
            bomber.setTargetY((safeLocationList.get(0) / cols) * tileSize);
            controller.finishWithSuccess();
        }
    }
}
