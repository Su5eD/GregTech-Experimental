package dev.su5ed.gtexperimental.util.inventory;

import net.minecraft.world.inventory.DataSlot;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class DataSlotWrapper extends DataSlot {
    private final IntSupplier getter;
    private final IntConsumer setter;

    public DataSlotWrapper(IntSupplier getter, IntConsumer setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public int get() {
        return this.getter.getAsInt();
    }

    @Override
    public void set(int value) {
        this.setter.accept(value);
    }
}
