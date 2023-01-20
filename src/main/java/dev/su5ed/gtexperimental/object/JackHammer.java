package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.item.ElectricToolItem;
import dev.su5ed.gtexperimental.item.ElectricToolItem.ElectricToolItemProperties;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.ItemProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.Lazy;

public enum JackHammer implements ItemProvider {
    BRONZE(50, 10000, 1, 50, 7.5F, Tiers.IRON),
    STEEL(100, 10000, 1, 50, 15F, Tiers.IRON),
    DIAMOND(250, 100000, 2, 100, 45F, Tiers.DIAMOND);

    private final Lazy<Item> instance;

    JackHammer(int operationEnergyCost, int maxCharge, int energyTier, int transferLimit, float efficiency, Tier tier) {
        this.instance = Lazy.of(() -> new ElectricToolItem(new ElectricToolItemProperties()
            .maxCharge(maxCharge)
            .operationEnergyCost(operationEnergyCost)
            .energyTier(energyTier)
            .transferLimit(transferLimit)
            .destroySpeed(efficiency)
            .tier(tier)
            .blockTags(BlockTags.MINEABLE_WITH_PICKAXE)
            .actions(ToolActions.PICKAXE_DIG)));
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "jack_hammer");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
