package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMagicEnergyAbsorber;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiMagicEnergyAbsorber extends GuiInventory<ContainerMagicEnergyAbsorber> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("magic_energy_absorber");

    public GuiMagicEnergyAbsorber(ContainerMagicEnergyAbsorber container) {
        super(container);
        
        addVerticalCycleButton(0, 176, 0, 192, 32, 10, 18, 16, TEXTURE, () -> GuiMagicEnergyAbsorber.this.container.base.drainAura ? 1 : 0);
        addVerticalCycleButton(2, 176, 0, 192, 32, 10, 35, 16, TEXTURE, () -> GuiMagicEnergyAbsorber.this.container.base.drainCrystalEnergy ? 1 : 0);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
