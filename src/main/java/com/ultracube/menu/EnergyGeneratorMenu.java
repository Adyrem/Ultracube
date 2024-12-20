package com.ultracube.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

import org.jetbrains.annotations.NotNull;

import com.ultracube.blockentity.EnergyGeneratorBlockEntity;
import com.ultracube.init.BlockInit;
import com.ultracube.init.MenuInit;
import com.ultracube.menu.slot.CustomCubeSlot;
import com.ultracube.menu.slot.CustomFuelSlot;

public class EnergyGeneratorMenu extends PlayerInventoryMenu {
    private final EnergyGeneratorBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData data;

    // Client Constructor
    public EnergyGeneratorMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, playerInv.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(4));
    }

    // Server Constructor
    public EnergyGeneratorMenu(int containerId, Inventory playerInv, BlockEntity blockEntity, ContainerData data) {
        super(MenuInit.ENERGY_GENERATOR_MENU.get(), containerId, playerInv);
        if (blockEntity instanceof EnergyGeneratorBlockEntity be) {
            this.blockEntity = be;
        } else {
            throw new IllegalStateException("Incorrect block entity class (%s) passed into EnergyGeneratorMenu!"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }

        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.data = data;

        createBlockEntityInventory(be);

        addDataSlots(data);
    }

    private void createBlockEntityInventory(EnergyGeneratorBlockEntity be) {
        ItemStackHandler itemStackHandler = be.getInventoryOptional();
        if (itemStackHandler != null) {
            addSlot(new CustomFuelSlot(itemStackHandler, 0, 44, 36));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot fromSlot = getSlot(pIndex);
        ItemStack fromStack = fromSlot.getItem();

        if (fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if (!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if (pIndex < 36) {
            // We are inside of the player's inventory
            if (!moveItemStackTo(fromStack, 36, 37, false))
                return ItemStack.EMPTY;
        } else if (pIndex < 37) {
            // We are inside of the block entity inventory
            if (!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else {
            System.err.println("Invalid slot index: " + pIndex);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(pPlayer, fromStack);

        return copyFromStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(this.levelAccess, pPlayer, BlockInit.ENERGY_GENERATOR_BLOCK.get());
    }

    public EnergyGeneratorBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public int getEnergy() {
        return this.data.get(0);
    }

    public int getMaxEnergy() {
        return this.data.get(1);
    }

    public int getBurnTime() {
        return this.data.get(2);
    }

    public int getMaxBurnTime() {
        return this.data.get(3);
    }

    public int getEnergyStoredScaled() {
        return (int) (((float) getEnergy() / (float) getMaxEnergy()) * 38);
    }
}