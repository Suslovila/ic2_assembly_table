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
//
//    //draws line from specified position to zero of cord system
//    public static void drawFloatyLine(
//            double xFrom,
//            double yFrom,
//            double zFrom,
//            int color,
//            ResourceLocation texture,
//            float speed,
//            float distance,
//            float width,
//            float xScale,
//            float yScale,
//            float zScale,
//            float time
//    ) {
//        Color co = new Color(color);
//        float r = co.getRed() / 255.0f;
//        float g = co.getGreen() / 255.0f;
//        float b = co.getBlue() / 255.0f;
//        glDepthMask(false);
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
//
//        Tessellator tessellator = Tessellator.instance;
//
//        bindTexture(texture);
//        glDisable(GL_CULL_FACE);
//        tessellator.startDrawing(5);
//
//        tessellator.setBrightness(15728880);
//
//        float dist = MathHelper.sqrt_double(xFrom * xFrom + yFrom * yFrom + zFrom * zFrom);
//        long blocks = Math.round(dist);
//        double length = blocks * (Config.golemLinkQuality / 2.0f);
//        float f9 = 0.0f;
//        double x0 = 1.0;
//
//        int i = 0;
//        while (i <= length * distance) {
//            double f2 = i / length;
//            float f3 = (float) (1.0f - Math.abs(i - length / 2.0f) / (length / 2.0f));
//            double dx = xFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0)) * 0.2f * f3);
//            double dy = yFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0)) * 0.2f * f3);
//            double dz = zFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0)) * 0.2f * f3);
//            tessellator.setColorRGBA_F(r, g, b, f3);
//            double x3 = (1.0f - f2) * dist - time * speed;
//
//            dx /= xScale;
//            dy /= yScale;
//            dz /= zScale;
//
//            tessellator.addVertexWithUV(dx * f2, dy * f2 - width, dz * f2, x3, x0);
//            tessellator.addVertexWithUV(dx * f2, dy * f2 + width, dz * f2, x3, f9);
//            ++i;
//        }
//        tessellator.draw();
//
//        tessellator.startDrawing(5);
//        int var84 = 0;
//        while (var84 <= length * distance) {
//            double f2 = var84 / length;
//            float f3 = (float) (1.0f - Math.abs(var84 - length / 2.0f) / (length / 2.0f));
//            double dx = xFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0)) * 0.2f * f3);
//            double dy = yFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0)) * 0.2f * f3);
//            double dz = zFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0)) * 0.2f * f3);
//            tessellator.setColorRGBA_F(r, g, b, f3);
//            double x3 = (1.0f - f2) * dist - time * speed;
//
//            dx /= xScale;
//            dy /= yScale;
//            dz /= zScale;
//
//            tessellator.addVertexWithUV(dx * f2 - width, dy * f2, dz * f2, x3, x0);
//            tessellator.addVertexWithUV(dx * f2 + width, dy * f2, dz * f2, x3, f9);
//            ++var84;
//        }
//        tessellator.draw();
//        glEnable(GL_CULL_FACE);
//        glDisable(GL_BLEND);
//        glDepthMask(true);
//    }

    public static void bindTexture(ResourceLocation resource) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
    }

    public static void bindTexture(String resource) {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ExampleMod.MOD_ID, resource));
    }

    public static float getTimeForRender(float partialTicks) {
        return Minecraft.getMinecraft().renderViewEntity.ticksExisted + partialTicks;

    }

    public static void coDirectZOrtWith(SusVec3 vec3) {

    }

}
