package dev.su5ed.gregtechmod.util;

import com.google.common.base.Strings;
import dev.su5ed.gregtechmod.GregTechMod;
import ic2.api.crops.CropCard;
import ic2.api.crops.CropProperties;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.IntStreamEx;

import java.util.ArrayList;
import java.util.List;

public class GtCropCard extends CropCard {
    private final String id;
    private final String owner;
    private final String discoveredBy;
    private final CropProperties properties;
    private final String[] attributes;
    private final int maxSize;
    private final ItemStack[] drops;
    private final ItemStack[] specialDrops;
    private final int growthSpeed;
    private final int harvestSize;
    private final int afterHarvestSize;
    private final List<BaseSeed> baseSeeds;

    public GtCropCard(CropBuilder builder) {
        this.id = builder.id;
        this.owner = builder.owner;
        this.discoveredBy = builder.discoveredBy;
        this.properties = builder.properties;
        this.attributes = builder.attributes;
        this.maxSize = builder.maxSize;
        this.drops = builder.drops;
        this.specialDrops = builder.specialDrops;
        this.growthSpeed = builder.growthSpeed;
        this.harvestSize = builder.harvestSize < 2 ? this.maxSize : builder.harvestSize;
        this.afterHarvestSize = builder.afterHarvestSize;
        this.baseSeeds = builder.baseSeeds;

        if (Strings.isNullOrEmpty(this.id)) {
            throw new IllegalArgumentException("The id must not be null or empty!");
        }
        else if (Strings.isNullOrEmpty(this.owner)) {
            throw new IllegalArgumentException("The owner must not be null or empty!");
        }
        else if (Strings.isNullOrEmpty(this.discoveredBy)) {
            throw new IllegalArgumentException("The discoveredBy must not be null or empty!");
        }
        else if (this.properties == null) {
            throw new IllegalArgumentException("The properties must not be null!");
        }
        else if (this.maxSize < 3) {
            throw new IllegalArgumentException("The max size must be at least 3!");
        }
        else if (this.afterHarvestSize < 1) {
            throw new IllegalArgumentException("The after harvest size must be at least 1!");
        }
    }

    public static CropBuilder create() {
        return new CropBuilder();
    }
    
    public void register() {
        Crops.instance.registerCrop(this);

        for (BaseSeed baseSeed : this.baseSeeds) {
            GregTechMod.LOGGER.info("Registering base seed for crop " + this.id);
            Crops.instance.registerBaseSeed(baseSeed.seed, this, baseSeed.size, baseSeed.growth, baseSeed.gain, baseSeed.resistance);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getOwner() {
        return this.owner;
    }

    @Override
    public String getDiscoveredBy() {
        return this.discoveredBy;
    }

    @Override
    public int getRootsLength(ICropTile cropTile) {
        return 5;
    }

    @Override
    public CropProperties getProperties() {
        return this.properties;
    }

    @Override
    public String[] getAttributes() {
        return this.attributes;
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }

    @Override
    public ItemStack[] getGains(ICropTile crop) {
        if (this.specialDrops != null && this.specialDrops.length > 0) {
            int drop = GtUtil.RANDOM.nextInt(this.specialDrops.length + 4);
            if (drop < this.specialDrops.length && !this.specialDrops[drop].isEmpty()) {
                return new ItemStack[] { this.specialDrops[drop].copy() };
            }
        }
        else if (this.drops != null && this.drops.length > 0) {
            for (ItemStack stack : this.drops) {
                if (stack.getCraftingRemainingItem().isEmpty()) {
                    return new ItemStack[] { stack.copy() };
                }
            }
        }
        return new ItemStack[0];
    }

    @Override
    public int getGrowthDuration(ICropTile cropTile) {
        return this.growthSpeed < 200 ? this.properties.getTier() * 200 : this.properties.getTier() * this.growthSpeed;
    }

    @Override
    public boolean canCross(ICropTile cropTile) {
        return cropTile.getCurrentSize() + 2 > getMaxSize();
    }

    @Override
    public boolean canBeHarvested(ICropTile cropTile) {
        return cropTile.getCurrentSize() >= this.harvestSize;
    }

    @Override
    public int getSizeAfterHarvest(ICropTile cropTile) {
        return this.afterHarvestSize;
    }

    @Override
    public List<ResourceLocation> getTexturesLocation() {
        return IntStreamEx.rangeClosed(1, getMaxSize())
            .mapToObj(i -> new ResourceLocation(this.owner, "block/crop/" + this.id + "_" + i))
            .toList();
    }

    @Override
    public boolean onRightClick(ICropTile cropTile, Player player) {
        return canBeHarvested(cropTile) && cropTile.performManualHarvest();
    }

    public static class CropBuilder {
        private String id;
        private String owner;
        private String discoveredBy;
        private CropProperties properties;
        private String[] attributes;
        private int maxSize;
        private ItemStack[] drops;
        private ItemStack[] specialDrops;
        private int growthSpeed;
        private int harvestSize;
        private int afterHarvestSize;
        private final List<BaseSeed> baseSeeds = new ArrayList<>(0);

        public CropBuilder id(String id) {
            this.id = id;
            return this;
        }

        public CropBuilder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public CropBuilder discoveredBy(String discoveredBy) {
            this.discoveredBy = discoveredBy;
            return this;
        }

        public CropBuilder properties(int tier, int chemistry, int consumable, int defensive, int colorful, int weed) {
            this.properties = new CropProperties(tier, chemistry, consumable, defensive, colorful, weed);
            return this;
        }

        public CropBuilder attributes(String[] attributes) {
            this.attributes = attributes;
            return this;
        }

        public CropBuilder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public CropBuilder drops(ItemStack[] drops) {
            this.drops = drops;
            return this;
        }

        public CropBuilder specialDrops(ItemStack[] specialDrops) {
            this.specialDrops = specialDrops;
            return this;
        }

        public CropBuilder growthSpeed(int growthSpeed) {
            this.growthSpeed = growthSpeed;
            return this;
        }

        public CropBuilder harvestSize(int harvestSize) {
            this.harvestSize = harvestSize;
            return this;
        }

        public CropBuilder afterHarvestSize(int afterHarvestSize) {
            this.afterHarvestSize = afterHarvestSize;
            return this;
        }

        public CropBuilder addBaseSeed(ItemStack seed) {
            if (!seed.isEmpty()) {
                return addBaseSeed(seed, 1, 1, 1, 1);
            }
            return this;
        }

        public CropBuilder addBaseSeed(ItemStack seed, int size, int growth, int gain, int resistance) {
            this.baseSeeds.add(new BaseSeed(seed, size, growth, gain, resistance));
            return this;
        }

        public GtCropCard build() {
            return new GtCropCard(this);
        }
    }
    
    private record BaseSeed(ItemStack seed, int size, int growth, int gain, int resistance) {}
}
