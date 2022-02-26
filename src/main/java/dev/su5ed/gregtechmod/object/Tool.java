package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.ModTags;
import dev.su5ed.gregtechmod.item.ScrewdriverItem;
import dev.su5ed.gregtechmod.item.ToolItem;
import dev.su5ed.gregtechmod.item.ToolItem.ToolItemProperties;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public enum Tool implements TaggedItemProvider {
    CROWBAR(() -> new ToolItem(new ToolItemProperties(ModObjects.itemProperties().durability(256)).attackDamage(6)), ModTags.CROWBAR),
    SCREWDRIVER(ScrewdriverItem::new, ModTags.SCREWDRIVER);
    
    private final Lazy<Item> instance;
    private final Tag.Named<Item> tag;

    Tool(Supplier<Item> supplier, Tag.Named<Item> tag) {
        this.instance = Lazy.of(() -> supplier.get().setRegistryName(getName()));
        this.tag = tag;
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Override
    public Tag.Named<Item> getTag() {
        return this.tag;
    }
}
