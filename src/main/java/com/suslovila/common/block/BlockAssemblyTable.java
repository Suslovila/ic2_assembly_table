package com.suslovila.common.block;

import com.suslovila.ExampleMod;
import com.suslovila.api.ILaserTargetBlock;
import com.suslovila.client.GuiIds;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class BlockAssemblyTable extends BlockContainer implements ILaserTargetBlock {
    protected BlockAssemblyTable(Material material) {
        super(material);
        setHardness(2.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAssemblyTable();
    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX, float clickY, float clickZ) {
        if (world.isRemote) return true;
        Block block = world.getBlock(x, y, z);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileAssemblyTable && player != null && block instanceof BlockAssemblyTable) {
            if (!player.isSneaking()) {
                player.openGui(ExampleMod.instance, GuiIds.ASSEMBLE_TABLE, world, x, y, z);
                return true;
            }
        }
        return false;
    }

    public void breakBlock(World world, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileAssemblyTable)
        {
            TileAssemblyTable table = (TileAssemblyTable)tile;
            for (int itemIndex = 0; itemIndex < table.getSizeInventory(); ++itemIndex)
            {
                ItemStack itemstack = table.getStackInSlot(itemIndex);

                if (itemstack != null)
                {
                    float xOffset = Utils.random.nextFloat() * 0.8F + 0.1F;
                    float yOffset = Utils.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float singleItemIterate = Utils.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
                    {
                        int stackSiseDecrease = Utils.random.nextInt(21) + 10;

                        if (stackSiseDecrease > itemstack.stackSize)
                        {
                            stackSiseDecrease = itemstack.stackSize;
                        }

                        itemstack.stackSize -= stackSiseDecrease;
                        entityitem = new EntityItem(world, (float)x + xOffset, (float)y + yOffset, (float)z + singleItemIterate, new ItemStack(itemstack.getItem(), stackSiseDecrease, itemstack.getMetadata()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float)Utils.random.nextGaussian() * f3;
                        entityitem.motionY = (float)Utils.random.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float)Utils.random.nextGaussian() * f3;

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            world.updateNeighborsAboutBlockChange(x, y, z, blockBroken);
        }

        super.breakBlock(world, x, y, z, blockBroken, meta);
    }
}
