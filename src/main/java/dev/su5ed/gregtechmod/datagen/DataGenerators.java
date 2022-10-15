package dev.su5ed.gregtechmod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        BlockTagsProvider blockTags = new BlockTagsGen(generator, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTagsGen(generator, blockTags, helper));
        generator.addProvider(event.includeServer(), new EntityTagsGen(generator, helper));
        generator.addProvider(event.includeServer(), new LootTableGen(generator));
        generator.addProvider(event.includeServer(), new LootModifierProvider(generator));

        generator.addProvider(event.includeClient(), new BlockStateGen(generator, helper));
        generator.addProvider(event.includeClient(), new ItemModelGen(generator, helper));
    }
    
    private DataGenerators() {}
}
