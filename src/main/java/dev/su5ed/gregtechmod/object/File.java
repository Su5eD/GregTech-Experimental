package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.CraftingToolItem;
import dev.su5ed.gregtechmod.item.ToolItem.ToolItemProperties;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum File implements ItemProvider {
    IRON(2, 128),
    BRONZE(3, 256),
    STEEL(3, 1280),
    TUNGSTEN_STEEL(4, 5120);

    private final Lazy<Item> instance;

    File(int attackDamage, int durability) {
        this.instance = Lazy.of(() -> new CraftingToolItem(new ToolItemProperties<>()
            .attackDamage(attackDamage)
            .durability(durability)
            .description(GtLocale.itemDescriptionKey("file")), 1, null).registryName(getName(), "file"));
    }


    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
