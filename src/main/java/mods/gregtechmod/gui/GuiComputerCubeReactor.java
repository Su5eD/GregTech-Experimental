package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeReactor;
import mods.gregtechmod.objects.blocks.teblocks.computercube.IComputerCubeModule;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeReactor;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiComputerCubeReactor extends GuiIC2<ContainerComputerCubeReactor> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/computer_cube_reactor.png");
    
    private ComputerCubeReactor module;

    public GuiComputerCubeReactor(ContainerComputerCubeReactor container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
        // cache module to prevent disappearing text when switching modules
        IComputerCubeModule newModule = this.container.base.getActiveModule();
        if (newModule instanceof ComputerCubeReactor) this.module = (ComputerCubeReactor) newModule;
        
        if (this.module != null) {
            int eu = this.module.getEu();
            int euOut = this.module.getEuOut();
            drawString(7, 108, GtUtil.translate("computercube.reactor.stats"), 16448255, false);
            drawString(7, 120, GtUtil.translate("computercube.reactor.output", GtUtil.formatNumber(eu), euOut), 16448255, false);
            drawString(7, 128, GtUtil.translate("computercube.reactor.hem", GtUtil.formatNumber(this.module.getHeatEffectModifier())), 16448255, false);
            drawString(7, 136, GtUtil.translate("computercube.reactor.heat", GtUtil.formatNumber(this.module.getHeat()), GtUtil.formatNumber(this.module.getMaxHeat())), 16448255, false);
            drawString(7, 144, GtUtil.translate("computercube.reactor.explosion_power", GtUtil.formatNumber(this.module.getExplosionStrength())), 16448255, false);
            drawString(7, 152, GtUtil.translate("computercube.reactor.runtime", euOut > 0 ? (eu / euOut) / 20F : 0), 16448255, false);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
