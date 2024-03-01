package com.suslovila.client.gui;

import com.suslovila.ExampleMod;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.common.inventory.container.ContainerTest;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAssemblyTable extends GuiContainer {
    private static final ResourceLocation IMAGE_URL = new ResourceLocation(ExampleMod.MOD_ID, "textures/gui/assembly_table.png");

    TileAssemblyTable tile;

    public GuiAssemblyTable(InventoryPlayer inventoryPlayer, TileAssemblyTable assembleTable) {
        super(new ContainerTest(inventoryPlayer, assembleTable));
        xSize = 176;
        ySize = 207;
        tile = assembleTable;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(IMAGE_URL);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

    }

    @Override
    public void initGui() {
        super.initGui();
        //важно: id кнопок напрямую связаны с логикой выбора шаблона
        for (int i = 0; i < TileAssemblyTable.patternAmount; i++) {
            GuiButton button = new ButtonAssemblyTable(i, guiLeft + 17 + 18 * i, guiTop + 9, 16, 16, this);
            this.buttonList.add(button);
        }
    }
    @Override
    protected void actionPerformed(GuiButton button)
    {
    }
    public RenderItem getItemRenderer() {
        return itemRender;
    }
    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }
    public int getGuiLeft(){
        return guiLeft;
    }
    public int getGuiTop(){
        return guiTop;
    }
}
