package com.suslovila.common.block;


import com.suslovila.common.tileEntity.TileAssemblyTable;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;

public class ModBlocks {
    public static int exampleRenderID = -1;

    public static BlockTest block = new BlockTest(Material.anvil);

    public static void register() {

        GameRegistry.registerBlock(block, "test");

        GameRegistry.registerTileEntity(TileAssemblyTable.class, "assemble_table");

    }

    public static void registerRender(){
        //exampleRenderID = RenderingRegistry.getNextAvailableRenderId();

    }
}
