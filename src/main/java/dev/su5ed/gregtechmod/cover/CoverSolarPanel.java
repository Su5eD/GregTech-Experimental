package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.GregTechConfig;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IElectricMachine;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CoverSolarPanel extends CoverGeneric {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("solar_panel");

    private final double daytimeEnergy;
    private final double nighttimeEnergy;

    public CoverSolarPanel(ResourceLocation name, ICoverable te, Direction side, ItemStack stack, double daytimeEnergy, double nighttimeEnergy) {
        super(name, te, side, stack);
        this.daytimeEnergy = daytimeEnergy;
        this.nighttimeEnergy = nighttimeEnergy;
    }

    @Override
    public void doCoverThings() {
        if (this.side == Direction.UP && this.be instanceof IElectricMachine machine && (!GregTechConfig.COMMON.solarPanelCoverOvervoltageProtection.get() || machine.getMaxInputEUp() < this.daytimeEnergy)) {
            Level level = ((BlockEntity) this.be).getLevel();
            BlockPos pos = ((BlockEntity) this.be).getBlockPos();
            if (!level.isThundering()) {
                boolean rain = level.isRainingAt(pos) && level.getBiome(pos).getDownfall() > 0;
                if ((!rain || level.getSkyDarken() < 4) && getSky(level, pos, this.side)) {
                    machine.addEnergy(rain || !level.isDay() ? this.nighttimeEnergy : this.daytimeEnergy);
                }
            }
        }
    }

    private boolean getSky(Level level, BlockPos pos, Direction side) {
        BlockPos relative = pos.relative(side);
        return level.getBlockState(relative).isAir() && level.canSeeSky(relative);
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
