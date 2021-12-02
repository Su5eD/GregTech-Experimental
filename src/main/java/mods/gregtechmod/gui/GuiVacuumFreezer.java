package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerVacuumFreezer;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiVacuumFreezer extends GuiStructure<ContainerVacuumFreezer> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("vacuum_freezer");

    public GuiVacuumFreezer(ContainerVacuumFreezer container) {
        super(container);
        
        addElement(new LinkedGauge(this, 58, 28, container.base, "progress", GregtechGauge.FREEZING));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
