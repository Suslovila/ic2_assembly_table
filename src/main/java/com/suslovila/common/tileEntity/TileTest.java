package com.suslovila.common.tileEntity;

import com.suslovila.common.inventory.container.SimpleInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileTest extends TileSerialised implements IInventory {
    SimpleInventory inv = new SimpleInventory(getSizeInventory(), "inv", 64);
    private static final int inventorySize = 24;
    private static final int inputSlotsAmount = 12;
    public static final int patternAmount = 8;


    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        inv.writeToNBT(nbttagcompound);
    }

    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        inv.readFromNBT(nbttagcompound);
    }


    @Override
    public int getSizeInventory() {
        return inventorySize;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inv.setInventorySlotContents(slot, stack);
//
//        if (currentRecipe == null) {
//            setNextCurrentRecipe();
//        }
    }

    @Override
    public String getInventoryName() {
        return ("tile.assemblyTableBlock.name");
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inv.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inv.getStackInSlotOnClosing(slot);
    }


    @Override
    public int getInventoryStackLimit() {
        return inv.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && !isInvalid();
    }

    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inv.isItemValidForSlot(slot, stack);
    }

    public int getInputSlotAmount() {
        return inputSlotsAmount;
    }

    public int getOutputSlotsAmount() {
        return getSizeInventory() - getInputSlotAmount();
    }

}
