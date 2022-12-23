package dev.su5ed.gregtechmod.api.cover;

import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public interface Cover<T> { // TODO javadocs
    CoverType<T> getType();

    Direction getSide();

    @Nullable
    Item getItem();
    
    boolean shouldTick(); // TODO Do we need this?

    void tick();

    boolean use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit);

    CoverInteractionResult onScrewdriverClick(Player player);

    boolean allowEnergyTransfer();

    boolean letsRedstoneIn();

    boolean letsRedstoneOut();

    boolean letsLiquidsIn();

    boolean letsLiquidsOut();

    boolean letsItemsIn();

    boolean letsItemsOut();

    boolean opensGui(Direction side);

    boolean acceptsRedstone();

    boolean overrideRedstoneOut();

    int getRedstoneInput();

    ResourceLocation getIcon();

    default FriendlyCompoundTag save() {
        FriendlyCompoundTag tag = new FriendlyCompoundTag();
        save(tag);
        return tag;
    }

    void save(FriendlyCompoundTag tag);

    void load(FriendlyCompoundTag tag);

    int getTickRate();

    void onCoverRemove();
}
