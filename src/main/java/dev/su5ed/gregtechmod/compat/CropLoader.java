package dev.su5ed.gregtechmod.compat;

import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.object.Dust;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.object.Nugget;
import dev.su5ed.gregtechmod.object.Smalldust;
import dev.su5ed.gregtechmod.util.GtCropCard;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;

public final class CropLoader {
    private static final String FLOWER = "Flower";
    private static final String COLOR = "Color";
    private static final String INGREDIENT = "Ingredient";
    private static final String SHINY = "Shiny";
    private static final String METAL = "Metal";
    private static final String PINE = "Pine";
    private static final String TIN = "Tin";
    private static final String BUSH = "Bush";
    private static final String COTTON = "Cotton";
    private static final String COPPER = "Copper";
    private static final String SILVER = "Silver";
    private static final String REED = "Reed";
    private static final String LEAD = "Lead";
    private static final String HEAVY = "Heavy";
    private static final String SILK = "Silk";
    private static final String VINE = "Vine";
    private static final String ADDITCTIVE = "Addictive";
    private static final String FIRE = "Fire";
    private static final String DARK = "Dark";
    private static final String ROTTEN = "Rotten";
    private static final String COAL = "Coal";
    private static final String OIL = "Oil";
    private static final String EMERALD = "Emerald";
    private static final String BERYLIUM = "Berylium";
    private static final String CRYSTAL = "Crystal";
    private static final String DIAMOND = "Diamond";
    private static final String UNDEAD = "Undead";
    private static final String WITHER = "Wither";
    private static final String BLAZE = "Blaze";
    private static final String SULFUR = "Sulfur";
    private static final String CHICKEN = "Chicken";
    private static final String EGG = "Egg";
    private static final String EDIBLE = "Edible";
    private static final String FEATHER = "Feather";
    private static final String COW = "Cow";
    private static final String TOXIC = "Toxic";
    private static final String CREEPER = "Creeper";
    private static final String EXPLOSIVE = "Explosive";
    private static final String SALTPETER = "Saltpeter";
    private static final String ENDER = "Ender";
    private static final String FISH = "Fish";
    private static final String PIG = "Pig";
    private static final String MILK = "Milk";
    private static final String SLIME = "Slime";
    private static final String BOUNCY = "Bouncy";
    private static final String STICKY = "Sticky";
    private static final String SPIDER = "Spider";
    private static final String HEALING = "Healing";
    private static final String NETHER = "Nether";
    private static final String GHAST = "Ghast";

    public static void registerCrops() {
        GregTechMod.LOGGER.info("Registering Crops");
        for (Crop type : Crop.values()) {
            String name = type.name().toLowerCase(Locale.ROOT);
            GtCropCard.create()
                .id(name)
                .owner(Reference.MODID)
                .discoveredBy(type.discoverer)
                .properties(type.tier, type.statChemistry, type.statConsumable, type.statDefensive, type.statColorful, type.statWeed)
                .attributes(type.attributes)
                .harvestSize(type.harvestSize)
                .afterHarvestSize(type.afterHarvestSize)
                .growthSpeed(type.growthSpeed)
                .maxSize(type.maxSize)
                .drops(type.drops)
                .specialDrops(type.specialDrops)
                .addBaseSeed(type.baseSeed)
                .build()
                .register();
        }
    }

    private CropLoader() {}
    
