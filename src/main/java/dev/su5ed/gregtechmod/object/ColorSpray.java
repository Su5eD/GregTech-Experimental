package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ColorSprayItem;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public enum ColorSpray implements TaggedItemProvider {
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    private final Lazy<Item> instance;
    private final DyeColor color;

    ColorSpray() {
        this.color = DyeColor.valueOf(name());
        this.instance = Lazy.of(() -> new ColorSprayItem(this.color).registryName(getName(), "color_spray"));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Nullable
    @Override
    public TagKey<Item> getTag() {
        return this.color.getTag();
    }
}
