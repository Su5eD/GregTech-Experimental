package mods.gregtechmod.init;

import ic2.api.item.IC2Items;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemStackModificator {

    public static void init() {
        modifyBlockHardnessAndResistance();
        modifyItemMaxStacksize();
        addJackHammerMinableBlocks();
        modifyToolDurability();
    }

    private static void modifyToolDurability() {
        if (GregTechConfig.GENERAL.smallerWoodToolDurability) {
            GregTechMod.LOGGER.info("Nerfing Wood Tool Durability");
            Items.WOODEN_SWORD.setMaxDamage(12);
            Items.WOODEN_PICKAXE.setMaxDamage(12);
            Items.WOODEN_SHOVEL.setMaxDamage(12);
            Items.WOODEN_AXE.setMaxDamage(12);
            Items.WOODEN_HOE.setMaxDamage(12);
        }
        if (GregTechConfig.GENERAL.smallerStoneToolDurability) {
            GregTechMod.LOGGER.info("Nerfing Stone Tool Durability");
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
        ModHandler.ic2ItemApi.getItem("upgrade").setMaxStackSize(GregTechConfig.FEATURES.upgradeStackSize);
        Items.CAKE.setMaxStackSize(64);
    }

    private static void addJackHammerMinableBlocks() {
        Collection<ItemStack> blocks = Stream.of(
                Stream.of(
                    Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.END_STONE,
                    Blocks.GLOWSTONE, Blocks.NETHER_BRICK, Blocks.RED_NETHER_BRICK, Blocks.END_BRICKS
                ).map(ItemStack::new),
                Stream.of(
                    Blocks.STONE, Blocks.SANDSTONE, ModHandler.ic2ItemApi.getBlock("foam"), ModHandler.ic2ItemApi.getBlock("wall")
                ).map(block -> new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE)),
                Stream.of(
                    IC2Items.getItem("resource", "reinforced_stone"), IC2Items.getItem("glass", "reinforced"),
                    IC2Items.getItem("reinforced_door"), IC2Items.getItem("resource", "basalt")
                )
            )
            .flatMap(Function.identity())
            .collect(Collectors.toList());
        GregTechAPI.instance().addJackHammerMinableBlocks(blocks);
    }
}
