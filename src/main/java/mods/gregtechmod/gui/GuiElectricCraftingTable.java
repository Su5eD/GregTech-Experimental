package mods.gregtechmod.gui;

import mods.gregtechmod.gui.element.BoundedFluidSlot;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricCraftingTable;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricCraftingTable;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiElectricCraftingTable extends GuiSimple<ContainerElectricCraftingTable> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_crafting_table");
    private static final int[][] BOUNDS = { { 142, 24 }, { 145, 24 }, { 150, 29 }, { 150, 32 }, { 145, 37 }, { 142, 37 }, { 137, 32 }, { 137, 29 } };

    public GuiElectricCraftingTable(ContainerElectricCraftingTable container) {
        super(container);
        
        addIconCycle(120, 4, TEXTURE, 0, 184, container.base::getThroughPutMode);
        addIconCycle(120, 40, TEXTURE, 0, 166, container.base::getCraftingMode);
        
        addElement(new BoundedFluidSlot(this, 135, 22, container.base.tank, BOUNDS, true));
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        
        if (this.container.base.tank.isEmpty()) {
            drawColoredRect(136, 23, 16, 16, GuiColors.SLOT_BACKGROUND);
        }
        
        GlStateManager.translate(0, 0, 1);
        drawTexturedRect(136, 23, 16, 16, 176, 0);
        GlStateManager.translate(0, 0, -1);
    }

    @Override
    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        
        if (this.container.base.getCraftingMode() != TileEntityElectricCraftingTable.CraftingMode.PATTERN) {
            GlStateManager.translate(0, 0, 300);
            drawRect(64, 6, 114, 56, 0x80000000);
            GlStateManager.translate(0, 0, -300);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
