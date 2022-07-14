package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.fusion.TileEntityFusionMaterialInjector;
import mods.gregtechmod.objects.blocks.teblocks.multiblock.TileEntityHatchIO;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFusionMaterialInjector extends ContainerBasicTank<TileEntityFusionMaterialInjector> {

    public ContainerFusionMaterialInjector(EntityPlayer player, TileEntityFusionMaterialInjector base) {
        super(player, base);
    }

    @Override
    protected InvSlot getInputSlot() {
        return this.base.tank.inputSlot;
    }

    @Override
    protected InvSlot getOutputSlot() {
        return this.base.tank.outputSlot;
    }
}
