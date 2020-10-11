package mods.gregtechmod.objects.items.components;

import ic2.api.item.ElectricItem;
import ic2.core.item.ItemBattery;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemLithiumBattery extends ItemBattery implements IModelInfoProvider {

    public ItemLithiumBattery() {
        super(null, 100000, 128, 1);
        setRegistryName("lithium_re_battery");
        this.addPropertyOverride(new ResourceLocation(GregTechMod.MODID, "battery_charge"), (stack, worldIn, entityIn) -> ElectricItem.manager.getCharge(stack) > 1 ? 1 : 0);
    }

    @Override
    public String getTranslationKey() {
        return GregTechMod.MODID+".item.lithium_re_battery";
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation("lithium_re_battery", "component"));
    }
}
