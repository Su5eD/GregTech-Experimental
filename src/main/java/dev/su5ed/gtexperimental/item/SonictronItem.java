package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.api.item.ExhaustingItem;
import dev.su5ed.gtexperimental.blockentity.SonictronBlockEntity;
import dev.su5ed.gtexperimental.menu.SonictronMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;

public class SonictronItem extends ResourceItem implements ExhaustingItem {

    public SonictronItem() {
        super(new ExtendedItemProperties<>().stacksTo(1));
    }

    @Override
    public boolean shouldExhaust(boolean isArmor) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                setCurrentIndex(stack, 0);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            setCurrentIndex(stack, -1);
            BlockPos pos = context.getClickedPos();
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SonictronBlockEntity) {
                return be.getCapability(ForgeCapabilities.ITEM_HANDLER)
                    .map(itemHandler -> {
                        Player player = context.getPlayer();
                        int slots = itemHandler.getSlots();
                        if (player.isShiftKeyDown()) {
                            NonNullList<ItemStack> inventory = NonNullList.withSize(slots, ItemStack.EMPTY);
                            for (int i = 0; i < slots; i++) {
                                inventory.set(i, itemHandler.getStackInSlot(i));
                            }
                            setStackInventory(stack, inventory);
                        }
                        else if (itemHandler instanceof IItemHandlerModifiable modifiable) {
                            NonNullList<ItemStack> inventory = getStackInventory(stack);
                            if (inventory.size() == slots) {
                                for (int i = 0; i < slots; i++) {
                                    modifiable.setStackInSlot(i, inventory.get(i));
                                }
                            }
                        }
                        return InteractionResult.SUCCESS;
                    })
                    .orElse(InteractionResult.PASS);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) {
            int currentIndex = getCurrentIndex(stack);
            if (level.getGameTime() % 2 == 0 && currentIndex > -1) {
                NonNullList<ItemStack> inventory = getStackInventory(stack);
                int containerSize = inventory.size();
                if (!inventory.isEmpty() && currentIndex < containerSize) {
                    SonictronBlockEntity.doSonictronSound(inventory.get(currentIndex), level, entity.blockPosition());
                    if (++currentIndex >= containerSize) {
                        currentIndex = entity instanceof Player player && player.containerMenu instanceof SonictronMenu ? 0 : -1;
                    }
                    setCurrentIndex(stack, currentIndex);
                }
            }
        }
    }

    public static int getCurrentIndex(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt("currentIndex");
    }

    public static void setCurrentIndex(ItemStack stack, int index) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("currentIndex", index);
    }

    public static NonNullList<ItemStack> getStackInventory(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag inventoryTag = tag.getList("inventory", Tag.TAG_COMPOUND);
        NonNullList<ItemStack> inventory = NonNullList.withSize(inventoryTag.size(), ItemStack.EMPTY);
        for (int i = 0; i < inventoryTag.size(); i++) {
            CompoundTag stackTag = inventoryTag.getCompound(i);
            inventory.set(i, ItemStack.of(stackTag));
        }
        return inventory;
    }

    public static void setStackInventory(ItemStack stack, NonNullList<ItemStack> inventory) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag inventoryTag = new ListTag();
        for (ItemStack inventoryStack : inventory) {
            inventoryTag.add(inventoryStack.save(new CompoundTag()));
        }
        tag.put("inventory", inventoryTag);
    }
}
