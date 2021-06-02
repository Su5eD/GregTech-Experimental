package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerChemicalReactor;
import net.minecraft.util.ResourceLocation;

public class GuiChemicalReactor extends GuiInventory<ContainerChemicalReactor> {

    public GuiChemicalReactor(ContainerChemicalReactor container) {
        super(container);
        
        for (int i = 0; i < 3; i++) {
            addElement(new LinkedGauge(this, 73 + i * 10, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_DOWN));
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/chemical_reactor.png");
    }
}
