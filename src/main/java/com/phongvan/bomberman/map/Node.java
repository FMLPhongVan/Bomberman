package com.phongvan.bomberman.map;

public class Node {
    private int x;
    private int y;
    private Node parent;
    private double fCost;
    private double gCost;
    private double hCost;

    public Node(int x, int y, Node parent, double gCost, double hCost) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        fCost = gCost + hCost;
    }

    public boolean isTargetNode(int endTileX, int endTileY) {
        return x == endTileX && y == endTileY;
    }

    public void setfCost(double fCost) {
        this.fCost = fCost;
    }

    public void setgCost(double gCost) {
        this.gCost = gCost;
    }

    public void sethCost(double hCost) {
        this.hCost = hCost;
    }

    public double getfCost() {
        return fCost;
    }

    public double getgCost() {
        return gCost;
    }

    public double gethCost() {
        return hCost;
    }

    public Node getParent() {
        return parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return String.format("x=%d, y=%d, g=%f, h=%f, f=%f", x, y, gCost, hCost, fCost);
    }
}
