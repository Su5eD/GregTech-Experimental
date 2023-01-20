package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.block.OreBlock;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.HarvestLevel;
import dev.su5ed.gtexperimental.util.TaggedBlockProvider;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Ore implements TaggedBlockProvider, TaggedItemProvider {
    GALENA(3),
    IRIDIUM(20, HarvestLevel.DIAMOND),
    RUBY(4, HarvestLevel.IRON),
    SAPPHIRE(4, HarvestLevel.IRON),
    BAUXITE(3),
    PYRITE(2),
    CINNABAR(3, HarvestLevel.IRON),
    SPHALERITE(2),
    TUNGSTATE(4, HarvestLevel.IRON),
    SHELDONITE(3.5F, HarvestLevel.DIAMOND),
    OLIVINE(3, HarvestLevel.DIAMOND),
    SODALITE(3, HarvestLevel.IRON),
    TETRAHEDRITE(3, HarvestLevel.IRON),
    CASSITERITE(3, HarvestLevel.IRON);

    private final Lazy<Block> block;
    private final Lazy<Item> item;
    private final TagKey<Item> itemTag;
    private final TagKey<Block> blockTag;
    private final HarvestLevel harvestLevel;

    Ore(float strength) {
        this(strength, HarvestLevel.STONE);
    }

    Ore(float strength, HarvestLevel harvestLevel) {
        this(() -> new OreBlock(strength), harvestLevel);
    }

    Ore(Supplier<Block> block, HarvestLevel harvestLevel) {
        this.block = Lazy.of(block);
        this.item = Lazy.of(() -> new BlockItem(getBlock(), ModObjects.itemProperties()));
        this.harvestLevel = harvestLevel;

        ResourceLocation tagName = new ResourceLocation("forge", "ores/" + getName());
        this.itemTag = ItemTags.create(tagName);
        this.blockTag = BlockTags.create(tagName);
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName() + "_ore");
    }

    @Override
    public Block getBlock() {
        return this.block.get();
    }

    @Override
    public Item getItem() {
        return this.item.get();
    }

    @Nullable
    @Override
    public TagKey<Item> getTag() {
        return this.itemTag;
    }

    @Override
    public TagKey<Block> getBlockTag() {
        return this.blockTag;
    }

    public HarvestLevel getHarvestLevel() {
        return this.harvestLevel;
    }
}
