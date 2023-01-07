package dev.su5ed.gregtechmod.menu;

import dev.su5ed.gregtechmod.api.GregTechAPI;
import dev.su5ed.gregtechmod.api.util.SonictronSound;
import dev.su5ed.gregtechmod.blockentity.SonictronBlockEntity;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.ModMenus;
import dev.su5ed.gregtechmod.util.inventory.ButtonClick;
import dev.su5ed.gregtechmod.util.inventory.InventorySlot;
import dev.su5ed.gregtechmod.util.inventory.ScrollDirection;
import dev.su5ed.gregtechmod.util.inventory.SlotStackCycle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SonictronMenu extends BlockEntityMenu<SonictronBlockEntity> {

    public SonictronMenu(int containerId, BlockPos pos, Player player, Inventory playerInventory) {
        super(ModMenus.SONICTRON.get(), GTBlockEntity.SONICTRON.getType(), containerId, pos, player, playerInventory);

        List<SonictronSound> sonictronSounds = GregTechAPI.instance().getSonictronSounds();
        List<ItemStack> stacks = sonictronSounds.stream()
            .map(sound -> new ItemStack(sound.item()))
            .toList();

        addInventorySlotBox(24, 19, 8, 8, 16, 16, (index, x, y) -> new SlotSonictronCycle(this.blockEntity.content, index, x, y, stacks, sonictronSounds));
    }

    private static class SlotSonictronCycle extends SlotStackCycle {
        private final List<SonictronSound> sonictronSounds;

        public SlotSonictronCycle(InventorySlot inventorySlot, int index, int x, int y, List<ItemStack> stacks, List<SonictronSound> sonictronSounds) {
            super(inventorySlot, index, x, y, stacks);

            this.sonictronSounds = sonictronSounds;
        }

        @Override
        public void slotClick(ButtonClick click, Player player, ItemStack stack) {
            ItemStack content = getItem();

            if (click == ButtonClick.MOUSE_RIGHT && !content.isEmpty()) {
                for (SonictronSound sound : this.sonictronSounds) {
                    if (content.getItem() == sound.item().asItem()) {
                        int count = Math.max(1, (content.getCount() + 1) % (sound.count() + 1));
                        content.setCount(count);

                        return;
                    }
                }
            }
            
            super.slotClick(click, player, stack);
        }

        @Override
        public void slotScroll(Player player, ScrollDirection direction, boolean shift) {
            ItemStack content = getItem();
            
            if (shift && !content.isEmpty()) {
                for (SonictronSound sound : this.sonictronSounds) {
                    if (content.getItem() == sound.item().asItem()) {
                        int mod = direction == ScrollDirection.DOWN ? 1 : -1;
                        int count = Math.max(1, (content.getCount() + mod) % (sound.count() + 1));
                        content.setCount(count);

                        return;
                    }
                }
            }
            
            super.slotScroll(player, direction, shift);
        }
    }
}
