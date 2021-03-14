package mods.gregtechmod.init;

import ic2.api.crops.CropCard;
import ic2.api.crops.CropProperties;
import ic2.api.crops.Crops;
import ic2.core.crop.cropcard.GenericCropCard;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.base.ItemBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class CropLoader {

    @SubscribeEvent
    public static void registerCrops(Crops.CropRegisterEvent event) {
        GregTechMod.logger.info("Registering crops");
        for (Crop type : Crop.values()) {
            CropCard crop = GenericCropCard.create(type.name().toLowerCase(Locale.ROOT))
                    .setOwner(Reference.MODID)
                    .setDiscoveredBy(type.discoverer)
                    .setProperties(new CropProperties(type.tier, type.statChemistry, type.statConsumable, type.statDefensive, type.statColorful, type.statWeed))
                    .setAttributes(type.attributes)
                    .setHarvestSize(type.harvestSize)
                    .setAfterHarvestSize(type.afterHarvestSize)
                    .setGrowthSpeed(type.growthSpeed)
                    .setMaxSize(type.maxSize)
                    .setDrops(type.drop)
                    .setSpecialDrops(type.specialDrops)
                    .register();

            if (type.baseSeed != null) {
                GregTechMod.logger.info("Registering base seed for crop "+type.name().toLowerCase(Locale.ROOT));
                Crops.instance.registerBaseSeed(type.baseSeed, crop, 1, 1, 1, 1);
            }
        }
        GregTechMod.logger.info("Finished registering crops");
    }

    public enum Crop {
        //Most crops have been already implemented by ic2, so don't be surprised why many are missing
        INDIGO("Eloraam", new ItemStack(BlockItems.Miscellaneous.INDIGO_BLOSSOM.getInstance()), new ItemStack(BlockItems.Miscellaneous.INDIGO_BLOSSOM.getInstance(), 4), 4, 4, 1, 0, 2, 1, 1, 0, 4, 0, true, "Flower", "Color", "Ingredient"),
        TINE("Gregorius Techneticies", new ItemStack(BlockItems.Nugget.TIN.getInstance()), ItemStack.EMPTY, 3, 3, 2, 0, 5, 2, 0, 3, 0, 0, false, "Shiny", "Metal", "Pine", "Tin", "Bush"),
        COPPON("Mr. Brain", new ItemStack(BlockItems.Nugget.COPPER.getInstance()), ItemStack.EMPTY, 3, 3, 2, 0, 6, 2, 0, 1, 1, 1, false, "Shiny", "Metal", "Cotton", "Copper", "Bush"),
        ARGENTIA("Eloraam", new ItemStack(BlockItems.Nugget.SILVER.getInstance()), ItemStack.EMPTY, 4, 4, 3, 0, 7, 2, 0, 1, 0, 0, false, "Shiny", "Metal", "Silver", "Reed"),
        PLUMBILIA("KingLemming", new ItemStack(BlockItems.Nugget.LEAD.getInstance()), ItemStack.EMPTY, 4, 4, 3, 0, 6, 2, 0, 3, 1, 1, false, "Heavy", "Metal", "Lead", "Reed");

        private Item instance;
        public final String discoverer;
        public final ItemStack drop;
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
        public final boolean hasItem;
        public final String[] attributes;

        Crop(String discoverer, ItemStack drop, ItemStack baseSeed, int maxSize, int harvestSize, int afterHarvestSize, int growthSpeed, int tier, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, boolean hasItem, String... attributes) {
            this(discoverer, drop, new ItemStack[0], baseSeed, maxSize, harvestSize, afterHarvestSize, growthSpeed, tier, statChemistry, statConsumable, statDefensive, statColorful, statWeed, hasItem, attributes);
        }

        Crop(String discoverer, ItemStack drop, ItemStack[] specialDrops, ItemStack baseSeed, int maxSize, int harvestSize, int afterHarvestSize, int growthSpeed, int tier, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, boolean hasItem, String... attributes) {
            this.discoverer = discoverer;
            this.drop = drop;
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
            this.hasItem = hasItem;
            this.attributes = attributes;
        }

        public Item getInstance() {
            if (this.instance == null) {
                if (this.hasItem) {
                    this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), null)
                            .setRegistryName(this.name().toLowerCase(Locale.ROOT))
                            .setTranslationKey(this.name().toLowerCase(Locale.ROOT))
                            .setCreativeTab(GregTechMod.GREGTECH_TAB);
                }
            }

            return this.instance;
        }
    }
}
