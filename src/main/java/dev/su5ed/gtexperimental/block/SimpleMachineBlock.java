package dev.su5ed.gtexperimental.block;

import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.OutputSide;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class SimpleMachineBlock extends BaseEntityBlock {
    public static final EnumProperty<OutputSide> OUTPUT_SIDE = EnumProperty.create("output_side", OutputSide.class);
    
    public SimpleMachineBlock(BlockEntityProvider provider) {
        super(provider);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OUTPUT_SIDE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        Direction facing = state.getValue(BlockStateProperties.FACING).getOpposite();
        return state.setValue(OUTPUT_SIDE, OutputSide.fromDirection(facing));
    }
}
