package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.SonictronSound;
import mods.gregtechmod.inventory.SlotStackCycle;
import mods.gregtechmod.objects.blocks.teblocks.TileEntitySonictron;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ContainerSonictron extends ContainerGtBase<TileEntitySonictron> {

    public ContainerSonictron(TileEntitySonictron base) {
        super(base);
        
        List<SonictronSound> sonictronSounds = GregTechAPI.instance().getSonictronSounds();
        List<ItemStack> stacks = sonictronSounds.stream()
                .map(SonictronSound::getItem)
                .collect(Collectors.toList());
        
        addSlotsToContainer(8, 8, 24, 19, 16, (index, x, y) -> new SlotSonictronCycle(base.content, index, x, y, stacks, sonictronSounds));
    }
    
    private static class SlotSonictronCycle extends SlotStackCycle {
        private final List<SonictronSound> sonictronSounds;

        public SlotSonictronCycle(InvSlot invSlot, int index, int x, int y, List<ItemStack> stacks, List<SonictronSound> sonictronSounds) {
            super(invSlot, index, x, y, stacks);
            
            this.sonictronSounds = sonictronSounds;
        }

        @Override
        public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
            ItemStack content = getStack();

            if (click == ButtonClick.MOUSE_RIGHT && !content.isEmpty()) {
                for (SonictronSound sound : this.sonictronSounds) {
                    if (StackUtil.checkItemEquality(content, sound.getItem())) {
                        int count = Math.max(1, (content.getCount() + 1) % (sound.getCount() + 1));
                        content.setCount(count);

                        return true;
                    }
                }
            }

            return super.slotClick(click, player, stack);
        }
    }
}
