package com.phongvan.bomberman.map;

import com.phongvan.bomberman.Bomberman;
import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.entities.Entities;
import com.phongvan.bomberman.entities.mobs.Ballooms;
import com.phongvan.bomberman.entities.destructible.Brick;
import com.phongvan.bomberman.entities.destructible.Portal;
import com.phongvan.bomberman.entities.indestructible.Grass;
import com.phongvan.bomberman.entities.indestructible.Wall;
import com.phongvan.bomberman.entities.mobs.*;
import com.phongvan.bomberman.entities.powerups.Bombs;
import com.phongvan.bomberman.entities.powerups.Flames;
import com.phongvan.bomberman.gui.PaneController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class MapHandler {
    private static MapHandler instance = null;
    private int level;
    private int rows;
    private int cols;
    private int portalX;
    private int portalY;
    private Character[][] map;

    private static final int[] dh = {-1, 0, 1,  0};
    private static final int[] dc = { 0, 1, 0, -1};

    public static final int DEFAULT_TILE_SIZE = 16;

    public static final Character GRASS_TILE = ' ';
    public static final Character WALL_TILE = '#';
    public static final Character BRICKS_TILE = '*';
    public static final Character PORTAL_TILE = 'x';
    public static final Character BOMBERMAN_TILE = 'p';
    public static final Character BALLOOM_TILE = '1';
    public static final Character ONEAL_TILE = '2';
    public static final Character BOMB_TILE = 'b';
    public static final Character FLAME_TILE = 'f';

    private MapHandler() {}

    public static MapHandler getInstance() {
        if (instance == null) {
            instance = new MapHandler();
        }

        return instance;
    }

    public void loadMap(int level) {
        try {
            InputStream input = Bomberman.class.getResourceAsStream("levels/level " + level + ".txt");
            assert input != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

            String data;
            data = reader.readLine();
            level = Integer.parseInt(data);

            data = reader.readLine();
            rows = Integer.parseInt(data);

            data = reader.readLine();
            cols = Integer.parseInt(data);

            map = new Character[rows][cols];

            int curRows = 0;
            while ((data = reader.readLine()) != null) {
                for (int i = 0; i < data.length(); ++i) {
                    map[curRows][i] = data.charAt(i);
                }

                ++curRows;
            }

            reader.close();

        } catch (IOException | NullPointerException e) {
            Logger.log(Logger.ERROR, "MapHandler", e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void processNewBoard() {
        portalX = portalY = -1;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (map[i][j] == WALL_TILE) {
                    Core.getInstance().addNewTile(new Wall(j, i));
                } else if (map[i][j] == GRASS_TILE) {
                    Core.getInstance().addNewTile(new Grass(j, i));
                } else if (map[i][j] == BRICKS_TILE) {
                    Core.getInstance().addNewTile(new Brick(j, i));
                } else if (map[i][j] == PORTAL_TILE) {
                    portalX = i; portalY = j;
                    Core.getInstance().addNewTile(new Brick(j, i));
                    map[i][j] = BRICKS_TILE;
                } else {
                    if (map[i][j] == FLAME_TILE) {
                        Core.getInstance().addNewPowerUps(new Flames(j, i));
                        Core.getInstance().addNewTile(new Brick(j, i));
                        map[i][j] = BRICKS_TILE;
                    } else if (map[i][j] == BOMB_TILE) {
                        Core.getInstance().addNewPowerUps(new Bombs(j, i));
                        Core.getInstance().addNewTile(new Brick(j, i));
                        map[i][j] = BRICKS_TILE;
                    } else if (map[i][j] == BALLOOM_TILE) {
                        Core.getInstance().addNewMob(new Ballooms(j, i, null));
                        Core.getInstance().addNewTile(new Grass(j, i));
                        map[i][j] = GRASS_TILE;
                    } else if (map[i][j] == ONEAL_TILE) {
                        Core.getInstance().addNewMob(new Oneal(j, i, null));
                        Core.getInstance().addNewTile(new Grass(j, i));
                        map[i][j] = GRASS_TILE;
                    } else {
                        Core.getInstance().addNewTile(new Grass(j, i));
                        map[i][j] = GRASS_TILE;
                    }
                }
            }
        }
    }

    public int countPossibleWays(int tileX, int tileY) {
        int cnt = 0;
        cnt += (map[tileY + 1][tileX] == GRASS_TILE || map[tileY + 1][tileX] == PORTAL_TILE) ? 1 : 0;
        cnt += (map[tileY - 1][tileX] == GRASS_TILE || map[tileY - 1][tileX] == PORTAL_TILE) ? 1 : 0;
        cnt += (map[tileY][tileX + 1] == GRASS_TILE || map[tileY][tileX + 1] == PORTAL_TILE) ? 1 : 0;
        cnt += (map[tileY][tileX - 1] == GRASS_TILE || map[tileY][tileX - 1] == PORTAL_TILE) ? 1 : 0;

        return cnt;
    }

    public boolean isTurnTile(int tileX, int tileY) {
        return countPossibleWays(tileX, tileY) == 2;
    }

    public boolean isForkTile(int tileX, int tileY) {
        return countPossibleWays(tileX, tileY) == 3;
    }

    public boolean isCrossTile(int tileX, int tileY) {
        return countPossibleWays(tileX, tileY) == 4;
    }

    private boolean checkTileCollision(double thisX, double thisY, double otherX, double otherY, boolean isBomber) {
        double rightA = thisX + getTileSize();
        double bottomA = thisY + getTileSize();

        double rightB = otherX + getTileSize();
        double bottomB = otherY + getTileSize();

        int tileX = (int) (otherX / getTileSize());
        int tileY = (int) (otherY / getTileSize());

        if (map[tileY][tileX] == WALL_TILE || map[tileY][tileX] == BRICKS_TILE
        || (Core.getInstance().thisTileHadBomb(tileX, tileY) && (isBomber && !Core.getInstance().getPlayer().isStepOnBomb()))) {
            if (bottomA <= otherY) {
                return false;
            }
            if (thisY >= bottomB) {
                return false;
            }
            if (rightA <= otherX) {
                return false;
            }
            if (thisX >= rightB) {
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     *
     *
     * @param newDirection
     * @param x
     * @param y
     * @param isBomber Special check if the mob is the bomber.
     * @return
     */
    public boolean canMoveTo(int newDirection, double x, double y, boolean isBomber) {
        int tileX = (int) (x + getTileSize() / 2) / getTileSize();
        int tileY = (int) (y + getTileSize() / 2) / getTileSize();

        if (Core.getInstance().thisTileHadBomb(tileX, tileY)) {
            if (!isBomber || (isBomber && !Core.getInstance().getPlayer().isStepOnBomb())) {
                return false;
            }
        }

        if (map[tileY][tileX] == GRASS_TILE || map[tileY][tileX] == PORTAL_TILE) {
            switch (newDirection) {
                case Mob.UP_DIR -> --tileY;
                case Mob.RIGHT_DIR -> ++tileX;
                case Mob.DOWN_DIR -> ++tileY;
                case Mob.LEFT_DIR -> --tileX;
            }

            return !checkTileCollision(x, y, tileX * getTileSize(), tileY * getTileSize(), isBomber);
        }

        return false;
    }

    public boolean canTurn(int tileX, int tileY, int newDirection) {
        switch (newDirection) {
            case Mob.UP_DIR -> --tileY;
            case Mob.RIGHT_DIR -> ++tileX;
            case Mob.DOWN_DIR -> ++tileY;
            case Mob.LEFT_DIR -> --tileX;
        }

        return (map[tileY][tileX] == GRASS_TILE || map[tileY][tileX] == PORTAL_TILE);
    }

    public synchronized void findShortestPath(Mob start, Entities end) {
        Set<Node> queue = new TreeSet<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.getfCost(), o2.getfCost());
            }
        });
        double[][] visited = new double[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j)
                visited[i][j] = -1;
        }
        int tileSize = getTileSize();
        int tileX, tileY;
        int endTileX, endTileY;
        double gCost, hCost, fCost;
        tileX = (int) ((start.getX() + tileSize / 2)  / tileSize);
        tileY = (int) ((start.getY() + tileSize / 2)  / tileSize);
        endTileX = (int) ((end.getX() + tileSize / 2)  / tileSize);
        endTileY = (int) ((end.getY() + tileSize / 2)  / tileSize);
        gCost = 0;
        hCost = getManhattanDistance(tileX, tileY, (int) (end.getX() / getTileSize()), (int) (end.getY() / getTileSize()));
        Node current = new Node(tileX, tileY, null, 0, hCost);
        if (current.isTargetNode(endTileX, endTileY)) {
            start.setDirection(Mob.UNSET);
            return;
        }
        queue.add(current);

        while (!queue.isEmpty()) {
            current = queue.iterator().next();
            System.out.println(current.getX() + " " + current.getY());
            if (current.isTargetNode(endTileX, endTileY)) {
                while (current.getParent() != null) {
                    if (current.getParent().getParent() == null) {
                        for (int dir = 0; dir < 4; ++dir) {
                            if (current.getX() == current.getParent().getX() + dc[dir]
                                    && current.getY() == current.getParent().getY() + dh[dir]) {
                                start.setDirection(dir);
                                queue.clear();
                                return;
                            }
                        }
                    }
                    current = current.getParent();
                }
            }

            tileX = current.getX();
            tileY = current.getY();
            visited[tileY][tileX] = current.getfCost();
            queue.remove(current);

            for (int dir = 0; dir < 4; ++dir) {
                tileX = current.getX() + dc[dir];
                tileY = current.getY() + dh[dir];
                if (tileX < 1 || tileY < 1 || tileX > cols || tileY > rows) continue;
                if (map[tileY][tileX] == WALL_TILE || map[tileY][tileX] == BRICKS_TILE) continue;
                gCost = current.getgCost() + 1;
                hCost = getManhattanDistance(tileX, tileY, endTileX, endTileY);
                fCost = gCost + hCost;
                if (visited[tileY][tileX] == -1) {
                    queue.add(new Node(tileX, tileY, current, gCost, hCost));
                } else if (visited[tileY][tileX] > fCost) {
                    queue.add(new Node(tileX, tileY, current, gCost, hCost));
                }
            }
        }

        start.setDirection(Mob.UNSET);
    }

    public boolean hasPlantedBomb(int tileX, int tileY) {
        return map[tileY][tileX] == BOMB_TILE;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMapWidth() {
        return cols * DEFAULT_TILE_SIZE * (int) PaneController.getInstance().getScaleSize();
    }

    public int getMapHeight() {
        return rows * DEFAULT_TILE_SIZE * (int) PaneController.getInstance().getScaleSize();
    }

    public int getTileSize() {
        return DEFAULT_TILE_SIZE * PaneController.getInstance().getScaleSize();
    }

    public void setTile(int tileX, int tileY, Character tileType) {
        map[tileY][tileX] = tileType;
    }

    public Character getTileType(int tileX, int tileY) {
        return map[tileY][tileX];
    }

    public boolean isPortalPosition(int tileX, int tileY) {
        return tileX == portalY && tileY == portalX;
    }

    private int getManhattanDistance(int startTileX, int startTileY, int endTileX, int endTileY) {
        return Math.abs(startTileX - endTileX) + Math.abs(startTileY - endTileY);
    }
}
