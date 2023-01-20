package dev.su5ed.gtexperimental.cover;

import dev.su5ed.gtexperimental.util.GtLocale;
import net.minecraft.core.Direction;

import java.util.Locale;

public enum InventoryMode {
    EXPORT,
    IMPORT(true),
    EXPORT_CONDITIONAL(false, true),
    IMPORT_CONDITIONAL(true, true),
    EXPORT_CONDITIONAL_INVERTED(false, true, true, false),
    IMPORT_CONDITIONAL_INVERTED(true, true, true, true),
    EXPORT_ALLOW_INPUT(false, false, false, true),
    IMPORT_ALLOW_OUTPUT(true),
    EXPORT_ALLOW_INPUT_CONDTIONAL(false, true, false, true),
    IMPORT_ALLOW_OUTPUT_CONDITIONAL(true, true),
    EXPORT_ALLOW_INPUT_CONDITIONAL_INVERTED(false, true, true, true),
    IMPORT_ALLOW_OUTPUT_CONDITIONAL_INVERTED(true, true, true, true);
    
    public static final InventoryMode[] VALUES = values();

    public final boolean isImport;
    public final boolean allowIO;
    public final boolean inverted;
    public final boolean conditional;

    InventoryMode() {
        this(false);
    }

    InventoryMode(boolean isImport) {
        this(isImport, false, isImport);
    }

    InventoryMode(boolean isImport, boolean conditional) {
        this(isImport, conditional, isImport);
    }

    InventoryMode(boolean isImport, boolean conditional, boolean allowIO) {
        this(isImport, conditional, false, allowIO);
    }

    InventoryMode(boolean isImport, boolean conditional, boolean inverted, boolean allowIO) {
        this.isImport = isImport;
        this.allowIO = allowIO;
        this.conditional = conditional;
        this.inverted = inverted;
    }

    public InventoryMode next() {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }

    public boolean consumesEnergy(Direction side) {
        return !(this.isImport && side == Direction.UP) && !(!this.isImport && side == Direction.DOWN);
    }

    public boolean allowsInput() {
        return this.allowIO || this.isImport;
    }

    public boolean allowsOutput() {
        return this.allowIO || !this.isImport;
    }

    public GtLocale.TranslationKey getMessageKey() {
        return GtLocale.key("cover", "inventory_mode", name().toLowerCase(Locale.ROOT));
    }
}
