package com.ultracube.client.screen;

import org.jetbrains.annotations.NotNull;

import com.ultracube.Ultracube;
import com.ultracube.menu.CubeMatterCreatorMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CubeMatterCreatorScreen extends AbstractContainerScreen<CubeMatterCreatorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Ultracube.MODID,
            "textures/gui/matter_creator.png");

    public CubeMatterCreatorScreen(CubeMatterCreatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderTransparentBackground(pGuiGraphics);
        pGuiGraphics.blit(RenderType::guiTextured, TEXTURE, this.leftPos, this.topPos, 0.f, 0.f,
                this.imageWidth,
                this.imageHeight, 256, 256);
        int energyScaled = this.menu.getEnergyStoredScaled();

        // AARRGGBB

        // Energy
        // background
        pGuiGraphics.fill(
                this.leftPos + 115,
                this.topPos + 20,
                this.leftPos + 131,
                this.topPos + 60,
                0xFF555555);

        // foreground
        pGuiGraphics.fill(
                this.leftPos + 116,
                this.topPos + 21 + (38 - energyScaled),
                this.leftPos + 130,
                this.topPos + 59,
                0xFFCC2222);

        // Progress
        // background
        pGuiGraphics.fill(
                this.leftPos + 65,
                this.topPos + 41,
                this.leftPos + 80,
                this.topPos + 45,
                0xFF555555);

        // foreground
        pGuiGraphics.fill(
                this.leftPos + 65,
                this.topPos + 41,
                this.leftPos + 65 + (15 - this.menu.getGenTimeScaled()),
                this.topPos + 45,
                0xFFCC2222);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        int energyStored = this.menu.getEnergy();
        int maxEnergy = this.menu.getMaxEnergy();

        Component text = Component.literal("Energy: " + energyStored + " / " + maxEnergy);
        if (isHovering(115, 20, 16, 40, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(this.font, text, pMouseX, pMouseY);
        }
    }
}