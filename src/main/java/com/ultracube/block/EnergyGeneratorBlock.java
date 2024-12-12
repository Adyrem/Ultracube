package com.ultracube.block;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.ultracube.blockentity.EnergyGeneratorBlockEntity;
import com.ultracube.blockentity.util.TickableBlockEntity;
import com.ultracube.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EnergyGeneratorBlock extends Block implements EntityBlock {

    public EnergyGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityInit.ENERGY_GENERATOR_BLOCK.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState,
            @NotNull BlockEntityType<T> pBlockEntityType) {
        return TickableBlockEntity.getTickerHelper(pLevel);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pBlockState, Level pLevel, BlockPos pBlockPos, Player pPlayer,
            BlockHitResult pBlockHitResult) {
        BlockEntity be = pLevel.getBlockEntity(pBlockPos);
        if (!(be instanceof EnergyGeneratorBlockEntity blockEntity))
            return InteractionResult.PASS;

        if (pLevel.isClientSide())
            return InteractionResult.SUCCESS;

        // open screen
        if (pPlayer instanceof ServerPlayer sPlayer) {
            sPlayer.openMenu(blockEntity, pBlockPos);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack pItemStack, BlockState pBlockState, Level pLevel, BlockPos pBlockPos, Player pPlayer,
            InteractionHand pInteractionHand, BlockHitResult pBlockHitResult) {
        BlockEntity be = pLevel.getBlockEntity(pBlockPos);
        if (!(be instanceof EnergyGeneratorBlockEntity blockEntity))
            return InteractionResult.PASS;

        if (pLevel.isClientSide())
            return InteractionResult.SUCCESS;

        // open screen
        if (pPlayer instanceof ServerPlayer sPlayer) {
            sPlayer.openMenu(blockEntity, pBlockPos);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
            @NotNull BlockState pNewState, boolean pMovedByPiston) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof EnergyGeneratorBlockEntity blockEntity) {
            // drop inventory
            Block.popResource(pLevel, pPos, blockEntity.getInventory().getStackInSlot(0));
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

}