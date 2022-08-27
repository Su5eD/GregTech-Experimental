package dev.su5ed.gregtechmod.block;

import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.VerticalRotation;
import ic2.api.tile.IWrenchable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BaseEntityBlock extends Block implements EntityBlock, IWrenchable {
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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return getBlockEntity(level, pos)
            .map(be -> be.use(state, level, pos, player, hand, hit))
            .orElseGet(() -> super.use(state, level, pos, player, hand, hit));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return getBlockEntity(level, pos)
            .flatMap(be -> be.getCloneItemStack(state, target, level, pos, player))
            .orElseGet(() -> super.getCloneItemStack(state, target, level, pos, player));
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side) {
        return getBlockEntity(level, pos)
            .map(be -> be.canConnectRedstone(state, level, pos, side))
            .filter(Boolean::booleanValue)
            .orElseGet(() -> super.canConnectRedstone(state, level, pos, side));
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
        return (lvl, pos, blockState, be) -> {
            if (be instanceof BaseBlockEntity blockEntity) {
                if (lvl.isClientSide) blockEntity.tickClient();
                else blockEntity.tickServer();
            }
        };
    }

    @Override
    public Direction getFacing(Level level, BlockPos pos) {
        return getBlockEntity(level, pos)
            .map(BaseBlockEntity::getFacing)
            .orElse(Direction.NORTH);
    }

    @Override
    public boolean setFacing(Level level, BlockPos pos, Direction facing, Player player) {
        return getBlockEntity(level, pos)
            .map(be -> be.setFacing(facing))
            .orElse(false);
    }

    @Override
    public boolean wrenchCanRemove(Level level, BlockPos pos, Player player) {
        return getBlockEntity(level, pos)
            .map(BaseBlockEntity::wrenchCanRemove)
            .orElse(false);
    }

    @Override
    public List<ItemStack> getWrenchDrops(Level level, BlockPos pos, BlockState state, BlockEntity be, Player player, int i) {
        return getDrops(state, (ServerLevel) level, pos, be, player, player.getUseItem());
    }

    private Optional<BaseBlockEntity> getBlockEntity(BlockGetter world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        return be instanceof BaseBlockEntity baseBe
            ? Optional.of(baseBe)
            : Optional.empty();
    }
}
