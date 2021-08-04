package mods.gregtechmod.util;

import ic2.core.IC2;
import ic2.core.gui.INumericValueHandler;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;

public class ButtonStateHandler implements INumericValueHandler {
    private final TileEntity base;
    private final int id;
    private final Supplier<Integer> valueGetter;
    
    public ButtonStateHandler(TileEntity base, int id, Supplier<Integer> valueGetter) {
        this.base = base;
        this.id = id;
        this.valueGetter = valueGetter;
    }
    
    @Override
    public int getValue() {
        return this.valueGetter.get();
    }
    
    @Override
    public void onChange(int value) {
        IC2.network.get(false).initiateClientTileEntityEvent(this.base, id + value);
    }
}
