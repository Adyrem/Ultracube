package com.ultracube.init;


import java.util.Set;
import java.util.function.Supplier;

import com.ultracube.Ultracube;
import com.ultracube.blockentity.CubeEnergyExtractorBlockEntity;
import com.ultracube.blockentity.EnergyGeneratorBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Ultracube.MODID);

            
    public static final Supplier<BlockEntityType<EnergyGeneratorBlockEntity>> ENERGY_GENERATOR_BLOCK = BLOCK_ENTITIES
            .register(
                "energy_generator_block",
                () -> new BlockEntityType<EnergyGeneratorBlockEntity>(EnergyGeneratorBlockEntity::new, BlockInit.ENERGY_GENERATOR_BLOCK.get()
            
        )
    );

    public static final Supplier<BlockEntityType<CubeEnergyExtractorBlockEntity>> CUBE_ENERGY_EXTRACTOR_BLOCK = BLOCK_ENTITIES
    .register(
        "cube_energy_extractor_block",
        () -> new BlockEntityType<CubeEnergyExtractorBlockEntity>(CubeEnergyExtractorBlockEntity::new, Set.of(BlockInit.CUBE_ENERGY_EXTRACTOR_BLOCK.get()
    )
)
);
}
