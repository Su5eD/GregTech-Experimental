package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public class CoverItem<T> extends ResourceItem {
    private final Supplier<CoverType> type;

    public CoverItem(ExtendedItemProperties<?> properties, Supplier<CoverType> type) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        if (!player.isShiftKeyDown()) {
            BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
            if (be != null) {
                return be.getCapability(Capabilities.COVER_HANDLER)
                    .filter(handler -> handler.placeCoverAtSide(this.type.get(), context.getClickedFace(), this, false))
                    .map(handler -> {
                        if (!player.isCreative()) stack.shrink(1);
                        return InteractionResult.SUCCESS;
                    })
                    .orElse(InteractionResult.PASS);
            }
        }
        return InteractionResult.PASS;
    }
}
