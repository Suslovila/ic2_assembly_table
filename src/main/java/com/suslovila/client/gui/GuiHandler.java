package com.suslovila.client.gui;

import com.suslovila.client.GuiIds;
import com.suslovila.common.inventory.container.ContainerTest;
import com.suslovila.common.tileEntity.TileTest;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (!world.blockExists(x, y, z)) {
            return null;
        }

        TileEntity tile = world.getTileEntity(x, y, z);

        switch (id) {
            case GuiIds.ASSEMBLE_TABLE:{
                if (!(tile instanceof TileTest)) {
                    return null;
                }
                return new ContainerTest(player.inventory, (TileTest)tile);

            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (!world.blockExists(x, y, z)) {
            return null;
        }

        TileEntity tile = world.getTileEntity(x, y, z);

        switch (id) {
            case GuiIds.ASSEMBLE_TABLE:{
                if (!(tile instanceof TileTest)) {
                    return null;
                }
                return new GuiTileTest(player.inventory, (TileTest)tile);

            }
        }
        return null;
    }
}
