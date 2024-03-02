package com.suslovila.network.packet;

import com.suslovila.ExampleMod;
import com.suslovila.common.tileEntity.TileAssemblyTable;
import com.suslovila.utils.SusVec3;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PacketUpdatePatterns implements IMessage {
    Short currentPatternId;
    List<TileAssemblyTable.AssemblyTablePattern> patterns;
    int x;
    int y;
    int z;

    public PacketUpdatePatterns() {

    }

    public PacketUpdatePatterns(int x, int y, int z, List<TileAssemblyTable.AssemblyTablePattern> patterns, Short currentPatternId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.currentPatternId = currentPatternId;
        this.patterns = patterns;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);

        buf.writeInt(patterns.size());
        for (TileAssemblyTable.AssemblyTablePattern pattern : patterns) {
            ByteBufUtils.writeUTF8String(buf, pattern.recipeId);
            buf.writeBoolean(pattern.isActive);
        }
        if (currentPatternId == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeInt(currentPatternId);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();

        int amount = buf.readInt();
        List<TileAssemblyTable.AssemblyTablePattern> readPatterns = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            TileAssemblyTable.AssemblyTablePattern pattern =
                    new TileAssemblyTable.AssemblyTablePattern(
                            ByteBufUtils.readUTF8String(buf),
                            buf.readBoolean()
                    );
            readPatterns.add(pattern);
        }
        boolean notNull = buf.readBoolean();
        if (notNull) {
            currentPatternId = buf.readShort();
        } else {
            currentPatternId = null;
        }
    }

    public static class Handler implements IMessageHandler<PacketUpdatePatterns, IMessage> {
        @Override
        public IMessage onMessage(PacketUpdatePatterns packet, MessageContext ctx) {
            World world = ExampleMod.proxy.getClientWorld();
            //chunk protection
            if (world.blockExists(packet.x, packet.y, packet.z)) {
                TileEntity tile = world.getTileEntity(packet.x, packet.y, packet.z);
                if (tile instanceof TileAssemblyTable) {
                    ((TileAssemblyTable) tile).patterns = packet.patterns;
                    ((TileAssemblyTable) tile).currentPatternId = packet.currentPatternId;
                }
            }
            return null;
        }
    }
}
