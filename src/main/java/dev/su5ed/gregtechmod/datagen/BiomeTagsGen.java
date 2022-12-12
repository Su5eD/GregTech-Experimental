package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BiomeTagsGen extends BiomeTagsProvider {
    
    public BiomeTagsGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags() {
        tag(GregTechTags.PLACE_BAUXITE)
            .addTags(Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL);

        tag(GregTechTags.PLACE_RUBY)
            .addTags(Tags.Biomes.IS_DESERT, BiomeTags.IS_SAVANNA, Tags.Biomes.IS_WASTELAND, BiomeTags.IS_BADLANDS)
            .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "fire_swamp"));
        
        tag(GregTechTags.PLACE_SAPPHIRE)
            .addTags(BiomeTags.IS_OCEAN, BiomeTags.IS_BEACH);

        tag(GregTechTags.PLACE_TETRAHEDRITE)
            .addTags(BiomeTags.IS_JUNGLE, Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_MUSHROOM, BiomeTags.IS_MOUNTAIN);

        tag(GregTechTags.PLACE_CASSITERITE)
            .addTags(BiomeTags.IS_TAIGA, Tags.Biomes.IS_MUSHROOM, BiomeTags.IS_MOUNTAIN);
    }
}
