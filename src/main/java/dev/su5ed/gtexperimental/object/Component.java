package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.DataOrbItem;
import dev.su5ed.gtexperimental.item.LithiumBatteryItem;
import dev.su5ed.gtexperimental.item.ResourceItem;
import dev.su5ed.gtexperimental.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Component implements TaggedItemProvider {
    ADVANCED_CIRCUIT_BOARD(GregTechTags.CIRCUIT_BOARD_TIER_4),
    ADVANCED_CIRCUIT_PARTS(GregTechTags.CRAFTING_CIRCUIT_PARTS_TIER_4),
    ALUMINIUM_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1),
    BASIC_CIRCUIT_BOARD(GregTechTags.CIRCUIT_BOARD_TIER_2),
    BRASS_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_0),
    BRONZE_GEAR(GregTechTags.BRONZE_GEAR),
    BRONZE_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_0),
    BRONZE_TURBINE_BLADE(GregTechTags.CRAFTING_BRONZE_TURBINE_BLADE),
    CARBON_TURBINE_BLADE(GregTechTags.CRAFTING_CARBON_TURBINE_BLADE),
    CUPRONICKEL_COIL(GregTechTags.CRAFTING_HEATING_COIL_TIER_0),
    DATA_ORB(DataOrbItem::new, GregTechTags.CIRCUIT_TIER_8),
    DATA_STORAGE_CIRCUIT(GregTechTags.CIRCUIT_TIER_5),
    DIAMOND_GRINDER(GregTechTags.CRAFTING_GRINDER),
    DIAMOND_SAWBLADE(GregTechTags.CRAFTING_DIAMOND_BLADE),
    DUCT_TAPE(GregTechTags.CRAFTING_DUCT_TAPE),
    IRIDIUM_GEAR(GregTechTags.IRIDIUM_GEAR),
    IRON_GEAR(GregTechTags.IRON_GEAR),
    IRON_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1),
    KANTHAL_COIL(GregTechTags.CRAFTING_HEATING_COIL_TIER_1),
    LAVA_FILTER(() -> new ResourceItem(new ExtendedItemProperties<>()
        .durability(100)
        .setNoRepair()
        .autoDescription()), null),
    LITHIUM_RE_BATTERY(LithiumBatteryItem::new, GregTechTags.CRAFTING_LI_BATTERY),
    MACHINE_PARTS(GregTechTags.CRAFTING_MACHINE_PARTS),
    MAGNALIUM_TURBINE_BLADE(GregTechTags.CRAFTING_MAGNALIUM_TURBINE_BLADE),
    NICHROME_COIL(GregTechTags.CRAFTING_HEATING_COIL_TIER_2),
    PROCESSOR_CIRCUIT_BOARD(GregTechTags.CIRCUIT_BOARD_TIER_6),
    STEEL_GEAR(GregTechTags.STEEL_GEAR),
    STEEL_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2),
    STEEL_TURBINE_BLADE(GregTechTags.CRAFTING_STEEL_TURBINE_BLADE),
    SUPERCONDUCTOR(GregTechTags.CRAFTING_SUPERCONDUCTOR),
    TITANIUM_GEAR(GregTechTags.TITANIUM_GEAR),
    TITANIUM_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_3),
    TUNGSTEN_STEEL_GEAR(GregTechTags.TUNGSTEN_STEEL_GEAR),
    TUNGSTEN_STEEL_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_3),
    TUNGSTEN_STEEL_TURBINE_BLADE(GregTechTags.CRAFTING_TUNGSTEN_STEEL_TURBINE_BLADE),
    WOLFRAMIUM_GRINDER(GregTechTags.CRAFTING_GRINDER);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

    Component(TagKey<Item> tag) {
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().autoDescription()));
        this.tag = tag;
    }

    Component(Supplier<Item> constructor, TagKey<Item> tag) {
        this.instance = Lazy.of(constructor);
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
