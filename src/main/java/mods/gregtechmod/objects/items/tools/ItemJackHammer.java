package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.objects.items.base.ItemToolElectricBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemJackHammer extends ItemToolElectricBase {
    protected final boolean canMineObsidian;
    public static final List<Block> effectiveBlocks = Arrays.asList(Blocks.COBBLESTONE, Blocks.STONE, Blocks.SANDSTONE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.GLOWSTONE, Blocks.NETHER_BRICK, Blocks.END_STONE);

    public ItemJackHammer(String name, int operationEnergyCost, int maxCharge, int tier, int transferLimit, float efficiency, boolean canMineObsidian) {
        super(name, null, maxCharge, transferLimit, tier, operationEnergyCost, Collections.emptySet());
        this.efficiency = efficiency;
        this.canMineObsidian = canMineObsidian;
        setMaxStackSize(1);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack itemStack) {
        Block block = state.getBlock();
        return effectiveBlocks.contains(block) && (this.canMineObsidian || !OreDictUnificator.isItemInstanceOf(block, "stoneObsidian", false));
    }
}
