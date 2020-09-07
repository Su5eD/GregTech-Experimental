package mods.gregtechmod.common.objects.items.components;

import ic2.api.item.ElectricItem;
import ic2.core.item.ItemBattery;
import ic2.core.ref.ItemName;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.util.IHasModel;
import net.minecraft.util.ResourceLocation;

public class ItemLithiumBattery extends ItemBattery implements IHasModel {

    public ItemLithiumBattery() {
        super(null, 100000, 128, 1);
        setRegistryName("lithium_re_battery");
        this.addPropertyOverride(new ResourceLocation(GregtechMod.MODID, "battery_charge"), (stack, worldIn, entityIn) -> ElectricItem.manager.getCharge(stack) > 1 ? 1 : 0);
    }

    @Override
    public String getTranslationKey() {
        return "item.lithium_re_battery";
    }

    @Override
    public void registerModels(ItemName name) {
        //Disables IC2 model registration
    }

    @Override
    public void registerModels() {
        GregtechMod.proxy.registerModel(this, 0, "lithium_battery", null, "component");
    }
}
