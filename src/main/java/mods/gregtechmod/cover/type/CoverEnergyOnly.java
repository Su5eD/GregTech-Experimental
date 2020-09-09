package mods.gregtechmod.cover.type;

import ic2.core.IC2;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.core.GregtechMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverEnergyOnly extends CoverGeneric {
    protected byte mode;

    public CoverEnergyOnly(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregtechMod.MODID, "blocks/covers/energy_only");
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode+1)%3);
        if (!player.world.isRemote) return true;

        switch (mode) {
            case 0:
                IC2.platform.messagePlayer(player, "Allow");
                break;
            case 1:
                IC2.platform.messagePlayer(player, "Allow (conditional)");
                break;
            case 2:
                IC2.platform.messagePlayer(player, "Disallow (conditional)");
                break;
        }

        return true;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return !(mode > 0 && te instanceof IGregtechMachine && (((IGregtechMachine)te).isAllowedToWork() != mode < 2));
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
