package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.ModBlock;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.util.HarvestLevel;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

class BlockTagsGen extends BlockTagsProvider {

    public BlockTagsGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagAppender<Block> pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        TagAppender<Block> ores = tag(Tags.Blocks.ORES);
        Map<HarvestLevel, TagAppender<Block>> harvestLevels = StreamEx.of(HarvestLevel.values())
            .mapToEntry(level -> tag(level.getTag()))
            .toMap();

        StreamEx.of(ModBlock.values())
            .map(ModBlock::getBlock)
            .forEach(block -> {
                pickaxe.add(block);
                harvestLevels.get(HarvestLevel.IRON).add(block);
            });

        StreamEx.of(Ore.values())
            .forEach(ore -> {
                Block block = ore.getBlock();
                HarvestLevel level = ore.getHarvestLevel();
                TagKey<Block> tag = ore.getBlockTag();

                pickaxe.add(block);
                harvestLevels.get(level).add(block);
                ores.addTag(tag);
                tag(tag).add(block);
            });

        tag(GregTechTags.MINEABLE_WITH_SHEARS)
            .addTag(BlockTags.LEAVES)
            .addTag(BlockTags.WOOL)
            .add(Blocks.COBWEB, Blocks.GRASS, Blocks.FERN, Blocks.DEAD_BUSH, Blocks.HANGING_ROOTS, Blocks.VINE, Blocks.TRIPWIRE);

        tag(GregTechTags.SUBMERGE_ORE_LAVA)
            .add(Blocks.LAVA);
    }

    @Override
    public String getName() {
        return Reference.NAME + super.getName();
    }
}
