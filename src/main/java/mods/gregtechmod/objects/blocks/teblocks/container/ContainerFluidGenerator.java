package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerFluidGenerator extends ContainerBasicTank<TileEntityFluidGenerator> {

    public ContainerFluidGenerator(EntityPlayer player, TileEntityFluidGenerator base) {
        super(player, base);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("solidFuelEnergy");
    }
}
