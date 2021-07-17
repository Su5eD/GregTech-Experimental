package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.inventory.gui.GeneratorFluidSlot;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiPlasmaGenerator extends GuiFluidGenerator {
    
    public GuiPlasmaGenerator(ContainerBasicTank<TileEntityFluidGenerator> container, GtFluidTank fluidTank) {
        super(container, fluidTank);
    }

    @Override
    protected void addFluidSlot() {
        addElement(new GeneratorFluidSlot(this, 63, 41, this.fluidTank, null, 0, 0, true));
    }

    @Override
    protected String getDisplayName() {
        return GtUtil.translateInfo("plasma_amount");
    }
    
    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/plasma_generator.png");
    }
}
