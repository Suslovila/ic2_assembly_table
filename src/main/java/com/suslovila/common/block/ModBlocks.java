package com.suslovila.common.block;


import com.suslovila.api.lasers.LaserConfig;
import com.suslovila.api.lasers.WrappedLaser;
import com.suslovila.common.block.item.BlockLaserItem;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.common.tileEntity.TileEntityLaser;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.init.InternalName;
import net.minecraft.block.material.Material;

import java.util.ArrayList;

public class ModBlocks {
    public static BlockAssemblyTable table = new BlockAssemblyTable(Material.anvil);
    public static BlockLaser laser = new BlockLaser();

    public static void register() {

        GameRegistry.registerBlock(table, "block_assemblyTable");
        GameRegistry.registerBlock(laser, BlockLaserItem.class, "block_laser");

        GameRegistry.registerTileEntity(TileAssemblyTable.class, "tile_assemblytable");
        GameRegistry.registerTileEntity(TileEntityLaser.class, "tile_laser");

        LaserConfig.registerLasers();
    }

    public static void registerRender() {
    }
}
