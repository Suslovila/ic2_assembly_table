package com.suslovila.client;


import com.suslovila.client.render.ClientEventHandler;
import com.suslovila.common.CommonProxy;
import com.suslovila.common.block.ModBlocks;
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

        //ClientRegistry.bindTileEntitySpecialRenderer(TileClass::class.java, TileRendererInstance)


        // RenderingRegistry.registerBlockHandler(BlockRenderer())

    }


    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public void registerRenderers() {
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.getInstance());
        //MinecraftForgeClient.registerItemRenderer(ModItems.item, CustomItemRenderer)
    }

    private void setupItemRenderers() {
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), ItemRenderer())
    }


    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

}