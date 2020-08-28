package mods.gregtechmod.common.core;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.common.init.BlockItemLoader;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class GregTechTab extends CreativeTabs {
    NonNullList<ItemStack> list;

    public GregTechTab(String label) {
        super("gregtechtab");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlockItemLoader.Specials.greg_coin.getInstance());
    }

    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
        this.list = list;

        BlockTileEntity block = TeBlockRegistry.get(GregtechTeBlock.LOCATION);
        addBlock(BlockItemLoader.Blocks.lesublock.getInstance());

        list.add(block.getItemStack(GregtechTeBlock.gtcentrifuge));
    }

    private void addItem(Item item) {
        item.getSubItems(this, list);
    }

    private void addBlock(Block block) {
        block.getSubBlocks(this, list);
    }
}
