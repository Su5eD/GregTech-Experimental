package dev.su5ed.gregtechmod.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CoverableModel extends BaseModel {
    private final Map<Direction, Material> textures;
    private final Map<Material, TextureAtlasSprite> sprites;
    private final ResourceLocation modelLocation;

    public CoverableModel(TextureAtlasSprite particle, Map<Direction, Material> textures, Map<Material, TextureAtlasSprite> sprites, ItemOverrides overrides, ItemTransforms transforms, ResourceLocation modelLocation) {
        super(particle, overrides, transforms);
        
        this.textures = textures;
        this.sprites = sprites;
        this.modelLocation = modelLocation;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (side != null) {
            Material material = this.textures.get(side);
            BlockElementFace face = new BlockElementFace(side.getOpposite(), 0, material.texture().toString(), FACE_UV);
            TextureAtlasSprite sprite = this.sprites.get(material);
            return bakeSingleQuad(face, sprite, side, this.modelLocation);
        }
        return List.of();
    }
}
