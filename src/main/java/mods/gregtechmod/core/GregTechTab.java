package mods.gregtechmod.core;

import ic2.api.item.IC2Items;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.init.FluidLoader;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.IItemProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.Arrays;

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

        Arrays.stream(BlockItems.Block.values())
                .map(BlockItems.Block::getInstance)
                .map(ItemStack::new)
                .forEach(list::add);
        Arrays.stream(GregTechTEBlock.VALUES)
                .forEach(teblock -> list.add(block.getItemStack(teblock)));
        Arrays.stream(BlockItems.Ore.values())
                .map(BlockItems.Ore::getInstance)
                .map(ItemStack::new)
                .forEach(list::add);
        addItems(BlockItems.Miscellaneous.values(), list);
        addItems(BlockItems.Ingot.values(), list);
        addItems(BlockItems.Plate.values(), list);
        addItems(BlockItems.Rod.values(), list);
        addItems(BlockItems.Dust.values(), list);
        addItems(BlockItems.Cell.values(), list);
        if (GregTechMod.classic) BlockItems.classicCells.values().stream()
            .map(ItemStack::new)
            .forEach(list::add);
        FluidLoader.FLUIDS.stream()
                .map(FluidLoader.IFluidProvider::getName)
                .map(name -> IC2Items.getItem("fluid_cell", name))
                .forEach(list::add);
        addItems(BlockItems.Component.values(), list);
        addItems(BlockItems.Upgrade.values(), list);
        addItems(BlockItems.CoverItem.values(), list);
        addItems(BlockItems.Smalldust.values(), list);
        addItems(BlockItems.Nugget.values(), list);
        addItems(BlockItems.Armor.values(), list);
        addItems(BlockItems.Tool.values(), list);
        addItems(BlockItems.ColorSpray.values(), list);
        addItems(BlockItems.Saw.values(), list);
        addItems(BlockItems.Hammer.values(), list);
        addItems(BlockItems.JackHammer.values(), list);
        addItems(BlockItems.Wrench.values(), list);
        addItems(BlockItems.SolderingMetal.values(), list);
        addItems(BlockItems.TurbineRotor.values(), list);
        addItems(BlockItems.File.values(), list);
        addItems(BlockItems.NuclearCoolantPack.values(), list);
        addItems(BlockItems.NuclearFuelRod.values(), list);
        Arrays.stream(BlockItems.Book.values())
                .map(BlockItems.Book::getInstance)
                .forEach(list::add);
    }

    public void addItems(IItemProvider[] values, NonNullList<ItemStack> list) {
        Arrays.stream(values)
                .map(IItemProvider::getInstance)
                .forEach(item -> item.getSubItems(this, list));
    }
}
