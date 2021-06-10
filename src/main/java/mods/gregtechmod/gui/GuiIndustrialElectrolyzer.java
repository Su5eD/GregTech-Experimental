package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.slot.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialElectrolyzer;
import net.minecraft.util.ResourceLocation;

public class GuiIndustrialElectrolyzer extends GuiInventory<ContainerIndustrialElectrolyzer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/industrial_electrolyzer.png");

    public GuiIndustrialElectrolyzer(ContainerIndustrialElectrolyzer container) {
        super(container);
        addElement(new CustomFluidSlot(this, 109, 45, container.base.tank, GregTechMod.COMMON_TEXTURE, 40, 0, false));

        addElement(new LinkedGauge(this, 73, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
        addElement(new LinkedGauge(this, 83, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
        addElement(new LinkedGauge(this, 93, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
