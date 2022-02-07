package dev.su5ed.gregtechmod.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import one.util.streamex.EntryStream;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ConnectedModelGeometry implements IModelGeometry<ConnectedModelGeometry> {
    private final Material particle;
    private final Map<String, Material> materials;

    public ConnectedModelGeometry(ResourceLocation particle, Map<String, ResourceLocation> textures) {
        this.particle = getMaterial(particle);
        this.materials = EntryStream.of(textures)
            .mapValues(ConnectedModelGeometry::getMaterial)
            .toImmutableMap();
    }
    
    private static Material getMaterial(ResourceLocation location) {
        return new Material(InventoryMenu.BLOCK_ATLAS, location);
    }

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite particle = spriteGetter.apply(this.particle);
        return new ConnectedModel(particle, this.materials, spriteGetter, overrides, owner.getCameraTransforms(), modelLocation);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return this.materials.values();
    }
}
