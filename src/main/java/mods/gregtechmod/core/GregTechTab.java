package mods.gregtechmod.core;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.api.BlockItems;
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
        return new ItemStack(BlockItems.Miscellaneous.greg_coin.getInstance());
    }

    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
        this.list = list;
        BlockTileEntity block = TeBlockRegistry.get(GregtechTeBlock.LOCATION);

        addBlock(BlockItems.Blocks.lesublock.getInstance());
        addTeBlock(GregtechTeBlock.industrial_centrifuge, block);
        addTeBlock(GregtechTeBlock.digital_chest, block);
        addTeBlock(GregtechTeBlock.quantum_chest, block);
        addTeBlock(GregtechTeBlock.quantum_tank, block);

        super.displayAllRelevantItems(list); //TODO: Add stuff manually
    }

    private void addItem(Item item) {
        item.getSubItems(this, list);
    }

    private void addBlock(Block block) {
        block.getSubBlocks(this, list);
    }

    private void addTeBlock(GregtechTeBlock teBlock, BlockTileEntity block) {
        this.list.add(block.getItemStack(teBlock));
    }
}
