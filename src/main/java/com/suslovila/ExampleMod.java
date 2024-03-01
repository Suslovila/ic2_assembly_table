package com.suslovila;

import com.suslovila.api.crafting.RecipeRegistry;
import com.suslovila.client.ClientProxy;
import com.suslovila.client.gui.GuiHandler;
import com.suslovila.common.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Mod(name = ExampleMod.NAME, modid = ExampleMod.MOD_ID, version = ExampleMod.VERSION)
public class ExampleMod {
	public static final String NAME = "nameThatWillBeShown";
	public static final String MOD_ID = "examplemod";
	public static final String VERSION = "1.0";

	public static final CreativeTabs tab = new CreativeTabs(NAME) {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.water);
        }
    };

	@Mod.Instance(MOD_ID)
	public static ExampleMod instance;

	@SidedProxy(clientSide = "com.suslovila.client.ClientProxy", serverSide = "com.suslovila.common.CommonProxy")
	public static CommonProxy proxy;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
		RecipeRegistry.instance.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		proxy.init(event);
		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);

	}

}
