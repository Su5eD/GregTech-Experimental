package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.upgrade.BatteryUpgrade;
import dev.su5ed.gtexperimental.item.upgrade.HVTransformerUpgrade;
import dev.su5ed.gtexperimental.item.upgrade.MachineLockUpgrade;
import dev.su5ed.gtexperimental.item.upgrade.PneumaticGeneratorUpgrade;
import dev.su5ed.gtexperimental.item.upgrade.QuantumChestUpgrade;
import dev.su5ed.gtexperimental.item.upgrade.RSEnergyCellUpgrade;
import dev.su5ed.gtexperimental.item.upgrade.SteamTankUpgrade;
import dev.su5ed.gtexperimental.item.upgrade.SteamUpgrade;
import dev.su5ed.gtexperimental.util.ProfileManager;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Upgrade implements TaggedItemProvider {
    HV_TRANSFORMER_UPGRADE(HVTransformerUpgrade::new),
    LITHIUM_BATTERY_UPGRADE(() -> new BatteryUpgrade(16, 1, 100000), GregTechTags.CRAFTING_LI_BATTERY),
    ENERGY_CRYSTAL_UPGRADE(() -> new BatteryUpgrade(16, ProfileManager.INSTANCE.isClassic() ? 2 : 3, ProfileManager.INSTANCE.isClassic() ? 100000 : 1000000), GregTechTags.SMALL_EU_STORE),
    LAPOTRON_CRYSTAL_UPGRADE(() -> new BatteryUpgrade(16, ProfileManager.INSTANCE.isClassic() ? 3 : 4, ProfileManager.INSTANCE.isClassic() ? 1000000 : 10000000), GregTechTags.MEDIUM_EU_STORE),
    ENERGY_ORB(() -> new BatteryUpgrade(16, ProfileManager.INSTANCE.isClassic() ? 4 : 5, ProfileManager.INSTANCE.isClassic() ? 10000000 : 100000000), GregTechTags.LARGE_EU_STORE),
    MACHINE_LOCK(MachineLockUpgrade::new),
    QUANTUM_CHEST(QuantumChestUpgrade::new),
    STEAM_UPGRADE(SteamUpgrade::new),
    STEAM_TANK(SteamTankUpgrade::new),
    PNEUMATIC_GENERATOR(PneumaticGeneratorUpgrade::new),
    RS_ENERGY_CELL(RSEnergyCellUpgrade::new);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;
    
    Upgrade(Supplier<Item> supplier) {
        this(supplier, null);
    }

    Upgrade(Supplier<Item> supplier, TagKey<Item> tag) {
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
