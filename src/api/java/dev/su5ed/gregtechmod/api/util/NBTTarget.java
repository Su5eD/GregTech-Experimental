package dev.su5ed.gregtechmod.api.util;

public enum NBTTarget {
    SYNC(true, false),
    SAVE(false, true),
    BOTH(true, true);

    public final boolean sync;
    public final boolean save;

    NBTTarget(boolean sync, boolean save) {
        this.sync = sync;
        this.save = save;
    }

    public boolean accepts(NBTTarget target) {
        return this.sync && target.sync || this.save && target.save;
    }
}
