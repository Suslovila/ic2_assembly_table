package com.suslovila.common.block;


import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.common.tileEntity.TileEntityLaser;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.init.InternalName;
import net.minecraft.block.material.Material;

public class ModBlocks {
    public static BlockAssemblyTable table = new BlockAssemblyTable(Material.anvil);
    public static BlockLaser laser = new BlockLaser(InternalName.active);

    public static void register() {

        GameRegistry.registerBlock(table, "block_assemblyTable");
        GameRegistry.registerBlock(laser, "block_laser");

        GameRegistry.registerTileEntity(TileAssemblyTable.class, "tile_assemblytable");
        GameRegistry.registerTileEntity(TileEntityLaser.class, "tile_laser");
    }

    public static void registerRender() {
        //exampleRenderID = RenderingRegistry.getNextAvailableRenderId();

    }
}
