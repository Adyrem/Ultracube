package com.ultracube.init;

import com.ultracube.Ultracube;
import com.ultracube.block.CubeEnergyExtractorBlock;
import com.ultracube.block.CubeMatterCreatorBlock;
import com.ultracube.block.EnergyGeneratorBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockInit {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Ultracube.MODID);

    public static final DeferredBlock<EnergyGeneratorBlock> ENERGY_GENERATOR_BLOCK = BLOCKS.registerBlock(
            "energy_generator_block",
            EnergyGeneratorBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE));

    public static final DeferredBlock<CubeEnergyExtractorBlock> CUBE_ENERGY_EXTRACTOR_BLOCK = BLOCKS.registerBlock(
            "cube_energy_extractor_block",
            CubeEnergyExtractorBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE));

    public static final DeferredBlock<CubeMatterCreatorBlock> CUBE_MATTER_CREATOR_BLOCK = BLOCKS.registerBlock(
            "cube_matter_creator_block",
            CubeMatterCreatorBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE));

    public static final DeferredBlock<Block> THE_CUBE_BLOCK = BLOCKS.registerBlock("the_cube_block",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE));

    public static final DeferredBlock<Block> THE_CUBE_UNCHARGED_BLOCK = BLOCKS.registerBlock(
            "the_cube_uncharged_block",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE));
}
