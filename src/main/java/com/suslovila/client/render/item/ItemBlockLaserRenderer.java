package com.suslovila.client.render.item;

import com.suslovila.ExampleMod;
import com.suslovila.api.lasers.LaserConfig;
import com.suslovila.client.render.tile.TileLaserRenderer;
import com.suslovila.utils.GraphicHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemBlockLaserRenderer implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GraphicHelper.bindTexture(TileLaserRenderer.textures.get(item.getMetadata()));
        if (type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        }
        GL11.glScaled(0.5, 0.5, 0.5);
        TileLaserRenderer.models.get(item.getMetadata()).renderAll();
        GL11.glPopMatrix();
    }
}
