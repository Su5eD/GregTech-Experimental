package mods.gregtechmod.objects.items.base;

import ic2.api.reactor.IReactor;
import ic2.core.item.reactor.ItemReactorHeatStorage;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.ICustomItemModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemNuclearHeatStorage extends ItemReactorHeatStorage implements ICustomItemModel {
    private final String name;

    public ItemNuclearHeatStorage(String name, int heatStorage) {
        super(null, heatStorage);
        this.name = name;
        setMaxStackSize(1);
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID + ".item." + this.name;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(GtUtil.translateItem("coolant.stored_heat") + ": " + this.getCustomDamage(stack));
    }

    @Override
    public float influenceExplosion(ItemStack stack, IReactor reactor) {
        return 1F + this.getMaxCustomDamage(stack) / 30000F;
    }

    @Override
    public ResourceLocation getItemModel() {
        return GtUtil.getModelResourceLocation(this.name, "nuclear");
    }
}
