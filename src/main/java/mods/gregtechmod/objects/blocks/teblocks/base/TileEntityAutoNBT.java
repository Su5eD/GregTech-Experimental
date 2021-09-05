package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.TileEntityInventory;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.nbt.NBTTagCompound;

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
}
