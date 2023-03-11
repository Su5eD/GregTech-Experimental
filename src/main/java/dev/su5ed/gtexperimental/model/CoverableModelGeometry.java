package dev.su5ed.gtexperimental.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BlockModel;
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
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class CoverableModelGeometry implements IUnbakedGeometry<CoverableModelGeometry> {
    private final BlockModel blockModel;

    public CoverableModelGeometry(BlockModel blockModel) {
        this.blockModel = blockModel;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite particleSprite = spriteGetter.apply(this.blockModel.getMaterial("particle"));
        //noinspection deprecation
        Map<Direction, Material> materials = StreamEx.of(this.blockModel.getElements())
            .flatMapToEntry(element -> element.faces)
            .mapValues(face -> this.blockModel.getMaterial(face.texture))
            .toMap();
        Map<Material, TextureAtlasSprite> sprites = StreamEx.ofValues(materials)
            .mapToEntry(spriteGetter)
            .distinctKeys()
            .toImmutableMap();
        return new CoverableModel(particleSprite, materials, sprites, overrides, owner.getTransforms(), modelLocation);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return this.blockModel.getMaterials(modelGetter, missingTextureErrors);
    }
}
