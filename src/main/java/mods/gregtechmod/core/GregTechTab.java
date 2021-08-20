package mods.gregtechmod.core;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.init.BlockItemLoader;
import mods.gregtechmod.init.FluidLoader;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class GregTechTab extends CreativeTabs {

    public GregTechTab() {
        super("gregtechmod");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlockItems.Miscellaneous.GREG_COIN.getInstance());
    }

    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
        BlockItemLoader.getBlocks().stream()
                .map(ItemStack::new)
                .filter(stack -> !stack.isEmpty())
                .forEach(list::add);
        BlockTileEntity block = TeBlockRegistry.get(GregTechTEBlock.LOCATION);
        Arrays.stream(GregTechTEBlock.VALUES)
                .forEach(teblock -> list.add(block.getItemStack(teblock)));
        BlockItemLoader.getItems().stream()
                .filter(item -> GregTechMod.classic || !(item instanceof ItemCellClassic))
                .map(ItemStack::new)
                .forEach(list::add);
        FluidLoader.FLUIDS.stream()
                .map(FluidLoader.IFluidProvider::getName)
                .map(ProfileDelegate::getCell)
                .forEach(list::add);
        Arrays.stream(BlockItems.Book.values())
                .map(BlockItems.Book::getInstance)
                .forEach(list::add);
    }
}
