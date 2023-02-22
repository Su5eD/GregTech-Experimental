package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.ClientSetup;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.Optional;

public class HardHammerItem extends HammerItem {

    public HardHammerItem(int attackDamage, int durability) {
        super(new ToolItemProperties<>()
            .attackDamage(attackDamage)
            .effectiveAganist(GregTechTags.HARD_HAMMER_EFFECTIVE)
            .durability(durability)
            .multiDescription(i -> "hard_hammer", 4));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        return Optional.ofNullable(level.getBlockEntity(pos))
            .flatMap(be -> be.getCapability(Capabilities.MACHINE_CONTROLLER).resolve())
            .map(controller -> {
                if (!level.isClientSide) {
                    boolean input = controller.isInputEnabled();
                    boolean output = controller.isOutputEnabled();

                    if (input = !input) output = !output;
                    controller.setInputEnabled(input);
                    controller.setOutputEnabled(output);

                    Component enabled = GtLocale.itemKey("hard_hammer", "enabled").toComponent();
                    Component disabled = GtLocale.itemKey("hard_hammer", "disabled").toComponent();
                    Component message = GtLocale.itemKey("hard_hammer", "auto_input")
                        .toComponent(input ? enabled : disabled, output ? enabled : disabled);

                    context.getPlayer().displayClientMessage(message, true);
                }
                return InteractionResult.SUCCESS;
            })
            .orElseGet(() -> super.onItemUseFirst(stack, context));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        // TODO FIX SOUND
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientSetup.playSound(SoundEvents.ANVIL_USE, GtUtil.RANDOM.nextFloat() * 0.1F + 0.9F));
        return copy.hurt(4, GtUtil.RANDOM, null) ? ItemStack.EMPTY : copy;
    }
}
