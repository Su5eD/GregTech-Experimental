package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntityIDSU;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collections;
import java.util.List;

public class ContainerIDSU extends ContainerEnergyStorage<TileEntityIDSU> {

    public ContainerIDSU(EntityPlayer player, TileEntityIDSU base) {
        super(player, base);
    }

    @Override
    public List<String> getNetworkedFields() {
        return Collections.singletonList("wrapper");
    }
}
