package com.suslovila.client.gui;

import com.suslovila.ExampleMod;
import com.suslovila.api.crafting.AssemblyTableRecipes;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.network.PacketHandler;
import com.suslovila.network.packet.PacketAssemblyTableRecipeSelected;
import com.suslovila.utils.GraphicHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;

class ButtonAssemblyTable extends GuiButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MOD_ID, "textures/gui/assembly_table.png");
    private static final int patternIsCurrentXPos = 196;
    private static final int patternCanStillCraftXPos = 177;
    private static final int patternCannotCraftXPos = 215;

    GuiAssemblyTable gui;

    public ButtonAssemblyTable(int id, int x, int y, int width, int height, GuiAssemblyTable gui) {
        super(id, x, y, width, height, "");
        this.gui = gui;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        TileAssemblyTable.AssemblyTablePattern pattern = gui.tile.getPatternById(this.id);
        if (pattern != null) {
            AssemblyTableRecipes.AssemblyTableRecipe recipe = AssemblyTableRecipes.instance().recipes.get(pattern.recipeId);
            if (pattern.isActive) {
                glEnable(GL_ALPHA_TEST);
                mc.renderEngine.bindTexture(TEXTURE);
                glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                int patternStateXPos = 0;
                TileAssemblyTable.AssemblyTablePattern currentPattern = gui.tile.currentPattern;
                if (currentPattern != null && currentPattern.equals(pattern)) {
                    patternStateXPos = patternIsCurrentXPos;
                } else if (gui.tile.canStillCraft(recipe)) {
                    patternStateXPos = patternCanStillCraftXPos;
                } else {
                    patternStateXPos = patternCannotCraftXPos;
                }
                drawTexturedModalRect(xPosition, yPosition, patternStateXPos, 1, 16, 16);

            }
            if (recipe != null) {
                drawStack(mc, recipe.result, this.xPosition, this.yPosition, 100f);
                if (isMouseOver()) {
                    for (int stackIndex = 0; stackIndex < recipe.inputs.size(); stackIndex++) {
                        ItemStack inputStack = recipe.inputs.get(stackIndex);
                        glPushMatrix();
                        float fadedColor = (float) Math.abs(Math.sin(GraphicHelper.getTimeForRender(0) / 8));
                        float colorRes = gui.tile.hasEnough(inputStack) ? 1.0f : fadedColor;
                        glColor4f(colorRes, colorRes, colorRes, 1f);
                        float scale = 0.9f;
                        glScalef(scale, scale, scale);
                        glTranslatef(mouseX / scale, mouseY / scale, 0);
                        drawStack(mc, inputStack, (int) (1 / scale + stackIndex * 15), (int) (1 / scale), 150f);
                        glPopMatrix();

                    }
                }
            }
            glColor4f(1f, 1f, 1f, 1f);

        }
    }

    public void drawStack(Minecraft mc, ItemStack item, int x, int y, float zLevel) {
        RenderHelper.enableGUIStandardItemLighting();
        glPushMatrix();
        glPushAttrib(GL_TRANSFORM_BIT);
        glEnable(GL12.GL_RESCALE_NORMAL);

        if (item != null) {
            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);

            float prevZ = gui.getItemRenderer().zLevel;
            gui.getItemRenderer().zLevel = zLevel;
            gui.getItemRenderer().renderWithColor = false;
            gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.getFontRenderer(), mc.renderEngine, item, x, y);
            gui.getItemRenderer().renderItemOverlayIntoGUI(gui.getFontRenderer(), mc.renderEngine, item, x, y);
            gui.getItemRenderer().zLevel = prevZ;
            gui.getItemRenderer().renderWithColor = true;
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_LIGHTING);
        }
        glPopAttrib();
        glPopMatrix();

    }


    public void playPressSound(SoundHandler soundHandlerIn) {
    }

}
