package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.generator.TileEntityMagicEnergyAbsorber;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMagicEnergyAbsorber extends ContainerFullInv<TileEntityMagicEnergyAbsorber> {

    public ContainerMagicEnergyAbsorber(EntityPlayer player, TileEntityMagicEnergyAbsorber base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 17));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 53));
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("drainCrystalEnergy");
        ret.add("drainAura");
        return ret;
    }
}
