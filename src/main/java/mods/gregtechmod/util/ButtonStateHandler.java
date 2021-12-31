package mods.gregtechmod.util;

import ic2.core.IC2;
import ic2.core.gui.INumericValueHandler;
import net.minecraft.tileentity.TileEntity;

import java.util.function.IntSupplier;

public class ButtonStateHandler implements INumericValueHandler {
    private final TileEntity base;
    private final int id;
    private final boolean sendUpdate;
    private final IntSupplier valueGetter;
    
    public ButtonStateHandler(TileEntity base, int id, boolean sendUpdate, IntSupplier valueGetter) {
        this.base = base;
        this.id = id;
        this.sendUpdate = sendUpdate;
        this.valueGetter = valueGetter;
    }
    
    @Override
    public int getValue() {
        return this.valueGetter.getAsInt();
    }
    
    @Override
    public void onChange(int value) {
        if (this.sendUpdate) IC2.network.get(false).initiateClientTileEntityEvent(this.base, this.id + value);
    }
}
