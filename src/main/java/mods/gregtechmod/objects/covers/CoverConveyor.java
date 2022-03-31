package mods.gregtechmod.objects.covers;

import ic2.core.util.StackUtil;
import ic2.core.util.StackUtil.AdjacentInv;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverConveyor extends CoverInventory {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("conveyor");

    public CoverConveyor(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (canWork()) {
            if (shouldUseEnergy(128)) {
                if (((IElectricMachine) te).canUseEnergy(128)) ((IElectricMachine) te).useEnergy(moveItemStack((TileEntity) te, side, mode));
            }
            else moveItemStack((TileEntity) te, side, mode);
        }
    }

    public static int moveItemStack(TileEntity source, EnumFacing side, InventoryMode mode) {
        AdjacentInv target = StackUtil.getAdjacentInventory(source, side);
        return target != null ? StackUtil.transfer(mode.isImport ? target.te : source, mode.isImport ? source : target.te, mode.isImport ? side.getOpposite() : side, 64) : 0;
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
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
