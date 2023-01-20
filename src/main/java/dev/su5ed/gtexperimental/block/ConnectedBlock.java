package dev.su5ed.gtexperimental.block;

import dev.su5ed.gtexperimental.GregTechConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.client.model.data.ModelProperty;
import one.util.streamex.StreamEx;

import java.util.EnumSet;
import java.util.Set;

public class ConnectedBlock extends ResourceBlock {
    public static final ModelProperty<Set<Direction>> DIRECTIONS = new ModelProperty<>();

    public ConnectedBlock(float strength, float resistance) {
        super(strength, resistance);
    }
    
    public Set<Direction> getConnections(BlockGetter world, BlockPos pos) {
        return GregTechConfig.CLIENT.connectedTextures.get() ? StreamEx.of(Direction.values())
            .filter(side -> world.getBlockState(pos.relative(side)).getBlock() == this)
            .toCollection(() -> EnumSet.noneOf(Direction.class))
            : Set.of();
    }
}
