package dev.su5ed.gregtechmod.model;

import dev.su5ed.gregtechmod.block.OreBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class OreModel extends BaseModel {
    private static final Material MATERIAL_STONE = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("block/stone"));
    private static final Material MATERIAL_NETHERRACK = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("block/netherrack"));
    private static final Material MATERIAL_END_STONE = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("block/end_stone"));

    private final Map<Direction, Material> textures;
    private final Map<Direction, Material> texturesNether;
    private final Map<Direction, Material> texturesEnd;
    private final ResourceLocation modelLocation;

    private final Map<Material, TextureAtlasSprite> sprites;

    public OreModel(TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms, Map<Direction, Material> textures, Map<Direction, Material> texturesNether, Map<Direction, Material> texturesEnd, Map<Material, TextureAtlasSprite> sprites, ResourceLocation modelLocation) {
        super(particle, overrides, transforms);

        this.textures = textures;
        this.texturesNether = texturesNether;
        this.texturesEnd = texturesEnd;
        this.modelLocation = modelLocation;
        this.sprites = sprites;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData tileData) {
        return state.getBlock() instanceof OreBlock block
            ? ModelData.builder()
                .with(OreBlock.ORE_MODEL_KEY, block.getModelKey(world, pos))
                .build()
            : tileData;
    }

    @Override // TODO Change particle depending on dimension
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
        if (side != null) {
            OreModelKey key = extraData.get(OreBlock.ORE_MODEL_KEY);
            Pair<Material, TextureAtlasSprite> texture = getOreTexture(side, key);
            BlockElementFace face = new BlockElementFace(side, 0, texture.getLeft().texture().toString(), FACE_UV);
            return bakeSingleQuad(face, texture.getRight(), side, this.modelLocation);
        }
        return List.of();
    }

    public Pair<Material, TextureAtlasSprite> getOreTexture(Direction side, @Nullable OreModelKey key) {
        Material texture;
        if (key == null) {
            texture = getMaterialWithFallback(side);
        }
        else if (key.texture() != OreModelKey.Texture.DEFAULT) {
            Direction override = key.sideOverrides().get(side);
            if (override != null) {
                texture = switch (key.texture()) {
                    case STONE -> getMaterialWithFallback(override);
                    case NETHERRACK -> getMaterialWithFallback(override, this.texturesNether, this.textures, this.texturesEnd);
                    default -> getMaterialWithFallback(override, this.texturesEnd, this.textures, this.texturesNether);
                };
            }
            else return switch (key.texture()) {
                case STONE -> Pair.of(MATERIAL_STONE, MATERIAL_STONE.sprite());
                case NETHERRACK -> Pair.of(MATERIAL_NETHERRACK, MATERIAL_NETHERRACK.sprite());
                default -> Pair.of(MATERIAL_END_STONE, MATERIAL_END_STONE.sprite());
            };
        }
        else {
            texture = this.textures.get(null);
        }
        
        return Pair.of(texture, this.sprites.get(texture));
    }
    
    private Material getMaterialWithFallback(Direction side) {
        return getMaterialWithFallback(side, this.textures, this.texturesNether, this.texturesEnd);
    }

    @SafeVarargs
    private Material getMaterialWithFallback(Direction side, Map<Direction, Material>... textures) {
        return StreamEx.of(textures)
            .map(map -> map.get(side))
            .nonNull()
            .findFirst()
            .orElse(null);
    }
}
