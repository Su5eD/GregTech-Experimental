package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RubberHammerItem extends HammerItem {

    public RubberHammerItem() {
        super(new ToolItemProperties<>()
            .attackDamage(4)
            .durability(128)
            .multiDescription(3));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if (!stack.isEnchanted()) {
            stack.enchant(Enchantments.KNOCKBACK, 2);
        }
        return false;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
        InteractionResult result;

        if (be instanceof IGregTechMachine machine && GtUtil.hurtStack(stack, 1, player, context.getHand())) {
            machine.setAllowedToWork(!machine.isAllowedToWork());

            String state = machine.isAllowedToWork() ? "enabled" : "disabled";
            GtUtil.sendActionBarMessage(player, GtLocale.itemKey("rubber_hammer", "processing_" + state));
            result = InteractionResult.SUCCESS;
        }
        else {
            result = super.onItemUseFirst(stack, context);
        }

        return result;
    }
}