    public enum Crop {
        ARGENTIA("Eloraam", Nugget.SILVER, 7, 4, 0, 3, 4, 2, 0, 1, 0, 0, SHINY, METAL, SILVER, REED),
        BLAZEREED("Mr. Brain", Items.BLAZE_POWDER, Items.BLAZE_ROD, 6, 4, 0, 1, 4, 0, 4, 1, 0, 0, FIRE, BLAZE, REED, SULFUR),
        BOBSYERUNCLERANKS("GenerikB", Smalldust.EMERALD, Items.EMERALD, 11, 4, 0, 1, 4, 4, 0, 8, 2, 9, SHINY, VINE, EMERALD, BERYLIUM, CRYSTAL),
//        BROWN_MUSHROOMS("Mr. Brain", Blocks.BROWN_MUSHROOM, new ItemStack[0], new ItemStack(Blocks.BROWN_MUSHROOM, 4), 1, 3, 0, 1, 3, 0, 2, 0, 0, 2, EDIBLE, MUSHROOM, INGREDIENT),
        COPPON("Mr. Brain", Nugget.COPPER, 6, 3, 0, 2, 3, 2, 0, 1, 1, 1, SHINY, METAL, COTTON, COPPER, BUSH),
        CORIUM("Gregorius Techneticies", Items.FEATHER, 6, 4, 0, 1, 4, 0, 2, 3, 1, 0, COW, SILK, VINE),
        CORPSEPLANT("Mr. Kenny", Items.ROTTEN_FLESH, new ItemStack[] { new ItemStack(Items.BONE_MEAL), new ItemStack(Items.BONE_MEAL), new ItemStack(Items.BONE) }, 5, 4, 0, 1, 4, 0, 2, 1, 0, 3, TOXIC, UNDEAD, VINE, EDIBLE, ROTTEN),
        CREEPERWEED("General Spaz", Items.GUNPOWDER, 7, 4, 0, 1, 4, 3, 0, 5, 1, 3, CREEPER, VINE, EXPLOSIVE, FIRE, SULFUR, SALTPETER, COAL),
        DIAREED("Direwolf20", Smalldust.DIAMOND, Items.DIAMOND, 12, 4, 0, 1, 4, 5, 0, 10, 2, 10, FIRE, SHINY, REED, COAL, DIAMOND, CRYSTAL),
        EGGPLANT("Link", Items.EGG, new ItemStack[] { new ItemStack(Items.CHICKEN), new ItemStack(Items.FEATHER), new ItemStack(Items.FEATHER), new ItemStack(Items.FEATHER) }, 6, 3, 900, 2, 3, 0, 4, 1, 0, 0, CHICKEN, EGG, EDIBLE, FEATHER, FLOWER, ADDITCTIVE),
        ENDERBLOOM("RichardG", Dust.ENDER_PEARL, new ItemStack[] { new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.ENDER_EYE) }, 10, 4, 0, 1, 4, 5, 0, 2, 1, 6, ENDER, FLOWER, SHINY),
        FLAX("Eloraam", Items.STRING, 2, 4, 0, 1, 4, 1, 1, 2, 0, 1, SILK, VINE, ADDITCTIVE),
        INDIGO("Eloraam", Miscellaneous.INDIGO_BLOSSOM, new ItemStack[0], Miscellaneous.INDIGO_BLOSSOM.getItemStack(4), 2, 4, 0, 1, 4, 1, 1, 0, 4, 0, FLOWER, COLOR, INGREDIENT),
        MEATROSE("VintageBeef", Items.PINK_DYE, new ItemStack[] { new ItemStack(Items.BEEF), new ItemStack(Items.PORKCHOP), new ItemStack(Items.CHICKEN), new ItemStack(Items.COD) }, 7, 4, 1500, 1, 4, 0, 4, 1, 3, 0, EDIBLE, FLOWER, COW, FISH, CHICKEN, PIG),
        MILKWART("Mr. Brain", Items.MILK_BUCKET, 6, 3, 900, 1, 3, 0, 3, 0, 1, 0, EDIBLE, MILK, COW),
        OILBERRIES("Spacetoad", Miscellaneous.OIL_BERRY, 9, 4, 0, 1, 4, 6, 1, 2, 1, 12, FIRE, DARK, REED, ROTTEN, COAL, OIL),
        PLUMBILIA("KingLemming", Nugget.LEAD, 6, 4, 0, 3, 4, 2, 0, 3, 1, 1, HEAVY, METAL, LEAD, REED),
//        RED_MUSHROOMS("Mr. Kenny", Blocks.RED_MUSHROOM, new ItemStack[0], new ItemStack(Blocks.RED_MUSHROOM, 4), 1, 3, 0, 1, 3, 0, 1, 3, 0, 2, TOXIC, MUSHROOM, INGREDIENT),
        SLIMEPLANT("Neowulf", Items.SLIME_BALL, 6, 4, 0, 3, 4, 3, 0, 0, 0, 2, SLIME, BOUNCY, STICKY, BUSH),
        SPIDERNIP("Mr. Kenny", Items.STRING, new ItemStack[] { new ItemStack(Items.SPIDER_EYE), new ItemStack(Blocks.COBWEB) }, 4, 4, 600, 1, 4, 2, 1, 4, 1, 3, TOXIC, SILK, SPIDER, FLOWER, INGREDIENT, ADDITCTIVE),
        TEARSTALKS("Neowulf", Items.GHAST_TEAR, 8, 4, 0, 1, 4, 1, 2, 0, 0, 0, HEALING, NETHER, INGREDIENT, REED, GHAST),
        TINE("Gregorius Techneticies", Nugget.TIN, 5, 3, 0, 2, 3, 2, 0, 3, 0, 0, SHINY, METAL, PINE, TIN, BUSH),
        WITHEREED("CovertJaguar", ModHandler.getModItem("coal_dust"), new ItemStack[] { new ItemStack(Items.COAL), new ItemStack(Items.COAL) }, 8, 4, 0, 1, 4, 2, 0, 4, 1, 3, FIRE, UNDEAD, REED, COAL, ROTTEN, WITHER);
        
