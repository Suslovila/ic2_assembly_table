package com.suslovila.common.inventory.container;

import com.suslovila.common.inventory.container.slot.SlotOutput;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.utils.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAssemblyTable extends Container {
    TileAssemblyTable tile;

    public ContainerAssemblyTable(InventoryPlayer inventoryPlayer, TileAssemblyTable tile) {
        this.tile = tile;

        for (int yOffset = 0; yOffset < 4; yOffset++) {
            for (int xOffset = 0; xOffset < 3; xOffset++) {
                addSlotToContainer(new Slot(tile, xOffset + yOffset * 3, 8 + xOffset * 18, 36 + yOffset * 18));
            }
        }

        //adding output slots
        for (int yOffset = 0; yOffset < 4; ++yOffset) {
            for (int xOffset = 0; xOffset < 3; ++xOffset) {
                int textureShift = 108;
                addSlotToContainer(new SlotOutput(tile, tile.getInputSlotAmount() + xOffset + yOffset * 3, 8 + xOffset * 18 + textureShift, 36 + yOffset * 18));
            }
        }
        //Player's hotbar
        //todo: вынести функцию создания хотбара игрока
        for (int l = 0; l < 3; l++) {
            for (int k1 = 0; k1 < 9; k1++) {
                addSlotToContainer(new Slot(inventoryPlayer, k1 + l * 9 + 9, 8 + k1 * 18, 123 + l * 18));
            }

        }
        for (int i1 = 0; i1 < 9; i1++) {
            addSlotToContainer(new Slot(inventoryPlayer, i1, 8 + i1 * 18, 181));
        }


    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }

    //todo: а как это работает :/ ?
//    @Override
//    public ItemStack transferStackInSlot(final EntityPlayer player, final int index) {
//        ItemStack itemstack = null;
//        Slot slot = (Slot) this.inventorySlots.get(index);
//        if ((slot != null) && slot.getHasStack()) {
//            final ItemStack itemstack1 = slot.getStack();
//            itemstack = itemstack1.copy();
//
//            final int containerSlotsIndexes = this.inventorySlots.size() - player.inventory.mainInventory.length;
//            boolean isBlockContainerSlot = index < containerSlotsIndexes;
//            if (isBlockContainerSlot) {
//                if (!mergeItemStack(itemstack1, containerSlotsIndexes, this.inventorySlots.size(), true)) {
//                    return null;
//                }
//            } else if (!mergeItemStack(itemstack1, 0, containerSlotsIndexes, false)) {
//                return null;
//            }
//            if (itemstack1.stackSize == 0) {
//                slot.putStack(null);
//            } else {
//                slot.onSlotChanged();
//            }
//            if (itemstack1.stackSize == itemstack.stackSize) {
//                return null;
//            }
//            slot.onPickupFromSlot(player, itemstack1);
//        }
//        return itemstack;
//    }
//
//
//










    protected boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
        boolean changed = false;
        if (stackToShift.isStackable()) {
            for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
                Slot slot = (Slot) inventorySlots.get(slotIndex);
                ItemStack stackInSlot = slot.getStack();
                if (stackInSlot != null && StackHelper.canStacksMerge(stackInSlot, stackToShift)) {
                    int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
                    if (resultingStackSize <= max) {
                        stackToShift.stackSize = 0;
                        stackInSlot.stackSize = resultingStackSize;
                        slot.onSlotChanged();
                        changed = true;
                    } else if (stackInSlot.stackSize < max) {
                        stackToShift.stackSize -= max - stackInSlot.stackSize;
                        stackInSlot.stackSize = max;
                        slot.onSlotChanged();
                        changed = true;
                    }
                }
            }
        }
        if (stackToShift.stackSize > 0) {
            for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
                Slot slot = (Slot) inventorySlots.get(slotIndex);
                ItemStack stackInSlot = slot.getStack();
                if (stackInSlot == null) {
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
                    stackInSlot = stackToShift.copy();
                    stackInSlot.stackSize = Math.min(stackToShift.stackSize, max);
                    stackToShift.stackSize -= stackInSlot.stackSize;
                    slot.putStack(stackInSlot);
                    slot.onSlotChanged();
                    changed = true;
                }
            }
        }
        return changed;
    }

    private boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
        for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++) {
            Slot slot = (Slot) inventorySlots.get(machineIndex);
            if (!slot.isItemValid(stackToShift)) {
                continue;
            }
            if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1)) {
                return true;
            }
        }
        return false;
    }

    //todo: под перепись
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack originalStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        int numSlots = inventorySlots.size();
        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            originalStack = stackInSlot.copy();
            if (slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots)) {
                // NOOP
            } else if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9) {
                if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots)) {
                    return null;
                }
            } else if (slotIndex >= numSlots - 9 && slotIndex < numSlots) {
                if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9)) {
                    return null;
                }
            } else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots)) {
                return null;
            }
            slot.onSlotChange(stackInSlot, originalStack);
            if (stackInSlot.stackSize <= 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (stackInSlot.stackSize == originalStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, stackInSlot);
        }
        return originalStack;
    }
}
