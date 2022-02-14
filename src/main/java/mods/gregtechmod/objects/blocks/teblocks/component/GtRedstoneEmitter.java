package mods.gregtechmod.objects.blocks.teblocks.component;

import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityAutoNBT;
import mods.gregtechmod.util.nbt.NBTPersistent;

public class GtRedstoneEmitter extends GtComponentBase {
    @NBTPersistent
    private int level;
    private final Runnable onUpdate;

    public GtRedstoneEmitter(TileEntityAutoNBT parent, Runnable onUpdate) {
        super(parent);
        this.onUpdate = onUpdate;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        if (level != this.level) {
            this.level = level;
            this.onUpdate.run();
            ((TileEntityAutoNBT) this.parent).updateAndNotifyNeighbors();
        }
    }

    public boolean emitsRedstone() {
        return this.level > 0;
    }
}
