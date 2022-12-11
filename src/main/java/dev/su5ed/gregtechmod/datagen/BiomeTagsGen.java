package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

public class BiomeTagsGen extends BiomeTagsProvider {
    
    public BiomeTagsGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags() {
        TagAppender<Biome> placeBauxite = tag(GregTechTags.PLACE_BAUXITE)
            .addTags(Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_TAIGA);
        StreamEx.of("rainforest", "highland", "pasture", "marsh", "grassland")
            .map(name -> new ResourceLocation(ModHandler.BIOMESOP_MODID, name))
            .forEach(placeBauxite::addOptional);

        TagAppender<Biome> placeRuby = tag(GregTechTags.PLACE_RUBY)
            .addTags(Tags.Biomes.IS_DESERT, BiomeTags.IS_SAVANNA, Tags.Biomes.IS_WASTELAND, BiomeTags.IS_BADLANDS);
        placeRuby.addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "fire_swamp"));
    }
}
