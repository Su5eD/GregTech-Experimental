package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.WrenchItem;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum Wrench implements TaggedItemProvider {
    IRON(128, 4),
    BRONZE(256, 6),
    STEEL(512, 8),
    TUNGSTEN_STEEL(5120, 10);

    private final Lazy<Item> instance;

    Wrench(int durability, int attackDamage) {
        this.instance = Lazy.of(() -> new WrenchItem(durability, attackDamage));
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "wrench");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Override
    public TagKey<Item> getTag() {
        return GregTechTags.WRENCH;
    }
}
