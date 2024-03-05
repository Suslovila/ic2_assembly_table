package com.suslovila.utils;

public class Position extends SusVec3 {
    public double x;
    public Position(int x, int y, int z) {
        super(x, y, z);
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getZ() {
        return (int) z;
    }
}
