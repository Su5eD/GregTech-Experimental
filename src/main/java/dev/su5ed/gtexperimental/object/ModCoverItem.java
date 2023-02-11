package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.item.CoverItem;
import dev.su5ed.gtexperimental.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public enum ModCoverItem implements TaggedItemProvider {
    ACTIVE_DETECTOR(ModCovers.ACTIVE_DETECTOR, GregTechTags.CRAFTING_WORK_DETECTOR),
    CONVEYOR(ModCovers.CONVEYOR, GregTechTags.CRAFTING_CONVEYOR),
    CRAFTING(ModCovers.CRAFTING, GregTechTags.CRAFTING_WORKBENCH),
    DRAIN(ModCovers.DRAIN, GregTechTags.CRAFTING_DRAIN),
    ENERGY_FLOW_CIRCUIT(ModCovers.ENERGY_ONLY, GregTechTags.CIRCUIT_TIER_7),
    ENERGY_METER(ModCovers.ENERGY_METER, GregTechTags.CRAFTING_ENERGY_METER),
    ITEM_METER(ModCovers.ITEM_METER, GregTechTags.CRAFTING_ITEM_METER),
    ITEM_VALVE(ModCovers.ITEM_VALVE, GregTechTags.CRAFTING_ITEM_VALVE),
    LIQUID_METER(ModCovers.LIQUID_METER, GregTechTags.CRAFTING_LIQUID_METER),
    MACHINE_CONTROLLER(ModCovers.MACHINE_CONTROLLER, GregTechTags.CRAFTING_WORK_CONTROLLER),
    PUMP_MODULE(ModCovers.PUMP_MODULE, GregTechTags.CRAFTING_PUMP),
    REDSTONE_CONDUCTOR(ModCovers.REDSTONE_CONDUCTOR, GregTechTags.CRAFTING_REDSTONE_CONDUCTOR),
    DATA_CONTROL_CIRCUIT(ModCovers.REDSTONE_ONLY, GregTechTags.CIRCUIT_TIER_6),
    REDSTONE_SIGNALIZER(ModCovers.REDSTONE_SIGNALIZER, GregTechTags.CRAFTING_REDSTONE_SIGNALIZER),
    SCREEN(ModCovers.SCREEN, GregTechTags.CRAFTING_MONITOR_TIER_2),
    SOLAR_PANEL(ModCovers.SOLAR_PANEL, GregTechTags.CRAFTING_SOLAR_PANEL),
    SOLAR_PANEL_LV(ModCovers.SOLAR_PANEL_LV, GregTechTags.CRAFTING_SOLAR_PANEL_LV),
    SOLAR_PANEL_MV(ModCovers.SOLAR_PANEL_MV, GregTechTags.CRAFTING_SOLAR_PANEL_MV),
    SOLAR_PANEL_HV(ModCovers.SOLAR_PANEL_HV, GregTechTags.CRAFTING_SOLAR_PANEL_HV);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

    <T> ModCoverItem(RegistryObject<CoverType<T>> cover, TagKey<Item> tag) {
        this.tag = tag;
        this.instance = Lazy.of(() -> new CoverItem<>(new ExtendedItemProperties<>().description(GtLocale.itemDescriptionKey(getName())), cover));
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
