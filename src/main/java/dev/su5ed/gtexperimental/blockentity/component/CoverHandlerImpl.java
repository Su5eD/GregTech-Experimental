package dev.su5ed.gtexperimental.blockentity.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.cover.Cover;
import dev.su5ed.gtexperimental.api.cover.CoverCategory;
import dev.su5ed.gtexperimental.api.cover.CoverHandler;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.network.NetworkHandler;
import dev.su5ed.gtexperimental.network.Networked;
import dev.su5ed.gtexperimental.object.ModCovers;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class CoverHandlerImpl<T extends BaseBlockEntity> extends GtComponentBase<T> implements CoverHandler {
    public static final ModelProperty<Map<Direction, Cover>> COVER_HANDLER_PROPERTY = new ModelProperty<>();
    private static final ResourceLocation NAME = location("cover_handler");

    private final Codec<Map<Direction, Cover>> coversCodec = createCoversCodec();
    private final Collection<CoverCategory> coverBlacklist;
    private final Predicate<Direction> sideFilter;
    private final LazyOptional<CoverHandler> optional = LazyOptional.of(() -> this);
    
    @Networked
    private Map<Direction, Cover> covers = new HashMap<>();

    static {
        NetworkHandler.registerHandler(CoverHandlerImpl.class, "covers", CoverHandlerImpl::getCoversCodec);
    }

    public CoverHandlerImpl(T te, Collection<CoverCategory> coverBlacklist, Predicate<Direction> sideFilter) {
        super(te);
        
        this.coverBlacklist = ImmutableList.copyOf(coverBlacklist);
        this.sideFilter = sideFilter;
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if (cap == Capabilities.COVER_HANDLER) {
            return this.optional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optional.invalidate();
    }

    @Override
    public Map<Direction, Cover> getCovers() {
        return ImmutableMap.copyOf(this.covers);
    }

    @Override
    public Optional<Cover> getCoverAtSide(Direction side) {
        return Optional.ofNullable(this.covers.get(side));
    }

    @Override
    public boolean placeCoverAtSide(CoverType type, Direction side, Item item, boolean simulate) {
        if (!this.covers.containsKey(side) && this.sideFilter.test(side) && !this.coverBlacklist.contains(type.getCategory()) && type.getCondition().test(this.parent)) {
            if (isServerSide() && !simulate) {
                Cover cover = type.create(this.parent, side, item);
                this.covers.put(side, cover);
                this.parent.setChanged();
                updateClient();
            }
            return true;
        }
        return false;
    }

    @Override    
    public Optional<Cover> removeCover(Direction side, boolean simulate) {
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
    public void tickServer() {
        super.tickServer();

        for (Cover cover : this.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && this.parent.getTicks() % tickRate == 0) {
                cover.tick();
            }
        }
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
        if (name.equals("covers")) {
            this.parent.updateRender();
        }
    }

    private Codec<Map<Direction, Cover>> getCoversCodec() {
        return coversCodec;
    }

    private Codec<Map<Direction, Cover>> createCoversCodec() {
        Codec<Cover> coverCodec = RecordCodecBuilder.create(instance -> instance.group(
            ModCovers.REGISTRY.get().getCodec().fieldOf("type").forGetter(Cover::getType),
            Direction.CODEC.fieldOf("side").forGetter(Cover::getSide),
            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(Cover::getItem),
            FriendlyCompoundTag.CODEC.fieldOf("tag").forGetter(Cover::save)
        ).apply(instance, (type, side, item, tag) -> {
            Cover cover = type.create(this.parent, side, item);
            cover.load(tag);
            return cover;
        }));
        return Codec.simpleMap(Direction.CODEC, coverCodec, StringRepresentable.keys(Direction.values()))
            .codec()
            .xmap(HashMap::new, Function.identity());
    }
}
