/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 * <p/>
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package com.suslovila.common.inventory.container;

import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.common.tileEntity.TileSynchronised;
import com.suslovila.utils.ItemStackHelper;
import com.suslovila.utils.nbt.INBTStoreable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.LinkedList;

public class SimpleInventory implements IInventory, INBTStoreable {

    private static final String ITEMS_NBT_NAME = "Items";
    private static final String SLOT_NBT_NAME = "Slot";
    private static final String SLOT_INDEX_NBT_NAME = "index";


    private final ItemStack[] contents;
    private final String name;
    private final int stackLimit;
    private final LinkedList<TileEntity> listeners = new LinkedList<TileEntity>();

    public SimpleInventory(int size, String invName, int invStackLimit) {
        contents = new ItemStack[size];
        name = invName;
        stackLimit = invStackLimit;
    }

    @Override
    public int getSizeInventory() {
        return contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return contents[slotId];
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        if (slotId < contents.length && contents[slotId] != null) {
            if (contents[slotId].stackSize > count) {
                ItemStack result = contents[slotId].splitStack(count);
                markDirty();
                return result;
            }
            if (contents[slotId].stackSize < count) {
                return null;
            }
            ItemStack stack = contents[slotId];
            setInventorySlotContents(slotId, null);
            return stack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        if (slotId >= contents.length) {
            return;
        }
        contents[slotId] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return name;
    }

    @Override
    public int getInventoryStackLimit() {
        return stackLimit;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        if (data.hasKey(ITEMS_NBT_NAME)) {
            readFromNBT(data, ITEMS_NBT_NAME);
        }
    }

    public void readFromNBT(NBTTagCompound data, String tag) {
        NBTTagList nbttaglist = data.getTagList(tag, Constants.NBT.TAG_COMPOUND);

        for (int j = 0; j < nbttaglist.tagCount(); ++j) {
            NBTTagCompound slot = nbttaglist.getCompoundTagAt(j);
            int index;
            if (slot.hasKey(SLOT_INDEX_NBT_NAME)) {
                index = slot.getInteger(SLOT_INDEX_NBT_NAME);
            } else {
                index = slot.getByte(SLOT_NBT_NAME);
            }
            if (index >= 0 && index < contents.length) {
                setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(slot));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        writeToNBT(data, ITEMS_NBT_NAME);
    }

    public void writeToNBT(NBTTagCompound data, String tag) {
        NBTTagList slots = new NBTTagList();
        for (byte index = 0; index < contents.length; ++index) {
            if (contents[index] != null && contents[index].stackSize > 0) {
                NBTTagCompound slot = new NBTTagCompound();
                slots.appendTag(slot);
                slot.setByte(SLOT_NBT_NAME, index);
                contents[index].writeToNBT(slot);
            }
        }
        data.setTag(tag, slots);
    }

    public void addListener(TileEntity listener) {
        this.listeners.add(listener);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        if (this.contents[slotId] == null) {
            return null;
        }

        ItemStack stackToTake = this.contents[slotId];
        setInventorySlotContents(slotId, null);
        return stackToTake;
    }

    public ItemStack[] getItemStacks() {
        return contents;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public void markDirty() {
        for (TileEntity handler : listeners) {
            if (handler.getWorld() != null) {
                if (handler instanceof TileAssemblyTable) {
                    ((TileAssemblyTable) handler).updateAvailablePatterns(new ArrayList<>());
                }
                if (handler instanceof TileSynchronised) {
                    ((TileSynchronised) handler).markForSaveAndSync();
                }
            }
        }
    }

    public boolean hasEnough(ItemStack requiredItemStack) {
        for (ItemStack stack : contents) {
            if (ItemStackHelper.areItemStacksEqual(stack, requiredItemStack) && stack.stackSize >= requiredItemStack.stackSize) {
                return true;
            }
        }
        return false;
    }

    public Integer getIndexOfSlotWithEnoughOf(ItemStack requiredItemStack) {
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack itemStack = contents[i];
            if (ItemStackHelper.areItemStacksEqual(itemStack, requiredItemStack) && itemStack.stackSize >= requiredItemStack.stackSize) {
                return i;
            }
        }
        return null;
    }
}
