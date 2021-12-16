package com.phongvan.bomberman.map;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.entities.Entities;
import com.phongvan.bomberman.entities.mobs.Ballooms;
import com.phongvan.bomberman.entities.destructible.Brick;
import com.phongvan.bomberman.entities.indestructible.Grass;
import com.phongvan.bomberman.entities.indestructible.Wall;
import com.phongvan.bomberman.entities.mobs.*;
import com.phongvan.bomberman.entities.powerups.*;
import com.phongvan.bomberman.gui.PaneController;

import java.util.*;

public class MapHandler {
    private static MapHandler instance = null;
    private int level;
    private int rows;
    private int cols;
    private int portalX;
    private int portalY;
    private int[][] map;

    public static final int[] dh = {-1, 0, 1,  0};
    public static final int[] dc = { 0, 1, 0, -1};

    public static final int DEFAULT_TILE_SIZE = 16;

    public static final int GRASS_TILE = 7;
    public static final int WALL_TILE = 6;
    public static final int BRICKS_TILE = 8;
    public static final int PORTAL_TILE = 5;
    public static final int BOMBERMAN_TILE = 2;
    public static final int BALLOOM_TILE = 26;
    public static final int ONEAL_TILE = 12;
    public static final int DOLL_TILE = 14;
    public static final int MINVO_TILE = 105;
    public static final int KONDORIA_TILE = 91;
    public static final int OVAPI_TILE = 87;
    public static final int PASS_TILE = 101;
    public static final int PONTAN_TILE = 93;
    public static final int BOMB_TILE = 161;
    public static final int FLAME_TILE = 162;
    public static final int SPEED_TILE = 163;
    public static final int WALL_PASS_TILE = 164;
    public static final int DETONATOR_TILE = 165;

    private MapHandler() {}

    public static MapHandler getInstance() {
        if (instance == null) {
            instance = new MapHandler();
        }

        return instance;
    }

