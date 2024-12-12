package com.ultracube.blockentity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.ultracube.Ultracube;
import com.ultracube.blockentity.util.CustomEnergyStorage;
import com.ultracube.blockentity.util.TickableBlockEntity;
import com.ultracube.init.BlockEntityInit;
import com.ultracube.menu.EnergyGeneratorMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class EnergyGeneratorBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider {

    private static final Component TITLE = Component.translatable("container." + Ultracube.MODID + ".energy_generator");

    public EnergyGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.ENERGY_GENERATOR_BLOCK.get(), pPos, pBlockState);
    }

    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final @Nullable ItemStackHandler inventoryOptional = this.inventory;

    private final CustomEnergyStorage energy = new CustomEnergyStorage(10000, 0, 100, 0);
    private final @Nullable CustomEnergyStorage energyOptional = this.energy;

    private int burnTime = 0, maxBurnTime = 0;

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> EnergyGeneratorBlockEntity.this.energy.getEnergyStored();
                case 1 -> EnergyGeneratorBlockEntity.this.energy.getMaxEnergyStored();
                case 2 -> EnergyGeneratorBlockEntity.this.burnTime;
                case 3 -> EnergyGeneratorBlockEntity.this.maxBurnTime;
                default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> EnergyGeneratorBlockEntity.this.energy.setEnergy(pValue);
                case 2 -> EnergyGeneratorBlockEntity.this.burnTime = pValue;
                case 3 -> EnergyGeneratorBlockEntity.this.maxBurnTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide())
            return;

        if (this.energy.getEnergyStored() < this.energy.getMaxEnergyStored()) {
            if (this.burnTime <= 0) {
                if (canBurn(this.inventory.getStackInSlot(0))) {
                    this.burnTime = this.maxBurnTime = getBurnTime(this.inventory.getStackInSlot(0));
                    this.inventory.getStackInSlot(0).shrink(1);
                    sendUpdate();
                }
            } else {
                this.burnTime--;
                this.energy.addEnergy(1);
                sendUpdate();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registryAccess) {
        super.saveAdditional(nbt, registryAccess);

        var tutorialmodData = new CompoundTag();
        tutorialmodData.put("Inventory", this.inventory.serializeNBT(registryAccess));
        tutorialmodData.put("Energy", this.energy.serializeNBT(registryAccess));
        tutorialmodData.putInt("BurnTime", this.burnTime);
        tutorialmodData.putInt("MaxBurnTime", this.maxBurnTime);
        nbt.put(Ultracube.MODID, tutorialmodData);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryAccess) {
        super.loadAdditional(nbt, registryAccess);

        CompoundTag tutorialmodData = nbt.getCompound(Ultracube.MODID);
        if (tutorialmodData.isEmpty())
            return;

        if (tutorialmodData.contains("Inventory", Tag.TAG_COMPOUND)) {
            this.inventory.deserializeNBT(registryAccess, tutorialmodData.getCompound("Inventory"));
        }

        if (tutorialmodData.contains("Energy", Tag.TAG_INT)) {
            this.energy.deserializeNBT(registryAccess, tutorialmodData.get("Energy"));
        }

        if (tutorialmodData.contains("BurnTime", Tag.TAG_INT)) {
            this.burnTime = tutorialmodData.getInt("BurnTime");
        }

        if (tutorialmodData.contains("MaxBurnTime", Tag.TAG_INT)) {
            this.maxBurnTime = tutorialmodData.getInt("MaxBurnTime");
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registryAccess) {
        CompoundTag nbt = super.getUpdateTag(registryAccess);
        saveAdditional(nbt, registryAccess);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory,
            @NotNull Player pPlayer) {
        return new EnergyGeneratorMenu(pContainerId, pPlayerInventory, this, this.containerData);
    }

    public @Nullable ItemStackHandler getInventoryOptional() {
        return this.inventoryOptional;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public @Nullable CustomEnergyStorage getEnergyOptional() {
        return this.energyOptional;
    }

    public CustomEnergyStorage getEnergy() {
        return this.energy;
    }

    private void sendUpdate() {
        setChanged();

        if (this.level != null)
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public int getBurnTime(ItemStack stack) {
        // TODO get burn time correctly
        return 0;
    }

    public boolean canBurn(ItemStack stack) {
        return getBurnTime(stack) > 0;
    }
}