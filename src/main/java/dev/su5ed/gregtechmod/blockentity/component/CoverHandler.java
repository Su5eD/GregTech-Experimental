package dev.su5ed.gregtechmod.blockentity.component;

import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.api.GregTechAPI;
import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.api.cover.ICoverProvider;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.blockentity.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.nbt.NBTHandler;
import dev.su5ed.gregtechmod.util.nbt.NBTHandlerRegistry;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent.Include;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent.Mode;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelProperty;
import one.util.streamex.StreamEx;

import java.util.*;

public class CoverHandler<T extends BaseBlockEntity & ICoverable> extends GtComponentBase<T> {
    public static final ModelProperty<CoverHandler<?>> COVER_HANDLER_PROPERTY = new ModelProperty<>();

    @NBTPersistent(mode = Mode.BOTH, include = Include.NOT_EMPTY, handler = CoverMapNBTSerializer.class)
    private Map<Direction, ICover> covers = new HashMap<>();
    private final Runnable changeHandler;

    public CoverHandler(T te, Runnable changeHandler) {
        super(te);
        this.changeHandler = changeHandler;
    }

    static {
        NBTSaveHandler.initClass(CoverHandler.class); // TODO Better way to register classes
        NBTHandlerRegistry.addSpecialHandler(CoverMapNBTSerializer.INSTANCE);
    }

    public Collection<ICover> getCovers() {
        return this.covers.values();
    }

    public Optional<ICover> getCoverAtSide(Direction side) {
        return Optional.ofNullable(this.covers.get(side));
    }

    public boolean placeCoverAtSide(ICover cover, Direction side, boolean simulate) {
        if (!this.covers.containsKey(side)) {
            if (isServerSide() && !simulate) {
                this.covers.put(side, cover);
                this.parent.setChanged();
                this.changeHandler.run();
            }
            return true;
        }
        return false;
    }

    public boolean removeCover(Direction side, boolean simulate) {
        return getCoverAtSide(side)
            .map(cover -> {
                if (isServerSide() && !simulate) {
                    cover.onCoverRemove();
                    this.covers.remove(side);
                    this.parent.setChanged();
                    this.changeHandler.run();
                }
                return true;
            })
            .orElse(false);
    }

    private static class CoverMapNBTSerializer implements NBTHandler<Map<Direction, ICover>, CompoundTag, CoverHandler<?>> {
        public static final CoverMapNBTSerializer INSTANCE = new CoverMapNBTSerializer();

        @Override
        public CompoundTag serialize(Map<Direction, ICover> value) {
            CompoundTag tag = new CompoundTag();
            value.forEach((facing, cover) -> {
                CompoundTag coverTag = new CompoundTag();

                coverTag.putString("name", cover.getName().toString());
                coverTag.put("item", cover.getItem().save(new CompoundTag()));
                coverTag.put("cover", cover.save());

                tag.put(facing.name(), coverTag);
            });
            return tag;
        }

        @Override
        public Map<Direction, ICover> deserialize(CompoundTag tag, CoverHandler<?> instance, Class<?> cls) {
            return StreamEx.of(tag.getAllKeys())
                .mapToEntry(Direction::valueOf, tag::getCompound)
                .mapToValuePartial((facing, coverTag) -> {
                    ResourceLocation name = new ResourceLocation(coverTag.getString("name"));
                    ItemStack stack = ItemStack.of(coverTag.getCompound("item"));
                    ICoverProvider provider = GregTechAPI.coverRegistry.getValue(name);

                    if (provider != null) {
                        ICover cover = provider.constructCover(facing, instance.parent, stack);
                        cover.load(coverTag.getCompound("cover"));

                        return Optional.of(cover);
                    }
                    else {
                        GregTechMod.LOGGER.error("CoverProvider for {} not found", name);
                        return Optional.empty();
                    }
                })
                .toImmutableMap();
        }
    }
}
