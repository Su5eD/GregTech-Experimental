package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

public class EntityTagsGen extends EntityTypeTagsProvider {

    public EntityTagsGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagAppender<EntityType<?>> screwdriverEffective = tag(GregTechTags.SCREWDRIVER_EFFECTIVE)
            .add(EntityType.SPIDER, EntityType.CAVE_SPIDER);
        StreamEx.of("hedge_spider", "king_spider", "swarm_spider", "carminite_broodling")
            .map(name -> new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, name))
            .forEach(screwdriverEffective::addOptional);

        tag(GregTechTags.WRENCH_EFFECTIVE)
            .add(EntityType.IRON_GOLEM)
            .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "carminite_golem"));
    }
}
