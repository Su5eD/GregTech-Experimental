package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.util.GtUtil;
import ic2.api.crops.ICropTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HydrationSprayItem extends CraftingToolItem {

    public HydrationSprayItem() {
        super(new ToolItemProperties<>()
            .durability(2560)
            .autoDescription(), 20, Miscellaneous.EMPTY_SPRAY_CAN.getItem());
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return ModHandler.ic2Loaded ? IC2CropHydrationHandler.onItemUseFirst(stack, context) : super.onItemUseFirst(stack, context);
    }

    private static class IC2CropHydrationHandler {
        public static InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockEntity be = level.getBlockEntity(pos);
            
            if (be instanceof ICropTile crop) {
                int water = crop.getStorageWater();
                if (water <= 100 && GtUtil.hurtStack(stack, 1, context.getPlayer())) {
                    crop.setStorageWater(water + 100);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        }
    }
}
