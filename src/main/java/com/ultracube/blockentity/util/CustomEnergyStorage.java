package com.ultracube.blockentity.util;

import net.neoforged.neoforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    public CustomEnergyStorage(int capacity) {
        super(capacity);
    }

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int energy) {
        if (energy < 0)
            energy = 0;
        if (energy > this.capacity)
            energy = this.capacity;

        this.energy = energy;
    }

    public void addEnergy(int energy) {
        setEnergy(this.energy + energy);
    }

    public void removeEnergy(int energy) {
        setEnergy(this.energy - energy);
    }
}
