package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Optional;

public class RubberHammerItem extends HammerItem {

    public RubberHammerItem() {
        super(new ToolItemProperties<>()
            .attackDamage(4)
            .attackSpeed(-2.5F)
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
        return Optional.ofNullable(context.getLevel().getBlockEntity(context.getClickedPos()))
            .flatMap(be -> be.getCapability(Capabilities.MACHINE_CONTROLLER).resolve())
            .filter(controller -> GtUtil.hurtStack(stack, 1, player, context.getHand()))
            .map(controller -> {
                controller.setAllowedToWork(!controller.isAllowedToWork());

                String state = controller.isAllowedToWork() ? "enabled" : "disabled";
                GtUtil.sendActionBarMessage(player, GtLocale.itemKey("rubber_hammer", "processing_" + state));
                return InteractionResult.SUCCESS;
            })
            .orElseGet(() -> super.onItemUseFirst(stack, context));
    }
}
