package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.GtUpgradeType;
import dev.su5ed.gregtechmod.blockentity.DigitalChestBlockEntity;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.item.UpgradeItem;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public enum Upgrade implements TaggedItemProvider {
    HV_TRANSFORMER(GtUpgradeType.TRANSFORMER, 2, 3, GregTechTags.CRAFTING_HV_TRANSFORMER_UPGRADE, (machine, player) -> {
        machine.addExtraTier();
    }),
    LITHIUM_BATTERY(GtUpgradeType.BATTERY, 16, 1, GregTechTags.CRAFTING_LI_BATTERY, (machine, player) -> {
        machine.addExtraEUCapacity(100000);
    }),
    ENERGY_CRYSTAL(GtUpgradeType.BATTERY, 16, GregTechMod.isClassic ? 2 : 3, GregTechMod.isClassic ? GregTechTags.CRAFTING_100K_EU_STORE : GregTechTags.CRAFTING_1KK_EU_STORE, (machine, player) -> {
        machine.addExtraEUCapacity(GregTechMod.isClassic ? 100000 : 1000000);
    }),
    LAPOTRON_CRYSTAL(GtUpgradeType.BATTERY, 16, GregTechMod.isClassic ? 3 : 4, GregTechMod.isClassic ? GregTechTags.CRAFTING_1KK_EU_STORE : GregTechTags.CRAFTING_10KK_EU_STORE, (machine, player) -> {
        machine.addExtraEUCapacity(GregTechMod.isClassic ? 1000000 : 10000000);
    }),
    ENERGY_ORB(GtUpgradeType.BATTERY, 16, GregTechMod.isClassic ? 4 : 5, GregTechMod.isClassic ? GregTechTags.CRAFTING_10KK_EU_STORE : GregTechTags.CRAFTING_100KK_EU_STORE, (machine, player) -> {
        machine.addExtraEUCapacity(GregTechMod.isClassic ? 10000000 : 100000000);
    }),
    MACHINE_LOCK(GtUpgradeType.LOCK, 1, 0, GregTechTags.CRAFTING_LOCK, (machine, player) -> {
        if (!machine.isOwnedBy(player.getGameProfile())) {
            player.displayClientMessage(GtLocale.itemKey("machine_lock", "error").toComponent(), false);
            return true;
        }
        return false;
    }),
    QUANTUM_CHEST(GtUpgradeType.OTHER, 1, 0, GregTechTags.CRAFTING_QUANTUM_CHEST_UPGRADE, (machine, player) -> {
        if (machine instanceof DigitalChestBlockEntity chest) chest.upgradeToQuantumChest(); 
    }),
    STEAM_UPGRADE(GtUpgradeType.STEAM, 1, 1, GregTechTags.CRAFTING_STEAM_UPGRADE, (machine, player) -> {
        if (!machine.hasSteamTank()) machine.addSteamTank();
    }),
    STEAM_TANK(GtUpgradeType.STEAM, 16, 1, GregTechTags.CRAFTING_STEAM_TANK, UpgradableBlockEntity::hasSteamTank, (machine, player) -> {
        FluidTank steamTank = machine.getSteamTank();
        if (steamTank != null) steamTank.setCapacity(steamTank.getCapacity() + 64000);
    }),
    PNEUMATIC_GENERATOR(GtUpgradeType.MJ, 1, 1, GregTechTags.CRAFTING_PNEUMATIC_GENERATOR, Upgrade::requireBuildCraftPresent),
    RS_ENERGY_CELL(GtUpgradeType.MJ, 16, 1, GregTechTags.CRAFTING_ENERGY_CELL_UPGRADE, UpgradableBlockEntity::hasMjUpgrade, Upgrade::requireBuildCraftPresent);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;
    
    Upgrade(GtUpgradeType type, int maxCount, int requiredTier, TagKey<Item> tag, BiPredicate<UpgradableBlockEntity, Player> beforeInsert) {
        this(type, maxCount, requiredTier, tag, be -> true, beforeInsert, (be, player) -> {});
    }
    
    Upgrade(GtUpgradeType type, int maxCount, int requiredTier, TagKey<Item> tag, BiConsumer<UpgradableBlockEntity, Player> afterInsert) {
        this(type, maxCount, requiredTier, tag, be -> true, (be, player) -> false, afterInsert);
    }
    
    Upgrade(GtUpgradeType type, int maxCount, int requiredTier, TagKey<Item> tag, Predicate<UpgradableBlockEntity> condition,
                BiPredicate<UpgradableBlockEntity, Player> beforeInsert) {
        this(type, maxCount, requiredTier, tag, condition, beforeInsert, (be, player) -> {});
    }

    Upgrade(GtUpgradeType type, int maxCount, int requiredTier, TagKey<Item> tag, Predicate<UpgradableBlockEntity> condition,
            BiConsumer<UpgradableBlockEntity, Player> afterInsert) {
        this(type, maxCount, requiredTier, tag, condition, (be, player) -> false, afterInsert);
    }
    
    Upgrade(GtUpgradeType type, int maxCount, int requiredTier, TagKey<Item> tag, Predicate<UpgradableBlockEntity> condition,
            BiPredicate<UpgradableBlockEntity, Player> beforeInsert, BiConsumer<UpgradableBlockEntity, Player> afterInsert) {
        this.instance = Lazy.of(() -> new UpgradeItem(
            new ExtendedItemProperties<>().autoDescription(),
            type, maxCount, requiredTier, condition, beforeInsert, afterInsert));
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
    
    private static boolean requireBuildCraftPresent(UpgradableBlockEntity be, Player player) { // TODO BC not available to us anymore
        if (!ModHandler.buildcraftLoaded) {
            player.displayClientMessage(GtLocale.key("info", "buildcraft_absent").toComponent(), false);
            return true;
        }
        return false;
    }
}
