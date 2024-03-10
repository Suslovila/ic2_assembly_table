package com.suslovila.utils;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class WorldHelper {
    public static boolean areBlocksBetween(SusVec3 posFrom, SusVec3 posTo, World world) {
//        MovingObjectPosition rayTrace = world.rayTraceBlocks(posFrom.toVec3(), posTo.toVec3());
//        return rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
        HashSet<SusPosition> poses = new HashSet<>(Arrays.asList(posFrom.toSusPos(), posTo.toSusPos()));
        SusVec3 delta = posTo.subtract(posFrom);
        SusVec3 ort = delta.normalize();
        for (int i = 1; i < delta.length(); i++) {
            SusVec3 scaledDelta = ort.scale(i);
            SusPosition pos = new SusPosition(
                    (int) (posFrom.x + scaledDelta.x),
                    (int) (posFrom.y + scaledDelta.y),
                    (int) (posFrom.x + scaledDelta.z)
            );
            if (world.getBlock(pos.x, pos.y, pos.z).getMaterial() != Material.air) {
                poses.add(pos);
            }
        }
        //only bounds
        return !(poses.size() == 2);
    }
}
