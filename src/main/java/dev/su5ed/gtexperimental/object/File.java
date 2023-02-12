package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.CraftingToolItem;
import dev.su5ed.gtexperimental.item.ToolItem.ToolItemProperties;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum File implements TaggedItemProvider {
    IRON(2, 128),
    BRONZE(3, 256),
    STEEL(3, 1280),
    TUNGSTEN_STEEL(4, 5120);

    private final Lazy<Item> instance;

    File(int attackDamage, int durability) {
        this.instance = Lazy.of(() -> new CraftingToolItem(new ToolItemProperties<>()
            .attackDamage(attackDamage)
            .durability(durability)
            .description(GtLocale.itemDescriptionKey("file")), 1, null));
    }
    
    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "file");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Override
    public TagKey<Item> getTag() {
        return GregTechTags.FILE; 
    }
}
