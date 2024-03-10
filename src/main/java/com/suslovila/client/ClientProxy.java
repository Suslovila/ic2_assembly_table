package com.suslovila.client;


import com.suslovila.client.render.ClientEventHandler;
import com.suslovila.client.render.item.ItemBlockLaserRenderer;
import com.suslovila.client.render.tile.TileLaserRenderer;
import com.suslovila.common.CommonProxy;
import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.tileEntity.TileEntityLaser;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }


    public void init(FMLInitializationEvent event) {
        super.init(event);
        ModBlocks.registerRender();
        setupItemRenderers();

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaser.class, new TileLaserRenderer());
    }


    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public void registerRenderers() {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.laser), new ItemBlockLaserRenderer());
    }

    private void setupItemRenderers() {
    }

    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }
}