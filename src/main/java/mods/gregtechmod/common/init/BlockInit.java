package mods.gregtechmod.common.init;

import mods.gregtechmod.common.objects.blocks.BlockBase;
import mods.gregtechmod.common.objects.blocks.BlockFluid;
import mods.gregtechmod.common.objects.blocks.ConnectedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
    public static List<Block> BLOCKS = new ArrayList<Block>();
    //TODO: move to materials class
    //TODO: Adjust hardnesses
    //BLOCKS
    public static Block BLOCK_ELECTRUM = new BlockBase("block_electrum", Material.IRON, 4F, 30F);
    public static Block BLOCK_ZINC = new BlockBase("block_zinc", Material.IRON, 3.5F, 30F);
    public static Block BLOCK_OLIVINE = new BlockBase("block_olivine", Material.IRON, 4.5F, 30F);
    public static Block BLOCK_GREEN_SAPPHIRE = new BlockBase("block_green_sapphire", Material.IRON, 4.5F, 30F);
    public static Block BLOCK_PLATINUM = new BlockBase("block_platinum", Material.IRON, 4F, 30F);
    public static Block BLOCK_TUNGSTEN = new BlockBase("block_tungsten", Material.IRON, 4.5F, 100F);
    public static Block BLOCK_NICKEL = new BlockBase("block_nickel", Material.IRON, 3F, 45F);
    public static Block BLOCK_TUNGSTENSTEEL = new ConnectedBlock("block_tungstensteel", Material.IRON, 100F); //TODO: resistance
    public static Block BLOCK_IRIDIUM_REINFORCED_TUNGSTENSTEEL = new ConnectedBlock("block_iridium_reinforced_tungstensteel", Material.IRON, 200F);
    public static Block BLOCK_IRIDIUM = new BlockBase("block_iridium", Material.IRON, 3.5F, 600F);
    public static Block BLOCK_OSMIUM = new BlockBase("block_osmium", Material.IRON, 4F, 900F);
    public static Block BLOCK_INVAR = new BlockBase("block_invar", Material.IRON, 4.5F, 30F);
    public static Block BLOCK_ADVANCED_MACHINE = new BlockBase("block_advanced_machine", Material.IRON, 4F, 30F);
    public static Block BLOCK_ALUMINIUM = new BlockBase("block_aluminium", Material.IRON, 3F, 30F);
    public static Block BLOCK_FUSION_COIL = new BlockBase("block_fusion_coil", Material.IRON, 4F, 30F);
    public static Block BLOCK_IRIDIUM_REINFORCED_STONE = new BlockBase("block_iridium_reinforced_stone", Material.IRON, 100F, 300F);
    public static Block BLOCK_LESUBLOCK = new BlockBase("block_lesublock", Material.IRON, 4F, 30F);
    public static Block BLOCK_RUBY = new BlockBase("block_ruby", Material.IRON, 4.5F, 30F);
    public static Block BLOCK_SAPPHIRE = new BlockBase("block_sapphire", Material.IRON, 4.5F, 30F);
    public static Block BLOCK_TITANIUM = new BlockBase("block_titanium", Material.IRON, 10F, 200F);
    public static Block BLOCK_CHROME = new BlockBase("block_chrome", Material.IRON, 10F, 100F);
    public static Block BLOCK_HIGHLY_ADVANCED_MACHINE = new BlockBase("block_highly_advanced_machine", Material.IRON, 10F, 100F);
    public static Block BLOCK_BRASS = new BlockBase("block_brass", Material.IRON, 3.5F, 30F);

    public static Block BLOCK_STANDARD_MACHINE_CASING = new ConnectedBlock("block_standard_machine_casing", Material.IRON, 4F);
    public static Block BLOCK_REINFORCED_MACHINE_CASING = new ConnectedBlock("block_reinforced_machine_casing", Material.IRON, 4F);
    public static Block BLOCK_ADVANCED_MACHINE_CASING = new ConnectedBlock("block_advanced_machine_casing", Material.IRON, 4F);

    //FLUIDS
    public static Block SEED_OIL_BLOCK = new BlockFluid("seed_oil", FluidInit.SEED_OIL, Material.WATER);
}
