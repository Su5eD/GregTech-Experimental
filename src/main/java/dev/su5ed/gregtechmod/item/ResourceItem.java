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
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ResourceItem extends Item {
    private final List<Lazy<MutableComponent>> description;
    private final boolean isFoil;
    private final boolean isEnchantable;

    public ResourceItem(ExtendedItemProperties<?> properties) {
        super(properties.properties);

        this.description = StreamEx.of(properties.description)
            .map(fun -> fun.apply(this))
            .toImmutableList();
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

        StreamEx.of(this.description)
            .map(lazy -> lazy.get().withStyle(ChatFormatting.GRAY))
            .forEach(tooltipcomponents::add);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.isFoil || super.isFoil(stack);
    }

    @SuppressWarnings("unchecked")
    public static class ExtendedItemProperties<T extends ExtendedItemProperties<T>> {
        private final Properties properties;

        private final List<Function<Item, Lazy<MutableComponent>>> description = new ArrayList<>();
        boolean isFoil;
        boolean isEnchantable = true;

        public ExtendedItemProperties() {
            this(ModObjects.itemProperties());
        }

        public ExtendedItemProperties(Properties properties) {
            this.properties = properties;
        }

        public T autoDescription() {
            this.description.add(item -> Lazy.of(() -> GtLocale.itemDescriptionKey(item.getRegistryName().getPath()).toComponent()));
            return (T) this;
        }

        public T multiDescription(int lines) {
            IntStreamEx.range(lines)
                .<Function<Item, Lazy<MutableComponent>>>mapToObj(i -> item -> Lazy.of(() -> {
                    String name = item.getRegistryName().getPath();
                    String path = i == 0 ? "description" : "description_" + i;
                    return GtLocale.key("item", name, path).toComponent();
                }))
                .forEach(this.description::add);
            return (T) this;
        }

        public T description(GtLocale.TranslationKey key) {
            return description(key.toComponent());
        }

        public T description(@Nullable MutableComponent component) {
            if (component != null) this.description.add(item -> Lazy.of(() -> component));
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
