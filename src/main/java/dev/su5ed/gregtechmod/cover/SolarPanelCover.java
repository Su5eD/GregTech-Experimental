package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.GregTechConfig;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.ElectricBlockEntity;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SolarPanelCover extends BaseCover<ElectricBlockEntity> {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("solar_panel");

    private final double daytimeEnergy;
    private final double nighttimeEnergy;

    public SolarPanelCover(CoverType<ElectricBlockEntity> type, ElectricBlockEntity be, Direction side, Item item, double daytimeEnergy, double nighttimeEnergy) {
        super(type, be, side, item);
        this.daytimeEnergy = daytimeEnergy;
        this.nighttimeEnergy = nighttimeEnergy;
    }

    @Override
    public void tick() {
        if (this.side == Direction.UP && (!GregTechConfig.COMMON.solarPanelCoverOvervoltageProtection.get() || this.energyHandler.getMaxInputEUp() < this.daytimeEnergy)) {
            Level level = ((BlockEntity) this.be).getLevel();
            BlockPos pos = ((BlockEntity) this.be).getBlockPos();
            if (!level.isThundering()) {
                boolean rain = level.isRainingAt(pos) && level.getBiome(pos).value().getDownfall() > 0;
                if ((!rain || level.getSkyDarken() < 4) && getSky(level, pos, this.side)) {
                    this.energyHandler.addEnergy(rain || !level.isDay() ? this.nighttimeEnergy : this.daytimeEnergy);
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
}
