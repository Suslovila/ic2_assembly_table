package com.suslovila.client.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    private static ClientEventHandler instance = new ClientEventHandler();

    private ClientEventHandler() {
    }

    public static ClientEventHandler getInstance() {
        return instance;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

    }
}