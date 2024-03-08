package com.suslovila.common.block;

import com.suslovila.ExampleMod;
import com.suslovila.client.GuiIds;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.common.tileEntity.TileEntityLaser;
import ic2.core.block.BlockMultiID;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableObject;

public class BlockLaser extends BlockContainer {
    protected BlockLaser(InternalName name) {
        super(Material.iron);
    }

    public TileEntity createNewTileEntity(World world, int metadata){
        return new TileEntityLaser();
    }
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return -1;
    }

    public int getRenderBlockPass() {
        return 1;
    }
}
