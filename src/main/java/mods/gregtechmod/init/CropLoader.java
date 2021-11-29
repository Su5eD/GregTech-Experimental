package mods.gregtechmod.init;

import ic2.api.crops.CropCard;
import ic2.api.crops.CropProperties;
import ic2.api.crops.Crops;
import ic2.core.crop.cropcard.GenericCropCard;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Locale;

@EventBusSubscriber(modid = Reference.MODID)
public class CropLoader {

    @SubscribeEvent
    public static void registerCrops(Crops.CropRegisterEvent event) {
        GregTechMod.LOGGER.info("Registering crops");
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
                GregTechMod.LOGGER.info("Registering base seed for crop " + type.name().toLowerCase(Locale.ROOT));
                Crops.instance.registerBaseSeed(type.baseSeed, crop, 1, 1, 1, 1);
            }
        }
        GregTechMod.LOGGER.info("Finished registering crops");
    }

    public enum Crop {
        //Most crops have been already implemented by ic2, don't be surprised why many are missing
        INDIGO("Eloraam", BlockItems.Miscellaneous.INDIGO_BLOSSOM.getItemStack(), BlockItems.Miscellaneous.INDIGO_BLOSSOM.getItemStack(4), 4, 4, 1, 0, 2, 1, 1, 0, 4, 0, "Flower", "Color", "Ingredient"),
        TINE("Gregorius Techneticies", BlockItems.Nugget.TIN.getItemStack(), ItemStack.EMPTY, 3, 3, 2, 0, 5, 2, 0, 3, 0, 0, "Shiny", "Metal", "Pine", "Tin", "Bush"),
        COPPON("Mr. Brain", BlockItems.Nugget.COPPER.getItemStack(), ItemStack.EMPTY, 3, 3, 2, 0, 6, 2, 0, 1, 1, 1, "Shiny", "Metal", "Cotton", "Copper", "Bush"),
        ARGENTIA("Eloraam", BlockItems.Nugget.SILVER.getItemStack(), ItemStack.EMPTY, 4, 4, 3, 0, 7, 2, 0, 1, 0, 0, "Shiny", "Metal", "Silver", "Reed"),
        PLUMBILIA("KingLemming", BlockItems.Nugget.LEAD.getItemStack(), ItemStack.EMPTY, 4, 4, 3, 0, 6, 2, 0, 3, 1, 1, "Heavy", "Metal", "Lead", "Reed");

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
        public final String[] attributes;

        Crop(String discoverer, ItemStack drop, ItemStack baseSeed, int maxSize, int harvestSize, int afterHarvestSize, int growthSpeed, int tier, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, String... attributes) {
            this(discoverer, drop, new ItemStack[0], baseSeed, maxSize, harvestSize, afterHarvestSize, growthSpeed, tier, statChemistry, statConsumable, statDefensive, statColorful, statWeed, attributes);
        }

        Crop(String discoverer, ItemStack drop, ItemStack[] specialDrops, ItemStack baseSeed, int maxSize, int harvestSize, int afterHarvestSize, int growthSpeed, int tier, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, String... attributes) {
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
            this.attributes = attributes;
        }
    }
}
