package com.suslovila.api.lasers;

public class WrappedLaser {
    //id is equal to metadata!
    public int meta;

    public String name;
    public String texturePath;
    public String modelPath;
    public double euBufferCapacity;
    public double euPerTick;

    public WrappedLaser(int meta, String name, String texturePath, String modelPath, double euBufferCapacity, double euPerTick) {
        this.meta = meta;
        this.name = name;
        this.texturePath = texturePath;
        this.modelPath = modelPath;
        this.euBufferCapacity = euBufferCapacity;
        this.euPerTick = euPerTick;
    }
}
