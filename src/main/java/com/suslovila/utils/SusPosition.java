package com.suslovila.utils;


//not really beautiful class, but still
public class SusPosition {
    int x;
    int y;
    int z;

    public SusPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SusPosition)) return false;
        SusPosition position = (SusPosition) obj;
        return this.x == position.x && this.y == position.y && this.z == position.z;
    }
}
