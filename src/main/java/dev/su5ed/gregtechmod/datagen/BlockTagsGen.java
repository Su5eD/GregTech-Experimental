package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.ModBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.List;

class BlockTagsGen extends BlockTagsProvider {
    
    public BlockTagsGen(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        List<TagsProvider.TagAppender<Block>> tags = StreamEx.of(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
            .map(this::tag)
            .toList();

        StreamEx.of(ModBlock.values())
            .map(ModBlock::getBlock)
            .forEach(block -> tags.forEach(tag -> tag.add(block)));
    }
}
