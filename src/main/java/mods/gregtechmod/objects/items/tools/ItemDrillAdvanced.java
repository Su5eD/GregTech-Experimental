package mods.gregtechmod.objects.items.tools;

import ic2.api.item.IMiningDrill;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolElectricBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ItemDrillAdvanced extends ItemToolElectricBase implements IMiningDrill {

    public ItemDrillAdvanced() {
        super("drill_advanced", 8, 128000, 3, 250, 5, EnumSet.of(ToolClass.Pickaxe, ToolClass.Shovel));
        setRarity(EnumRarity.UNCOMMON);
        setRegistryName("drill_advanced");
        setTranslationKey("drill_advanced");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.efficiency = 35;
        this.showTier = false;
        this.hasEmptyVariant = true;
    }

    @Override
    public int energyUse(ItemStack stack, World world, BlockPos pos, IBlockState state) {
        return (int) this.operationEnergyCost / 4;
    }

    @Override
    public int breakTime(ItemStack stack, World world, BlockPos pos, IBlockState state) {
        return (int) this.efficiency;
    }

    @Override
    public boolean breakBlock(ItemStack stack, World world, BlockPos pos, IBlockState state) {
        return tryUsePower(stack, this.operationEnergyCost);
    }
}
