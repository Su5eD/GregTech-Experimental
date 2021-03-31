package mods.gregtechmod.cover.type;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CoverSolarPanel extends CoverGeneric {
    private double voltageDay;
    private double voltageNight;

    public CoverSolarPanel(ICoverable te, EnumFacing side, ItemStack stack, double EUtDay, double EUtNight) {
        super(te, side, stack);
        this.voltageDay = EUtDay;
        this.voltageNight = EUtNight;
    }

    @Override
    public void doCoverThings() {
        if (!(side == EnumFacing.UP) || !(te instanceof IGregTechMachine)) return;

        if (GregTechConfig.BALANCE.solarPanelCoverOvervoltageProtection && !(((IGregTechMachine)te).getInputVoltage() * 32 >= this.voltageDay)) return;

        World world = ((TileEntity)te).getWorld();
        BlockPos pos = ((TileEntity)te).getPos();
        if (!world.isThundering()) {
            boolean bRain = world.isRaining() && world.getBiome(((TileEntity)te).getPos()).getRainfall() > 0;
            if ((!bRain || world.getSkylightSubtracted() < 4) && getSkyAtSide(world, pos, side)) {
                ((IGregTechMachine)te).addEnergy(bRain || !world.isDaytime()?voltageNight:voltageDay);
            }
        }
    }

    private boolean getSkyAtSide(World world, BlockPos pos, EnumFacing side) {
        return getSky(world, pos.offset(side));
    }

    private boolean getSky(World world, BlockPos pos) {
        if (!(world.getBlockState(pos) == Blocks.AIR.getDefaultState())) return false;
        return world.canBlockSeeSky(pos);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/solar_panel");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setDouble("voltageDay", this.voltageDay);
        nbt.setDouble("voltageNight", this.voltageNight);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.voltageDay = nbt.getDouble("voltageDay");
        this.voltageNight = nbt.getDouble("voltageNight");
    }

    @Override
    public int getTickRate() {
        return 1;
    }
}
