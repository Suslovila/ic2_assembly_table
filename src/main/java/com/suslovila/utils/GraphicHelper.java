package com.suslovila.utils;


import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor4f;

public class GraphicHelper {
    public static void bindColor(int color, float alpha, float fadeFactor) {
        Color co = new Color(color);
        float r = co.getRed() / 255.0f;
        float g = co.getGreen()/ 255.0f;
        float b = co.getBlue() / 255.0f;
        glColor4f(r * fadeFactor, g * fadeFactor, b * fadeFactor, alpha);
    }
}
