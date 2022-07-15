package dev.su5ed.gregtechmod.blockentity.component;

import com.google.common.collect.ImmutableMap;
import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.api.GregTechAPI;
import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.api.cover.ICoverProvider;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.util.NBTTarget;
import dev.su5ed.gregtechmod.blockentity.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.nbt.NBTHandler;
import dev.su5ed.gregtechmod.util.nbt.NBTHandlerRegistry;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
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

public class CoverHandler<T extends BaseBlockEntity & ICoverable> extends GtComponentBase<T> {
    public static final ModelProperty<Map<Direction, ICover>> COVER_HANDLER_PROPERTY = new ModelProperty<>();
    private static final ResourceLocation NAME = location("cover_handler");

    @NBTPersistent(target = NBTTarget.BOTH, handler = CoverMapNBTSerializer.class)
    private Map<Direction, ICover> covers = new HashMap<>();

    public CoverHandler(T te) {
        super(te);
    }

    static {
        NBTSaveHandler.initClass(CoverHandler.class); // TODO Better way to register classes
        NBTHandlerRegistry.addSpecialHandler(CoverMapNBTSerializer.INSTANCE);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    public Map<Direction, ICover> getCovers() {
        return ImmutableMap.copyOf(this.covers);
    }

    public Optional<ICover> getCoverAtSide(Direction side) {
        return Optional.ofNullable(this.covers.get(side));
    }

    public boolean placeCoverAtSide(ICover cover, Direction side, boolean simulate) {
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

    private static class CoverMapNBTSerializer implements NBTHandler<Map<Direction, ICover>, CompoundTag, CoverHandler<?>> {
        public static final CoverMapNBTSerializer INSTANCE = new CoverMapNBTSerializer();

        @Override
        public CompoundTag serialize(Map<Direction, ICover> value, NBTTarget target) {
            CompoundTag tag = new CompoundTag();
            value.forEach((facing, cover) -> {
                CompoundTag coverTag = new CompoundTag();

                coverTag.putString("name", cover.getName().toString());
                coverTag.putString("item", cover.getItem().getRegistryName().toString());
                coverTag.put("cover", cover.save(target));

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
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(coverTag.getString("item")));
                    ICoverProvider provider = GregTechAPI.coverRegistry.get().getValue(name);

                    if (provider != null) {
                        ICover cover = provider.constructCover(facing, instance.parent, item);
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
