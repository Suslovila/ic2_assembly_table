package com.suslovila.common;

import com.suslovila.Config;
import com.suslovila.ExampleMod;
import com.suslovila.client.gui.GuiHandler;
import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.event.FMLEventListener;
import com.suslovila.common.item.ModItems;
import com.suslovila.network.PacketHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.registerServerConfig(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(FMLEventListener.getInstance());
        MinecraftForge.EVENT_BUS.register(FMLEventListener.getInstance());
        ModBlocks.register();
        ModItems.register();
        PacketHandler.init();
    }


    public void init(FMLInitializationEvent event) {

    }


    public void postInit(FMLPostInitializationEvent event) {

    }

    public void registerRenderers() {}

    public World getClientWorld() {
        return null;
    }
}
