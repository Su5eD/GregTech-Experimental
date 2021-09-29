package mods.gregtechmod.init;

import com.zuxelus.energycontrol.api.EnergyControlRegister;
import com.zuxelus.energycontrol.api.IItemCard;
import com.zuxelus.energycontrol.api.IItemKit;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.BlockLightSource;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.objects.items.ItemSensorCard;
import mods.gregtechmod.objects.items.ItemSensorKit;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.IItemProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockItemLoader {
    private static final Set<net.minecraft.block.Block> BLOCKS = new LinkedHashSet<>();
    private static final Set<Item> ITEMS = new LinkedHashSet<>();
    private static final Set<Item> ITEM_BLOCKS = new LinkedHashSet<>();
    
    public static Collection<Item> getAllItems() {
        return GtUtil.mergeCollection(ITEM_BLOCKS, ITEMS);
    }
    
    public static Set<Block> getBlocks() {
        return Collections.unmodifiableSet(BLOCKS);
    }
    
    public static Set<Item> getItems() {
        return Collections.unmodifiableSet(ITEMS);
    }

    private static void registerItem(Item item) {
        if (!ITEMS.add(item)) throw new IllegalArgumentException("Duplicate registry entry: " + item.getRegistryName());
    }

    private static void registerBlock(net.minecraft.block.Block block) {
        if (!BLOCKS.add(block)) throw new IllegalArgumentException("Duplicate registry entry: " + block.getRegistryName());
    }

    private static void registerBlockItem(Block block) {
        registerBlock(block);
        Item itemBlock = new ItemBlock(block).setRegistryName(block.getRegistryName());
        if (!ITEM_BLOCKS.add(itemBlock)) throw new IllegalArgumentException("Duplicate registry entry: " + block.getRegistryName());
    }

    static void init() {
        BlockItems.classicCells = Stream.<FluidLoader.IFluidProvider>concat(
                Arrays.stream(FluidLoader.Liquid.values()),
                Arrays.stream(FluidLoader.Gas.values())
        )
                .filter(FluidLoader.IFluidProvider::hasClassicCell)
                .collect(Collectors.toMap(FluidLoader.IFluidProvider::getName, provider -> new ItemCellClassic(provider.getName(), provider.getDescription(), provider.getFluid())));
        if (FluidRegistry.isFluidRegistered("biomass")) BlockItems.classicCells.put("biomass", new ItemCellClassic("biomass", null, FluidRegistry.getFluid("biomass")));
        if (FluidRegistry.isFluidRegistered("bio.ethanol")) BlockItems.classicCells.put("bio.ethanol", new ItemCellClassic("bio.ethanol", null, FluidRegistry.getFluid("bio.ethanol")));

        registerBlock(BlockItems.lightSource = new BlockLightSource());
        Arrays.stream(BlockItems.Block.values()).map(BlockItems.Block::getBlockInstance).forEach(BlockItemLoader::registerBlockItem);
        Arrays.stream(BlockItems.Ore.values()).map(BlockItems.Ore::getBlockInstance).forEach(BlockItemLoader::registerBlockItem);
        Stream.<IItemProvider[]>of(BlockItems.Miscellaneous.values(), BlockItems.Ingot.values(), BlockItems.Plate.values(), BlockItems.Rod.values(), BlockItems.Dust.values(),
                BlockItems.Smalldust.values(), BlockItems.Nugget.values(), BlockItems.Cell.values(), BlockItems.CoverItem.values(), BlockItems.Component.values(),
                BlockItems.Upgrade.values(), BlockItems.Armor.values(), BlockItems.NuclearCoolantPack.values(), BlockItems.NuclearFuelRod.values(), BlockItems.JackHammer.values(), 
                BlockItems.Tool.values(), BlockItems.Wrench.values(), BlockItems.Hammer.values(), BlockItems.SolderingMetal.values(), BlockItems.TurbineRotor.values(),
                BlockItems.File.values(), BlockItems.Saw.values(), BlockItems.ColorSpray.values()
        )
                .flatMap(Arrays::stream)
                .map(IItemProvider::getInstance)
                .forEach(BlockItemLoader::registerItem);
        BlockItems.classicCells.values().forEach(BlockItemLoader::registerItem);
        if (Loader.isModLoaded("energycontrol")) registerEnergyControlItems();
    }

    @Optional.Method(modid = "energycontrol")
    public static void registerEnergyControlItems() {
        BlockItems.sensorKit = new ItemSensorKit();
        BlockItems.sensorCard = new ItemSensorCard();
        EnergyControlRegister.registerKit((IItemKit) BlockItems.sensorKit);
        EnergyControlRegister.registerCard((IItemCard) BlockItems.sensorCard);
    }
}
