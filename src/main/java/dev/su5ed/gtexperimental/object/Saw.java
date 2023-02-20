package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.CraftingToolItem;
import dev.su5ed.gtexperimental.item.ToolItem;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.ItemProvider;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public enum Saw implements TaggedItemProvider {
    IRON(128, 3, 2),
    BRONZE(256, 4, 3),
    STEEL(1280, 6, 4),
    TUNGSTEN_STEEL(5120, 8, 5);

    private final Lazy<Item> instance;

    Saw(int durability, int efficiency, int attackDamage) {
        this.instance = Lazy.of(() -> new CraftingToolItem(new ToolItem.ToolItemProperties<>()
            .attackDamage(attackDamage)
            .destroySpeed(efficiency)
            .tier(Tiers.IRON)
            .actions(ToolActions.AXE_DIG)
            .blockTags(BlockTags.MINEABLE_WITH_AXE, BlockTags.LEAVES)
            .durability(durability)
            .description(GtLocale.itemDescriptionKey("saw")), 1, null));
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "saw");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Override
    public TagKey<Item> getTag() {
        return GregTechTags.SAW;
    }
}
