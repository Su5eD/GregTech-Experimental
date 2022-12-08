package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.util.Reference;
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
    
    public BiomeTagsGen(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, Reference.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags() {
        TagAppender<Biome> placeBauxite = tag(GregTechTags.PLACE_BAUXITE)
            .addTags(Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_TAIGA);
        StreamEx.of("rainforest", "highland", "pasture", "marsh", "grassland")
            .map(name -> new ResourceLocation("biomesoplenty", name))
            .forEach(placeBauxite::addOptional);
    }
}
