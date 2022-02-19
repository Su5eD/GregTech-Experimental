package dev.su5ed.gregtechmod.block;

import dev.su5ed.gregtechmod.blockentity.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.VerticalRotation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.List;

public class BaseEntityBlock extends Block implements EntityBlock {
    private final BlockEntityProvider provider;
    
    public BaseEntityBlock(BlockEntityProvider provider) {
        this(Properties.of(Material.METAL)
            .strength(-1, 0), provider);
    }

    public BaseEntityBlock(Properties properties, BlockEntityProvider provider) {
        super(properties);
        
        this.provider = provider;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder
            .add(BlockStateProperties.FACING)
            .add(VerticalRotation.ROTATION_PROPERTY);
    }
    
    protected VerticalRotation getVerticalRotation() {
        return VerticalRotation.MIRROR_BACK;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
            .setValue(BlockStateProperties.FACING, this.provider.getAllowedFacings().getFacing(context))
            .setValue(VerticalRotation.ROTATION_PROPERTY, getVerticalRotation());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(GtLocale.key("block", getRegistryName().getPath(), "description").toComponent().withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.provider.getType().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() ? (lvl, pos, blockState, be) -> {
            if (be instanceof BaseBlockEntity blockEntity) blockEntity.tickServer();
        } : null;
    }
}
