package com.suslovila;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    private static String categoryName = "ic2AssemblyTable";
    public static int laserSinkTier;
    public static int golemLinkQuality = 16;

    public static int laserBufferCapacity;
    public static int laserEnergyTransferAmountPerTick;
    public static void registerServerConfig(File modCfg) {
        Configuration cfg = new Configuration(modCfg);
        try {
            laserBufferCapacity = cfg.getInt(
                    "laserBufferCapacity",
                    categoryName,
                    10_000,
                    0,
                    Integer.MAX_VALUE,
                    "Laser EU buffer capacity"
            );

            laserSinkTier = cfg.getInt(
                    "laserSinkTier",
                    categoryName,
                    5,
                    0,
                    Integer.MAX_VALUE,
                    "Laser EU buffer capacity"
            );
            laserEnergyTransferAmountPerTick = cfg.getInt(
                    "laserEnergyTransferAmountPerTick",
                    categoryName,
                    1,
                    1,
                    Integer.MAX_VALUE,
                    "Laser EU buffer capacity"
            );

        } catch (Exception exception) {
            System.out.println("error writing config for mod: " + ExampleMod.NAME);
        } finally {
            cfg.save();
        }
    }
}
