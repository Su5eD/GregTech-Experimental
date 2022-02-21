package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.api.cover.ICoverProvider;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CoverItem extends ResourceItem {
    private final ICoverProvider coverProvider;

    public CoverItem(Properties pProperties, MutableComponent description, ICoverProvider coverProvider) {
        super(pProperties, description);

        this.coverProvider = coverProvider;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        if (!player.isShiftKeyDown()) {
            BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());

            if (be instanceof ICoverable coverable) {
                Direction side = context.getClickedFace();
                ICover cover = this.coverProvider.constructCover(side, coverable, this);

                if (coverable.placeCoverAtSide(cover, player, side, false)) {
                    if (!player.isCreative()) stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}
