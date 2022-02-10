package dev.su5ed.gregtechmod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public final class DataGenerators {
    public static final DataGenerators INSTANCE = new DataGenerators();

    private DataGenerators() {}

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeServer()) {
            generator.addProvider(new BlockTagsGen(generator, helper));
            generator.addProvider(new LootTableGen(generator));
        }

        if (event.includeClient()) {
            generator.addProvider(new BlockStateGen(generator, helper));
            generator.addProvider(new ItemModelGen(generator, helper));
        }
    }
}
