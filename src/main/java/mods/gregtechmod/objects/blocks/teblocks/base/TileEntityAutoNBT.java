package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.TileEntityInventory;
import ic2.core.gui.dynamic.IGuiValueProvider;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleSupplier;

public abstract class TileEntityAutoNBT extends TileEntityInventory implements IGuiValueProvider {
    private final Map<String, DoubleSupplier> guiValues = new HashMap<>();
    
    protected int tickCounter;

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        this.tickCounter++;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTSaveHandler.readClassFromNBT(this, nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTSaveHandler.writeClassToNBT(this, nbt);
        return super.writeToNBT(nbt);
    }
    
    @Override
    public final List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        getNetworkedFields(ret);
        return ret;
    }
    
    public void getNetworkedFields(List<? super String> list) {}
    
    public void addGuiValue(String name, DoubleSupplier supplier) {
        if (this.guiValues.containsKey(name)) throw new IllegalArgumentException("Duplicate Gui value " + name);
        
        this.guiValues.put(name, supplier);
    }

    @Override
    public final double getGuiValue(String name) {
        DoubleSupplier supplier = this.guiValues.get(name);
        if (supplier != null) return supplier.getAsDouble();
        
        throw new IllegalArgumentException("Cannot get value for " + name);
    }
    
    protected boolean isRedstonePowered() {
        return this.world != null && this.world.getRedstonePowerFromNeighbors(this.pos) > 0;
    }
}
