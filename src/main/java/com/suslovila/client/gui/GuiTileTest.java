package com.suslovila.client.gui;

import com.suslovila.ExampleMod;
import com.suslovila.common.tileEntity.TileTest;
import com.suslovila.common.inventory.container.ContainerTest;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiTileTest extends GuiContainer {
    private static final ResourceLocation IMAGE_URL = new ResourceLocation(ExampleMod.MOD_ID, "textures/gui/assembly_table.png");

    TileTest tile;

    public GuiTileTest(InventoryPlayer inventoryPlayer, TileTest assembleTable) {
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
        for (int i = 0; i < TileTest.patternAmount; i++) {
            GuiButton button = new GuiButtonInvisible(i, 100, 200, 100, 20, "");
            this.buttonList.add(button);
            this.buttonList.add(new button(50 + 18 * j, 20 + 18 * i));

        }
    }
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if(button.id == 1)
        {
            System.out.println("My Button is Clicked!");
        }
    }
}
