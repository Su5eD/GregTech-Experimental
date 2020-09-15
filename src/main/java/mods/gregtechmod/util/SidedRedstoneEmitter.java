package mods.gregtechmod.util;

import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.TileEntityComponent;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class SidedRedstoneEmitter extends TileEntityComponent {
    private final Map<EnumFacing, Byte> levels = new HashMap<>();
    private Map<EnumFacing, Byte> oldLevels = new HashMap<>();

    public SidedRedstoneEmitter(TileEntityBlock parent) {
        super(parent);
    }

    public int getLevel(EnumFacing side) {
        if (!levels.containsKey(side)) return 0;
        return this.levels.get(side);
    }

    public void setLevel(EnumFacing side, byte newLevel) {
        if (this.levels.containsKey(side)) {
            if (this.levels.get(side) == newLevel) return;
        }
        this.levels.put(side, newLevel);
        this.onChange();
    }

    public void onChange() {
        this.parent.getWorld().notifyNeighborsOfStateChange(this.parent.getPos(), this.parent.getBlockType(), false);
    }

    public boolean enableWorldTick() {
        return !this.levels.isEmpty();
    }

    public void onWorldTick() {
        assert !this.levels.isEmpty() && !this.levels.equals(this.oldLevels);
        this.oldLevels = levels;
    }
}
