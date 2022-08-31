package dev.su5ed.gregtechmod.blockentity.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.su5ed.gregtechmod.api.cover.Cover;
import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.network.NetworkHandler;
import dev.su5ed.gregtechmod.network.Networked;
import dev.su5ed.gregtechmod.object.ModCovers;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class CoverHandlerImpl<T extends BaseBlockEntity> extends GtComponentBase<T> implements CoverHandler {
    public static final ModelProperty<Map<Direction, Cover<?>>> COVER_HANDLER_PROPERTY = new ModelProperty<>();
    private static final ResourceLocation NAME = location("cover_handler");

    private final Codec<Map<Direction, Cover<?>>> coversCodec = createCoversCodec();
    private final Collection<CoverCategory> coverBlacklist;
    
    @Networked
    private Map<Direction, Cover<?>> covers = new HashMap<>();

    static {
        NetworkHandler.registerHandler(CoverHandlerImpl.class, "covers", CoverHandlerImpl::getCoversCodec);
    }

    public CoverHandlerImpl(T te, Collection<CoverCategory> coverBlacklist) {
        super(te);
        
        this.coverBlacklist = ImmutableList.copyOf(coverBlacklist);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public Map<Direction, Cover<?>> getCovers() {
        return ImmutableMap.copyOf(this.covers);
    }

    @Override
    public Optional<Cover<?>> getCoverAtSide(Direction side) {
        return Optional.ofNullable(this.covers.get(side));
    }

    @Override
    public <U> boolean placeCoverAtSide(CoverType<U> type, Direction side, Item item, boolean simulate) {
        if (!this.covers.containsKey(side) && !this.coverBlacklist.contains(type.getCategory()) && type.getCoverableClass().isInstance(this.parent)) {
            if (isServerSide() && !simulate) {
                //noinspection unchecked
                Cover<?> cover = type.create((U) this.parent, side, item);
                this.covers.put(side, cover);
                this.parent.setChanged();
                updateClient();
            }
            return true;
        }
        return false;
    }

    @Override    
    public Optional<Cover<?>> removeCover(Direction side, boolean simulate) {
        return getCoverAtSide(side)
            .map(cover -> {
                if (isServerSide() && !simulate) {
                    cover.onCoverRemove();
                    this.covers.remove(side);
                    this.parent.setChanged();
                    updateClient();
                }
                return cover;
            });
    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);
        tag.put("covers", this.covers, this.coversCodec);
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);
        this.covers = tag.get("covers", this.coversCodec);
    }

    @Override
    public void onFieldUpdate(String name) {
        if (name.equals("covers")) GtUtil.updateRender(this.parent);
    }

    private Codec<Map<Direction, Cover<?>>> getCoversCodec() {
        return coversCodec;
    }

    private Codec<Map<Direction, Cover<?>>> createCoversCodec() {
        Codec<Cover<?>> coverCodec = RecordCodecBuilder.create(instance -> instance.group(
            ModCovers.REGISTRY.get().getCodec().fieldOf("type").forGetter(Cover::getType),
            Direction.CODEC.fieldOf("side").forGetter(Cover::getSide),
            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(Cover::getItem),
            FriendlyCompoundTag.CODEC.fieldOf("tag").forGetter(Cover::save)
        ).apply(instance, (type, side, item, tag) -> {
            //noinspection unchecked
            Cover<T> cover = ((CoverType<T>) type).create(this.parent, side, item);
            cover.load(tag);
            return cover;
        }));
        return Codec.simpleMap(Direction.CODEC, coverCodec, StringRepresentable.keys(Direction.values()))
            .codec()
            .xmap(HashMap::new, Function.identity());
    }
}
