package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.HardHammerItem;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum Hammer implements TaggedItemProvider {
    IRON(4, 128),
    BRONZE(6, 256),
    STEEL(8, 512),
    TUNGSTEN_STEEL(10, 5120);

    private final Lazy<Item> instance;

    Hammer(int attackDamage, int durability) {
        this.instance = Lazy.of(() -> new HardHammerItem(attackDamage, durability));
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "hammer");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Override
    public TagKey<Item> getTag() {
        return GregTechTags.HARD_HAMMER; 
    }
}
