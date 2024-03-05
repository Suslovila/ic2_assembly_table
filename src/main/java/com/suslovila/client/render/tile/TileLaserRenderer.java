package com.suslovila.client.render.tile;

import com.suslovila.api.ILaserTarget;
import com.suslovila.common.tileEntity.TileEntityLaser;
import com.suslovila.utils.GraphicHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import static org.lwjgl.opengl.GL11.*;

public class TileLaserRenderer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityLaser)) return;
        TileEntityLaser tileLaser = (TileEntityLaser) tile;
        renderLaser(tileLaser, x, y, z, partialTicks);
        glPushMatrix();
        glTranslated(x + 0.5, y + 0.5, z + 0.5);
        glPopMatrix();
    }

    private void renderLaser(TileEntityLaser tileLaser, double x, double y, double z, float partialTicks) {
        if (tileLaser.laserDestinationPos == null) return;
        GraphicHelper.drawFloatyLine(
                tileLaser.xCoord,
                tileLaser.yCoord,
                tileLaser.zCoord,
                tileLaser.laserDestinationPos.x,
                tileLaser.laserDestinationPos.y,
                tileLaser.laserDestinationPos.z,
                partialTicks,
                0,
                "textures/misc/wispy.png",
                0.01f,
                Math.min(partialTicks, 10.0F),
                0.15f
        );
    }
}
