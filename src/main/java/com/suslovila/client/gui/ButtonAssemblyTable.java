package com.suslovila.client.gui;

import com.suslovila.ExampleMod;
import com.suslovila.api.crafting.AssemblyTableRecipes;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.network.PacketHandler;
import com.suslovila.network.packet.PacketAssemblyTableRecipeSelected;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

class ButtonAssemblyTable extends GuiButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MOD_ID, "textures/gui/assembly_table.png");


    GuiAssemblyTable gui;

    public ButtonAssemblyTable(int id, int x, int y, int width, int height, GuiAssemblyTable gui) {
        super(id, x, y, width, height, "");
        this.gui = gui;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        //super.drawButton(mc, mouseX, mouseY);
        TileAssemblyTable.AssemblyTablePattern pattern = gui.tile.getPatternById(this.id);
        if (pattern != null) {
            AssemblyTableRecipes.AssemblyTableRecipe recipe = AssemblyTableRecipes.instance().recipes.get(pattern.recipeId);
            if (pattern.isActive) {
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                mc.renderEngine.bindTexture(TEXTURE);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                TileAssemblyTable.AssemblyTablePattern currentPattern = gui.tile.currentPattern;
                if (currentPattern !=null &&  currentPattern.equals(pattern)) {
                    drawTexturedModalRect(xPosition, yPosition, 196, 1, 16, 16);
                } else if (gui.tile.canStillCraft(recipe)) {
                    drawTexturedModalRect(xPosition, yPosition, 177, 1, 16, 16);
                }
                else{
                    drawTexturedModalRect(xPosition, yPosition, 215, 1, 16, 16);
                }
            }
            if (recipe != null) {
                drawStack(mc, recipe.result, this.xPosition, this.yPosition);
            }
        }
    }

    public void drawStack(Minecraft mc, ItemStack item, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        int i1 = 240;
        int k1 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1 / 1.0F, k1 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (item != null) {
            GL11.glEnable(GL11.GL_LIGHTING);
            float prevZ = gui.getItemRenderer().zLevel;
            gui.getItemRenderer().zLevel = 200F;
            gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.getFontRenderer(), mc.renderEngine, item, x, y);
            gui.getItemRenderer().renderItemOverlayIntoGUI(gui.getFontRenderer(), mc.renderEngine, item, x, y);
            gui.getItemRenderer().zLevel = prevZ;
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        GL11.glPopAttrib();
        GL11.glPopMatrix();

    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        //soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public int getButtonHeight() {
        return this.height;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PacketHandler.INSTANCE.sendToServer(new PacketAssemblyTableRecipeSelected(gui.tile.xCoord, gui.tile.yCoord, gui.tile.zCoord, this.id));
    }
}
