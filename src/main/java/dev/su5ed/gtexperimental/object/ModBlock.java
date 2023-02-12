package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.block.ConnectedBlock;
import dev.su5ed.gtexperimental.block.ResourceBlock;
import dev.su5ed.gtexperimental.util.GtUtil;
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

import java.util.function.Supplier;

public enum ModBlock implements TaggedBlockProvider, TaggedItemProvider {
    ADVANCED_MACHINE_CASING(() -> new ConnectedBlock(3, 30)),
    ALUMINIUM(3, 30),
    BRASS(3.5F, 30),
    CHROME(10, 100),
    ELECTRUM(4, 30),
    FUSION_COIL(4, 30),
    GREEN_SAPPHIRE(4.5F, 30),
    HIGHLY_ADVANCED_MACHINE(10, 100),
    INVAR(4.5F, 30),
    IRIDIUM(3.5F, 600),
    IRIDIUM_REINFORCED_STONE(100, 300),
    IRIDIUM_REINFORCED_TUNGSTEN_STEEL(() -> new ConnectedBlock(200, 400)),
    LEAD(3, 60),
    LESUBLOCK(4, 30),
    NICKEL(3, 45),
    OLIVINE(4.5F, 30),
    OSMIUM(4, 900),
    PLATINUM(4, 30),
    REINFORCED_MACHINE_CASING(() -> new ConnectedBlock(3, 60)),
    RUBY(4.5F, 30),
    SAPPHIRE(4.5F, 30),
    SILVER(3, 30),
    STANDARD_MACHINE_CASING(() -> new ConnectedBlock(3, 30)),
    STEEL(3, 100),
    TITANIUM(10, 200),
    TUNGSTEN(4.5F, 100),
    TUNGSTEN_STEEL(() -> new ConnectedBlock(100, 300)),
    ZINC(3.5F, 30);

    private final Lazy<Block> block;
    private final Lazy<Item> item;
    private final TagKey<Item> itemTag;
    private final TagKey<Block> blockTag;

    ModBlock(float strength, float resistance) {
        this(() -> new ResourceBlock(strength, resistance));
    }

    ModBlock(Supplier<Block> block) {
        this.block = Lazy.of(block);
        this.item = Lazy.of(() -> new BlockItem(getBlock(), ModObjects.itemProperties()));

        ResourceLocation tagName = new ResourceLocation("forge", "storage_blocks/" + getName());
        this.itemTag = ItemTags.create(tagName);
        this.blockTag = BlockTags.create(tagName);
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "block");
    }

    @Override
    public Block getBlock() {
        return this.block.get();
    }

    @Override
    public Item getItem() {
        return this.item.get();
    }

    @Override
    public TagKey<Item> getTag() {
        return this.itemTag;
    }

    @Override
    public TagKey<Block> getBlockTag() {
        return this.blockTag;
    }
}
