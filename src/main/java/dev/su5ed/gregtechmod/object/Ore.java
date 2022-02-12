package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.block.OreBlock;
import dev.su5ed.gregtechmod.util.BlockProvider;
import dev.su5ed.gregtechmod.util.HarvestLevel;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;

import java.util.Locale;
import java.util.function.Supplier;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public enum Ore implements BlockProvider, ItemProvider {
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
    private final HarvestLevel harvestLevel;
    
    Ore(float strength) {
        this(strength, HarvestLevel.STONE);
    }

    Ore(float strength, HarvestLevel harvestLevel) {
        this(() -> new OreBlock(strength), harvestLevel);
    }

    Ore(Supplier<Block> block, HarvestLevel harvestLevel) {
        ResourceLocation name = location(getName() + "_ore");
        this.block = Lazy.of(() -> block.get().setRegistryName(name));
        this.item = Lazy.of(() -> new BlockItem(getBlock(), ModObjects.ITEM_PROPERTIES).setRegistryName(name));
        this.harvestLevel = harvestLevel;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Block getBlock() {
        return this.block.get();
    }

    @Override
    public Item getItem() {
        return this.item.get();
    }

    public HarvestLevel getHarvestLevel() {
        return this.harvestLevel;
    }
}
