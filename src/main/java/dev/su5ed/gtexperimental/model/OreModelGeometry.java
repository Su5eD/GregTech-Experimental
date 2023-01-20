package dev.su5ed.gtexperimental.model;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class OreModelGeometry implements IUnbakedGeometry<OreModelGeometry> {
    private final Material particle;
    private final Map<Direction, Material> textures;
    private final Map<Direction, Material> texturesNether;
    private final Map<Direction, Material> texturesEnd;

    public OreModelGeometry(ResourceLocation particle, Map<Direction, ResourceLocation> textures, Map<Direction, ResourceLocation> texturesNether, Map<Direction, ResourceLocation> texturesEnd) {
        this.particle = GtUtil.getMaterial(particle);
        this.textures = getMaterials(textures);
        this.texturesNether = getMaterials(texturesNether);
        this.texturesEnd = getMaterials(texturesEnd);
    }

    private static Map<Direction, Material> getMaterials(Map<Direction, ResourceLocation> textures) {
        return EntryStream.of(textures)
            .mapValues(GtUtil::getMaterial)
            .toImmutableMap();
    }

    @Override
    public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite particleSprite = spriteGetter.apply(this.particle);
        Map<Material, TextureAtlasSprite> sprites = StreamEx.of(getMaterials())
            .mapToEntry(spriteGetter)
            .distinctKeys()
            .toImmutableMap();
        return new OreModel(particleSprite, overrides, owner.getTransforms(), this.textures, this.texturesNether, this.texturesEnd, sprites, modelLocation);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return getMaterials();
    }

    private Collection<Material> getMaterials() {
        return EntryStream.of(this.textures)
            .append(this.texturesNether)
            .append(this.texturesEnd)
            .values()
            .toImmutableList();
    }
}
