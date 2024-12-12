package com.ultracube;

import com.ultracube.init.BlockEntityInit;
import com.ultracube.init.BlockInit;
import com.ultracube.init.CapabilityInit;
import com.ultracube.init.CreativeTabInit;
import com.ultracube.init.ItemInit;
import com.ultracube.init.MenuInit;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Ultracube.MODID)
public class Ultracube {
    public static final String MODID = "ultracube";

    public Ultracube(IEventBus modEventBus, ModContainer modContainer) {
        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        CreativeTabInit.CREATIVE_MODE_TABS.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
        MenuInit.MENU_TYPES.register(modEventBus);

        modEventBus.addListener(new CapabilityInit()::registerCapabilities);

    }
}
