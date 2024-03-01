package com.suslovila.network;

import com.suslovila.ExampleMod;
import com.suslovila.network.packet.PacketAssemblyTableRecipeSelected;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public final class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ExampleMod.NAME.toLowerCase());

    public static void init() {
        int idx = 0;

        INSTANCE.registerMessage(PacketAssemblyTableRecipeSelected.Handler.class, PacketAssemblyTableRecipeSelected.class, idx++, Side.SERVER);

    }
}
