package com.suslovila.common.event;

import com.suslovila.client.render.ClientEventHandler;

public class FMLEventListener {
//event class

    private static FMLEventListener instance = new FMLEventListener();

    private FMLEventListener() {
    }

    public static FMLEventListener getInstance() {
        return instance;
    }


}