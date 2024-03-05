package com.suslovila.common.tileEntity;

import com.suslovila.Config;
import com.suslovila.api.ILaserTarget;
import com.suslovila.api.ILaserTargetBlock;
import com.suslovila.utils.RotatableHandler;
import com.suslovila.utils.SafeTimeTracker;
import com.suslovila.utils.SusVec3;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.LinkedList;
import java.util.List;

public class TileEntityLaser extends TileSynchronised implements IEnergySink {
    private double euBuffer;
    public int euBufferCapacity = Config.laserBufferCapacity;
    private final SafeTimeTracker searchTracker = new SafeTimeTracker(50, 100);
    public ILaserTarget laserTarget;
    public ForgeDirection facing = ForgeDirection.DOWN;

    public SusVec3 laserDestinationPos;


    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;

        //laser.iterateTexture();

        if (canFindTable()) {
            findTable();
        }

        if (laserTarget != null) {
            sendEnergy();
            laserDestinationPos = laserTarget.getLaserStreamPos();
            markForSync();
        }

    }

    @Override
    public double getDemandedEnergy() {
        return Math.min(euBufferCapacity - euBuffer, getMaxEnergyPerTick());
    }

    public double getMaxEnergyPerTick() {
        return Config.laserEnergyTransferAmountPerTick;
    }

    @Override
    public int getSinkTier() {
        return Config.laserSinkTier;
    }

    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double amount, double voltage) {
        double toAdd = Math.min(amount, (double) this.euBufferCapacity - this.euBuffer);
        this.euBuffer += toAdd;
        return amount - toAdd;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return forgeDirection != this.facing;
    }

    protected void findTable() {
        int minX = xCoord - 5;
        int minY = yCoord - 5;
        int minZ = zCoord - 5;
        int maxX = xCoord + 5;
        int maxY = yCoord + 5;
        int maxZ = zCoord + 5;

        switch (facing) {
            case WEST:
                maxX = xCoord;
                break;
            case EAST:
                minX = xCoord;
                break;
            case DOWN:
                maxY = yCoord;
                break;
            case UP:
                minY = yCoord;
                break;
            case NORTH:
                maxZ = zCoord;
                break;
            default:
            case SOUTH:
                minZ = zCoord;
                break;
        }

        List<ILaserTarget> targets = new LinkedList<ILaserTarget>();

        if (minY < 0) {
            minY = 0;
        }
        if (maxY > 255) {
            maxY = 255;
        }

        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (worldObj.getBlock(x, y, z) instanceof ILaserTargetBlock) {
                        TileEntity tile = worldObj.getTileEntity(x, y, z);

                        if (tile instanceof ILaserTarget) {
                            ILaserTarget table = (ILaserTarget) tile;

                            if (table.requiresLaserEnergy()) {
                                targets.add(table);
                            }
                        }
                    }
                }
            }
        }

        if (targets.isEmpty()) {
            return;
        }

        laserTarget = targets.get(worldObj.rand.nextInt(targets.size()));
        laserDestinationPos = laserTarget.getLaserStreamPos();
        markForSaveAndSync();
    }

    protected boolean canFindTable() {
        return searchTracker.markTimeIfDelay(worldObj);
    }

    protected boolean isValidTable() {
        if (laserTarget == null || !laserTarget.isValidTarget() || !laserTarget.requiresLaserEnergy()) {
            return false;
        }

        return true;
    }

    public boolean isTranslatingEnergy() {
        return laserTarget != null;
    }

    public void sendEnergy() {
        double energyToSend = Math.min(euBuffer, Config.laserEnergyTransferAmountPerTick);
        double energyLeft = laserTarget.receiveLaserEnergy(this, energyToSend, 1);
        euBuffer = euBuffer - (energyToSend - energyLeft);
        markForSaveAndSync();
        markDirty();
    }

    @Override
    public void writeCustomNBT(NBTTagCompound rootNbt) {
        RotatableHandler.writeRotation(rootNbt, facing);
        if(laserDestinationPos != null) laserDestinationPos.writeToNBT(rootNbt);
    }

    @Override
    public void readCustomNBT(NBTTagCompound rootNbt) {
        facing = RotatableHandler.readRotation(rootNbt);
        laserDestinationPos = SusVec3.fromNbt(rootNbt);
    }


}