package com.ultracube.client.handler;

import com.ultracube.Ultracube;
import com.ultracube.client.screen.CubeEnergyExtractorScreen;
import com.ultracube.client.screen.EnergyGeneratorScreen;
import com.ultracube.init.MenuInit;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Ultracube.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent  event) {
        event.register(MenuInit.ENERGY_GENERATOR_MENU.get(), EnergyGeneratorScreen::new);
        event.register(MenuInit.CUBE_ENERGY_EXTRACTOR_MENU.get(), CubeEnergyExtractorScreen::new);
    }

}
