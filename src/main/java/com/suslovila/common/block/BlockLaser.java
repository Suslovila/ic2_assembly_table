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

public class BlockLaser extends BlockMultiID {
    protected BlockLaser(InternalName name) {
        super(name, Material.iron);
    }

    @Override
    public Class<? extends TileEntity> getTeClass(int i, MutableObject<Class<?>[]> mutableObject, MutableObject<Object[]> mutableObject1) {
        return TileEntityLaser.class;
    }
}
