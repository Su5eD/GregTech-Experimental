package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeReactor;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeReactor;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiComputerCubeReactor extends GuiComputerCubeModule<ComputerCubeReactor, ContainerComputerCubeReactor> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("computer_cube_reactor");

    public GuiComputerCubeReactor(ContainerComputerCubeReactor container) {
        super(container, 176, ComputerCubeReactor.class);
    }

    @Override
    protected void drawBackground(ComputerCubeReactor module, float partialTicks, int mouseX, int mouseY) {
        int eu = module.getEu();
        int euOut = module.getEuOut();
        drawString(7, 108, GtUtil.translate("computercube.reactor.stats"), GuiColors.WHITE, false);
        drawString(7, 120, GtUtil.translate("computercube.reactor.output", GtUtil.formatNumber(eu), euOut), GuiColors.WHITE, false);
        drawString(7, 128, GtUtil.translate("computercube.reactor.hem", GtUtil.formatNumber(module.getHeatEffectModifier())), GuiColors.WHITE, false);
        drawString(7, 136, GtUtil.translate("computercube.reactor.heat", GtUtil.formatNumber(module.getHeat()), GtUtil.formatNumber(module.getMaxHeat())), GuiColors.WHITE, false);
        drawString(7, 144, GtUtil.translate("computercube.reactor.explosion_power", GtUtil.formatNumber(module.getExplosionStrength())), GuiColors.WHITE, false);
        drawString(7, 152, GtUtil.translate("computercube.reactor.runtime", euOut > 0 ? (eu / euOut) / 20F : 0), GuiColors.WHITE, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
