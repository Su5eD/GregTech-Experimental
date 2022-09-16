package dev.su5ed.gregtechmod.util;

import dev.su5ed.gregtechmod.api.util.QuadFunction;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.IntStreamEx;

import java.util.function.Predicate;

public final class InvUtil {

    private InvUtil() {}

    public static int moveItemStack(BlockEntity from, BlockEntity to, Direction fromSide, Direction toSide, int maxTargetSize, int minTargetSize) {
        return moveItemStack(from, to, fromSide, toSide, maxTargetSize, minTargetSize, stack -> true);
    }

    public static int moveItemStack(BlockEntity from, BlockEntity to, Direction fromSide, Direction toSide, int maxTargetSize, int minTargetSize, Predicate<ItemStack> filter) {
        return moveItemStack(from, to, fromSide, toSide, dest -> true, filter, (source, dest, sourceStack, sourceSlot) -> {
            for (int j = 0; j < dest.getSlots(); j++) {
                int count = moveSingleItemStack(source, dest, sourceStack, sourceSlot, j, minTargetSize, maxTargetSize);
                if (count > 0) return count;
            }
            return 0;
        });
    }

    public static int moveItemStackIntoSlot(BlockEntity from, BlockEntity to, Direction fromSide, Direction toSide, int destSlot, int maxTargetSize, int minTargetSize) {
        return moveItemStack(from, to, fromSide, toSide, dest -> dest.getSlots() - 1 >= destSlot, stack -> true, (source, dest, sourceStack, sourceSlot) -> {
            int count = moveSingleItemStack(source, dest, sourceStack, sourceSlot, destSlot, minTargetSize, maxTargetSize);
            return Math.max(count, 0);
        });
    }

    private static int moveItemStack(BlockEntity from, BlockEntity to, Direction fromSide, Direction toSide, Predicate<IItemHandler> condition, Predicate<ItemStack> filter, QuadFunction<IItemHandler, IItemHandler, ItemStack, Integer, Integer> consumer) {
        if (from != null && to != null) {
            return from.getCapability(ForgeCapabilities.ITEM_HANDLER, fromSide).resolve()
                .flatMap(source -> to.getCapability(ForgeCapabilities.ITEM_HANDLER, toSide).resolve()
                    .filter(condition)
                    .flatMap(dest -> IntStreamEx.range(source.getSlots()).boxed()
                        .mapToEntry(i -> source.extractItem(i, source.getSlotLimit(i), true))
                        .removeValues(ItemStack::isEmpty)
                        .filterValues(filter)
                        .mapKeyValue((i, stack) -> consumer.apply(source, dest, stack, i))
                        .findFirst()
                    )
                )
                .orElse(0);
        }

        return 0;
    }

    private static int moveSingleItemStack(IItemHandler source, IItemHandler dest, ItemStack sourceStack, int sourceSlot, int destSlot, int minTargetSize, int maxTargetSize) {
        ItemStack destStack = dest.getStackInSlot(destSlot);

        ItemStack sourceStackCopy = sourceStack.copy();
        int free = destStack.getMaxStackSize() - destStack.getCount();
        sourceStackCopy.setCount(Math.min(sourceStackCopy.getCount(), Math.min(maxTargetSize - destStack.getCount(), free)));

        int totalSize = sourceStackCopy.getCount() + destStack.getCount();
        if (totalSize >= minTargetSize && (destStack.isEmpty() || ItemHandlerHelper.canItemStacksStack(sourceStackCopy, destStack))) {
            ItemStack inserted = dest.insertItem(destSlot, sourceStackCopy, false);
            int count = sourceStackCopy.getCount();
            if (inserted.isEmpty()) source.extractItem(sourceSlot, count, false);
            return count;
        }

        return 0;
    }
}
