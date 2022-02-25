package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.util.GtLocale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ResourceItem extends Item {
    @Nullable
    private final MutableComponent description;
    private boolean isFoil;
    private boolean isEnchantable;
    
    public ResourceItem(Properties properties) {
        this(properties, (MutableComponent) null);
    }
    
    public ResourceItem(Properties properties, String description) {
        this(properties, GtLocale.itemDescriptionKey(description).toComponent());
    }
    
    public ResourceItem(Properties properties, MutableComponent description) {
        super(properties);
        
        this.description = description;
    }
    
    public ResourceItem setIsFoil(boolean value) {
        this.isFoil = value;
        return this;
    }
    
    public ResourceItem setIsEnchantable(boolean value) {
        this.isEnchantable = value;
        return this;
    }
    
    public ResourceItem registryName(String... paths) {
        setRegistryName(String.join("_", paths));
        return this;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return this.isEnchantable && super.isEnchantable(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        
        if (this.description != null) pTooltipComponents.add(this.description.withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return this.isFoil || super.isFoil(pStack);
    }
}
