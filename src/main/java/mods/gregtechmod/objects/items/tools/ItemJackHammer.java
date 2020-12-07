package mods.gregtechmod.objects.items.tools;

import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemElectricTool;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemJackHammer extends ItemElectricTool implements IModelInfoProvider {
    protected final String name;
    protected final boolean canMineObsidian;
    public static final List<Block> effectiveBlocks = Arrays.asList(Blocks.COBBLESTONE, Blocks.STONE, Blocks.SANDSTONE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.GLOWSTONE, Blocks.NETHER_BRICK, Blocks.END_STONE);

    public ItemJackHammer(String name, int operationEnergyCost, int maxCharge, int tier, int transferLimit, float efficiency, boolean canMineObsidian) {
        super(null, operationEnergyCost, HarvestLevel.Diamond, Collections.emptySet());
        this.name = name;
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.efficiency = efficiency;
        this.canMineObsidian = canMineObsidian;
        setMaxStackSize(1);
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID+".item."+name;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack itemStack) {
        Block block = state.getBlock();
        return effectiveBlocks.contains(block) && (this.canMineObsidian || !OreDictUnificator.isItemInstanceOf(block, "stoneObsidian", false));
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation(this.name, "tool"));
    }
}
