package com.suslovila.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryUtils {
    public static int[] createSlotArray(int first, int count) {
        int[] slots = new int[count];
        for (int k = first; k < first + count; k++) {
            slots[k - first] = k;
        }
        return slots;
    }


    public static int addToRandomInventoryAround(World world, int x, int y, int z, ItemStack stack) {
        Collections.shuffle(directions);
        for (ForgeDirection orientation : directions) {
            Position pos = new Position(x, y, z, orientation);
            pos.moveForwards(1.0);

            TileEntity tileInventory = BlockUtils.getTileEntity(world, (int) pos.x, (int) pos.y, (int) pos.z);
            ITransactor transactor = Transactor.getTransactorFor(tileInventory);
            if (transactor != null && !(tileInventory instanceof IEngine) && !(tileInventory instanceof ILaserTarget) && transactor.add(stack, orientation.getOpposite(), false).stackSize > 0) {
                return transactor.add(stack, orientation.getOpposite(), true).stackSize;
            }
        }
        return 0;

    }

    public static int addToRandomInjectableAround(World world, int x, int y, int z, ForgeDirection from, ItemStack stack) {
        List<IInjectable> possiblePipes = new ArrayList<IInjectable>();
        List<ForgeDirection> pipeDirections = new ArrayList<ForgeDirection>();

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (from.getOpposite() == side) {
                continue;
            }

            Position pos = new Position(x, y, z, side);

            pos.moveForwards(1.0);

            TileEntity tile = BlockUtils.getTileEntity(world, (int) pos.x, (int) pos.y, (int) pos.z);

            if (tile instanceof IInjectable) {
                if (!((IInjectable) tile).canInjectItems(side.getOpposite())) {
                    continue;
                }

                possiblePipes.add((IInjectable) tile);
                pipeDirections.add(side.getOpposite());
            } else {
                IInjectable wrapper = CompatHooks.INSTANCE.getInjectableWrapper(tile, side);
                if (wrapper != null) {
                    possiblePipes.add(wrapper);
                    pipeDirections.add(side.getOpposite());
                }
            }
        }

        if (possiblePipes.size() > 0) {
            int choice = RANDOM.nextInt(possiblePipes.size());

            IInjectable pipeEntry = possiblePipes.get(choice);

            return pipeEntry.injectItem(stack, true, pipeDirections.get(choice), null);
        }
        return 0;
    }


    public static int addToRandomInjectableAround(World world, int x, int y, int z, ForgeDirection from, ItemStack stack) {
        List<IInjectable> possiblePipes = new ArrayList<IInjectable>();
        List<ForgeDirection> pipeDirections = new ArrayList<ForgeDirection>();

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (from.getOpposite() == side) {
                continue;
            }

            Position pos = new Position(x, y, z, side);

            pos.moveForwards(1.0);

            TileEntity tile = BlockUtils.getTileEntity(world, (int) pos.x, (int) pos.y, (int) pos.z);

            if (tile instanceof IInjectable) {
                if (!((IInjectable) tile).canInjectItems(side.getOpposite())) {
                    continue;
                }

                possiblePipes.add((IInjectable) tile);
                pipeDirections.add(side.getOpposite());
            } else {
                IInjectable wrapper = CompatHooks.INSTANCE.getInjectableWrapper(tile, side);
                if (wrapper != null) {
                    possiblePipes.add(wrapper);
                    pipeDirections.add(side.getOpposite());
                }
            }
        }

        if (possiblePipes.size() > 0) {
            int choice = RANDOM.nextInt(possiblePipes.size());

            IInjectable pipeEntry = possiblePipes.get(choice);

            return pipeEntry.injectItem(stack, true, pipeDirections.get(choice), null);
        }
        return 0;
    }
}
