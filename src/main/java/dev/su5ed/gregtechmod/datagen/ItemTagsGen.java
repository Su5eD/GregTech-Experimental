package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.Ore;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;

public class ItemTagsGen extends ItemTagsProvider {

    public ItemTagsGen(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, pBlockTagsProvider, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagAppender<Item> ores = tag(Tags.Items.ORES);

        StreamEx.of(Ore.values())
            .map(Ore::getItem)
            .forEach(ores::add);
    }

    @Override
    public String getName() {
        return Reference.NAME + " Item Tags";
    }
}
