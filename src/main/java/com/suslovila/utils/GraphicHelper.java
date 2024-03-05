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

    //draws line from specified position to zero of cord system
    public static void drawFloatyLine(
            double xFrom,
            double yFrom,
            double zFrom,
            int color,
            ResourceLocation texture,
            float speed,
            float distance,
            float width,
            float xScale,
            float yScale,
            float zScale,
            float time
    ) {
        Color co = new Color(color);
        float r = co.getRed() / 255.0f;
        float g = co.getGreen() / 255.0f;
        float b = co.getBlue() / 255.0f;
        glDepthMask(false);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        Tessellator tessellator = Tessellator.instance;

        bindTexture(texture);
        glDisable(GL_CULL_FACE);
        tessellator.startDrawing(5);

        tessellator.setBrightness(15728880);

        float dist = MathHelper.sqrt_double(xFrom * xFrom + yFrom * yFrom + zFrom * zFrom);
        long blocks = Math.round(dist);
        double length = blocks * (Config.golemLinkQuality / 2.0f);
        float f9 = 0.0f;
        double x0 = 1.0;

        int i = 0;
        while (i <= length * distance) {
            double f2 = i / length;
            float f3 = (float) (1.0f - Math.abs(i - length / 2.0f) / (length / 2.0f));
            double dx = xFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0)) * 0.2f * f3);
            double dy = yFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0)) * 0.2f * f3);
            double dz = zFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0)) * 0.2f * f3);
            tessellator.setColorRGBA_F(r, g, b, f3);
            double x3 = (1.0f - f2) * dist - time * speed;

            dx /= xScale;
            dy /= yScale;
            dz /= zScale;

            tessellator.addVertexWithUV(dx * f2, dy * f2 - width, dz * f2, x3, x0);
            tessellator.addVertexWithUV(dx * f2, dy * f2 + width, dz * f2, x3, f9);
            ++i;
        }
        tessellator.draw();

        tessellator.startDrawing(5);
        int var84 = 0;
        while (var84 <= length * distance) {
            double f2 = var84 / length;
            float f3 = (float) (1.0f - Math.abs(var84 - length / 2.0f) / (length / 2.0f));
            double dx = xFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 4.0)) * 0.2f * f3);
            double dy = yFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 3.0)) * 0.2f * f3);
            double dz = zFrom + (MathHelper.sin((float) (((dist * (1.0f - f2) * Config.golemLinkQuality / 2.0f) - (time % 32767.0f / 5.0f)) / 2.0)) * 0.2f * f3);
            tessellator.setColorRGBA_F(r, g, b, f3);
            double x3 = (1.0f - f2) * dist - time * speed;

            dx /= xScale;
            dy /= yScale;
            dz /= zScale;

            tessellator.addVertexWithUV(dx * f2 - width, dy * f2, dz * f2, x3, x0);
            tessellator.addVertexWithUV(dx * f2 + width, dy * f2, dz * f2, x3, f9);
            ++var84;
        }
        tessellator.draw();
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glDepthMask(true);
    }

    public static void bindTexture(ResourceLocation resource) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
    }
    public static void bindTexture(String resource) {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ExampleMod.MOD_ID, resource));
    }

    public static void drawFloatyLine(double x, double y, double z, double x2, double y2, double z2, float partialTicks, int color, String texture, float speed, float distance, float width) {
        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
        double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
        double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
        double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
        GL11.glTranslated(-iPX + x2, -iPY + y2, -iPZ + z2);
        float time = (float)(System.nanoTime() / 30000000L);
        Color co = new Color(color);
        float r = (float)co.getRed() / 255.0F;
        float g = (float)co.getGreen() / 255.0F;
        float b = (float)co.getBlue() / 255.0F;
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        Tessellator tessellator = Tessellator.instance;
        double dc1x = (double)((float)(x - x2));
        double dc1y = (double)((float)(y - y2));
        double dc1z = (double)((float)(z - z2));
        bindTexture(texture);
        GL11.glDisable(2884);
        tessellator.startDrawing(5);
        double dx2 = 0.0D;
        double dy2 = 0.0D;
        double dz2 = 0.0D;
        double d3 = x - x2;
        double d4 = y - y2;
        double d5 = z - z2;
        float dist = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
        float blocks = (float)Math.round(dist);
        float length = blocks * ((float)Config.golemLinkQuality / 2.0F);
        float f9 = 0.0F;
        float f10 = 1.0F;

        for(int i = 0; (float)i <= length * distance; ++i) {
            float f2 = (float)i / length;
            float f2a = (float)i * 1.5F / length;
            f2a = Math.min(0.75F, f2a);
            float f3 = 1.0F - Math.abs((float)i - length / 2.0F) / (length / 2.0F);
            double dx = dc1x + (double)(MathHelper.sin((float)((z % 16.0D + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / 4.0D)) * 0.5F * f3);
            double dy = dc1y + (double)(MathHelper.sin((float)((x % 16.0D + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / 3.0D)) * 0.5F * f3);
            double dz = dc1z + (double)(MathHelper.sin((float)((y % 16.0D + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / 2.0D)) * 0.5F * f3);
            tessellator.setColorRGBA_F(r, g, b, f3);
            float f13 = (1.0F - f2) * dist - time * speed;
            tessellator.addVertexWithUV(dx * (double)f2, dy * (double)f2 - (double)width, dz * (double)f2, (double)f13, (double)f10);
            tessellator.addVertexWithUV(dx * (double)f2, dy * (double)f2 + (double)width, dz * (double)f2, (double)f13, (double)f9);
        }

        tessellator.draw();
        tessellator.startDrawing(5);

        for(int var84 = 0; (float)var84 <= length * distance; ++var84) {
            float f2 = (float)var84 / length;
            float f2a = (float)var84 * 1.5F / length;
            f2a = Math.min(0.75F, f2a);
            float f3 = 1.0F - Math.abs((float)var84 - length / 2.0F) / (length / 2.0F);
            double dx = dc1x + (double)(MathHelper.sin((float)((z % 16.0D + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / 4.0D)) * 0.5F * f3);
            double dy = dc1y + (double)(MathHelper.sin((float)((x % 16.0D + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / 3.0D)) * 0.5F * f3);
            double dz = dc1z + (double)(MathHelper.sin((float)((y % 16.0D + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / 2.0D)) * 0.5F * f3);
            tessellator.setColorRGBA_F(r, g, b, f3);
            float f13 = (1.0F - f2) * dist - time * speed;
            tessellator.addVertexWithUV(dx * (double)f2 - (double)width, dy * (double)f2, dz * (double)f2, (double)f13, (double)f10);
            tessellator.addVertexWithUV(dx * (double)f2 + (double)width, dy * (double)f2, dz * (double)f2, (double)f13, (double)f9);
        }

        tessellator.draw();
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
    }

}
