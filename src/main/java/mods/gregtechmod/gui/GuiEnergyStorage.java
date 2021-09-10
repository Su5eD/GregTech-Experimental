package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiEnergyStorage extends GuiIC2<ContainerEnergyStorage<?>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/energy_storage.png");
    
    private final int chargeBarLength;
    private final int chargeBoltOffset;

    public GuiEnergyStorage(ContainerEnergyStorage container) {
        this(container, 116, 64);
    }
    
    protected GuiEnergyStorage(ContainerEnergyStorage container, int chargeBarLength, int chargeBoltOffset) {
        super(container);
        this.chargeBarLength = chargeBarLength;
        this.chargeBoltOffset = chargeBoltOffset;
    }
    
    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        bindTexture();
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        drawString(11, 8, this.container.base.getGuiName(), 16448255, false);
        double capacity = this.container.base.getEUCapacity();
        int offsetY = getInfoOffsetY();
        drawString("jei.energy", offsetY, GtUtil.formatNumber(this.container.base.getStoredEU()));
        drawString("teblock.lesu.max_energy", offsetY + 8, GtUtil.formatNumber(capacity));
        drawString("teblock.lesu.max_input", offsetY + 16, GtUtil.formatNumber(this.container.base.getMaxInputEUp()));
        drawString("teblock.lesu.output", offsetY + 24, GtUtil.formatNumber(this.container.base.getMaxOutputEUp()));
        
        double charge = this.container.base.getStoredEU() / (double) this.container.base.getEUCapacity();
        drawColoredRect(8, 73, (int) (charge * this.chargeBarLength), 5, -16711681);
        drawRect(this.chargeBoltOffset + 2, 73, 1);
        drawRect(this.chargeBoltOffset + 1, 74, 1);
        drawRect(this.chargeBoltOffset, 75, 4);
        drawRect(this.chargeBoltOffset + 2, 76, 1);
        drawRect(this.chargeBoltOffset + 1, 77, 1);
    }

    protected int getInfoOffsetY() {
        return 16;
    }
    
    private void drawRect(int x, int y, int width) {
        drawColoredRect(x, y, width, 1, -1);
    }
    
    protected void drawString(String translationKey, int y, Object... args) {
        drawString(11, y, GtUtil.translate(translationKey, args), 16448255, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
