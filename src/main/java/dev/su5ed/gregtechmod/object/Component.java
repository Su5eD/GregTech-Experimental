package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.ModTags;
import dev.su5ed.gregtechmod.item.DataOrbItem;
import dev.su5ed.gregtechmod.item.LithiumBatteryItem;
import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public enum Component implements TaggedItemProvider {
    ADVANCED_CIRCUIT_BOARD(ModTags.CIRCUIT_BOARD_TIER_4),
    ADVANCED_CIRCUIT_PARTS(ModTags.CRAFTING_CIRCUIT_PARTS_TIER_4),
    ALUMINIUM_HULL(ModTags.CRAFTING_RAW_MACHINE_TIER_1),
    BASIC_CIRCUIT_BOARD(ModTags.CIRCUIT_BOARD_TIER_2),
    BRASS_HULL(ModTags.CRAFTING_RAW_MACHINE_TIER_0),
    BRONZE_GEAR(ModTags.BRONZE_GEAR),
    BRONZE_HULL(ModTags.CRAFTING_RAW_MACHINE_TIER_0),
    BRONZE_TURBINE_BLADE(ModTags.CRAFTING_BRONZE_TURBINE_BLADE),
    CARBON_TURBINE_BLADE(ModTags.CRAFTING_CARBON_TURBINE_BLADE),
    CUPRONICKEL_COIL(ModTags.CRAFTING_HEATING_COIL_TIER_0),
    DATA_ORB(DataOrbItem::new, ModTags.CIRCUIT_TIER_8),
    DATA_STORAGE_CIRCUIT(ModTags.CIRCUIT_TIER_5),
    DIAMOND_GRINDER(ModTags.CRAFTING_GRINDER),
    DIAMOND_SAWBLADE(ModTags.CRAFTING_DIAMOND_BLADE),
    DUCT_TAPE(ModTags.CRAFTING_DUCT_TAPE),
    IRIDIUM_GEAR(ModTags.IRIDIUM_GEAR),
    IRON_GEAR(ModTags.IRON_GEAR),
    IRON_HULL(ModTags.CRAFTING_RAW_MACHINE_TIER_1),
    KANTHAL_COIL(ModTags.CRAFTING_HEATING_COIL_TIER_1),
    LITHIUM_RE_BATTERY(LithiumBatteryItem::new, ModTags.CRAFTING_LI_BATTERY),
    MACHINE_PARTS(ModTags.CRAFTING_MACHINE_PARTS),
    MAGNALIUM_TURBINE_BLADE(ModTags.CRAFTING_MAGNALIUM_TURBINE_BLADE),
    NICHROME_COIL(ModTags.CRAFTING_HEATING_COIL_TIER_2),
    PROCESSOR_CIRCUIT_BOARD(ModTags.CIRCUIT_BOARD_TIER_6),
    STEEL_GEAR(ModTags.STEEL_GEAR),
    STEEL_HULL(ModTags.CRAFTING_RAW_MACHINE_TIER_2),
    STEEL_TURBINE_BLADE(ModTags.CRAFTING_STEEL_TURBINE_BLADE),
    SUPERCONDUCTOR(ModTags.CRAFTING_SUPERCONDUCTOR),
    TITANIUM_GEAR(ModTags.TITANIUM_GEAR),
    TITANIUM_HULL(ModTags.CRAFTING_RAW_MACHINE_TIER_3),
    TUNGSTEN_STEEL_GEAR(ModTags.TUNGSTEN_STEEL_GEAR),
    TUNGSTEN_STEEL_HULL(ModTags.CRAFTING_RAW_MACHINE_TIER_3),
    TUNGSTEN_STEEL_TURBINE_BLADE(ModTags.CRAFTING_TUNGSTEN_STEEL_TURBINE_BLADE),
    WOLFRAMIUM_GRINDER(ModTags.CRAFTING_GRINDER);

    private final Lazy<Item> instance;
    public final TagKey<Item> tag;

    Component(TagKey<Item> tag) {
        this.tag = tag;

        String name = getName();
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(GtLocale.profileItemDescriptionKey(name))).registryName(name));
    }

    Component(Supplier<Item> constructor, TagKey<Item> tag) {
        this.tag = tag;

        this.instance = Lazy.of(() -> constructor.get().setRegistryName(getName()));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Override
    public TagKey<Item> getTag() {
        return this.tag;
    }
}
