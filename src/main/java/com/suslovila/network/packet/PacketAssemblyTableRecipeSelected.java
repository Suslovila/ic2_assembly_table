package com.suslovila.network.packet;


import com.suslovila.common.tileEntity.TileAssemblyTable;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketAssemblyTableRecipeSelected implements IMessage {

    private int x;
    private int y;
    private int z;
    int buttonId;

    public PacketAssemblyTableRecipeSelected() {
    }

    public PacketAssemblyTableRecipeSelected(int x, int y, int z, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.buttonId = id;
    }

    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(buttonId);
    }

    public void fromBytes(ByteBuf buffer) {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        buttonId = buffer.readInt();

    }

    public static class Handler implements IMessageHandler<PacketAssemblyTableRecipeSelected, IMessage> {
        @Override
        public IMessage onMessage(PacketAssemblyTableRecipeSelected packet, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            //chunk protection
            if (world.blockExists(packet.x, packet.y, packet.z)) {
                TileEntity tile = world.getTileEntity(packet.x, packet.y, packet.z);
                if (tile instanceof TileAssemblyTable) {
                    TileAssemblyTable table = (TileAssemblyTable) tile;
                    TileAssemblyTable.AssemblyTablePattern pattern = table.getPatternById(packet.buttonId);
                    if (pattern == null) return null;
                    if (pattern.isActive) {
                        table.removePatternWithUpdate(packet.buttonId);
                    } else {
                        table.activatePattern(packet.buttonId);
                    }
                }
            }
            return null;
        }
    }
}
