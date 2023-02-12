package dev.su5ed.gtexperimental.datagen;

import net.minecraft.SharedConstants;
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
        generator.addProvider(event.includeServer(), new EntityTagsGen(generator, helper));
        generator.addProvider(event.includeServer(), new BiomeTagsGen(generator, helper));
        generator.addProvider(event.includeServer(), new FluidTagsGen(generator, helper));
        generator.addProvider(event.includeServer(), new LootTableGen(generator));
        generator.addProvider(event.includeServer(), new LootModifierProvider(generator));
        generator.addProvider(event.includeServer(), new RecipeGen(generator));
        // Always run item tags last
        generator.addProvider(event.includeServer(), new ItemTagsGen(generator, blockTags, helper));

        generator.addProvider(event.includeClient(), new BlockStateGen(generator, helper));
        generator.addProvider(event.includeClient(), new ItemModelGen(generator, helper));
        
        DataGenerator plateRecipePackGenerator = new DataGenerator(generator.getOutputFolder().resolve("packs/plate_recipes"), generator.getInputFolders(), SharedConstants.getCurrentVersion(), false);
        generator.addProvider(event.includeServer(), new PlateRecipePackGen(plateRecipePackGenerator));
    }
    
    private DataGenerators() {}
}
