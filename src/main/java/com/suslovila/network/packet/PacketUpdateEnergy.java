package com.suslovila.network.packet;

import com.suslovila.ExampleMod;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketUpdateEnergy implements IMessage {
    int x;
    int y;
    int z;
    double energyAmount;

    public PacketUpdateEnergy() {
    }

    public PacketUpdateEnergy(int x, int y, int z, double energyAmount) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.energyAmount = energyAmount;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeDouble(energyAmount);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();

        energyAmount = buf.readDouble();
    }

    public static class Handler implements IMessageHandler<PacketUpdateEnergy, IMessage> {
        @Override
        public IMessage onMessage(PacketUpdateEnergy packet, MessageContext ctx) {
            World world = ExampleMod.proxy.getClientWorld();
            //chunk protection
            if (world.blockExists(packet.x, packet.y, packet.z)) {
                TileEntity tile = world.getTileEntity(packet.x, packet.y, packet.z);
                if (tile instanceof TileAssemblyTable) {
                    ((TileAssemblyTable) tile).euBuffer = packet.energyAmount;
                }
            }
            return null;
        }
    }
}
