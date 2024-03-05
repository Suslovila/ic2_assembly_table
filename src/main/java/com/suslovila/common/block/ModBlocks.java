package com.suslovila.common.block;


import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.common.tileEntity.TileEntityLaser;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.init.InternalName;
import net.minecraft.block.material.Material;

public class ModBlocks {
    public static int exampleRenderID = -1;

    public static BlockAssemblyTable table = new BlockAssemblyTable(Material.anvil);


    public static void register() {

        GameRegistry.registerBlock(table, "assemblyTable");

        GameRegistry.registerTileEntity(TileAssemblyTable.class, "assemble_table");
        GameRegistry.registerTileEntity(TileEntityLaser.class, "laser");
        new BlockLaser(InternalName.blockMachine);
    }

    public static void registerRender(){
        //exampleRenderID = RenderingRegistry.getNextAvailableRenderId();

    }
}
