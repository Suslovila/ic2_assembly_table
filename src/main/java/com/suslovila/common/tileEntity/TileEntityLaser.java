package com.suslovila.common.tileEntity;

import com.suslovila.Config;
import com.suslovila.ExampleMod;
import com.suslovila.api.lasers.ILaserTarget;
import com.suslovila.api.lasers.ILaserTargetBlock;
import com.suslovila.api.lasers.LaserConfig;
import com.suslovila.api.lasers.WrappedLaser;
import com.suslovila.utils.SafeTimeTracker;
import com.suslovila.utils.SusVec3;
import com.suslovila.utils.WorldHelper;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TileEntityLaser extends TileEntityBlock implements IEnergySink {
    private double euBuffer = 0;
    private final SafeTimeTracker searchTracker = new SafeTimeTracker(25, 50);
    public ILaserTarget laserTarget = null;
    public SusVec3 laserDestinationPos;
    private boolean addedToEnergyNet = false;
    public int meta;

    public TileEntityLaser(int meta) {
        super();
        this.meta = meta;
    }

    public TileEntityLaser() {
        super();
    }

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
    }


    protected void updateEntityClient() {

    }

    protected void updateEntityServer() {
        if (laserTarget != null) {
            if (!laserTarget.requiresLaserEnergy() || !isValidTable()) {
                laserTarget = null;
                laserDestinationPos = null;
                if (canFindTable()) {
                    findTable();
                }
            } else {
                sendEnergy();
                laserDestinationPos = laserTarget.getLaserStreamPos();
                markForSync();
            }
        } else {
            if (canFindTable()) {
                findTable();
            }
        }
    }


    @Override
    public double getDemandedEnergy() {
        return Math.min(getEUBufferCapacity() - euBuffer, getMaxEnergyPerTick());
    }

    public double getMaxEnergyPerTick() {
        return getEUPerTick();
    }

    @Override
    public int getSinkTier() {
        return Config.laserSinkTier;
    }

    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double amount, double voltage) {
        double toAdd = Math.min(amount, Math.min(getMaxEnergyPerTick(), this.getEUBufferCapacity() - this.euBuffer));
        this.euBuffer += toAdd;
        markDirty();
        markForSave();
        return amount - toAdd;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return forgeDirection.ordinal() != (int) this.getFacing();
    }

    protected void findTable() {
        int minX = xCoord - 5;
        int minY = yCoord - 5;
        int minZ = zCoord - 5;
        int maxX = xCoord + 5;
        int maxY = yCoord + 5;
        int maxZ = zCoord + 5;
        ForgeDirection facing = ForgeDirection.getOrientation(getFacing());
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
                        boolean areBlocksBetween = WorldHelper.areBlocksBetween(new SusVec3(xCoord, yCoord, zCoord), new SusVec3(x, y, z), worldObj);
                        if (areBlocksBetween) continue;
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
        boolean areBlocksBetween = WorldHelper.areBlocksBetween(new SusVec3(xCoord, yCoord, zCoord), laserTarget.getLaserStreamPos(), worldObj);
        if (areBlocksBetween) return false;
        return true;
    }

    public boolean isTranslatingEnergy() {
        return laserTarget != null;
    }

    public void sendEnergy() {
        double energyToSend = Math.min(euBuffer, getEUPerTick());
        double energyLeft = laserTarget.receiveLaserEnergy(this, energyToSend, 1);
        euBuffer = euBuffer - (energyToSend - energyLeft);
        markForSave();
        markDirty();
    }


    private static final String ENERGY_NBT = ExampleMod.MOD_ID + "energy";
    private static final String META_NBT = ExampleMod.MOD_ID + "meta";


    public void writeCustomNBT(NBTTagCompound rootNbt) {
        rootNbt.setDouble(ENERGY_NBT, euBuffer);
        rootNbt.setInteger(META_NBT, meta);
        if (laserDestinationPos != null) laserDestinationPos.writeToNBT(rootNbt);
    }

    public void readCustomNBT(NBTTagCompound rootNbt) {
        euBuffer = rootNbt.getDouble(ENERGY_NBT);
        meta = rootNbt.getInteger(META_NBT);
        laserDestinationPos = SusVec3.fromNbt(rootNbt);
    }

    public final void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.readCustomNBT(nbttagcompound);
    }

    public final void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        this.writeCustomNBT(nbttagcompound);
    }


    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeCustomNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 7, nbttagcompound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readCustomNBT(pkt.getNbtCompound());
    }

    public void markForSaveAndSync() {
        markForSave();
        markForSync();
    }

    public void markForSave() {
        worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
    }

    public void markForSync() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }


    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0;
    }

    public double getEUBufferCapacity() {
        return LaserConfig.getByMeta(meta).euBufferCapacity;
    }

    public double getEUPerTick() {
        return LaserConfig.getByMeta(meta).euPerTick;
    }

}