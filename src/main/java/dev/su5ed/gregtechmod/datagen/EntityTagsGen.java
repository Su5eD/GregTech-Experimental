package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.Reference;
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
        addOptional(screwdriverEffective, ModHandler.TWILIGHT_FOREST_MODID,
            "hedge_spider", "king_spider", "swarm_spider", "carminite_broodling");

        StreamEx.of(GregTechTags.WRENCH_EFFECTIVE, GregTechTags.HARD_HAMMER_EFFECTIVE)
            .forEach(tagKey -> tag(tagKey)
                .add(EntityType.IRON_GOLEM)
                .addOptional(new ResourceLocation(ModHandler.TWILIGHT_FOREST_MODID, "carminite_golem")));

        TagAppender<EntityType<?>> bugSprayEffective = tag(GregTechTags.BUG_SPRAY_EFFECTIVE)
            .add(EntityType.SPIDER, EntityType.CAVE_SPIDER);
        addOptional(bugSprayEffective, ModHandler.TWILIGHT_FOREST_MODID,
            "hedge_spider", "king_spider", "swarm_spider", "carminite_broodling", "fire_beetle", "slime_beetle");
        
        TagAppender<EntityType<?>> iceSprayEffective = tag(GregTechTags.ICE_SPRAY_EFFECTIVE)
            .addOptionalTag(GregTechTags.SLIMES.location());
        addOptional(iceSprayEffective, ModHandler.TWILIGHT_FOREST_MODID, "fire_beetle", "maze_slime", "slime_beetle");
    }

    @Override
    public String getName() {
        return Reference.NAME + " Entity Tags";
    }

    private static void addOptional(TagAppender<EntityType<?>> tag, String modid, String... names) {
        StreamEx.of(names)
            .map(name -> new ResourceLocation(modid, name))
            .forEach(tag::addOptional);
    }
}
