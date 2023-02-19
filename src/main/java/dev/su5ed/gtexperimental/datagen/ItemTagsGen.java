package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Armor;
import dev.su5ed.gtexperimental.object.ColorSpray;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.File;
import dev.su5ed.gtexperimental.object.Hammer;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModBlock;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Rod;
import dev.su5ed.gtexperimental.object.Saw;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.object.Upgrade;
import dev.su5ed.gtexperimental.object.Wrench;
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

public class ItemTagsGen extends ItemTagsProvider {

    public ItemTagsGen(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        GregTechTags.getAllMaterials().forEach(this::tag);

        StreamEx.<TaggedItemProvider>of(Armor.values())
            .append(ColorSpray.values())
            .append(Component.values())
            .append(Dust.values())
            .append(File.values())
            .append(Hammer.values())
            .append(Ingot.values())
            .append(Miscellaneous.values())
            .append(ModCoverItem.values())
            .append(NuclearCoolantPack.values())
            .append(Nugget.values())
            .append(Ore.values())
            .append(ModBlock.values())
            .append(Plate.values())
            .append(Rod.values())
            .append(Saw.values())
            .append(Smalldust.values())
            .append(Tool.values())
            .append(Upgrade.values())
            .append(Wrench.values())
            .mapToEntry(TaggedItemProvider::getTag, ItemProvider::getItem)
            .nonNullKeys()
            .mapKeys(this::tag)
            .forKeyValue(TagAppender::add);

        EntryStream.of(
                Tags.Items.ORES, Ore.values(),
                Tags.Items.DUSTS, Dust.values(),
                GregTechTags.SMALL_DUSTS, Smalldust.values(),
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

        tag(GregTechTags.SMALL_EU_STORE)
            .add(Component.LITHIUM_RE_BATTERY.asItem());
        tag(GregTechTags.CRAFTING_RAW_MACHINE_TIER_4)
            .add(ModBlock.HIGHLY_ADVANCED_MACHINE.asItem());
        tag(GregTechTags.COLORED_WOOL)
            .add(Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL, Items.LIME_WOOL,
                Items.PINK_WOOL, Items.GRAY_WOOL, Items.LIGHT_GRAY_WOOL, Items.CYAN_WOOL, Items.PURPLE_WOOL,
                Items.BLUE_WOOL, Items.BROWN_WOOL, Items.GREEN_WOOL, Items.RED_WOOL, Items.BLACK_WOOL);
        tag(GregTechTags.RAW_FOOD)
            .add(Items.CHICKEN, Items.BEEF, Items.PORKCHOP, Items.MUTTON, Items.COD, Items.RABBIT, Items.SALMON);
        tag(GregTechTags.COOKED_FOOD)
            .add(Items.COOKED_CHICKEN, Items.COOKED_BEEF, Items.COOKED_PORKCHOP, Items.COOKED_MUTTON, Items.COOKED_COD, Items.COOKED_RABBIT, Items.COOKED_SALMON);

        tag(GregTechTags.UNIVERSAL_IRON_INGOT).addTag(Tags.Items.INGOTS_IRON);
        tag(GregTechTags.UNIVERSAL_IRON_PLATE).addTag(Plate.IRON.getTag());
        tag(GregTechTags.UNIVERSAL_IRON_ROD).addTag(Rod.IRON.getTag());
        tag(GregTechTags.ANY_IRON_INGOT)
            .addTag(Tags.Items.INGOTS_IRON)
            .addTag(GregTechTags.UNIVERSAL_IRON_INGOT);

        // Mods
        tag(GregTechTags.CRAFTING_RAW_MACHINE_TIER_0)
            .addOptional(new ResourceLocation(ModHandler.THERMAL_MODID, "machine_frame"));
        tag(GregTechTags.OIL_SAND)
            .addOptional(new ResourceLocation(ModHandler.THERMAL_MODID, "oil_sand"));
        tag(GregTechTags.stone("quarried"))
            .addOptionalTag(new ResourceLocation(ModHandler.RAILCRAFT_MODID, "quarried"));
        tag(GregTechTags.RAW_FOOD)
            .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "raw_meef"))
            .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "raw_venison"));
        tag(GregTechTags.COOKED_FOOD)
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
