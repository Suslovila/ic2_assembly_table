package com.suslovila.utils;


import com.suslovila.Config;
import com.suslovila.ExampleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class GraphicHelper {
    public static void bindColor(int color, float alpha, float fadeFactor) {
        Color co = new Color(color);
        float r = co.getRed() / 255.0f;
        float g = co.getGreen() / 255.0f;
        float b = co.getBlue() / 255.0f;
        glColor4f(r * fadeFactor, g * fadeFactor, b * fadeFactor, alpha);
    }


    public static void bindTexture(ResourceLocation resource) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
    }

    public static void bindTexture(String resource) {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ExampleMod.MOD_ID, resource));
    }

    public static float getTimeForRender(float partialTicks) {
        return Minecraft.getMinecraft().renderViewEntity.ticksExisted + partialTicks;

    }
}
