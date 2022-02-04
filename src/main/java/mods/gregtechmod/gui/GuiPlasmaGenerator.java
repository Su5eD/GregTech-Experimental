package mods.gregtechmod.gui;

import mods.gregtechmod.gui.element.GeneratorFluidSlot;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiPlasmaGenerator extends GuiFluidGenerator {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("plasma_generator");

    public GuiPlasmaGenerator(ContainerBasicTank<TileEntityFluidGenerator> container, GtFluidTank fluidTank) {
        super(container, fluidTank);
    }

    @Override
    protected void addFluidSlot() {
        addElement(new GeneratorFluidSlot(this, 63, 41, this.fluidTank));
    }

    @Override
    protected String getDisplayName() {
        return GtLocale.translateInfo("plasma_amount");
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
