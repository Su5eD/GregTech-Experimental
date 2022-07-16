package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Cover;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CoverItem extends ResourceItem {
    private final CoverType coverProvider;

    public CoverItem(ExtendedItemProperties<?> properties, CoverType coverProvider) {
        super(properties);

        this.coverProvider = coverProvider;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        if (!player.isShiftKeyDown()) {
            BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());

            if (be instanceof Coverable coverable) {
                Direction side = context.getClickedFace();
                Cover cover = this.coverProvider.create(coverable, side, this);

                if (coverable.placeCoverAtSide(cover, player, side, false)) {
                    if (!player.isCreative()) stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}
