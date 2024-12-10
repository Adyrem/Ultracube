package com.ultracube.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import com.ultracube.init.ItemInit;


public class CustomCubeSlot extends SlotItemHandler {

    public CustomCubeSlot(IItemHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() == ItemInit.THE_CUBE_BLOCK_ITEM.get()) {
           return true;
        }

        return false;
    }
}
