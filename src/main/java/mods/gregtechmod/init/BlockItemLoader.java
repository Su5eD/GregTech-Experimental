package mods.gregtechmod.init;

import com.zuxelus.energycontrol.api.EnergyContolRegister;
import com.zuxelus.energycontrol.api.IItemCard;
import com.zuxelus.energycontrol.api.IItemKit;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.objects.blocks.BlockLightSource;
import mods.gregtechmod.objects.items.ItemSensorCard;
import mods.gregtechmod.objects.items.ItemSensorKit;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockItemLoader {
    static final Set<Block> BLOCKS = new LinkedHashSet<>();
    static final Set<Item> ITEMS = new LinkedHashSet<>();

    private static Item registerItem(Item item) {
        if (!ITEMS.add(item)) throw new IllegalStateException("Duplicate registry entry: "+item.getRegistryName());
        return item;
    }

    private static Block registerBlock(Block block) {
        if (!BLOCKS.add(block)) throw new IllegalStateException("Duplicate registry entry: "+block.getRegistryName());
        return block;
    }

    private static Block registerBlockItem(Block block) {
        registerBlock(block);
        registerItem(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }

    static void init() {
        BlockItems.lightSource = registerBlock(new BlockLightSource());
        Arrays.stream(BlockItems.Armor.values()).forEach(armor -> registerItem(armor.getInstance()));
        Arrays.stream(BlockItems.Blocks.values()).forEach(block -> registerBlockItem(block.getInstance()));
        Arrays.stream(BlockItems.Cells.values()).forEach(cell -> registerItem(cell.getInstance()));
        Arrays.stream(BlockItems.ColorSprays.values()).forEach(spray -> registerItem(spray.getInstance()));
        Arrays.stream(BlockItems.Components.values()).forEach(component -> registerItem(component.getInstance()));
        Arrays.stream(BlockItems.Covers.values()).forEach(coverItem -> registerItem(coverItem.getInstance()));
        Arrays.stream(BlockItems.Dusts.values()).forEach(dust -> registerItem(dust.getInstance()));
        Arrays.stream(BlockItems.Files.values()).forEach(file -> registerItem(file.getInstance()));
        Arrays.stream(BlockItems.Hammers.values()).forEach(hammer -> registerItem(hammer.getInstance()));
        Arrays.stream(BlockItems.Ingots.values()).forEach(ingot -> registerItem(ingot.getInstance()));
        Arrays.stream(BlockItems.JackHammers.values()).forEach(jackHammer -> registerItem(jackHammer.getInstance()));
        Arrays.stream(BlockItems.Miscellaneous.values()).forEach(misc -> registerItem(misc.getInstance()));
        Arrays.stream(BlockItems.NuclearCoolantPacks.values()).forEach(pack -> registerItem(pack.getInstance()));
        Arrays.stream(BlockItems.NuclearFuelRods.values()).forEach(nuclearRod -> registerItem(nuclearRod.getInstance()));
        Arrays.stream(BlockItems.Nuggets.values()).forEach(nugget -> registerItem(nugget.getInstance()));
        Arrays.stream(BlockItems.Ores.values()).forEach(block -> registerBlockItem(block.getInstance()));
        Arrays.stream(BlockItems.Plates.values()).forEach(plate -> registerItem(plate.getInstance()));
        Arrays.stream(BlockItems.Rods.values()).forEach(rod -> registerItem(rod.getInstance()));
        Arrays.stream(BlockItems.Saws.values()).forEach(saw -> registerItem(saw.getInstance()));
        Arrays.stream(BlockItems.Smalldusts.values()).forEach(smallDust -> registerItem(smallDust.getInstance()));
        Arrays.stream(BlockItems.SolderingMetals.values()).forEach(solderingMetal -> registerItem(solderingMetal.getInstance()));
        Arrays.stream(BlockItems.Tools.values()).forEach(tool -> registerItem(tool.getInstance()));
        Arrays.stream(BlockItems.TurbineRotors.values()).forEach(rotor -> registerItem(rotor.getInstance()));
        Arrays.stream(BlockItems.Upgrades.values()).forEach(upgrade -> registerItem(upgrade.getInstance()));
        Arrays.stream(BlockItems.Wrenches.values()).forEach(wrench -> registerItem(wrench.getInstance()));
        if (Loader.isModLoaded("energycontrol")) registerEnergyControlItems();

        GregTechObjectAPI.setItemMap(ITEMS.stream().collect(Collectors.toMap(value -> value.getRegistryName().toString().split(":")[1], ItemStack::new)));
        GregTechObjectAPI.setBlockMap(BLOCKS.stream().collect(Collectors.toMap(value -> value.getRegistryName().toString().split(":")[1], value -> value)));
    }

    @Optional.Method(modid = "energycontrol")
    public static void registerEnergyControlItems() {
        BlockItems.sensorKit = new ItemSensorKit();
        BlockItems.sensorCard = new ItemSensorCard();
        EnergyContolRegister.registerKit((IItemKit) BlockItems.sensorKit);
        EnergyContolRegister.registerCard((IItemCard) BlockItems.sensorCard);
    }
}
