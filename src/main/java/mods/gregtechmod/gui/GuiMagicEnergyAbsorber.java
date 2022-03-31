package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMagicEnergyAbsorber;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiMagicEnergyAbsorber extends GuiInventory<ContainerMagicEnergyAbsorber> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("magic_energy_absorber");

    public GuiMagicEnergyAbsorber(ContainerMagicEnergyAbsorber container) {
        super(container);

        addIconCycle(10, 18, TEXTURE, 176, 0, 16, true, () -> container.base.drainAura);
        addIconCycle(10, 35, TEXTURE, 176, 0, 16, true, () -> container.base.drainCrystalEnergy);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
