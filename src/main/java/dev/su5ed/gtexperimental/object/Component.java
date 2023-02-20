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
    ADVANCED_CIRCUIT_BOARD(GregTechTags.itemTag("advanced_circuit_board")),
    ADVANCED_CIRCUIT_PARTS(null),
    ALUMINIUM_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1),
    BASIC_CIRCUIT_BOARD(GregTechTags.itemTag("basic_circuit_board")),
    BRASS_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_0),
    BRONZE_GEAR(GregTechTags.gear("bronze")),
    BRONZE_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_0),
    BRONZE_TURBINE_BLADE(GregTechTags.itemTag("bronze_turbine_blade")),
    CARBON_TURBINE_BLADE(GregTechTags.itemTag("carbon_turbine_blade")),
    CUPRONICKEL_COIL(GregTechTags.itemTag("cupronickel_coil")),
    DATA_ORB(DataOrbItem::new, GregTechTags.itemTag("data_orb")),
    DATA_STORAGE_CIRCUIT(GregTechTags.itemTag("data_storage_circuit")),
    DIAMOND_GRINDER(GregTechTags.CRAFTING_GRINDER),
    DIAMOND_SAWBLADE(GregTechTags.itemTag("diamond_sawblade")),
    DUCT_TAPE(GregTechTags.DUCT_TAPE),
    IRIDIUM_GEAR(GregTechTags.gear("iridium")),
    IRON_GEAR(GregTechTags.gear("iron")),
    IRON_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1),
    KANTHAL_COIL(GregTechTags.itemTag("kanthal_coil")),
    LAVA_FILTER(() -> new ResourceItem(new ExtendedItemProperties<>()
        .durability(100)
        .setNoRepair()
        .setNoEnchant()
        .autoDescription()), null),
    LITHIUM_RE_BATTERY(LithiumBatteryItem::new, GregTechTags.CRAFTING_LI_BATTERY),
    MACHINE_PARTS(GregTechTags.itemTag("machine_parts")),
    MAGNALIUM_TURBINE_BLADE(GregTechTags.itemTag("magnalium_turbine_blade")),
    NICHROME_COIL(GregTechTags.itemTag("nichrome_coil")),
    PROCESSOR_CIRCUIT_BOARD(GregTechTags.itemTag("processor_circuit_board")),
    STEEL_GEAR(GregTechTags.gear("steel")),
    STEEL_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2),
    STEEL_TURBINE_BLADE(GregTechTags.itemTag("steel_turbine_blade")),
    SUPERCONDUCTOR(GregTechTags.itemTag("superconductor")),
    TITANIUM_GEAR(GregTechTags.gear("titanium")),
    TITANIUM_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_3),
    TUNGSTEN_STEEL_GEAR(GregTechTags.gear("tungsten_steel")),
    TUNGSTEN_STEEL_HULL(GregTechTags.CRAFTING_RAW_MACHINE_TIER_3),
    TUNGSTEN_STEEL_TURBINE_BLADE(GregTechTags.itemTag("tungsten_steel_turbine_blade")),
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
