package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import java.util.List;

public class ContainerMachineBase<T extends IInventory> extends ContainerFullInv<T> {

    public ContainerMachineBase(EntityPlayer player, T base, int height) {
        super(player, base, height);
    }

    @Override
    public final List<String> getNetworkedFields() {
        return super.getNetworkedFields();
    }
    
    protected void getNetworkedFields(List<? super String> list) {
        list.add("guiProgress");
    }
}
