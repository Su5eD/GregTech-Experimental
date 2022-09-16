package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.item.SolderingMetalItem;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Miscellaneous implements TaggedItemProvider {
    SOLDERING_LEAD(() -> new SolderingMetalItem(10)),
    SOLDERING_TIN(() -> new SolderingMetalItem(50)),
    EMPTY_SPRAY_CAN(() -> new ResourceItem(new ExtendedItemProperties<>().autoDescription()), GregTechTags.CRAFTING_SPRAY_CAN);
    
    private final Lazy<Item> instance;
    private final TagKey<Item> tag;
    
    Miscellaneous(Supplier<Item> supplier) {
        this(supplier, null);
    }

    Miscellaneous(Supplier<Item> supplier, TagKey<Item> tag) {
        this.instance = Lazy.of(supplier);
        this.tag = tag;
    }

    @Override
    public String getRegistryName() {
        return getName();
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
