package com.suslovila;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    private static String categoryName = "ic2AssemblyTable";
    public static int laserSinkTier;
    public static void registerServerConfig(File modCfg) {
        Configuration cfg = new Configuration(modCfg);
        try {

            laserSinkTier = cfg.getInt(
                    "laserSinkTier",
                    categoryName,
                    5,
                    0,
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
