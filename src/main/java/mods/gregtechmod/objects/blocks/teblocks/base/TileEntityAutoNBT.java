package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.TileEntityInventory;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public abstract class TileEntityAutoNBT extends TileEntityInventory {

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
}
