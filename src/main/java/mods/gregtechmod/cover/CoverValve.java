package mods.gregtechmod.cover;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricalMachine;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverValve extends CoverPump {

    public CoverValve(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!canWork()) return;

        if (te instanceof IElectricalMachine && mode.consumesEnergy(side) && ((IElectricalMachine)te).getUniversalEnergy() >= 128) {
            if (((IElectricalMachine) te).getUniversalEnergy() >= 128) ((IElectricalMachine)te).useEnergy(CoverConveyor.moveItemStack((TileEntity)te, side, mode), false);
        } else CoverConveyor.moveItemStack((TileEntity)te, side, mode);

        super.doCoverThings();
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/valve");
    }

    @Override
    public boolean letsItemsIn() {
        return mode.allowsInput();
    }

    @Override
    public boolean letsItemsOut() {
        return mode.allowsOutput();
    }

    @Override
    public int getTickRate() {
        return 2;
    }
}
