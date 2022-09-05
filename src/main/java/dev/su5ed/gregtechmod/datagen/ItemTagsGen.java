package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Component;
import dev.su5ed.gregtechmod.object.Dust;
import dev.su5ed.gregtechmod.object.Ingot;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.object.ModCoverItem;
import dev.su5ed.gregtechmod.object.Nugget;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.object.Plate;
import dev.su5ed.gregtechmod.object.Rod;
import dev.su5ed.gregtechmod.object.Tool;
import dev.su5ed.gregtechmod.object.Upgrade;
import dev.su5ed.gregtechmod.util.ItemProvider;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTagsGen extends ItemTagsProvider {

    public ItemTagsGen(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        Map<TagKey<Item>, TagAppender<Item>> tags = new HashMap<>();
        StreamEx.<TaggedItemProvider>of(Component.values())
            .append(Dust.values())
            .append(Ingot.values())
            .append(Miscellaneous.values())
            .append(ModCoverItem.values())
            .append(Nugget.values())
            .append(Ore.values())
            .append(Plate.values())
            .append(Rod.values())
            .append(Tool.values())
            .append(Upgrade.values())
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
            Tags.Items.RODS, Rod.values()
        )
            .mapKeys(this::tag)
            .flatMapValues(providers -> StreamEx.of(providers)
                .map(TaggedItemProvider::getTag)
                .nonNull())
            .forKeyValue(TagAppender::addTag);

        StreamEx.of(GregTechTags.HEAT_VENT, GregTechTags.COMPONENT_HEAT_VENT, GregTechTags.ADVANCED_HEAT_VENT, GregTechTags.OVERCLOCKED_HEAT_VENT)
            .mapToEntry(this::tag, tag -> getAllBaseModItems(tag.location().getPath()))
            .flatMapValues(Collection::stream)
            .forKeyValue(TagAppender::addOptional);
        
        tag(GregTechTags.EMPTY_FLUID_CELL)
            .addOptional(new ResourceLocation(ModHandler.IC2_MODID, "empty_cell"));
        
        tag(GregTechTags.EMPTY_FUEL_CAN)
            .addOptional(new ResourceLocation(ModHandler.IC2_MODID, "empty_fuel_can"));
        
        // Tag IC2 plates
        StreamEx.of("bronze_plate", "copper_plate", "gold_plate", "iron_plate",
            "lapis_plate", "lead_plate", "obsidian_plate", "steel_plate", "tin_plate"
        )
            .mapToEntry(name -> tag(GregTechTags.plate(name.replace("_plate", ""))), name -> new ResourceLocation(ModHandler.IC2_MODID, name))
            .forKeyValue(TagAppender::addOptional);
        
        tag(GregTechTags.IRIDIUM_ALLOY)
            .addOptional(new ResourceLocation(ModHandler.IC2_MODID, "iridium"));
    }

    private static List<ResourceLocation> getAllBaseModItems(String name) {
        return StreamEx.of(ModHandler.BASE_MODS)
            .map(modid -> new ResourceLocation(modid, name))
            .toList();
    }

    @Override
    public String getName() {
        return Reference.NAME + " Item Tags";
    }
}
