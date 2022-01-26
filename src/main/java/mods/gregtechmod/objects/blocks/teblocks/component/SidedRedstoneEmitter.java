package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.TileEntityBlock;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class SidedRedstoneEmitter extends GtComponentBase {
    private final Map<EnumFacing, Integer> levels = new HashMap<>();
    private Map<EnumFacing, Integer> oldLevels = new HashMap<>();

    public SidedRedstoneEmitter(TileEntityBlock parent) {
        super(parent);
    }

    public int getLevel(EnumFacing side) {
        return this.levels.getOrDefault(side, 0);
    }

    public void setLevel(EnumFacing side, int newLevel) {
        if (getLevel(side) != newLevel) {
            this.levels.put(side, newLevel);
            onChange();
        }
    }

    public void onChange() {
        this.parent.getWorld().notifyNeighborsOfStateChange(this.parent.getPos(), this.parent.getBlockType(), false);
    }

    @Override
    public boolean enableWorldTick() {
        return !this.levels.isEmpty();
    }

    @Override
    public void onWorldTick() {
        if (!this.levels.isEmpty() && !this.levels.equals(this.oldLevels)) this.oldLevels = levels;
    }
}
