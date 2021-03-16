package mods.gregtechmod.init;

import ic2.api.item.IC2Items;
import ic2.api.item.IItemAPI;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackModificator {
    public static void init() {
        modifyBlockHardnessAndResistance();
        modifyItemMaxStacksize();
        addJackHammerMinableBlocks();
        modifyToolDurability();
    }

    private static void modifyToolDurability() {
        if (GregTechConfig.GENERAL.smallerWoodToolDurability) {
            GregTechMod.logger.info("Nerfing Wood Tool Durability");
            Items.WOODEN_SWORD.setMaxDamage(12);
            Items.WOODEN_PICKAXE.setMaxDamage(12);
            Items.WOODEN_SHOVEL.setMaxDamage(12);
            Items.WOODEN_AXE.setMaxDamage(12);
            Items.WOODEN_HOE.setMaxDamage(12);
        }
        if (GregTechConfig.GENERAL.smallerStoneToolDurability) {
            GregTechMod.logger.info("Nerfing Stone Tool Durability");
            Items.STONE_SWORD.setMaxDamage(48);
            Items.STONE_PICKAXE.setMaxDamage(48);
            Items.STONE_SHOVEL.setMaxDamage(48);
            Items.STONE_AXE.setMaxDamage(48);
            Items.STONE_HOE.setMaxDamage(48);
        }
    }

    private static void modifyBlockHardnessAndResistance() {
        Blocks.BRICK_BLOCK.setResistance(15);
        Blocks.IRON_BLOCK.setResistance(30);
        Blocks.DIAMOND_BLOCK.setResistance(60);
        if (GregTechConfig.GENERAL.harderStone) {
            Blocks.STONE.setHardness(16);
            Blocks.BRICK_BLOCK.setHardness(32);
            Blocks.COBBLESTONE.setHardness(12);
            Blocks.STONEBRICK.setHardness(24);
        }
    }

    private static void modifyItemMaxStacksize() {
        IC2Items.getItemAPI().getItem("upgrade").setMaxStackSize(GregTechConfig.FEATURES.upgradeStackSize);
        Items.CAKE.setMaxStackSize(64);
    }

    private static void addJackHammerMinableBlocks() {
        addJackHammerMinableBlock(Blocks.COBBLESTONE);
        addJackHammerMinableBlock(Blocks.MOSSY_COBBLESTONE);
        addJackHammerMinableBlock(Blocks.STONE, OreDictionary.WILDCARD_VALUE);
        addJackHammerMinableBlock(Blocks.SANDSTONE, OreDictionary.WILDCARD_VALUE);
        addJackHammerMinableBlock(Blocks.NETHERRACK);
        addJackHammerMinableBlock(Blocks.GLOWSTONE);
        addJackHammerMinableBlock(Blocks.NETHER_BRICK);
        addJackHammerMinableBlock(Blocks.RED_NETHER_BRICK);
        addJackHammerMinableBlock(Blocks.END_STONE);
        addJackHammerMinableBlock(Blocks.END_BRICKS);

        IItemAPI api = IC2Items.getItemAPI();
        addJackHammerMinableBlock(api.getBlock("foam"), OreDictionary.WILDCARD_VALUE);
        addJackHammerMinableBlock(api.getBlock("wall"), OreDictionary.WILDCARD_VALUE);
        GregTechAPI.JACK_HAMMER_MINABLE_BLOCKS.add(IC2Items.getItem("resource", "reinforced_stone"));
        GregTechAPI.JACK_HAMMER_MINABLE_BLOCKS.add(IC2Items.getItem("glass", "reinforced"));
        GregTechAPI.JACK_HAMMER_MINABLE_BLOCKS.add(IC2Items.getItem("reinforced_door"));
        GregTechAPI.JACK_HAMMER_MINABLE_BLOCKS.add(IC2Items.getItem("resource", "basalt"));
    }

    private static void addJackHammerMinableBlock(Block block) {
        addJackHammerMinableBlock(block, 0);
    }

    private static void addJackHammerMinableBlock(Block block, int metadata) {
        GregTechAPI.JACK_HAMMER_MINABLE_BLOCKS.add(new ItemStack(block, 1, metadata));
    }
}
