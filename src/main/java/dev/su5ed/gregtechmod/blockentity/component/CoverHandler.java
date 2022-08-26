package dev.su5ed.gregtechmod.blockentity.component;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.su5ed.gregtechmod.api.cover.Cover;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.network.NetworkHandler;
import dev.su5ed.gregtechmod.network.Networked;
import dev.su5ed.gregtechmod.object.ModObjects;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class CoverHandler<T extends BaseBlockEntity & Coverable> extends GtComponentBase<T> {
    public static final ModelProperty<Map<Direction, Cover>> COVER_HANDLER_PROPERTY = new ModelProperty<>();
    private static final ResourceLocation NAME = location("cover_handler");
    
    private final Codec<Map<Direction, Cover>> coversCodec = createCoversCodec();
    
    @Networked
    private Map<Direction, Cover> covers = new HashMap<>();
    
    static {
        NetworkHandler.registerHandler(CoverHandler.class, "covers", CoverHandler::getCoversCodec);
    }
    
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
        if (name.equals("covers")) this.parent.updateRenderClient();
    }

    private Codec<Map<Direction, Cover>> getCoversCodec() {
        return coversCodec;
    }

    private Codec<Map<Direction, Cover>> createCoversCodec() {
        Codec<Cover> coverCodec = RecordCodecBuilder.create(instance -> instance.group(
            ModObjects.coverRegistry.get().getCodec().fieldOf("type").forGetter(Cover::getType),
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
