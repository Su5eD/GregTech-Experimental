package mods.gregtechmod.common.cover.types;

import ic2.core.IC2;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.cover.ICoverable;
import mods.gregtechmod.common.util.IGregtechMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class CoverLiquidMeter extends CoverGeneric {
    private byte mode;

    public CoverLiquidMeter(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregtechMachine)) return;
        IFluidHandler handler = ((TileEntity)te).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
        if (handler == null) return;
        int capacity = 0;
        int amount = 0;
        for (IFluidTankProperties properties : handler.getTankProperties()) {
            capacity += properties.getCapacity();
            FluidStack fluid = properties.getContents();
            if (fluid != null) amount += fluid.amount;
        }
        if (capacity > 0) {
            capacity /= 15;
            double strength = (double) amount / capacity;
            ((IGregtechMachine)te).setRedstoneOutput(side, (byte) (mode == 0 ? strength : 15-strength));
        } else {
            ((IGregtechMachine)te).setRedstoneOutput(side, (byte) (mode == 0 ? 0 : 15));
        }
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode + 1)%2);
        if (!player.world.isRemote) return false;

        if (mode == 0) IC2.platform.messagePlayer(player, "Normal");
        else IC2.platform.messagePlayer(player, "Inverted");
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

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregtechMod.MODID, "blocks/covers/liquid_meter");
    }

    @Override
    public short getTickRate() {
        return 5;
    }

    @Override
    public void onCoverRemoved() {
        if (te instanceof IGregtechMachine) ((IGregtechMachine) te).setRedstoneOutput(side, (byte) 0);
    }

    @Override
    public boolean allowEnergyTransfer() {
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
}
