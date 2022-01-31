package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.generator.TileEntityMagicEnergyAbsorber;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMagicEnergyAbsorber extends ContainerGtInventory<TileEntityMagicEnergyAbsorber> {

    public ContainerMagicEnergyAbsorber(EntityPlayer player, TileEntityMagicEnergyAbsorber base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 17));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 53));
        
        addSlotToContainer(SlotInteractive.serverOnly(10, 18, base::switchDrainAura));
        addSlotToContainer(SlotInteractive.serverOnly(10, 35, base::switchDrainCrystalEnergy));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("drainAura");
        list.add("drainCrystalEnergy");
    }
}
