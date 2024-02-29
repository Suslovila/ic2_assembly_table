package com.suslovila;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    public static boolean pvpLiteEnabled = false;
    public static int arenaWorldId;

    public static boolean consumeEldritchDiaryAfterUse;
    public static String modPrefix = "somePrefix";


    public static void registerServerConfig(File modCfg) {
        Configuration cfg = new Configuration(modCfg);
        try {

            //EXAMPLES

            pvpLiteEnabled = cfg.getBoolean(
				"EnablePvPLite",
	            "PvPLite",
	            false,
	            "Включить ограничения для PvPLite мира");

            arenaWorldId = cfg.getInt(
				"PvPLiteWorldID",
	            "PvPLite",
	            666,
	            0,
	            Integer.MAX_VALUE,
	            "ID PvPLite Мира"
            );

            modPrefix = cfg.getString(
				"ModPrefix",
	            "core", modPrefix,
	            "Префикс системных сообщений мода"
            );
        } catch (Exception var8) {
	        System.out.println("what");
        } finally {
            cfg.save();
        }
    }
}
