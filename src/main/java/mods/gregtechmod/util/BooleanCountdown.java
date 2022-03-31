package mods.gregtechmod.util;

public class BooleanCountdown {
    private final int initialCount;
    private int count;

    public BooleanCountdown(int count) {
        this.initialCount = count;
    }

    public void reset() {
        this.count = this.initialCount;
    }

    public void countDown() {
        if (this.count > 0) this.count--;
    }

    public boolean get() {
        return this.count > 0;
    }
}
