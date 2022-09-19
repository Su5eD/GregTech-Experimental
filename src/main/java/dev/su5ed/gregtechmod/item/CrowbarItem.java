package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.GtLocale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrowbarItem extends ToolItem {

    public CrowbarItem() {
        super(new ToolItemProperties<>()
            .durability(256)
            .autoDescription()
            .attackDamage(6)
            .attackSpeed(-3));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        if (ModHandler.railcraftLoaded) {
            components.add(GtLocale.itemKey("crowbar", "description_rc").toComponent().withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, components, isAdvanced);
    }
}
