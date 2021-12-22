package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import java.util.List;

public abstract class ContainerBase<T extends IInventory> extends ContainerFullInv<T> {

    public ContainerBase(EntityPlayer player, T base) {
        super(player, base, 166);
    }

    @Override
    public final List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        getNetworkedFields(ret);
        return ret;
    }

    protected void getNetworkedFields(List<? super String> list) {}
}
