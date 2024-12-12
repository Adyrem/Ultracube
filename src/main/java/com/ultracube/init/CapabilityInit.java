package com.ultracube.init;

import com.ultracube.blockentity.CubeEnergyExtractorBlockEntity;
import com.ultracube.blockentity.CubeMatterCreatorBlockEntity;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CapabilityInit {

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BlockEntityInit.CUBE_MATTER_CREATOR_BLOCK.get(),
                CubeMatterCreatorBlockEntity.ENERGY_STORAGE_PROVIDER);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BlockEntityInit.CUBE_ENERGY_EXTRACTOR_BLOCK.get(),
                CubeEnergyExtractorBlockEntity.ENERGY_STORAGE_PROVIDER);
    }

}
