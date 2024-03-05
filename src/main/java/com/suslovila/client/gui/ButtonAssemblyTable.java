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
    //todo: при желании добавить рендер составляющих рецепта
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
        //super.drawButton(mc, mouseX, mouseY);
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
               drawStack(mc, recipe.result, this.xPosition, this.yPosition);
//                for(int stackIndex = 0; stackIndex < recipe.inputs.size(); stackIndex++){
//                    ItemStack inputStack = recipe.inputs.get(stackIndex);
//                    glPushMatrix();
//                    float colorRes = (gui.tile.hasEnough(inputStack) ? 1.0f : 0.3f);
//                    glColor4f(colorRes, colorRes, colorRes, 1f);
//                    glScaled(0.5, 0.5, 0.5);
//                    drawStack(mc, inputStack, xPosition + stackIndex * 4, yPosition + 5);
//                    glPopMatrix();
//                }
            }
        }
    }

    public void drawStack(Minecraft mc, ItemStack item, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        glPushMatrix();
        glPushAttrib(GL_TRANSFORM_BIT);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glEnable(GL12.GL_RESCALE_NORMAL);
        int i1 = 240;
        int k1 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1 / 1.0F, k1 / 1.0F);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (item != null) {
            glEnable(GL_LIGHTING);
            float prevZ = gui.getItemRenderer().zLevel;
            gui.getItemRenderer().zLevel = 200F;
            gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.getFontRenderer(), mc.renderEngine, item, x, y);
            gui.getItemRenderer().renderItemOverlayIntoGUI(gui.getFontRenderer(), mc.renderEngine, item, x, y);
            gui.getItemRenderer().zLevel = prevZ;
            glDisable(GL_LIGHTING);
        }
        glPopAttrib();
        glPopMatrix();

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
        //PacketHandler.INSTANCE.sendToServer(new PacketAssemblyTableRecipeSelected(gui.tile.xCoord, gui.tile.yCoord, gui.tile.zCoord, this.id));
    }
}
