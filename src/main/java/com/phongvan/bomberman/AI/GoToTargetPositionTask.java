package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.entities.mobs.Mob;
import com.phongvan.bomberman.map.MapHandler;
import com.phongvan.bomberman.map.Node;

import java.util.*;

public class GoToTargetPositionTask extends LeafTask {
    public GoToTargetPositionTask(Data data) {
        super(data);
    }

    public GoToTargetPositionTask(Data data, String name) {
        super(data, name);
    }

    @Override
    public boolean checkCondition() {
        return data.getPlayer().getTargetX() != Mob.UNSET && data.getPlayer().getTargetY() != Mob.UNSET;
    }

    @Override
    public void start() {
        Logger.log(Logger.INFO, "GoToTargetPosition", "Start going to target position");
    }

    @Override
    public void end() {
        Logger.log(Logger.INFO, "GoToTargetPosition", "Status: " + controller.succeeded());
    }

    @Override
    public void execute() {
        /*Bomber player = data.getPlayer();
        int tileSize = MapHandler.getInstance().getTileSize();
        int tileX = (int) player.getX() / tileSize;
        int tileY = (int) player.getY() / tileSize;
        int targetX = (int) player.getTargetX() / tileSize;
        int targetY = (int) player.getTargetY() / tileSize;
        int rows = MapHandler.getInstance().getRows();
        int cols = MapHandler.getInstance().getCols();

        Set<Node> queue = new TreeSet<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                int value = Double.compare(o1.getfCost(), o2.getfCost());
                if (value == 0) {
                    if (o1.getX() == o2.getX() && o1.getY() == o2.getY() && o1.getgCost() == o2.getgCost()) {
                        return Double.compare(o1.gethCost(), o2.gethCost());
                    }
                    else return -1;
                }

                return value;
            }
        });
        double[][] visited = new double[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j)
                visited[i][j] = -1;
        }

        double gCost, hCost, fCost;
        gCost = 0;
        hCost = MapHandler.getInstance().getManhattanDistance(tileX, tileY, targetX, targetY);
        Node current = new Node(tileX, tileY, null, 0, hCost);
        if (current.isTargetNode(targetX, targetY)) {
            player.setDirection(Mob.UNSET);
            controller.finishWithSuccess();
            return;
        }
        queue.add(current);

        while (!queue.isEmpty()) {
            current = queue.iterator().next();
            //System.out.println(current.getX() + " " + current.getY());
            if (current.isTargetNode(targetX, targetY)) {
                while (current.getParent() != null) {
                    if (current.getParent().getParent() == null) {
                        for (int dir = 0; dir < 4; ++dir) {
                            if (current.getX() == current.getParent().getX() + MapHandler.dc[dir]
                                    && current.getY() == current.getParent().getY() + MapHandler.dh[dir]) {
                                player.setDirection(dir);
                                player.move(true);
                                queue.clear();
                                controller.finishWithFailure();
                                return;
                            }
                        }
                    }
                    current = current.getParent();
                }
            }

            int x = current.getX();
            int y = current.getY();
            visited[y][x] = current.getfCost();
            queue.remove(current);

            for (int dir = 0; dir < 4; ++dir) {
                int u = x + MapHandler.dc[dir];
                int v = y + MapHandler.dh[dir];
                if (u < 1 || v < 1 || u > cols || v > rows
                        || data.bNodes[v][u].status == Data.NodeStatus.WALL
                        || data.bNodes[v][u].status == Data.NodeStatus.BRICK
                        || data.bNodes[v][u].status == Data.NodeStatus.VERY_DANGEROUS
                        || (Core.getInstance().thisTileHadBomb(u, v) && !data.getPlayer().isStepOnBomb())
                        || (Core.getInstance().thisTileHadBomb(u, v) && data.getPlayer().isStepOnBomb() && !Core.getInstance().thisTileHadBomb(x, y))) {
                    continue;
                }
                //System.out.println(dir + " " + tileX + " " + tileY);
                gCost = current.getgCost() + 1;
                hCost = MapHandler.getInstance().getManhattanDistance(x, y, targetX, targetY);
                fCost = gCost + hCost;
                if (visited[v][u] == -1) {
                    queue.add(new Node(u, v, current, gCost, hCost));
                } else if (visited[v][u] > fCost) {
                    queue.add(new Node(u, v, current, gCost, hCost));
                }
            }
        }

        controller.finishWithFailure();

        /*if (player.getX() == player.getTargetX() && player.getY() == player.getTargetY()) {
            controller.finishWithSuccess();
        } else {
            player.move(true);
            controller.finishWithFailure();
        }*/

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

        int targetX = (int) bomber.getTargetX() / tileSize;
        int targetY = (int) bomber.getTargetY() / tileSize;

        if (targetX == tileX && targetY == tileY) {
            bomber.setDirection(Mob.UNSET);
            controller.finishWithSuccess();
            return;
        }

        while (!queue.isEmpty()) {
            int node = queue.poll();
            int x = node % cols, y = node / cols;

            if (x == targetX && y == targetY) {
                //System.out.println(1);
                while (parent[y][x] != -1) {
                    //System.out.println(x + " " + y);
                    node = parent[y][x];
                    if (parent[node/ cols][node % cols] == -1) {
                        for (int dir = 0; dir < 4; ++dir) {
                            if (x == parent[y][x] % cols + MapHandler.dc[dir]
                                    && y == parent[y][x] / cols + MapHandler.dh[dir]) {
                                bomber.setDirection(dir);
                                System.out.println(dir);
                                bomber.move(true);
                                queue.clear();
                                controller.finishWithFailure();
                                return;
                            }
                        }
                    }

                    node = parent[y][x];
                    y = node / cols;
                    x = node % cols;
                }
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
                parent[v][u] = y * cols + x;
                if (data.bNodes[v][u].status == Data.NodeStatus.DANGEROUS) {
                    if (16 * distance[v][u] / bomber.getVelocity() < data.bNodes[v][u].remainingTime - 5)
                        queue.add(v * cols + u);
                } else if (data.bNodes[v][u].status == Data.NodeStatus.SAFE) {
                    queue.add(v * cols + u);
                }
            }

            controller.finishWithFailure();
        }
    }
}
