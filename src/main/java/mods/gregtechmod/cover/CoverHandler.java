package mods.gregtechmod.cover;

import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.state.UnlistedProperty;
import mods.gregtechmod.api.cover.CoverRegistry;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.HashMap;
import java.util.Map;

public class CoverHandler extends TileEntityComponent {
    public static final IUnlistedProperty<CoverHandler> COVER_HANDLER_PROPERTY = new UnlistedProperty<>("coverhandler", CoverHandler.class);
    public final HashMap<EnumFacing, ICover> covers = new HashMap<>();
    private final ICoverable te;
    protected final Runnable changeHandler;

    public <T extends TileEntityBlock & ICoverable> CoverHandler(T te, Runnable changeHandler) {
        super(te);
        this.te = te;
        this.changeHandler = changeHandler;
    }

    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        if (nbt.isEmpty()) return;
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (nbt.hasKey(facing.getName())) {
                NBTTagCompound cNbt = nbt.getCompoundTag(facing.getName());
                ItemStack stack = new ItemStack((NBTTagCompound) cNbt.getTag("item"));
                ICover cover = CoverRegistry.constructCover(cNbt.getString("name"), facing, te, stack);
                cover.readFromNBT(cNbt);
                this.covers.put(facing, cover);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNbt() {
        if (this.covers.isEmpty()) return null;
        NBTTagCompound ret = new NBTTagCompound();
        for (Map.Entry<EnumFacing, ICover> entry : this.covers.entrySet()) {
            ICover cover = entry.getValue();
            ItemStack stack = cover.getItem();
            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setString("name", CoverRegistry.getCoverName(cover));
            NBTTagCompound tNbt = new NBTTagCompound();
            if (stack != null) stack.writeToNBT(tNbt);
            nbt.setTag("item", tNbt);
            cover.writeToNBT(nbt);
            ret.setTag(entry.getKey().getName(), nbt);
        }
        return ret;
    }

    public boolean placeCoverAtSide(ICover cover, EnumFacing side, boolean simulate) {
        if (covers.containsKey(side)) return false;
        ResourceLocation icon = cover.getIcon();
        if (icon != null) {
            if (!simulate) {
                covers.put(side, cover);
                this.changeHandler.run();
            }
            return true;
        }
        return false;
    }

    public boolean removeCover(EnumFacing side, boolean simulate) {
        if (this.covers.containsKey(side)) {
            if (!simulate) {
                this.covers.remove(side);
                this.changeHandler.run();
            }
            return true;
        }
        return false;
    }
}
