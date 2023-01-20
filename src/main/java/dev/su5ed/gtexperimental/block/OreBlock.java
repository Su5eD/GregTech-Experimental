package dev.su5ed.gtexperimental.block;

import dev.su5ed.gtexperimental.GregTechConfig;
import dev.su5ed.gtexperimental.model.OreModelKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.EnumMap;
import java.util.Map;

public class OreBlock extends Block {
    public static final ModelProperty<OreModelKey> ORE_MODEL_KEY = new ModelProperty<>();

    public OreBlock(float strength) {
        super(BlockBehaviour.Properties.of(Material.STONE)
            .strength(strength)
            .requiresCorrectToolForDrops());
    }

    public OreModelKey getModelKey(BlockGetter world, BlockPos pos) {
        OreModelKey.Texture texture = renderAsOre(world, pos);
        boolean hiddenOres = GregTechConfig.COMMON.hiddenOres.get();

        Map<Direction, Direction> overrides = new EnumMap<>(Direction.class);
        for (Direction side : Direction.values()) {
            int sideIndex = Math.abs(side.get3DDataValue() ^ pos.getX() ^ pos.getY() ^ pos.getZ());
            if (!hiddenOres || sideIndex % 12 > 6) overrides.put(side, Direction.from3DDataValue(sideIndex % 6));
        }

        return new OreModelKey(overrides, texture);
    }

    private OreModelKey.Texture renderAsOre(BlockGetter world, BlockPos pos) {
        for (Direction side : Direction.values()) {
            BlockPos neighbor = pos.relative(side);
            BlockState state = world.getBlockState(neighbor);

            if (!state.isAir()) {
                Block block = state.getBlock();
                if (block == Blocks.STONE) return OreModelKey.Texture.STONE;
                else if (block == Blocks.NETHERRACK) return OreModelKey.Texture.NETHERRACK;
                else if (block == Blocks.END_STONE) return OreModelKey.Texture.END_STONE;
            }
        }
        return OreModelKey.Texture.DEFAULT;
    }
}
