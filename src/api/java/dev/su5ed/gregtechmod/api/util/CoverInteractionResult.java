package dev.su5ed.gregtechmod.api.util;

public enum CoverInteractionResult {
    RERENDER(true, true),
    CHANGED(true, true),
    SUCCESS(true, false),
    PASS(false, false);
    
    private final boolean success;
    private final boolean changed;

    CoverInteractionResult(boolean success, boolean changed) {
        this.success = success;
        this.changed = changed;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isChanged() {
        return this.changed;
    }
}
