package mods.gregtechmod.cover.type;

import ic2.core.IC2;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CoverRedstoneConductor extends CoverGeneric {
    protected byte mode;

    public CoverRedstoneConductor(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregtechMachine)) return;
        BlockPos pos = ((TileEntity)te).getPos();
        World world = ((TileEntity)te).getWorld();

        if (mode == 0) {
            byte strongestRedstone = 0;
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing == this.side) continue;
                ICover cover = te.getCoverAtSide(facing);
                if (cover != null) strongestRedstone = (byte) Math.max(world.getRedstonePower(pos.offset(facing), facing), cover.getRedstoneInput());
            }
            ((IGregtechMachine)te).setRedstoneOutput(this.side, strongestRedstone);
        }
        else if (mode < 7) {
            EnumFacing side = EnumFacing.byIndex(mode - 1);
            ((IGregtechMachine) te).setRedstoneOutput(this.side, (byte) (world.getRedstonePower(pos.offset(side), side) - 1));
        }
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/redstone_conductor");
    }

    @Override
    public short getTickRate() {
        return 1;
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode+1)%7);
        if (!player.world.isRemote) return true;

        switch (mode) {
            case 0:
                IC2.platform.messagePlayer(player, "Conducts strongest Input");
                break;
            case 1:
                IC2.platform.messagePlayer(player, "Conducts from bottom Input");
                break;
            case 2:
                IC2.platform.messagePlayer(player, "Conducts from top Input");
                break;
            case 3:
                IC2.platform.messagePlayer(player, "Conducts from north Input");
                break;
            case 4:
                IC2.platform.messagePlayer(player, "Conducts from south Input");
                break;
            case 5:
                IC2.platform.messagePlayer(player, "Conducts from west Input");
                break;
            case 6:
                IC2.platform.messagePlayer(player, "Conducts from east Input");
                break;
        }

        return true;
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
        return true;
    }

    @Override
    public boolean letsItemsOut() {
        return true;
    }

    @Override
    public boolean overrideRedstoneOut() {
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("mode", this.mode);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = nbt.getByte("mode");
    }
}
