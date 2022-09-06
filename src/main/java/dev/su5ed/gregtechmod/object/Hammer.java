package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.item.HardHammerItem;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public enum Hammer implements TaggedItemProvider {
    IRON(4, 128),
    BRONZE(6, 256),
    STEEL(8, 512),
    TUNGSTEN_STEEL(10, 5120);

    private final Lazy<Item> instance;

    Hammer(int attackDamage, int durability) {
        this.instance = Lazy.of(() -> new HardHammerItem(attackDamage, durability).registryName(getName(), "hammer"));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Nullable
    @Override
    public TagKey<Item> getTag() {
        return GregTechTags.HARD_HAMMER; 
    }
}
