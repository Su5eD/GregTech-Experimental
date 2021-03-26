package mods.gregtechmod.cover.type;

import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

public class CoverRedstoneConductor extends CoverGeneric {
    protected ConductorMode mode = ConductorMode.STRONGEST;

    public CoverRedstoneConductor(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregTechMachine)) return;
        BlockPos pos = ((TileEntity)te).getPos();
        World world = ((TileEntity)te).getWorld();

        if (mode == ConductorMode.STRONGEST) {
            byte strongest = 0;
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing == this.side) continue;

                strongest = (byte) Math.max(strongest, getPowerFromSide(facing, world, pos));
            }
            ((IGregTechMachine)te).setRedstoneOutput(this.side, strongest);
        }
        else {
            EnumFacing side = EnumFacing.byIndex(mode.ordinal() - 1);
            ((IGregTechMachine) te).setRedstoneOutput(this.side, (byte) (getPowerFromSide(side, world, pos) - 1));
        }
    }

    public int getPowerFromSide(EnumFacing side, World world, BlockPos pos) {
        int power = world.getRedstonePower(pos.offset(side), side);

        ICover cover = te.getCoverAtSide(side);
        if (cover != null) {
            if (cover.letsRedstoneIn()) return Math.max(power, cover.getRedstoneInput());
        } else {
            return power;
        }

        return 0;
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/redstone_conductor");
    }

    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
    }

    private enum ConductorMode {
        STRONGEST,
        DOWN,
        UP,
        NORTH,
        SOUTH,
        WEST,
        EAST;

        private static final ConductorMode[] VALUES = values();

        public ConductorMode next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        public String getMessageKey() {
            return Reference.MODID+".cover.conductor_mode."+this.name().toLowerCase(Locale.ROOT);
        }
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
        nbt.setString("mode", this.mode.name());
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = ConductorMode.valueOf(nbt.getString("mode"));
    }
}
