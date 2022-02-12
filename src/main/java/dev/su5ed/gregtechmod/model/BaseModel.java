package dev.su5ed.gregtechmod.model;

import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.data.IDynamicBakedModel;

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
}
