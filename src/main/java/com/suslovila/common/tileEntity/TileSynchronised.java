package com.suslovila.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileSynchronised extends TileEntity {
   public final void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.readCustomNBT(nbttagcompound);
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
   }

   public final void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      this.writeCustomNBT(nbttagcompound);
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      this.writeCustomNBT(nbttagcompound);
      return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, nbttagcompound);
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      this.readCustomNBT(pkt.getNbtCompound());
   }

   public void markForSaveAndSync(){
      markForSave();
      markForSync();
   }

   public void markForSave() {
      worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
   }

   public void markForSync() {
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
   }
}
