package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricCraftingTable;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricCraftingTable;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiElectricCraftingTable extends GuiSimple<ContainerElectricCraftingTable> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_crafting_table");

    public GuiElectricCraftingTable(ContainerElectricCraftingTable container) {
        super(container);
        
        addIconCycle(120, 4, TEXTURE, 0, 184, container.base::getThroughPutMode);
        addIconCycle(120, 40, TEXTURE, 0, 166, container.base::getCraftingMode);
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
