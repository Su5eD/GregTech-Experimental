package dev.su5ed.gtexperimental.block;

import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.VerticalRotation;
import ic2.api.tile.IWrenchable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class BaseEntityBlock extends Block implements EntityBlock, IWrenchable {
    private static final ResourceLocation CONTENT_KEY = location("content");
    private static final LootContext.DynamicDrop CONTENT_DROP = (context, stackConsumer) -> {
        BlockEntity be = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (be instanceof BaseBlockEntity base) {
            List<ItemStack> drops = new ArrayList<>();
            base.provideAdditionalDrops(drops);
            drops.forEach(stackConsumer);
        }
    };
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private final BlockEntityProvider provider;
    @Nullable
    private final Supplier<Component> description;

    public BaseEntityBlock(BlockEntityProvider provider) {
        this(Properties.of(Material.METAL)
            .strength(-1, 0), provider);
    }

    public BaseEntityBlock(Properties properties, BlockEntityProvider provider) {
        super(properties);

        this.provider = provider;
        this.description = Lazy.of(() -> {
            GtLocale.TranslationKey key = GtLocale.key("block", ForgeRegistries.BLOCKS.getKey(this).getPath(), "description");
            if (FMLLoader.getDist() == Dist.CLIENT && I18n.exists(key.key())) {
                return key.toComponent().withStyle(ChatFormatting.GRAY);
            }
            return null;
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder
            .add(BlockStateProperties.FACING)
            .add(VerticalRotation.ROTATION_PROPERTY)
            .add(ACTIVE);
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

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getBlockEntity(level, pos)
            .map(be -> be.getSignal(state, level, pos, direction))
            .orElseGet(() -> super.getSignal(state, level, pos, direction));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
            .setValue(BlockStateProperties.FACING, this.provider.getAllowedFacings().getFacing(context))
            .setValue(VerticalRotation.ROTATION_PROPERTY, getVerticalRotation())
            .setValue(ACTIVE, false);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        getBlockEntity(level, pos)
            .ifPresent(be -> be.setPlacedBy(level, pos, state, placer, stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        Component desc = this.description.get();
        if (desc != null) {
            tooltip.add(desc);
        }

        this.provider.getDummyInstance().appendHoverText(stack, level, tooltip, flag);
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
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return super.getDrops(state, builder.withDynamicDrop(CONTENT_KEY, CONTENT_DROP));
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getBlockEntity(level, pos)
            .map(be -> be.isFlammable(state, level, pos, direction))
            .orElseGet(() -> super.isFlammable(state, level, pos, direction));
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return super.getFlammability(state, level, pos, direction);
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
