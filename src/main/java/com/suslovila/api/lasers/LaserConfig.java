package com.suslovila.api.lasers;


import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;


public class LaserConfig {
    public static final ArrayList<WrappedLaser> lasers = new ArrayList<>();

    public static void registerLasers() {
        String jsonPath = Paths.get(".").toAbsolutePath() + "/config/ic2_assembly_table/lasers.json";
        try (Reader reader = new FileReader(jsonPath)) {
            JsonElement json = new JsonParser().parse(reader);
            JsonArray lasersJson = json.getAsJsonArray();
            for (int i = 0; i < lasersJson.size(); i++) {
                JsonObject laserJson = lasersJson.get(i).getAsJsonObject();
                registerSingleLaser(
                        laserJson.get("meta").getAsInt(),
                        laserJson.get("name").getAsString(),
                        laserJson.get("texturePath").getAsString(),
                        laserJson.get("modelPath").getAsString(),
                        laserJson.get("euBufferCapacity").getAsDouble(),
                        laserJson.get("euPerTick").getAsDouble()
                );
            }
        } catch (Exception exception) {
            System.out.println("Error reading config: " + jsonPath);
            exception.printStackTrace();
        }
    }

    public static void registerSingleLaser(int meta, String name, String texturePath, String modelPath, double euBufferCapacity, double euPerTick) {
        lasers.add(new WrappedLaser(
                        meta,
                        name,
                        texturePath,
                        modelPath,
                        euBufferCapacity,
                        euPerTick
                )
        );
    }

    public static WrappedLaser getByMeta(int meta) {
        return lasers.get(meta);
    }
}
