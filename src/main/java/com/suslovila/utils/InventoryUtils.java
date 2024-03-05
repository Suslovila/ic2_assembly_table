package com.suslovila.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InventoryUtils {
    public static int[] createSlotArray(int first, int count) {
        int[] slots = new int[count];
        for (int k = first; k < first + count; k++) {
            slots[k - first] = k;
        }
        return slots;
    }


    public static ItemStack placeItemStackIntoInventory(ItemStack stack, IInventory inventory, int side, boolean doit, boolean force, int indexFrom) {
        ItemStack itemstack = stack.copy();
        ItemStack itemstack1 = insertStack(inventory, itemstack, side, doit, force, indexFrom);
        if (itemstack1 != null && itemstack1.stackSize != 0) {
            return itemstack1.copy();
        } else {
            if (doit) {
                inventory.markDirty();
            }
            return null;
        }
    }

    public static ItemStack insertStack(IInventory inventory, ItemStack stack1, int side, boolean doit, boolean force, int indexFrom) {
        if (inventory instanceof ISidedInventory && side > -1) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] aint = isidedinventory.getSlotsForFace(side);
            if (aint != null) {
                for (int j = indexFrom; j < aint.length && stack1 != null && stack1.stackSize > 0; ++j) {
                    if (inventory.getStackInSlot(aint[j]) != null && inventory.getStackInSlot(aint[j]).isItemEqual(stack1)) {
                        stack1 = attemptInsertion(inventory, stack1, aint[j], side, doit, force);
                    }

                    if (stack1 == null || stack1.stackSize == 0) {
                        break;
                    }
                }
            }

            if (aint != null && stack1 != null && stack1.stackSize > 0) {
                for (int j = indexFrom; j < aint.length && stack1 != null && stack1.stackSize > 0; ++j) {
                    stack1 = attemptInsertion(inventory, stack1, aint[j], side, doit, force);
                    if (stack1 == null || stack1.stackSize == 0) {
                        break;
                    }
                }
            }
        } else {
            int k = inventory.getSizeInventory();

            for (int l = 0; l < k && stack1 != null && stack1.stackSize > 0; ++l) {
                if (inventory.getStackInSlot(l) != null && inventory.getStackInSlot(l).isItemEqual(stack1)) {
                    stack1 = attemptInsertion(inventory, stack1, l, side, doit, force);
                }

                if (stack1 == null || stack1.stackSize == 0) {
                    break;
                }
            }

            if (stack1 != null && stack1.stackSize > 0) {
                TileEntityChest dc = null;
                if (inventory instanceof TileEntity) {
                    dc = getDoubleChest((TileEntity) inventory);
                    if (dc != null) {
                        int k2 = dc.getSizeInventory();

                        for (int l = 0; l < k2 && stack1 != null && stack1.stackSize > 0; ++l) {
                            if (dc.getStackInSlot(l) != null && dc.getStackInSlot(l).isItemEqual(stack1)) {
                                stack1 = attemptInsertion(dc, stack1, l, side, doit, force);
                            }

                            if (stack1 == null || stack1.stackSize == 0) {
                                break;
                            }
                        }
                    }
                }

                if (stack1 != null && stack1.stackSize > 0) {
                    for (int l = 0; l < k && stack1 != null && stack1.stackSize > 0; ++l) {
                        stack1 = attemptInsertion(inventory, stack1, l, side, doit, force);
                        if (stack1 == null || stack1.stackSize == 0) {
                            break;
                        }
                    }

                    if (stack1 != null && stack1.stackSize > 0 && dc != null) {
                        int k2 = dc.getSizeInventory();

                        for (int l = 0; l < k2 && stack1 != null && stack1.stackSize > 0; ++l) {
                            if (dc.getStackInSlot(l) != null && dc.getStackInSlot(l).isItemEqual(stack1)) {
                                stack1 = attemptInsertion(dc, stack1, l, side, doit, force);
                            }

                            if (stack1 == null || stack1.stackSize == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (stack1 != null && stack1.stackSize == 0) {
            stack1 = null;
        }

        return stack1;
    }

    private static ItemStack attemptInsertion(IInventory inventory, ItemStack stack, int slot, int side, boolean doit, boolean force) {
        ItemStack slotStack = inventory.getStackInSlot(slot);
        if (canInsertItemToInventory(inventory, stack, slot, side) || force) {
            boolean flag = false;
            if (slotStack == null) {
                if (inventory.getInventoryStackLimit() < stack.stackSize) {
                    ItemStack in = stack.splitStack(inventory.getInventoryStackLimit());
                    if (doit) {
                        inventory.setInventorySlotContents(slot, in);
                    }
                } else {
                    if (doit) {
                        inventory.setInventorySlotContents(slot, stack);
                    }

                    stack = null;
                }

                flag = true;
            } else if (areItemStacksEqualStrict(slotStack, stack)) {
                int k = Math.min(inventory.getInventoryStackLimit() - slotStack.stackSize, stack.getMaxStackSize() - slotStack.stackSize);
                int l = Math.min(stack.stackSize, k);
                stack.stackSize -= l;
                if (doit) {
                    slotStack.stackSize += l;
                }

                flag = l > 0;
            }

            if (flag && doit) {
                if (inventory instanceof TileEntityHopper) {
                    ((TileEntityHopper) inventory).setTransferCooldown(8);
                    inventory.markDirty();
                }

                inventory.markDirty();
            }
        }

        return stack;
    }

    public static ItemStack getFirstItemInInventory(IInventory inventory, int size, int side, boolean doit) {
        ItemStack stack1 = null;
        if (inventory instanceof ISidedInventory && side > -1) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int j = 0; j < aint.length; ++j) {
                if (stack1 == null && inventory.getStackInSlot(aint[j]) != null) {
                    stack1 = inventory.getStackInSlot(aint[j]).copy();
                    stack1.stackSize = size;
                }

                if (stack1 != null) {
                    stack1 = attemptExtraction(inventory, stack1, aint[j], side, false, false, false, doit);
                }

                if (stack1 != null) {
                    break;
                }
            }
        } else {
            int k = inventory.getSizeInventory();

            for (int l = 0; l < k; ++l) {
                if (stack1 == null && inventory.getStackInSlot(l) != null) {
                    stack1 = inventory.getStackInSlot(l).copy();
                    stack1.stackSize = size;
                }

                if (stack1 != null) {
                    stack1 = attemptExtraction(inventory, stack1, l, side, false, false, false, doit);
                }

                if (stack1 != null) {
                    break;
                }
            }
        }

        if (stack1 != null && stack1.stackSize != 0) {
            return stack1.copy();
        } else {
            if (doit) {
                inventory.markDirty();
            }

            return null;
        }
    }


    public static ItemStack extractStack(IInventory inventory, ItemStack stack1, int side, boolean useOre, boolean ignoreDamage, boolean ignoreNBT, boolean doit) {
        ItemStack outStack = null;
        if (inventory instanceof ISidedInventory && side > -1) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int j = 0; j < aint.length && stack1 != null && stack1.stackSize > 0 && outStack == null; ++j) {
                outStack = attemptExtraction(inventory, stack1, aint[j], side, useOre, ignoreDamage, ignoreNBT, doit);
            }
        } else {
            int k = inventory.getSizeInventory();

            for (int l = 0; l < k && stack1 != null && stack1.stackSize > 0 && outStack == null; ++l) {
                outStack = attemptExtraction(inventory, stack1, l, side, useOre, ignoreDamage, ignoreNBT, doit);
            }
        }

        return outStack != null && outStack.stackSize != 0 ? outStack.copy() : null;
    }

    public static ItemStack attemptExtraction(IInventory inventory, ItemStack stack, int slot, int side, boolean useOre, boolean ignoreDamage, boolean ignoreNBT, boolean doit) {
        ItemStack slotStack = inventory.getStackInSlot(slot);
        ItemStack outStack = stack.copy();
        if (canExtractItemFromInventory(inventory, slotStack, slot, side)) {
            boolean flag = false;
            if (areItemStacksEqual(slotStack, stack, useOre, ignoreDamage, ignoreNBT)) {
                outStack = slotStack.copy();
                outStack.stackSize = stack.stackSize;
                int k = stack.stackSize - slotStack.stackSize;
                if (k >= 0) {
                    outStack.stackSize -= k;
                    if (doit) {
                        slotStack = null;
                        inventory.setInventorySlotContents(slot, (ItemStack) null);
                    }
                } else if (doit) {
                    slotStack.stackSize -= outStack.stackSize;
                    inventory.setInventorySlotContents(slot, slotStack);
                }

                flag = true;
                if (flag && doit) {
                    inventory.markDirty();
                }

                return outStack;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean canInsertItemToInventory(IInventory inventory, ItemStack stack1, int par2, int par3) {
        return stack1 != null && inventory.isItemValidForSlot(par2, stack1) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(par2, stack1, par3));
    }

    public static boolean canExtractItemFromInventory(IInventory inventory, ItemStack stack1, int par2, int par3) {
        return stack1 != null && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canExtractItem(par2, stack1, par3));
    }

    public static boolean compareMultipleItems(ItemStack c1, ItemStack[] c2) {
        if (c1 != null && c1.stackSize > 0) {
            for (ItemStack is : c2) {
                if (is != null && c1.isItemEqual(is) && ItemStack.areItemStackTagsEqual(c1, is)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean areItemStacksEqualStrict(ItemStack stack0, ItemStack stack1) {
        return areItemStacksEqual(stack0, stack1, false, false, false);
    }


    public static boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, boolean useOre, boolean ignoreDamage, boolean ignoreNBT) {
        if (stack0 == null && stack1 != null) {
            return false;
        } else if (stack0 != null && stack1 == null) {
            return false;
        } else if (stack0 == null && stack1 == null) {
            return true;
        } else {
            if (useOre) {
                int od = OreDictionary.getOreID(stack0);
                if (od != -1) {
                    ItemStack[] ores = (ItemStack[]) OreDictionary.getOres(Integer.valueOf(od)).toArray(new ItemStack[0]);
                    if (containsMatch(false, new ItemStack[]{stack1}, ores)) {
                        return true;
                    }
                }
            }

            boolean t1 = true;
            if (!ignoreNBT) {
                t1 = ItemStack.areItemStackTagsEqual(stack0, stack1);
            }

            boolean t2 = stack0.getMetadata() != stack1.getMetadata();
            if (ignoreDamage && stack0.isItemStackDamageable() && stack1.isItemStackDamageable()) {
                t2 = false;
            }

            if (t2 && ignoreDamage && (stack0.getMetadata() == 32767 || stack1.getMetadata() == 32767)) {
                t2 = false;
            }

            return stack0.getItem() != stack1.getItem() ? false : (t2 ? false : t1);
        }
    }


    public static TileEntityChest getDoubleChest(TileEntity tile) {
        if (tile != null && tile instanceof TileEntityChest) {
            if (((TileEntityChest) tile).adjacentChestXNeg != null) {
                return ((TileEntityChest) tile).adjacentChestXNeg;
            }

            if (((TileEntityChest) tile).adjacentChestXPos != null) {
                return ((TileEntityChest) tile).adjacentChestXPos;
            }

            if (((TileEntityChest) tile).adjacentChestZNeg != null) {
                return ((TileEntityChest) tile).adjacentChestZNeg;
            }

            if (((TileEntityChest) tile).adjacentChestZPos != null) {
                return ((TileEntityChest) tile).adjacentChestZPos;
            }
        }

        return null;
    }

    public static boolean containsMatch(boolean strict, ItemStack[] inputs, ItemStack... targets) {
        for (ItemStack input : inputs) {
            for (ItemStack target : targets) {
                if (itemMatches(target, input, strict)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean itemMatches(ItemStack target, ItemStack input, boolean strict) {
        return (input != null || target == null) && (input == null || target != null) ? target.getItem() == input.getItem() && (target.getMetadata() == 32767 && !strict || target.getMetadata() == input.getMetadata()) : false;
    }
}
