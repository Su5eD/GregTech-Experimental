package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CoverSolarPanel extends CoverGeneric {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("solar_panel");

    private final double daytimeEnergy;
    private final double nighttimeEnergy;

    public CoverSolarPanel(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack, double daytimeEnergy, double nighttimeEnergy) {
        super(name, te, side, stack);
        this.daytimeEnergy = daytimeEnergy;
        this.nighttimeEnergy = nighttimeEnergy;
    }

    @Override
    public void doCoverThings() {
        if (side != EnumFacing.UP || !(te instanceof IElectricMachine)) return;

        if (GregTechConfig.BALANCE.solarPanelCoverOvervoltageProtection && ((IElectricMachine) te).getMaxInputEUp() < this.daytimeEnergy) return;

        World world = ((TileEntity) te).getWorld();
        BlockPos pos = ((TileEntity) te).getPos();
        if (!world.isThundering()) {
            boolean bRain = world.isRaining() && world.getBiome(pos).getRainfall() > 0;
            if ((!bRain || world.getSkylightSubtracted() < 4) && getSkyAtSide(world, pos, side)) {
                ((IElectricMachine) te).addEnergy(bRain || !world.isDaytime() ? nighttimeEnergy : daytimeEnergy);
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
        return TEXTURE;
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
