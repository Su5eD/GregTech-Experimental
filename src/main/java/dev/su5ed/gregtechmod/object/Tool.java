package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.item.AdvancedDrillItem;
import dev.su5ed.gregtechmod.item.AdvancedSawItem;
import dev.su5ed.gregtechmod.item.ScrewdriverItem;
import dev.su5ed.gregtechmod.item.ToolItem;
import dev.su5ed.gregtechmod.item.ToolItem.ToolItemProperties;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Tool implements TaggedItemProvider {
    CROWBAR(() -> new ToolItem(new ToolItemProperties<>()
        .durability(256)
        .autoDescription()
        .attackDamage(6)), GregTechTags.CROWBAR),
    SCREWDRIVER(ScrewdriverItem::new, GregTechTags.SCREWDRIVER),
    ADVANCED_DRILL(AdvancedDrillItem::new, GregTechTags.LARGE_DRILL),
    ADVANCED_SAW(AdvancedSawItem::new);
    // TODO
    // Item Scanner
    // Debug Item Scanner
    
    private final Lazy<Item> instance;
    private final TagKey<Item> tag;
    
    Tool(Supplier<Item> supplier) {
        this(supplier, null);
    }

    Tool(Supplier<Item> supplier, TagKey<Item> tag) {
        this.instance = Lazy.of(() -> supplier.get().setRegistryName(getName()));
        this.tag = tag;
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Nullable
    @Override
    public TagKey<Item> getTag() {
        return this.tag;
    }
}
