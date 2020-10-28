package mods.gregtechmod.init;

import ic2.api.crops.CropCard;
import ic2.api.crops.CropProperties;
import ic2.api.crops.Crops;
import ic2.core.crop.cropcard.GenericCropCard;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class CropLoader {

    @SubscribeEvent
    public static void registerCrops(Crops.CropRegisterEvent event) {
        GregTechMod.logger.info("Registering crops");
        for (BlockItems.Crops type : BlockItems.Crops.values()) {
            CropCard crop = GenericCropCard.create(type.name())
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
                GregTechMod.logger.info("Registering base seed for crop "+type.name());
                Crops.instance.registerBaseSeed(type.baseSeed, crop, 1, 1, 1, 1);
            }
        }
        GregTechMod.logger.info("Finished registering crops");
    }
}
