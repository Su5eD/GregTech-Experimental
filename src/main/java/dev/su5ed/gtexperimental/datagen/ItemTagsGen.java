package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Armor;
import dev.su5ed.gtexperimental.object.ColorSpray;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Rod;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.object.Upgrade;
import dev.su5ed.gtexperimental.util.ItemProvider;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemTagsGen extends ItemTagsProvider {

    public ItemTagsGen(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        Map<TagKey<Item>, TagAppender<Item>> tags = new HashMap<>();

        GregTechTags.getAllMaterials().forEach(key -> tags.put(key, tag(key)));

        StreamEx.<TaggedItemProvider>of(Component.values())
            .append(Dust.values())
            .append(Smalldust.values())
            .append(Ingot.values())
            .append(Miscellaneous.values())
            .append(ModCoverItem.values())
            .append(NuclearCoolantPack.values())
            .append(Nugget.values())
            .append(Ore.values())
            .append(Plate.values())
            .append(Rod.values())
            .append(Tool.values())
            .append(Upgrade.values())
            .append(Armor.values())
            .mapToEntry(TaggedItemProvider::getTag, ItemProvider::getItem)
            .nonNullKeys()
            .mapKeys(tag -> tags.computeIfAbsent(tag, this::tag))
            .forKeyValue(TagAppender::add);

        EntryStream.of(
                Tags.Items.ORES, Ore.values(),
                Tags.Items.DUSTS, Dust.values(),
                Tags.Items.INGOTS, Ingot.values(),
                Tags.Items.NUGGETS, Nugget.values(),
                GregTechTags.PLATES, Plate.values(),
                Tags.Items.RODS, Rod.values(),
                Tags.Items.DYES, ColorSpray.values()
            )
            .mapKeys(this::tag)
            .flatMapValues(providers -> StreamEx.of(providers)
                .map(TaggedItemProvider::getTag)
                .nonNull())
            .forKeyValue(TagAppender::addTag);

        StreamEx.of(GregTechTags.HEAT_VENT, GregTechTags.COMPONENT_HEAT_VENT, GregTechTags.ADVANCED_HEAT_VENT, GregTechTags.OVERCLOCKED_HEAT_VENT)
            .mapToEntry(this::tag, tag -> ModHandler.getAliasedModItems(tag.location().getPath()).values())
            .flatMapValues(Collection::stream)
            .forKeyValue(TagAppender::addOptional);

        modItem(GregTechTags.EMPTY_FLUID_CELL, "empty_cell");
        modItem(GregTechTags.EMPTY_FUEL_CAN, "empty_fuel_can");
        modItem(GregTechTags.ADVANCED_CIRCUIT, "advanced_circuit");
        modItem(GregTechTags.CIRCUIT, "circuit");
        modItem(GregTechTags.INSULATED_COPPER_CABLE, "insulated_copper_cable");
        modItem(GregTechTags.COPPER_CABLE, "copper_cable");
        modItem(GregTechTags.GOLD_CABLE, "gold_cable");
        modItem(GregTechTags.INSULATED_GOLD_CABLE, "insulated_gold_cable");
        modItem(GregTechTags.material("plates", "carbon"), "carbon_plate");
        modItem(GregTechTags.ADVANCED_ALLOY, "alloy");
        modItem(GregTechTags.REINFORCED_STONE, "reinforced_stone");
        modItem(GregTechTags.RESIN, "resin");
        modItem(GregTechTags.RUBBER, "rubber");
        modItem(GregTechTags.LAPOTRON_CRYSTAL, "lapotron_crystal");
        modItem(GregTechTags.HV_TRANSFORMER, "hv_transformer");
        modItem(GregTechTags.TRANSFORMER_UPGRADE, "transformer_upgrade");
        modItem(GregTechTags.CARBON_MESH, "carbon_mesh");
        modItem(GregTechTags.CARBON_FIBRE, "carbon_fibre");
        modItem(GregTechTags.GENERATOR, "generator");
        modItem(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1, "machine");
        modItem(GregTechTags.COAL_BALL, "coal_ball");
        modItem(GregTechTags.COMPRESSED_COAL_BALL, "coal_block");
        modItem(GregTechTags.COAL_CHUNK, "coal_chunk");
        modItem(GregTechTags.IRIDIUM_ALLOY, "iridium");
        modItem(GregTechTags.CARBON_PLATE, "carbon_plate");
        modItem(GregTechTags.PUMP, "pump");
        modItem(GregTechTags.ENERGY_CRYSTAL, "energy_crystal");
        modItem(GregTechTags.material("ingots", "mixed_metal"), "mixed_metal_ingot");
        modItem(GregTechTags.material("ingots", "refined_iron"), "refined_iron_ingot");
        tag(GregTechTags.OIL_SAND)
            .addOptional(new ResourceLocation(ModHandler.THERMAL_MODID, "oil_sand"));
        tag(GregTechTags.stone("quarried"))
            .addOptionalTag(new ResourceLocation(ModHandler.RAILCRAFT_MODID, "quarried"));

        tag(GregTechTags.COLORED_WOOL)
            .add(Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL, Items.LIME_WOOL,
                Items.PINK_WOOL, Items.GRAY_WOOL, Items.LIGHT_GRAY_WOOL, Items.CYAN_WOOL, Items.PURPLE_WOOL,
                Items.BLUE_WOOL, Items.BROWN_WOOL, Items.GREEN_WOOL, Items.RED_WOOL, Items.BLACK_WOOL);

        tag(GregTechTags.RAW_FOOD)
            .add(Items.CHICKEN, Items.BEEF, Items.PORKCHOP, Items.MUTTON, Items.COD, Items.RABBIT, Items.SALMON)
            .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "raw_meef"))
            .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "raw_venison"));
        tag(GregTechTags.COOKED_FOOD)
            .add(Items.COOKED_CHICKEN, Items.COOKED_BEEF, Items.COOKED_PORKCHOP, Items.COOKED_MUTTON, Items.COOKED_COD, Items.COOKED_RABBIT, Items.COOKED_SALMON)
            .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "cooked_meef"))
            .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "cooked_venison"));
    }

    @Override
    public String getName() {
        return Reference.NAME + super.getName();
    }

    private void modItem(TagKey<Item> key, String name) {
        TagAppender<Item> appender = tag(key);
        ModHandler.getAliasedModItems(name).values().forEach(appender::addOptional);
    }
}
