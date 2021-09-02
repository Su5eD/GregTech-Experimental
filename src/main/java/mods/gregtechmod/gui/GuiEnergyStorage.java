package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiEnergyStorage extends GuiIC2<ContainerEnergyStorage> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/energy_storage.png");

    public GuiEnergyStorage(ContainerEnergyStorage container) {
        super(container);
    }
    
    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        bindTexture();
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        drawString(11, 8, "L.E.S.U", 16448255, false);
        double capacity = this.container.base.getEUCapacity();
        drawString("jei.energy", 16, GtUtil.formatNumber(this.container.base.getStoredEU()));
        drawString("teblock.lesu.max_energy", 24, GtUtil.formatNumber(capacity));
        drawString("teblock.lesu.max_input", 32, GtUtil.formatNumber(this.container.base.getMaxInputEUp()));
        drawString("teblock.lesu.output", 40, GtUtil.formatNumber(this.container.base.getMaxOutputEUp()));
        if (capacity >= 1999999999) {
            drawString("teblock.lesu.warning", 48);
            drawString("teblock.lesu.max_reached", 56);
        }
        
        double charge = this.container.base.getStoredEU() / (double) this.container.base.getEUCapacity();
        drawColoredRect(8, 73, (int) (charge * 116), 5, -16711681);
        drawRect(66, 73, 1);
        drawRect(65, 74, 1);
        drawRect(64, 75, 4);
        drawRect(66, 76, 1);
        drawRect(65, 77, 1);
    }

    private void drawRect(int x, int y, int width) {
        drawColoredRect(x, y, width, 1, -1);
    }
    
    private void drawString(String translationKey, int y, Object... args) {
        drawString(11, y, GtUtil.translate(translationKey, args), 16448255, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
