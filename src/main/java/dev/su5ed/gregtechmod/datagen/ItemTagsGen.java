package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Component;
import dev.su5ed.gregtechmod.object.ModCoverItem;
import dev.su5ed.gregtechmod.object.Ore;
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
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTagsGen extends ItemTagsProvider {

    public ItemTagsGen(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, pBlockTagsProvider, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagAppender<Item> ores = tag(Tags.Items.ORES);

        StreamEx.of(Ore.values())
            .map(Ore::getItem)
            .forEach(ores::add);

        Map<TagKey<Item>, TagAppender<Item>> tags = new HashMap<>();
        StreamEx.<TaggedItemProvider>of(Component.values())
            .append(ModCoverItem.values())
            .append(Tool.values())
            .append(Upgrade.values())
            .mapToEntry(TaggedItemProvider::getTag, ItemProvider::getItem)
            .nonNullKeys()
            .mapKeys(tag -> tags.computeIfAbsent(tag, this::tag))
            .forKeyValue(TagAppender::add);

        StreamEx.of(GregTechTags.HEAT_VENT, GregTechTags.COMPONENT_HEAT_VENT, GregTechTags.ADVANCED_HEAT_VENT, GregTechTags.OVERCLOCKED_HEAT_VENT)
            .mapToEntry(this::tag, tag -> getAllBaseModItems(tag.location().getPath()))
            .flatMapValues(Collection::stream)
            .forKeyValue(TagAppender::addOptional);
        
        tag(GregTechTags.EMPTY_FLUID_CELL)
            .addOptional(new ResourceLocation(ModHandler.IC2_MODID, "empty_cell"));
        
        tag(GregTechTags.EMPTY_FUEL_CAN)
            .addOptional(new ResourceLocation(ModHandler.IC2_MODID, "empty_fuel_can"));
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
