package mods.gregtechmod.cover;

import ic2.api.energy.EnergyNet;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CoverSolarPanel extends CoverGeneric {
    private double daytimeEnergy;
    private double nighttimeEnergy;

    public CoverSolarPanel(ICoverable te, EnumFacing side, ItemStack stack, double daytimeEnergy, double nighttimeEnergy) {
        super(te, side, stack);
        this.daytimeEnergy = daytimeEnergy;
        this.nighttimeEnergy = nighttimeEnergy;
    }

    @Override
    public void doCoverThings() {
        if (!(side == EnumFacing.UP) || !(te instanceof IElectricMachine)) return;

        if (GregTechConfig.BALANCE.solarPanelCoverOvervoltageProtection && ((IElectricMachine)te).getSinkTier() < EnergyNet.instance.getTierFromPower(this.daytimeEnergy)) return;

        World world = ((TileEntity)te).getWorld();
        BlockPos pos = ((TileEntity)te).getPos();
        if (!world.isThundering()) {
            boolean bRain = world.isRaining() && world.getBiome(((TileEntity)te).getPos()).getRainfall() > 0;
            if ((!bRain || world.getSkylightSubtracted() < 4) && getSkyAtSide(world, pos, side)) {
                ((IElectricMachine)te).addEnergy(bRain || !world.isDaytime() ? nighttimeEnergy : daytimeEnergy);
            }
        }
    }

    private boolean getSkyAtSide(World world, BlockPos pos, EnumFacing side) {
        return getSky(world, pos.offset(side));
    }

    private boolean getSky(World world, BlockPos pos) {
        if (!GtUtil.isAir(world, pos)) return false;
        return world.canBlockSeeSky(pos);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/solar_panel");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setDouble("daytimeEnergy", this.daytimeEnergy);
        nbt.setDouble("nighttimeEnergy", this.nighttimeEnergy);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.daytimeEnergy = nbt.getDouble("daytimeEnergy");
        this.nighttimeEnergy = nbt.getDouble("nighttimeEnergy");
    }

    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    public CoverType getType() {
        return CoverType.ENERGY;
    }
}
