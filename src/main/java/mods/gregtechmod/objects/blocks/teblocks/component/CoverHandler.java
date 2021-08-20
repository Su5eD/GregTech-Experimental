package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.state.UnlistedProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverProvider;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.HashMap;
import java.util.Map;

public class CoverHandler extends TileEntityComponent {
    public static final IUnlistedProperty<CoverHandler> COVER_HANDLER_PROPERTY = new UnlistedProperty<>("coverhandler", CoverHandler.class);
    public final Map<EnumFacing, ICover> covers = new HashMap<>();
    private final Runnable changeHandler;

    public <T extends TileEntityBlock & ICoverable> CoverHandler(T te, Runnable changeHandler) {
        super(te);
        this.changeHandler = changeHandler;
    }

    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        if (!nbt.isEmpty()) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                if (nbt.hasKey(facing.getName())) {
                    NBTTagCompound coverNbt = nbt.getCompoundTag(facing.getName());
                    ItemStack stack = new ItemStack((NBTTagCompound) coverNbt.getTag("item"));
                    ResourceLocation name = new ResourceLocation(coverNbt.getString("name"));
                    ICoverProvider provider = GregTechAPI.COVERS.getValue(name);
                    if (provider != null) {
                        ICover cover = provider.constructCover(facing, (ICoverable) this.parent, stack);
                        cover.readFromNBT(coverNbt);
                        this.covers.put(facing, cover);
                    } else {
                        GregTechMod.logger.error("CoverProvider for {} not found", name);
                    }
                }
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

            nbt.setString("name", cover.getName().toString());
            NBTTagCompound stackNbt = new NBTTagCompound();
            stack.writeToNBT(stackNbt);
            nbt.setTag("item", stackNbt);
            cover.writeToNBT(nbt);
            ret.setTag(entry.getKey().getName(), nbt);
        }
        return ret;
    }

    public boolean placeCoverAtSide(ICover cover, EnumFacing side, boolean simulate) {
        if (!this.covers.containsKey(side)) {
            ResourceLocation icon = cover.getIcon();
            if (icon != null) {
                if (!simulate) {
                    this.covers.put(side, cover);
                    this.changeHandler.run();
                }
                return true;
            }
        }
        return false;
    }

    public boolean removeCover(EnumFacing side, boolean simulate) {
        ICover cover = this.covers.get(side);
        if (cover != null) {
            if (!simulate) {
                cover.onCoverRemove();
                this.covers.remove(side);
                this.changeHandler.run();
            }
            return true;
        }
        return false;
    }
}
