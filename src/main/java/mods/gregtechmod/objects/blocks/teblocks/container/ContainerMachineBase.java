package mods.gregtechmod.objects.blocks.teblocks.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import java.util.List;

public class ContainerMachineBase<T extends IInventory> extends ContainerGtInventory<T> {

    public ContainerMachineBase(EntityPlayer player, T base) {
        super(player, base);
    }
    
    @Override
    public void getNetworkedFields(List<? super String> list) {
        list.add("progress");
        list.add("maxProgress");
    }
}
