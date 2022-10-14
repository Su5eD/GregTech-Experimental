package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.ClientSetup;
import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

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
        BlockEntity te = level.getBlockEntity(pos);

        if (te instanceof IGregTechMachine machine) {
            if (!level.isClientSide) {
                boolean input = machine.isInputEnabled();
                boolean output = machine.isOutputEnabled();

                if (input = !input) output = !output;
                machine.setInputEnabled(input);
                machine.setOutputEnabled(output);

                Component enabled = GtLocale.itemKey("hard_hammer", "enabled").toComponent();
                Component disabled = GtLocale.itemKey("hard_hammer", "disabled").toComponent();
                Component message = GtLocale.itemKey("hard_hammer", "auto_input")
                    .toComponent(input ? enabled : disabled, output ? enabled : disabled);
                
                context.getPlayer().displayClientMessage(message, true);
            }
            return InteractionResult.SUCCESS;
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientSetup.playSound(SoundEvents.ANVIL_USE, GtUtil.RANDOM.nextFloat() * 0.1F + 0.9F));
        return copy.hurt(4, GtUtil.RANDOM, null) ? ItemStack.EMPTY : copy;
    }
}
