package com.ultracube.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.ultracube.Ultracube;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Ultracube.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CreativeTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, Ultracube.MODID);

    public static final List<Supplier<? extends ItemLike>> ULTRACUBE_ITEMS = new ArrayList<>();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ULTRACUBE_TAB = CREATIVE_MODE_TABS.register(
            "ultracube_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ultracube_tab"))
                    .icon(ItemInit.ENERGY_GENERATOR_BLOCK_ITEM.get()::getDefaultInstance)
                    .displayItems((displayParams, output) -> ULTRACUBE_ITEMS
                            .forEach(itemLike -> output.accept(itemLike.get())))
                    .build());

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ULTRACUBE_TAB.get()) {
            event.accept(BlockInit.THE_CUBE_BLOCK);
            event.accept(BlockInit.THE_CUBE_UNCHARGED_BLOCK);
            event.accept(ItemInit.CUBE_MATTER_ITEM);
            event.accept(BlockInit.ENERGY_GENERATOR_BLOCK);
            event.accept(BlockInit.CUBE_MATTER_CREATOR_BLOCK);
            event.accept(BlockInit.CUBE_ENERGY_EXTRACTOR_BLOCK);
        }
    }
}