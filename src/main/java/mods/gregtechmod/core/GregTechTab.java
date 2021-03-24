package mods.gregtechmod.core;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.objects.BlockItems;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class GregTechTab extends CreativeTabs {

    public GregTechTab() {
        super("gregtechtab");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlockItems.Miscellaneous.GREG_COIN.getInstance());
    }

    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
        BlockTileEntity block = TeBlockRegistry.get(GregTechTEBlock.LOCATION);

        addBlock(list, BlockItems.Block.LESUBLOCK.getInstance());
        addTeBlock(list, GregTechTEBlock.INDUSTRIAL_CENTRIFUGE, block);
        addTeBlock(list, GregTechTEBlock.DIGITAL_CHEST, block);
        addTeBlock(list, GregTechTEBlock.QUANTUM_CHEST, block);
        addTeBlock(list, GregTechTEBlock.QUANTUM_TANK, block);
        for (BlockItems.Book type : BlockItems.Book.values()) {
            list.add(type.getInstance());
        }

        super.displayAllRelevantItems(list);
    }

    private void addBlock(NonNullList<ItemStack> list, Block block) {
        block.getSubBlocks(this, list);
    }

    private static void addTeBlock(NonNullList<ItemStack> list, GregTechTEBlock teBlock, BlockTileEntity block) {
        list.add(block.getItemStack(teBlock));
    }
}
