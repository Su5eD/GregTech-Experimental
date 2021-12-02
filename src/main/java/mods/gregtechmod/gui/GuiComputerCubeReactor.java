package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeReactor;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeReactor;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
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
        drawString(7, 108, GtLocale.translate("computercube.reactor.stats"), GuiColors.WHITE, false);
        drawString(7, 120, GtLocale.translate("computercube.reactor.output", JavaUtil.formatNumber(eu), euOut), GuiColors.WHITE, false);
        drawString(7, 128, GtLocale.translate("computercube.reactor.hem", JavaUtil.formatNumber(module.getHeatEffectModifier())), GuiColors.WHITE, false);
        drawString(7, 136, GtLocale.translate("computercube.reactor.heat", JavaUtil.formatNumber(module.getHeat()), JavaUtil.formatNumber(module.getMaxHeat())), GuiColors.WHITE, false);
        drawString(7, 144, GtLocale.translate("computercube.reactor.explosion_power", JavaUtil.formatNumber(module.getExplosionStrength())), GuiColors.WHITE, false);
        drawString(7, 152, GtLocale.translate("computercube.reactor.runtime", euOut > 0 ? (eu / euOut) / 20F : 0), GuiColors.WHITE, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
