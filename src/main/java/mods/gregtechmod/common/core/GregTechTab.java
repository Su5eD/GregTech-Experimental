package mods.gregtechmod.common.core;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.common.init.BlockInit;
import mods.gregtechmod.common.init.ItemInit;
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
        return new ItemStack(ItemInit.GREG_COIN);
    }

    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
        this.list = list;

        BlockTileEntity block = TeBlockRegistry.get(GregtechTeBlock.location);
        addBlock(BlockInit.BLOCK_LESUBLOCK);

        list.add(block.getItemStack(GregtechTeBlock.gtcentrifuge));
    }

    private void addItem(Item item) {
        item.getSubItems(this, list);
    }

    private void addBlock(Block block) {
        ItemStack stack = new ItemStack(block);
        block.getSubBlocks(this, list);
    }
}
