package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gtexperimental.item.TurbineRotorItem;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.ItemProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum TurbineRotor implements ItemProvider {
    BRONZE(60, 10, 15000),
    STEEL(80, 20, 10000),
    MAGNALIUM(100, 50, 10000),
    TUNGSTEN_STEEL(90, 15, 30000),
    CARBON(125, 100, 2500);

    private final Lazy<Item> instance;

    TurbineRotor(int efficiency, int efficiencyMultiplier, int durability) {
        this.instance = Lazy.of(() -> new TurbineRotorItem(
            new ExtendedItemProperties<>(ModObjects.itemProperties().durability(durability).setNoRepair())
                .description(GtLocale.translateGenericDescription("turbine_rotor", efficiency))
                .setNoEnchant(), efficiency, efficiencyMultiplier));
    }
    
    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "turbine_rotor");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
