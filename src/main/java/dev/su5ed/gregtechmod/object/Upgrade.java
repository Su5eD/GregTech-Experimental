package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import dev.su5ed.gregtechmod.item.upgrade.BatteryUpgrade;
import dev.su5ed.gregtechmod.item.upgrade.HVTransformerUpgrade;
import dev.su5ed.gregtechmod.item.upgrade.MachineLockUpgrade;
import dev.su5ed.gregtechmod.item.upgrade.PneumaticGeneratorUpgrade;
import dev.su5ed.gregtechmod.item.upgrade.QuantumChestUpgrade;
import dev.su5ed.gregtechmod.item.upgrade.RSEnergyCellUpgrade;
import dev.su5ed.gregtechmod.item.upgrade.SteamTankUpgrade;
import dev.su5ed.gregtechmod.item.upgrade.SteamUpgrade;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Upgrade implements TaggedItemProvider {
    HV_TRANSFORMER(HVTransformerUpgrade::new, GregTechTags.CRAFTING_HV_TRANSFORMER_UPGRADE),
    LITHIUM_BATTERY(() -> new BatteryUpgrade(16, 1, 100000), GregTechTags.CRAFTING_LI_BATTERY),
    ENERGY_CRYSTAL(() -> new BatteryUpgrade(16, GregTechMod.isClassic ? 2 : 3, GregTechMod.isClassic ? 100000 : 1000000), GregTechMod.isClassic ? GregTechTags.CRAFTING_100K_EU_STORE : GregTechTags.CRAFTING_1KK_EU_STORE),
    LAPOTRON_CRYSTAL(() -> new BatteryUpgrade(16, GregTechMod.isClassic ? 3 : 4, GregTechMod.isClassic ? 1000000 : 10000000), GregTechMod.isClassic ? GregTechTags.CRAFTING_1KK_EU_STORE : GregTechTags.CRAFTING_10KK_EU_STORE),
    ENERGY_ORB(() -> new BatteryUpgrade(16, GregTechMod.isClassic ? 4 : 5, GregTechMod.isClassic ? 10000000 : 100000000), GregTechMod.isClassic ? GregTechTags.CRAFTING_10KK_EU_STORE : GregTechTags.CRAFTING_100KK_EU_STORE),
    MACHINE_LOCK(MachineLockUpgrade::new, GregTechTags.CRAFTING_LOCK),
    QUANTUM_CHEST(QuantumChestUpgrade::new, GregTechTags.CRAFTING_QUANTUM_CHEST_UPGRADE),
    STEAM_UPGRADE(SteamUpgrade::new, GregTechTags.CRAFTING_STEAM_UPGRADE),
    STEAM_TANK(SteamTankUpgrade::new, GregTechTags.CRAFTING_STEAM_TANK),
    PNEUMATIC_GENERATOR(PneumaticGeneratorUpgrade::new, GregTechTags.CRAFTING_PNEUMATIC_GENERATOR),
    RS_ENERGY_CELL(RSEnergyCellUpgrade::new, GregTechTags.CRAFTING_ENERGY_CELL_UPGRADE);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

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
