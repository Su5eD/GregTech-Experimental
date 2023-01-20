package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.util.GtLocale;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ToolActions;

public class SawItem extends CraftingToolItem {

    public SawItem(int durability, int efficiency, int attackDamage) {
        super(new ToolItemProperties<>()
            .attackDamage(attackDamage)
            .destroySpeed(efficiency)
            .tier(Tiers.IRON)
            .actions(ToolActions.AXE_DIG)
            .blockTags(BlockTags.MINEABLE_WITH_AXE, BlockTags.LEAVES)
            .durability(durability)
            .description(GtLocale.itemDescriptionKey("saw")), 1, null);
    }
}
