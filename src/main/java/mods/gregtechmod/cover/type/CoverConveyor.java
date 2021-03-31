package mods.gregtechmod.cover.type;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.InventoryMode;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverConveyor extends CoverInventory {

    public CoverConveyor(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!canWork()) return;

        if (te instanceof IGregTechMachine && mode.consumesEnergy(side) && ((IGregTechMachine)te).getUniversalEnergyCapacity() >= 128) {
            if (((IGregTechMachine) te).getUniversalEnergy() >= 128) ((IGregTechMachine)te).useEnergy(moveItemStack((TileEntity)te, side, mode), false);
        } else moveItemStack((TileEntity)te, side, mode);
    }

    public static int moveItemStack(TileEntity source, EnumFacing side, InventoryMode mode) {
        StackUtil.AdjacentInv target = StackUtil.getAdjacentInventory(source, side);
        if (target != null) return StackUtil.transfer(mode.isImport ? target.te : source, mode.isImport ? source : target.te, mode.isImport ? side.getOpposite() : side, 64);
        return 0;
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/conveyor");
    }

    @Override
    public boolean letsLiquidsIn() {
        return true;
    }

    @Override
    public boolean letsLiquidsOut() {
        return true;
    }

    @Override
    public int getTickRate() {
        return 10;
    }
}
