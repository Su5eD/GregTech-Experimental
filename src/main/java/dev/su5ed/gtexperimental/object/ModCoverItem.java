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
    ACTIVE_DETECTOR(ModCovers.ACTIVE_DETECTOR),
    CONVEYOR(ModCovers.CONVEYOR),
    CRAFTING(ModCovers.CRAFTING),
    DRAIN(ModCovers.DRAIN),
    ENERGY_FLOW_CIRCUIT(ModCovers.ENERGY_ONLY, GregTechTags.itemTag("energy_flow_circuit")),
    ENERGY_METER(ModCovers.ENERGY_METER),
    ITEM_METER(ModCovers.ITEM_METER),
    ITEM_VALVE(ModCovers.ITEM_VALVE),
    LIQUID_METER(ModCovers.LIQUID_METER),
    MACHINE_CONTROLLER(ModCovers.MACHINE_CONTROLLER),
    PUMP_MODULE(ModCovers.PUMP_MODULE),
    REDSTONE_CONDUCTOR(ModCovers.REDSTONE_CONDUCTOR),
    DATA_CONTROL_CIRCUIT(ModCovers.REDSTONE_ONLY, GregTechTags.itemTag("data_control_circuit")),
    REDSTONE_SIGNALIZER(ModCovers.REDSTONE_SIGNALIZER),
    SCREEN(ModCovers.SCREEN),
    SOLAR_PANEL(ModCovers.SOLAR_PANEL, GregTechTags.SOLAR_GENERATOR),
    SOLAR_PANEL_LV(ModCovers.SOLAR_PANEL_LV),
    SOLAR_PANEL_MV(ModCovers.SOLAR_PANEL_MV),
    SOLAR_PANEL_HV(ModCovers.SOLAR_PANEL_HV);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

    <T> ModCoverItem(RegistryObject<CoverType<T>> cover) {
        this(cover, null);
    }
    
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
