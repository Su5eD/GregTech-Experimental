package mods.gregtechmod.cover.type;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.core.GregTechMod;
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
        if (mode % 6 > 1 && te instanceof IGregtechMachine && (((IGregtechMachine)te).isAllowedToWork() != (mode % 6 < 4))) return;

        if (!(mode%2==1 && side==EnumFacing.UP) && !(mode%2==0 && side==EnumFacing.DOWN) && te instanceof IGregtechMachine && ((IGregtechMachine)te).getUniversalEnergy() >= 128) {
            ((IGregtechMachine)te).useEnergy(CoverConveyor.moveItemStack((TileEntity)te, side, mode), false);
        } else CoverConveyor.moveItemStack((TileEntity)te, side, mode);

        super.doCoverThings();
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregTechMod.MODID, "blocks/covers/valve");
    }

    @Override
    public boolean letsItemsIn() {
        return mode>=6||mode%2!=0;
    }

    @Override
    public boolean letsItemsOut() {
        return mode>=6||mode%2==0;
    }

    @Override
    public short getTickRate() {
        return 2;
    }
}
