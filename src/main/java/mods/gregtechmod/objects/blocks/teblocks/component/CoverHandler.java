package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.TileEntityBlock;
import ic2.core.block.state.UnlistedProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverProvider;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.nbt.INBTSerializer;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.nbt.NBTPersistent.Include;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.HashMap;
import java.util.Map;

public class CoverHandler extends GtComponentBase {
    public static final IUnlistedProperty<CoverHandler> COVER_HANDLER_PROPERTY = new UnlistedProperty<>("coverhandler", CoverHandler.class);
    
    @NBTPersistent(include = Include.NOT_EMPTY, using = CoverMapNBTSerializer.class)
    public final Map<EnumFacing, ICover> covers = new HashMap<>();
    private final Runnable changeHandler;

    public <T extends TileEntityBlock & ICoverable> CoverHandler(T te, Runnable changeHandler) {
        super(te);
        this.changeHandler = changeHandler;
    }
    
    static {
        NBTSaveHandler.addSpecialSerializer(Map.class, CoverMapNBTSerializer::new);
    }

    public boolean placeCoverAtSide(ICover cover, EnumFacing side, boolean simulate) {
        if (!this.covers.containsKey(side)) {
            ResourceLocation icon = cover.getIcon();
            if (icon != null) {
                if (!simulate) {
                    this.covers.put(side, cover);
                    this.parent.markDirty();
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
                this.parent.markDirty();
                this.changeHandler.run();
            }
            return true;
        }
        return false;
    }

    private static class CoverMapNBTSerializer implements INBTSerializer<Map<EnumFacing, ICover>, NBTTagCompound> {
        @Override
        public NBTTagCompound serialize(Map<EnumFacing, ICover> map) {
            NBTTagCompound nbt = new NBTTagCompound();
            map.forEach((facing, cover) -> {
                NBTTagCompound tag = new NBTTagCompound();

                tag.setString("name", cover.getName().toString());
                tag.setTag("item", cover.getItem().writeToNBT(new NBTTagCompound()));
                tag.setTag("cover", cover.writeToNBT(new NBTTagCompound()));

                nbt.setTag(facing.name(), tag);
            });
            return nbt;
        }

        @Override
        public Map<EnumFacing, ICover> deserialize(NBTTagCompound nbt, Object instance, Class<?> cls) {
            Map<EnumFacing, ICover> map = new HashMap<>();
            for (String str : nbt.getKeySet()) {
                NBTTagCompound tag = nbt.getCompoundTag(str);
                EnumFacing facing = EnumFacing.valueOf(str);

                ResourceLocation name = new ResourceLocation(tag.getString("name"));
                ItemStack stack = new ItemStack(tag.getCompoundTag("item"));
                ICoverProvider provider = GregTechAPI.COVERS.getValue(name);
                if (provider != null) {
                    ICover cover = provider.constructCover(facing, (ICoverable) ((CoverHandler) instance).parent, stack);
                    cover.readFromNBT(tag.getCompoundTag("cover"));
                    map.put(facing, cover);
                } else {
                    GregTechMod.LOGGER.error("CoverProvider for {} not found", name);
                }
            }
            return map;
        }
    }
}
