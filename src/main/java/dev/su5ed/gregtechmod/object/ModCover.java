package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.ModTags;
import dev.su5ed.gregtechmod.api.cover.ICoverProvider;
import dev.su5ed.gregtechmod.cover.Cover;
import dev.su5ed.gregtechmod.item.CoverItem;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum ModCover implements ItemProvider {
    ACTIVE_DETECTOR(ModTags.CRAFTING_WORK_DETECTOR),
    CONVEYOR(ModTags.CRAFTING_CONVEYOR),
    CRAFTING(ModTags.CRAFTING_WORKBENCH),
    DRAIN(ModTags.CRAFTING_DRAIN),
    ENERGY_FLOW_CIRCUIT(Cover.ENERGY_ONLY, ModTags.CIRCUIT_TIER_0),
    ENERGY_METER(ModTags.CRAFTING_ENERGY_METER),
    ITEM_METER(ModTags.CRAFTING_ITEM_METER),
    ITEM_VALVE(ModTags.CRAFTING_ITEM_VALVE),
    LIQUID_METER(ModTags.CRAFTING_LIQUID_METER),
    MACHINE_CONTROLLER(ModTags.CRAFTING_WORK_CONTROLLER),
    PUMP_MODULE(ModTags.CRAFTING_PUMP),
    REDSTONE_CONDUCTOR(ModTags.CRAFTING_REDSTONE_CONDUCTOR),
    DATA_CONTROL_CIRCUIT(Cover.REDSTONE_ONLY, ModTags.CIRCUIT_TIER_6),
    REDSTONE_SIGNALIZER(ModTags.CRAFTING_REDSTONE_SIGNALIZER),
    SCREEN(ModTags.CRAFTING_MONITOR_TIER_2),
    SOLAR_PANEL(ModTags.CRAFTING_SOLAR_PANEL),
    SOLAR_PANEL_LV(ModTags.CRAFTING_SOLAR_PANEL_LV),
    SOLAR_PANEL_MV(ModTags.CRAFTING_SOLAR_PANEL_MV),
    SOLAR_PANEL_HV(ModTags.CRAFTING_SOLAR_PANEL_HV);

    private final Lazy<Item> instance;
    public final Tag<Item> tag;
    
    ModCover(Tag<Item> tag) {
        this.tag = tag;
        this.instance = createItemInstance(Cover.valueOf(name()).getInstance());
    }

    ModCover(Cover cover, Tag<Item> tag) {
        this.tag = tag;
        this.instance = createItemInstance(cover.getInstance());
    }
    
    private Lazy<Item> createItemInstance(ICoverProvider provider) {
        return Lazy.of(() -> new CoverItem(ModObjects.DEFAULT_ITEM_PROPERTIES, GtLocale.itemDescriptionKey(getName()).toComponent(), provider).registryName(getName()));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
