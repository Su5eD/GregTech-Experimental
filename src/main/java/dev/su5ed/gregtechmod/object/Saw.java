package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.CraftingToolItem;
import dev.su5ed.gregtechmod.item.ToolItem;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.Lazy;

public enum Saw implements ItemProvider {
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
}
