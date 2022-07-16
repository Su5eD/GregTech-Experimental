package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.cover.ModCoverType;
import dev.su5ed.gregtechmod.item.CoverItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum ModCoverItem implements TaggedItemProvider {
    ACTIVE_DETECTOR(GregTechTags.CRAFTING_WORK_DETECTOR),
    CONVEYOR(GregTechTags.CRAFTING_CONVEYOR),
    CRAFTING(GregTechTags.CRAFTING_WORKBENCH),
    DRAIN(GregTechTags.CRAFTING_DRAIN),
    ENERGY_FLOW_CIRCUIT(ModCoverType.ENERGY_ONLY, GregTechTags.CIRCUIT_TIER_0),
    ENERGY_METER(GregTechTags.CRAFTING_ENERGY_METER),
    ITEM_METER(GregTechTags.CRAFTING_ITEM_METER),
    ITEM_VALVE(GregTechTags.CRAFTING_ITEM_VALVE),
    LIQUID_METER(GregTechTags.CRAFTING_LIQUID_METER),
    MACHINE_CONTROLLER(GregTechTags.CRAFTING_WORK_CONTROLLER),
    PUMP_MODULE(GregTechTags.CRAFTING_PUMP),
    REDSTONE_CONDUCTOR(GregTechTags.CRAFTING_REDSTONE_CONDUCTOR),
    DATA_CONTROL_CIRCUIT(ModCoverType.REDSTONE_ONLY, GregTechTags.CIRCUIT_TIER_6),
    REDSTONE_SIGNALIZER(GregTechTags.CRAFTING_REDSTONE_SIGNALIZER),
    SCREEN(GregTechTags.CRAFTING_MONITOR_TIER_2),
    SOLAR_PANEL(GregTechTags.CRAFTING_SOLAR_PANEL),
    SOLAR_PANEL_LV(GregTechTags.CRAFTING_SOLAR_PANEL_LV),
    SOLAR_PANEL_MV(GregTechTags.CRAFTING_SOLAR_PANEL_MV),
    SOLAR_PANEL_HV(GregTechTags.CRAFTING_SOLAR_PANEL_HV);

    private final Lazy<Item> instance;
    public final TagKey<Item> tag;
    
    ModCoverItem(TagKey<Item> tag) {
        this.tag = tag;
        this.instance = createItemInstance(ModCoverType.valueOf(name()).get());
    }

    ModCoverItem(ModCoverType cover, TagKey<Item> tag) {
        this.tag = tag;
        this.instance = createItemInstance(cover.get());
    }
    
    private Lazy<Item> createItemInstance(CoverType provider) {
        return Lazy.of(() -> new CoverItem(new ExtendedItemProperties<>().description(GtLocale.itemDescriptionKey(getName())), provider).registryName(getName()));
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
