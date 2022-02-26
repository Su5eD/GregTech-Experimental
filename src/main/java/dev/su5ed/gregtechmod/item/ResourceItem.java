package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.object.ModObjects;
import dev.su5ed.gregtechmod.util.GtLocale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class ResourceItem extends Item {
    @Nullable
    private final Lazy<MutableComponent> description;
    private final boolean isFoil;
    private final boolean isEnchantable;

    public ResourceItem(ExtendedItemProperties<?> properties) {
        super(properties.properties);

        this.description = properties.description == null ? null : properties.description.apply(this);
        this.isFoil = properties.isFoil;
        this.isEnchantable = properties.isEnchantable;
    }

    public ResourceItem registryName(String... paths) {
        setRegistryName(String.join("_", paths));
        return this;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return this.isEnchantable && super.isEnchantable(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipcomponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipcomponents, isAdvanced);

        if (this.description != null) tooltipcomponents.add(this.description.get().withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.isFoil || super.isFoil(stack);
    }

    @SuppressWarnings("unchecked")
    public static class ExtendedItemProperties<T extends ExtendedItemProperties<T>> {
        private final Properties properties;
        
        @Nullable
        private Function<Item, Lazy<MutableComponent>> description; // TODO Multiple descriptions
        boolean isFoil;
        boolean isEnchantable = true;
        
        public ExtendedItemProperties() {
            this(ModObjects.itemProperties());
        }

        public ExtendedItemProperties(Properties properties) {
            this.properties = properties;
        }

        public T autoDescription() {
            this.description = item -> Lazy.of(() -> GtLocale.itemDescriptionKey(item.getRegistryName().getPath()).toComponent());
            return (T) this;
        }
        
        public T description(GtLocale.TranslationKey key) {
            return description(key.toComponent());
        }
        
        public T description(@Nullable MutableComponent component) {
            if (component != null) this.description = item -> Lazy.of(() -> component);
            return (T) this;
        }

        public T foil(boolean isFoil) {
            this.isFoil = isFoil;
            return (T) this;
        }

        public T setNoEnchant() {
            this.isEnchantable = false;
            return (T) this;
        }
    }
}