        public final String discoverer;
        public final ItemStack[] drops;
        public final ItemStack[] specialDrops;
        public final ItemStack baseSeed;
        public final int maxSize;
        public final int harvestSize;
        public final int afterHarvestSize;
        public final int growthSpeed;
        public final int tier;
        public final int statChemistry;
        public final int statConsumable;
        public final int statDefensive;
        public final int statColorful;
        public final int statWeed;
        public final String[] attributes;
        
        Crop(String discoverer, ItemLike drop, ItemLike specialDrop, int tier, int maxSize, int growthSpeed, int afterHarvestSize, int harvestSize, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, String... attributes) {
            this(discoverer, drop, new ItemStack[]{ new ItemStack(specialDrop) }, tier, maxSize, growthSpeed, afterHarvestSize, harvestSize, statChemistry, statConsumable, statDefensive, statColorful, statWeed, attributes);
        }
        
        Crop(String discoverer, ItemLike drop, int tier, int maxSize, int growthSpeed, int afterHarvestSize, int harvestSize, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, String... attributes) {
            this(discoverer, drop, new ItemStack[0], tier, maxSize, growthSpeed, afterHarvestSize, harvestSize, statChemistry, statConsumable, statDefensive, statColorful, statWeed, attributes);
        }
        
        Crop(String discoverer, ItemLike drop, ItemStack[] specialDrops, int tier, int maxSize, int growthSpeed, int afterHarvestSize, int harvestSize, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, String... attributes) {
            this(discoverer, drop, specialDrops, ItemStack.EMPTY, tier, maxSize, growthSpeed, afterHarvestSize, harvestSize, statChemistry, statConsumable, statDefensive, statColorful, statWeed, attributes);
        }

        Crop(String discoverer, ItemLike drop, ItemStack[] specialDrops, ItemStack baseSeed, int tier, int maxSize, int growthSpeed, int afterHarvestSize, int harvestSize, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, String... attributes) {
            this.discoverer = discoverer;
            this.drops = new ItemStack[] { new ItemStack(drop) };
            this.specialDrops = specialDrops;
            this.baseSeed = baseSeed;
            this.maxSize = maxSize;
            this.harvestSize = harvestSize;
            this.afterHarvestSize = afterHarvestSize;
            this.growthSpeed = growthSpeed;
            this.tier = tier;
            this.statChemistry = statChemistry;
            this.statConsumable = statConsumable;
            this.statDefensive = statDefensive;
            this.statColorful = statColorful;
            this.statWeed = statWeed;
            this.attributes = attributes;
        }
    }
}
