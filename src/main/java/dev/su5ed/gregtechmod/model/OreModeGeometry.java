package dev.su5ed.gregtechmod.model;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class OreModeGeometry implements IModelGeometry<OreModeGeometry> {
    private final Material particle;
    private final Map<Direction, Material> textures;
    private final Map<Direction, Material> texturesNether;
    private final Map<Direction, Material> texturesEnd;

    public OreModeGeometry(ResourceLocation particle, Map<Direction, ResourceLocation> textures, Map<Direction, ResourceLocation> texturesNether, Map<Direction, ResourceLocation> texturesEnd) {
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
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite particleSprite = spriteGetter.apply(this.particle);
        Map<Material, TextureAtlasSprite> sprites = StreamEx.of(getTextures())
            .mapToEntry(spriteGetter)
            .distinctKeys()
            .toImmutableMap();
        return new OreModel(particleSprite, overrides, owner.getCameraTransforms(), this.textures, this.texturesNether, this.texturesEnd, sprites, modelLocation);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return getTextures();
    }

    private Collection<Material> getTextures() {
        return EntryStream.of(this.textures)
            .append(this.texturesNether)
            .append(this.texturesEnd)
            .values()
            .toImmutableList();
    }
}
