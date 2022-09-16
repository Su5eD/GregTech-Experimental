package dev.su5ed.gregtechmod.model;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ConnectedModelGeometry implements IUnbakedGeometry<ConnectedModelGeometry> {
    private final Material particle;
    private final Map<String, Material> materials;

    public ConnectedModelGeometry(ResourceLocation particle, Map<String, ResourceLocation> textures) {
        this.particle = GtUtil.getMaterial(particle);
        this.materials = EntryStream.of(textures)
            .mapValues(GtUtil::getMaterial)
            .toImmutableMap();
    }

    @Override
    public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite particleSprite = spriteGetter.apply(this.particle);
        Map<Material, TextureAtlasSprite> sprites = StreamEx.ofValues(this.materials)
            .mapToEntry(spriteGetter)
            .distinctKeys()
            .toImmutableMap();
        return new ConnectedModel(particleSprite, this.materials, sprites, overrides, owner.getTransforms(), modelLocation);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return this.materials.values();
    }
}
