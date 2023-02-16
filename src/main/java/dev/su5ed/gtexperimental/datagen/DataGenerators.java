package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.datagen.pack.ClassicIC2RecipesPackGen;
import dev.su5ed.gtexperimental.datagen.pack.ExperimentalIC2RecipesPackGen;
import dev.su5ed.gtexperimental.datagen.pack.FTBICRecipesPackGen;
import dev.su5ed.gtexperimental.datagen.pack.HarderRecipesPackGen;
import dev.su5ed.gtexperimental.datagen.pack.PlateRecipesPackGen;
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

        DataGenerator plateRecipesPackGenerator = createGenerator(generator, "plate_recipes");
        generator.addProvider(event.includeServer(), new PlateRecipesPackGen(plateRecipesPackGenerator));

        DataGenerator harderRecipesPackGenerator = createGenerator(generator, "harder_recipes");
        generator.addProvider(event.includeServer(), new HarderRecipesPackGen(harderRecipesPackGenerator));

        DataGenerator classicIc2PackGenerator = createGenerator(generator, ClassicIC2RecipesPackGen.NAME);
        generator.addProvider(event.includeServer(), new ClassicIC2RecipesPackGen(classicIc2PackGenerator));

        DataGenerator experimentalIc2PackGenerator = createGenerator(generator, ExperimentalIC2RecipesPackGen.NAME);
        generator.addProvider(event.includeServer(), new ExperimentalIC2RecipesPackGen(experimentalIc2PackGenerator));

        DataGenerator ftbicPackGenerator = createGenerator(generator, FTBICRecipesPackGen.NAME);
        generator.addProvider(event.includeServer(), new FTBICRecipesPackGen(ftbicPackGenerator));
    }

    private DataGenerators() {}

    private static DataGenerator createGenerator(DataGenerator generator, String name) {
        return new DataGenerator(generator.getOutputFolder().resolve("packs/" + name), generator.getInputFolders(), SharedConstants.getCurrentVersion(), false);
    }
}
