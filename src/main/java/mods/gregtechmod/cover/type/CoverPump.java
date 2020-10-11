package mods.gregtechmod.cover.type;

import ic2.core.IC2;
import ic2.core.util.LiquidUtil;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class CoverPump extends CoverGeneric {
    protected byte mode;

    public CoverPump(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (mode % 6 > 1 && te instanceof IGregtechMachine && (((IGregtechMachine)te).isAllowedToWork() != (mode % 6 < 4))) return;
        if (LiquidUtil.isFluidTile((TileEntity)te, side)) {

            LiquidUtil.AdjacentFluidHandler target = LiquidUtil.getAdjacentHandler((TileEntity)te, side);
            if (target == null) return;

            FluidStack stack = LiquidUtil.drainTile(mode%2==0 ? (TileEntity)te : target.handler, mode%2==0 ? side : side.getOpposite(), 1000, true);
            if (stack == null) return;

            if (te instanceof IGregtechMachine && !(mode%2==1 && side==EnumFacing.UP) && !(mode%2==0 && side==EnumFacing.DOWN) && ((IGregtechMachine)te).getUniversalEnergy() >= Math.min(1, stack.amount/100)) {
                ((IGregtechMachine)te).useEnergy(Math.min(1, stack.amount/100), false);
            }

            LiquidUtil.transfer(mode%2==0 ? (TileEntity)te : target.handler, side, mode%2==0 ? target.handler : (TileEntity)te, 1000);
        }
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode + 1)%12);
        if (!player.world.isRemote) return true;

        switch (mode) {
            case 0:
                IC2.platform.messagePlayer(player, "Export");
                break;
            case 1:
                IC2.platform.messagePlayer(player, "Import");
                break;
            case 2:
                IC2.platform.messagePlayer(player, "Export (conditional)");
                break;
            case 3:
                IC2.platform.messagePlayer(player, "Import (conditional)");
                break;
            case 4:
                IC2.platform.messagePlayer(player, "Export (invert cond)");
                break;
            case 5:
                IC2.platform.messagePlayer(player, "Import (invert cond)");
                break;
            case 6:
                IC2.platform.messagePlayer(player, "Export allow Input");
                break;
            case 7:
                IC2.platform.messagePlayer(player, "Import allow Output");
                break;
            case 8:
                IC2.platform.messagePlayer(player, "Export allow Input (conditional)");
                break;
            case 9:
                IC2.platform.messagePlayer(player, "Import allow Output (conditional)");
                break;
            case 10:
                IC2.platform.messagePlayer(player, "Export allow Input (invert cond)");
                break;
            case 11:
                IC2.platform.messagePlayer(player, "Import allow Output (invert cond)");
                break;
        }
        return true;
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregTechMod.MODID, "blocks/covers/pump");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("mode", mode);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = nbt.getByte("mode");
    }

    @Override
    public boolean allowEnergyTransfer() {
        return true;
    }

    @Override
    public boolean letsRedstoneIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneOut() {
        return true;
    }

    @Override
    public boolean letsLiquidsIn() {
        if (mode > 1 && te instanceof IGregtechMachine && (((IGregtechMachine)te).isAllowedToWork() != mode % 6 < 4)) return false;

        return mode>=6||mode%2 != 0;
    }

    @Override
    public boolean letsLiquidsOut() {
        if (mode > 1 && te instanceof IGregtechMachine && (((IGregtechMachine)te).isAllowedToWork() != mode % 6 < 4)) return false;

        return mode>=6||mode%2 == 0;
    }

    @Override
    public boolean letsItemsIn() {
        return true;
    }

    @Override
    public boolean letsItemsOut() {
        return true;
    }

    @Override
    public short getTickRate() {
        return 1;
    }
}
