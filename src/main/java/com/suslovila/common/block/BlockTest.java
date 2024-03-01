package com.suslovila.common.block;

import com.suslovila.ExampleMod;
import com.suslovila.client.GuiIds;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTest extends BlockContainer {
    //todo: не забыть добаить дроп предметов при поломке!!!!!!!!!
    protected BlockTest(Material material) {
        super(material);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAssemblyTable();
    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX, float clickY, float clickZ) {
        Block block = world.getBlock(x, y, z);
        TileEntity tile = world.getTileEntity(x, y, z);
        // Открыть GUI только, если игрок, блок и TileEntity не равны нулю, а также TileEntity является инстанцией нужного.
        if (block != null && tile instanceof TileAssemblyTable && player != null) {
            // Открыть GUI, если игрок не сидит.
            if (!player.isSneaking()) {
                // Открыть у игрока GUI из мода (первый аргумент) под id (второй аргумент).
                player.openGui(ExampleMod.instance, GuiIds.ASSEMBLE_TABLE, world, x, y, z);
                return true;
            }
        }
        return false;
    }
}
