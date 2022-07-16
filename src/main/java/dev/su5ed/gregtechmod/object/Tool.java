package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.item.ScrewdriverItem;
import dev.su5ed.gregtechmod.item.ToolItem;
import dev.su5ed.gregtechmod.item.ToolItem.ToolItemProperties;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public enum Tool implements TaggedItemProvider {
    CROWBAR(() -> new ToolItem(new ToolItemProperties(ModObjects.itemProperties().durability(256)).autoDescription().attackDamage(6)), GregTechTags.CROWBAR),
    SCREWDRIVER(ScrewdriverItem::new, GregTechTags.SCREWDRIVER);
    
    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

    Tool(Supplier<Item> supplier, TagKey<Item> tag) {
        this.instance = Lazy.of(() -> supplier.get().setRegistryName(getName()));
        this.tag = tag;
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Override
    public TagKey<Item> getTag() {
        return this.tag;
    }
}
