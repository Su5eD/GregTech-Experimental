package mods.gregtechmod.objects.items.components;

import ic2.api.item.ElectricItem;
import ic2.core.item.ItemBattery;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLithiumBattery extends ItemBattery implements IModelInfoProvider {

    public ItemLithiumBattery() {
        super(null, 100000, 128, 1);
        setRegistryName("lithium_re_battery");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.addPropertyOverride(new ResourceLocation(Reference.MODID, "battery_charge"), (stack, worldIn, entityIn) -> ElectricItem.manager.getCharge(stack) > 1 ? 1 : 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(GtUtil.translateInfo("tier", this.tier));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID+".item.lithium_re_battery";
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
