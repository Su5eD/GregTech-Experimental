package dev.su5ed.gregtechmod.api.util;

public enum CoverInteractionResult {
    UPDATE(true),
    SUCCESS(true),
    PASS(false);
    
    private final boolean isSuccess;

    CoverInteractionResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    
    public boolean isSuccess() {
        return this.isSuccess;
    }
}
