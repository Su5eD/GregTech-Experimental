package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum Cell implements ItemProvider {
    CARBON("C"),
    ICE("H2O"),
    NITROCARBON("NC"),
    SODIUM_SULFIDE("NaS"),
    SULFUR("S"),
    SULFURIC_ACID("H2SO4");

    private final Lazy<Item> instance;

    Cell(String description) {
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(Component.literal(description))));
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "cell");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
