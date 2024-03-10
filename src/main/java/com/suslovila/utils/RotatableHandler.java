package com.suslovila.utils;

import com.suslovila.ExampleMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import static org.lwjgl.opengl.GL11.glRotatef;

public class RotatableHandler {
    public static String TAG_FACING = ExampleMod.MOD_ID + "_facing";

    public static ForgeDirection readRotation(NBTTagCompound tag) {
        return ForgeDirection.getOrientation(tag.getByte(TAG_FACING));
    }

    public static void writeRotation(NBTTagCompound tag, ForgeDirection facing) {
        tag.setByte(TAG_FACING, (byte) facing.ordinal());
    }

    public static void rotateFromOrientation(ForgeDirection facing) {
        switch (facing) {
            case DOWN: {
                break;
            }
            case UP: {
                glRotatef(180f, 1f, 0f, 0f);
                break;
            }

            case NORTH: {
                glRotatef(90f, 1f, 0f, 0f);
                break;
            }

            case SOUTH: {
                glRotatef(-90f, 1f, 0f, 0f);
                break;
            }

            case WEST: {
                glRotatef(-90f, 0f, 0f, 1f);
                break;
            }

            case EAST: {
                glRotatef(90f, 0f, 0f, 1f);
                break;
            }
            default: {
                break;
            }
        }
    }

    public static SusVec3 getFacingVector(ForgeDirection facing) {
        return SusVec3.getVec3FromForgeDirection(facing);
    }
}