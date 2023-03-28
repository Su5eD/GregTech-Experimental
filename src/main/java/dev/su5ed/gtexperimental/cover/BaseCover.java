package dev.su5ed.gtexperimental.cover;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.cover.Cover;
import dev.su5ed.gtexperimental.api.cover.CoverHandler;
import dev.su5ed.gtexperimental.api.cover.CoverInteractionResult;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.api.machine.MachineController;
import dev.su5ed.gtexperimental.api.machine.MachineProgress;
import dev.su5ed.gtexperimental.api.machine.PowerHandler;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class BaseCover implements Cover {
    private final CoverType type;
    protected final BlockEntity be;
    protected final Direction side;
    protected final Item item;

    protected final CoverHandler coverHandler;
    @Nullable
    protected final MachineController machineController;
    @Nullable
    protected final MachineProgress machineProgress;
    @Nullable
    protected final PowerHandler energyHandler;

    protected BaseCover(CoverType type, BlockEntity be, Direction side, Item item) {
        this.type = type;
        this.be = be;
        this.side = side;
        this.item = item;

        this.coverHandler = GtUtil.getRequiredCapability(be, Capabilities.COVER_HANDLER);
        this.machineController = be.getCapability(Capabilities.MACHINE_CONTROLLER).orElse(null);
        this.machineProgress = be.getCapability(Capabilities.MACHINE_PROGRESS).orElse(null);
        this.energyHandler = be.getCapability(Capabilities.ENERGY_HANDLER).orElse(null);
    }

    @Override
    public CoverType getType() {
        return this.type;
    }

    @Override
    public Direction getSide() {
        return this.side;
    }

    @Nullable
    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public void tick() {}

    @Override
    public boolean use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    @Override
    public final CoverInteractionResult onScrewdriverClick(Player player) {
        return !player.level.isClientSide && player instanceof ServerPlayer sp ? onServerScrewdriverClick(sp) : onClientScrewdriverClick(player);
    }

    protected CoverInteractionResult onClientScrewdriverClick(Player player) {
        return CoverInteractionResult.PASS;
    }

    protected CoverInteractionResult onServerScrewdriverClick(ServerPlayer player) {
        return CoverInteractionResult.PASS;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return false;
    }

    @Override
    public boolean letsRedstoneIn() {
        return false;
    }

    @Override
    public boolean letsRedstoneOut() {
        return false;
    }

    @Override
    public boolean letsLiquidsIn() {
        return false;
    }

    @Override
    public boolean letsLiquidsOut() {
        return false;
    }

    @Override
    public boolean letsItemsIn() {
        return false;
    }

    @Override
    public boolean letsItemsOut() {
        return false;
    }

    @Override
    public boolean opensGui(Direction side) {
        return true;
    }

    @Override
    public boolean acceptsRedstone() {
        return false;
    }

    @Override
    public boolean overrideRedstoneOut() {
        return false;
    }

    @Override
    public int getRedstoneInput() {
        return 0;
    }

    @Override
    public void save(FriendlyCompoundTag tag) {

    }

    @Override
    public void load(FriendlyCompoundTag tag) {

    }

    @Override
    public boolean shouldTick() {
        return true;
    }

    @Override
    public int getTickRate() {
        return 0;
    }

    @Override
    public void onCoverRemove() {}
}
