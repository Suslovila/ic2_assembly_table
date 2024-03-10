package com.suslovila.common.block.item;

import com.suslovila.common.tileEntity.TileEntityLaser;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLaserItem extends ItemBlock {
    public BlockLaserItem(Block block) {
        super(block);
        this.setMaxDurability(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int meta) {
        return meta;
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        if(world.isRemote) return false;
        if (placed) {
            try {
                TileEntityLaser laser = (TileEntityLaser) world.getTileEntity(x, y, z);
                laser.setFacing((short) ForgeDirection.getOrientation(side).ordinal());
                laser.markDirty();
                world.markBlockForUpdate(x, y, z);
            } catch (Exception exception) {
               System.out.println("error placing block");
                exception.printStackTrace();
            }
        }

        return placed;
    }
}