    public void loadMap(int level) {
        String[] data = XMLConverter.getXMLData(level);
        map = new int[rows][cols];

        for (int i = 1; i < data.length; ++i) {
            String[] line = data[i].split(",");
            for (int j = 0; j < cols; ++j) {
                map[i - 1][j] = Integer.parseInt(line[j]);
            }
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
                    } else if (map[i][j] == SPEED_TILE) {
                        Core.getInstance().addNewPowerUps(new Speed(j, i));
                        Core.getInstance().addNewTile(new Brick(j, i));
                        map[i][j] = BRICKS_TILE;
                    } else if (map[i][j] == DETONATOR_TILE) {
                        Core.getInstance().addNewPowerUps(new Detonator(j, i));
                        Core.getInstance().addNewTile(new Brick(j, i));
                        map[i][j] = BRICKS_TILE;
                    } else if (map[i][j] == WALL_PASS_TILE) {
                        Core.getInstance().addNewPowerUps(new WallPass(j, i));
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
                    } else if (map[i][j] == DOLL_TILE) {
                        Core.getInstance().addNewMob(new Doll(j, i, null));
                        Core.getInstance().addNewTile(new Grass(j, i));
                        map[i][j] = GRASS_TILE;
                    } else if (map[i][j] == MINVO_TILE) {
                        Core.getInstance().addNewMob(new Minvo(j, i, null));
                        Core.getInstance().addNewTile(new Grass(j, i));
                        map[i][j] = GRASS_TILE;
                    } else if (map[i][j] == KONDORIA_TILE) {
                        Core.getInstance().addNewMob(new Kondoria(j, i, null));
                        Core.getInstance().addNewTile(new Grass(j, i));
                        map[i][j] = GRASS_TILE;
                    } else if (map[i][j] == OVAPI_TILE) {
                        Core.getInstance().addNewMob(new Ovapi(j, i, null));
                        Core.getInstance().addNewTile(new Grass(j, i));
                        map[i][j] = GRASS_TILE;
                    } else if (map[i][j] == PASS_TILE) {
                        Core.getInstance().addNewMob(new Pass(j, i, null));
                        Core.getInstance().addNewTile(new Grass(j, i));
                        map[i][j] = GRASS_TILE;
                    } else if (map[i][j] == PONTAN_TILE) {
                        Core.getInstance().addNewMob(new Pontan(j, i, null));
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

    private boolean checkTileCollision(double thisX, double thisY, double otherX, double otherY,
                                       boolean wallPass, boolean bombPass, boolean isBomber) {
        double rightA = thisX + getTileSize();
        double bottomA = thisY + getTileSize();

        double rightB = otherX + getTileSize();
        double bottomB = otherY + getTileSize();

        int tileX = (int) (otherX / getTileSize());
        int tileY = (int) (otherY / getTileSize());

        if (map[tileY][tileX] == WALL_TILE || (map[tileY][tileX] == BRICKS_TILE && !wallPass)
        || (Core.getInstance().thisTileHadBomb(tileX, tileY) && (isBomber && !Core.getInstance().getPlayer().isStepOnBomb()))
        || (Core.getInstance().thisTileHadBomb(tileX, tileY) && (!isBomber && !bombPass))) {
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
    public boolean canMoveTo(int newDirection, double x, double y, boolean wallPass, boolean bombPass, boolean isBomber) {
        int tileX = (int) (x + getTileSize() / 2) / getTileSize();
        int tileY = (int) (y + getTileSize() / 2) / getTileSize();

        if (Core.getInstance().thisTileHadBomb(tileX, tileY)) {
            if ((!isBomber && !bombPass) || (isBomber && !Core.getInstance().getPlayer().isStepOnBomb())) {
                return false;
            }
        }

        if (map[tileY][tileX] == GRASS_TILE || map[tileY][tileX] == PORTAL_TILE
                || (map[tileY][tileX] == BRICKS_TILE && wallPass)) {
            switch (newDirection) {
                case Mob.UP_DIR -> --tileY;
                case Mob.RIGHT_DIR -> ++tileX;
                case Mob.DOWN_DIR -> ++tileY;
                case Mob.LEFT_DIR -> --tileX;
            }

            return !checkTileCollision(x, y, tileX * getTileSize(), tileY * getTileSize(),
                                        wallPass, bombPass, isBomber);
        }

        return false;
    }

    public boolean canTurn(int tileX, int tileY, int newDirection, boolean wallPass) {
        switch (newDirection) {
            case Mob.UP_DIR -> --tileY;
            case Mob.RIGHT_DIR -> ++tileX;
            case Mob.DOWN_DIR -> ++tileY;
            case Mob.LEFT_DIR -> --tileX;
        }

        return (map[tileY][tileX] == GRASS_TILE || map[tileY][tileX] == PORTAL_TILE
                || (map[tileY][tileX] == BRICKS_TILE && wallPass));
    }

    public synchronized void findShortestPath(Mob start, Entities end) {
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
            //System.out.println(current.getX() + " " + current.getY());
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
                if (tileX < 1 || tileY < 1 || tileX > cols || tileY > rows
                        || map[tileY][tileX] == WALL_TILE || (map[tileY][tileX] == BRICKS_TILE && !start.canWallPass())
                        || (Core.getInstance().thisTileHadBomb(tileX, tileY) && !start.canBombPass())) {
                    continue;
                }
                //System.out.println(dir + " " + tileX + " " + tileY);
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

    public void setTile(int tileX, int tileY, int tileType) {
        map[tileY][tileX] = tileType;
    }

    public int getTileType(int tileX, int tileY) {
        return map[tileY][tileX];
    }

    public boolean isPortalPosition(int tileX, int tileY) {
        return tileX == portalY && tileY == portalX;
    }

    public int getPortalX() {
        return portalX;
    }

    public int getPortalY() {
        return portalY;
    }

    public int getManhattanDistance(int startTileX, int startTileY, int endTileX, int endTileY) {
        return Math.abs(startTileX - endTileX) + Math.abs(startTileY - endTileY);
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
}
