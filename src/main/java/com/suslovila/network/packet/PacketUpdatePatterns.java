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
    int x;
    int y;
    int z;
    TileAssemblyTable.AssemblyTablePattern currentPattern;
    List<TileAssemblyTable.AssemblyTablePattern> patterns;

    public PacketUpdatePatterns() {

    }

    public PacketUpdatePatterns(int x, int y, int z, List<TileAssemblyTable.AssemblyTablePattern> patterns, TileAssemblyTable.AssemblyTablePattern currentPattern) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.currentPattern = currentPattern;
        this.patterns = patterns;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);

        buf.writeInt(patterns.size());
        for (TileAssemblyTable.AssemblyTablePattern pattern : patterns) {
            ByteBufUtils.writeUTF8String(buf, pattern.recipeId);
            buf.writeBoolean(pattern.isActive);
        }
        if (currentPattern == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            ByteBufUtils.writeUTF8String(buf, currentPattern.recipeId);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();

        int amount = buf.readInt();
        List<TileAssemblyTable.AssemblyTablePattern> readPatterns = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            String recipeId = ByteBufUtils.readUTF8String(buf);
            boolean isActive = buf.readBoolean();
            TileAssemblyTable.AssemblyTablePattern pattern = new TileAssemblyTable.AssemblyTablePattern(recipeId, isActive);
            readPatterns.add(pattern);
        }
        patterns = readPatterns;
        boolean currentPatternNotNull = buf.readBoolean();
        if (currentPatternNotNull) {
            String recipeId = ByteBufUtils.readUTF8String(buf);
            currentPattern = new TileAssemblyTable.AssemblyTablePattern(recipeId, true);
        } else {
            currentPattern = null;
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
                    ((TileAssemblyTable) tile).currentPattern = packet.currentPattern;
                }
            }
            return null;
        }
    }
}
