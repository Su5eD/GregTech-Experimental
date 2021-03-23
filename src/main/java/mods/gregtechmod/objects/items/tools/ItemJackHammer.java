package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.objects.items.base.ItemToolElectricBase;
import mods.gregtechmod.util.OreDictUnificator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;

public class ItemJackHammer extends ItemToolElectricBase {
    protected final boolean canMineObsidian;

    public ItemJackHammer(String name, int operationEnergyCost, int maxCharge, int tier, int transferLimit, float efficiency, boolean canMineObsidian) {
        super(name, maxCharge, transferLimit, tier, operationEnergyCost, Collections.emptySet());
        this.efficiency = efficiency;
        this.canMineObsidian = canMineObsidian;
        setMaxStackSize(1);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack itemStack) {
        Block block = state.getBlock();
        for (ItemStack stack : GregTechAPI.JACK_HAMMER_MINABLE_BLOCKS) {
            Item item = stack.getItem();
            int meta = stack.getMetadata();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() == block && (meta == OreDictionary.WILDCARD_VALUE || block.getMetaFromState(state) == meta)) {
                return this.canMineObsidian || !OreDictUnificator.isItemInstanceOf(block, "stoneObsidian", false);
            }
        }
        return false;
    }
}
