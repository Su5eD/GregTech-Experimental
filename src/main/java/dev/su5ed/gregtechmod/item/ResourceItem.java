package dev.su5ed.gregtechmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ResourceItem extends Item {
    private final Supplier<String> description;
    private final boolean isFoil;

    public ResourceItem(Properties pProperties, Supplier<String> description) {
        this(pProperties, description, false);
    }
    
    public ResourceItem(Properties pProperties, Supplier<String> description, boolean isFoil) {
        super(pProperties);
        
        this.description = description;
        this.isFoil = isFoil;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        
        String desc = this.description.get();
        if (desc != null) pTooltipComponents.add(new TextComponent(desc).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return this.isFoil || super.isFoil(pStack);
    }
}
