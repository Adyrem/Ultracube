package com.ultracube.blockentity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.ultracube.Ultracube;
import com.ultracube.blockentity.util.CustomEnergyStorage;
import com.ultracube.blockentity.util.TickableBlockEntity;
import com.ultracube.init.BlockEntityInit;
import com.ultracube.init.ItemInit;
import com.ultracube.menu.CubeEnergyExtractorMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class CubeEnergyExtractorBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider {

    public static final ICapabilityProvider<CubeEnergyExtractorBlockEntity, Direction, IEnergyStorage> ENERGY_STORAGE_PROVIDER = (
            be, side) -> be.getEnergyOptional() != null ? be.getEnergyOptional() : null;

    private static final Component TITLE = Component.translatable("container." + Ultracube.MODID + ".energy_generator");

    public CubeEnergyExtractorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.CUBE_ENERGY_EXTRACTOR_BLOCK.get(), pPos, pBlockState);
    }

    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final @Nullable ItemStackHandler inventoryOptional = this.inventory;

    private final CustomEnergyStorage energy = new CustomEnergyStorage(10000, 1000, 1000, 0);
    private final @Nullable CustomEnergyStorage energyOptional = this.energy;

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> CubeEnergyExtractorBlockEntity.this.energy.getEnergyStored();
                case 1 -> CubeEnergyExtractorBlockEntity.this.energy.getMaxEnergyStored();
                default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> CubeEnergyExtractorBlockEntity.this.energy.setEnergy(pValue);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide())
            return;

        if (canGenerate(this.inventory.getStackInSlot(0))) {
            this.energy.addEnergy(10);
        }

        pushEnergy();
        sendUpdate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registryAccess) {
        super.saveAdditional(nbt, registryAccess);

        var data = new CompoundTag();
        data.put("Inventory", this.inventory.serializeNBT(registryAccess));
        data.put("Energy", this.energy.serializeNBT(registryAccess));
        nbt.put(Ultracube.MODID, data);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryAccess) {
        super.loadAdditional(nbt, registryAccess);

        CompoundTag data = nbt.getCompound(Ultracube.MODID);
        if (data.isEmpty())
            return;

        if (data.contains("Inventory", Tag.TAG_COMPOUND)) {
            this.inventory.deserializeNBT(registryAccess, data.getCompound("Inventory"));
        }

        if (data.contains("Energy", Tag.TAG_INT)) {
            this.energy.deserializeNBT(registryAccess, data.get("Energy"));
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
        return new CubeEnergyExtractorMenu(pContainerId, pPlayerInventory, this, this.containerData);
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

    public boolean canGenerate(ItemStack stack) {
        if (this.energy.getEnergyStored() >= this.energy.getMaxEnergyStored()) {
            return false;
        }

        if (stack.getItem() != ItemInit.THE_CUBE_BLOCK_ITEM.get()) {
            return false;
        }

        return true;
    }

    private void pushEnergy() {
        // Transmit power out all sides.
        for (Direction side : Direction.values()) {
            IEnergyStorage selfHandler = this.getEnergyOptional();
            if (selfHandler == null) {
                continue;
            }
            // Get the other energy handler
            IEnergyStorage otherHandler = level.getCapability(Capabilities.EnergyStorage.BLOCK,
                    getBlockPos().relative(side), side.getOpposite());
            if (otherHandler != null) {
                // If the other handler can receive power transmit ours
                if (otherHandler.canReceive()) {
                    int energyToReceive = selfHandler.extractEnergy(selfHandler.getEnergyStored(), true);
                    int received = otherHandler.receiveEnergy(energyToReceive, false);
                    selfHandler.extractEnergy(received, false);
                }
            }
        }
    }

}