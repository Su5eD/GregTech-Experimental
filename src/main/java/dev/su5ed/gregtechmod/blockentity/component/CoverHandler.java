package dev.su5ed.gregtechmod.blockentity.component;

import com.google.common.collect.ImmutableMap;
import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Cover;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.api.util.NBTTarget;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.object.ModObjects;
import dev.su5ed.gregtechmod.util.nbt.NBTHandler;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class CoverHandler<T extends BaseBlockEntity & Coverable> extends GtComponentBase<T> {
    public static final ModelProperty<Map<Direction, Cover>> COVER_HANDLER_PROPERTY = new ModelProperty<>();
    private static final ResourceLocation NAME = location("cover_handler");
    
    @NBTPersistent(target = NBTTarget.BOTH, handler = CoverMapNBTSerializer.class)
    private Map<Direction, Cover> covers = new HashMap<>();
    
    public CoverHandler(T te) {
        super(te);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    public Map<Direction, Cover> getCovers() {
        return ImmutableMap.copyOf(this.covers);
    }

    public Optional<Cover> getCoverAtSide(Direction side) {
        return Optional.ofNullable(this.covers.get(side));
    }

    public boolean placeCoverAtSide(Cover cover, Direction side, boolean simulate) {
        if (!this.covers.containsKey(side)) {
            if (isServerSide() && !simulate) {
                this.covers.put(side, cover);
                this.parent.setChanged();
                updateClient();
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
                    updateClient();
                }
                return true;
            })
            .orElse(false);
    }

    @Override
    public void onFieldUpdate(String name) {
        if (name.equals("covers")) this.parent.updateRenderClient();
    }

    public static class CoverMapNBTSerializer implements NBTHandler<Map<Direction, Cover>, CompoundTag, CoverHandler<?>> {
        public static final CoverMapNBTSerializer INSTANCE = new CoverMapNBTSerializer();

        @Override
        public CompoundTag serialize(Map<Direction, Cover> value, NBTTarget target) {
            CompoundTag tag = new CompoundTag();
            value.forEach((facing, cover) -> {
                CompoundTag coverTag = new CompoundTag();

                coverTag.putString("name", cover.getType().getRegistryName().toString());
                coverTag.putString("item", cover.getItem().getRegistryName().toString());
                coverTag.put("cover", cover.save(target));

                tag.put(facing.name(), coverTag);
            });
            return tag;
        }

        @Override
        public Map<Direction, Cover> deserialize(CompoundTag tag, CoverHandler<?> instance, Class<?> cls) {
            return StreamEx.of(tag.getAllKeys())
                .mapToEntry(Direction::valueOf, tag::getCompound)
                .mapToValuePartial((facing, coverTag) -> {
                    ResourceLocation name = new ResourceLocation(coverTag.getString("name"));
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(coverTag.getString("item")));
                    CoverType type = ModObjects.coverRegistry.get().getValue(name);

                    if (type != null) {
                        Cover cover = type.create(instance.parent, facing, item);
                        cover.load(coverTag.getCompound("cover"), false);

                        return Optional.of(cover);
                    }
                    else {
                        GregTechMod.LOGGER.error("CoverProvider for {} not found", name);
                        return Optional.empty();
                    }
                })
                .toMap();
        }
    }
}
