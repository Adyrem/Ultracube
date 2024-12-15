package com.ultracube.init;

import com.ultracube.Ultracube;
import com.ultracube.item.FuelItem;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemInit {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Ultracube.MODID);

    public static final DeferredItem<BlockItem> ENERGY_GENERATOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "energy_generator_block",
            BlockInit.ENERGY_GENERATOR_BLOCK, new Item.Properties());

    public static final DeferredItem<BlockItem> CUBE_ENERGY_EXTRACTOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "cube_energy_extractor_block",
            BlockInit.CUBE_ENERGY_EXTRACTOR_BLOCK, new Item.Properties());

    public static final DeferredItem<BlockItem> CUBE_MATTER_CREATOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "cube_matter_creator_block",
            BlockInit.CUBE_MATTER_CREATOR_BLOCK, new Item.Properties());

    public static final DeferredItem<BlockItem> THE_CUBE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "the_cube_block",
            BlockInit.THE_CUBE_BLOCK, new Item.Properties()
                    .fireResistant()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1));

    public static final DeferredItem<FuelItem> CUBE_MATTER_ITEM = ITEMS.register("cube_matter",
            (key) -> new FuelItem(
                    new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, key)),
                    800));

}
