package com.suslovila.client.gui;

import com.suslovila.common.tileEntity.TileTest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButtonInvisible extends GuiButton {
    TileTest tileAssemblyTable;

    public GuiButtonInvisible(int stateName, int x, int y, int width, int height, String displayString, TileTest tileTest) {
        super(stateName, x, y, width, height, displayString);
        tileAssemblyTable = tileTest;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    //super.drawButton(mc, mouseX,);
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public int getButtonHeight() {
        return this.height;
    }
}
