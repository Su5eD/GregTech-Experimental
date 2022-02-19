package dev.su5ed.gregtechmod.model;

import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.IDynamicBakedModel;

import java.util.List;

public abstract class BaseModel implements IDynamicBakedModel {
    protected static final FaceBakery BAKERY = new FaceBakery();
    public static final BlockFaceUV FACE_UV = new BlockFaceUV(new float[] { 0, 0, 16, 16 }, 0);
    public static final Vector3f MAX = new Vector3f(16, 16, 16);
    public static final Vector3f MAX_DOWN = new Vector3f(16, 0, 16);

    private final TextureAtlasSprite particle;
    private final ItemOverrides overrides;
    private final ItemTransforms transforms;

    protected BaseModel(TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms) {
        this.particle = particle;
        this.overrides = overrides;
        this.transforms = transforms;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.particle;
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrides;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemTransforms getTransforms() {
        return this.transforms;
    }
    
    protected static List<BakedQuad> bakeSingleQuad(BlockElementFace face, TextureAtlasSprite sprite, Direction side, ResourceLocation pModelLocation) {
        BakedQuad quad = BAKERY.bakeQuad(Vector3f.ZERO, side == Direction.DOWN ? MAX_DOWN : MAX, face, sprite, side, BlockModelRotation.X0_Y0, null, true, pModelLocation);
        return List.of(quad);
    }
}
