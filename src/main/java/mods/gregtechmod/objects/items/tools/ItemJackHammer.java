package mods.gregtechmod.objects.items.tools;

import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemElectricTool;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class ItemJackHammer extends ItemElectricTool implements IModelInfoProvider {
    protected final String name;

    public ItemJackHammer(String material, int operationEnergyCost, int maxCharge, int tier, int transferLimit, float efficiency) {
        super(null, operationEnergyCost, HarvestLevel.Diamond, Collections.emptySet());
        this.name = "jack_hammer_"+material;
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.efficiency = efficiency;
        setRegistryName(name);
        setMaxStackSize(1);
    }

    @Override
    public String getTranslationKey() {
        return "item."+name;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack itemStack) {
        if (state.getMaterial() == Material.ROCK && !(state.getBlock() instanceof BlockOre)) return true;
        return super.canHarvestBlock(state, itemStack);
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregtechMod.getModelResourceLocation(this.name, "tool"));
    }
}
