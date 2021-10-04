package mods.gregtechmod.gui;

import ic2.core.gui.CustomButton;
import ic2.core.gui.CycleHandler;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMagicEnergyAbsorber;
import mods.gregtechmod.util.ButtonStateHandler;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiMagicEnergyAbsorber extends GuiInventory<ContainerMagicEnergyAbsorber> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("magic_energy_absorber");

    public GuiMagicEnergyAbsorber(ContainerMagicEnergyAbsorber container) {
        super(container);
        
        CycleHandler crystalEnergyCycleHandler = new CycleHandler(176, 0, 192, 32, 16, true, 2,
                new ButtonStateHandler(container.base, 0, () -> GuiMagicEnergyAbsorber.this.container.base.drainAura ? 1 : 0));
        this.addElement(new CustomButton(this, 10, 18, 16, 16, crystalEnergyCycleHandler, TEXTURE, crystalEnergyCycleHandler));
        
        CycleHandler auraCycleHandler = new CycleHandler(176, 0, 192, 32, 16, true, 2,
                new ButtonStateHandler(container.base, 2, () -> GuiMagicEnergyAbsorber.this.container.base.drainCrystalEnergy ? 1 : 0));
        this.addElement(new CustomButton(this, 10, 35, 16, 16, auraCycleHandler, TEXTURE, auraCycleHandler));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
