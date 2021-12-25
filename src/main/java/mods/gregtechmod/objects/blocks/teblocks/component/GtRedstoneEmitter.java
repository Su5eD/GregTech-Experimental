package mods.gregtechmod.objects.blocks.teblocks.component;

import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityAutoNBT;
import net.minecraft.nbt.NBTTagCompound;

public class GtRedstoneEmitter extends GtComponentBase {
    private int level;
    private final Runnable onUpdate;

    public GtRedstoneEmitter(TileEntityAutoNBT parent, Runnable onUpdate) {
        super(parent);
        this.onUpdate = onUpdate;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        if (level != this.level) {
            this.level = level;
            this.onUpdate.run();
            ((TileEntityAutoNBT) this.parent).updateRender();
        }
    }

    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        this.level = nbt.getInteger("level");
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("level", this.level);
        return nbt;
    }
}
