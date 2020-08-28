package mods.gregtechmod.common.cover.type;

import ic2.core.IC2;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.common.core.GregtechMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverConveyor extends CoverGeneric {
    protected byte mode;

    public CoverConveyor(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (mode % 6 > 1 && te instanceof IGregtechMachine && (((IGregtechMachine)te).isAllowedToWork() != (mode % 6 < 4))) return;

        if (te instanceof IGregtechMachine && !(mode%2==1 && side==EnumFacing.UP) && !(mode%2==0&&side==EnumFacing.DOWN) && ((IGregtechMachine)te).getUniversalEnergyCapacity() >= 128) {
            if (((IGregtechMachine)te).getUniversalEnergy() >= 128) {
                ((IGregtechMachine)te).useEnergy(moveItemStack((TileEntity)te, side, mode), false);
            }
        }
        else moveItemStack((TileEntity)te, side, mode);
    }

    public static int moveItemStack(TileEntity source, EnumFacing side, byte mode) {
        StackUtil.AdjacentInv target = StackUtil.getAdjacentInventory(source, side);
        if (target != null) return StackUtil.transfer(mode%2==0 ? source : target.te, mode%2==0 ? target.te : source, mode%2==0?side:side.getOpposite(), 64);
        return 0;
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
        return new ResourceLocation(GregtechMod.MODID, "blocks/covers/conveyor");
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
        return true;
    }

    @Override
    public boolean letsLiquidsOut() {
        return true;
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
        return 10;
    }
}
