package mods.gregtechmod.init;

import ic2.api.item.IC2Items;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.objects.BlockItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;

public class CraftingRecipeLoader {

    public static void init() {
        GregTechAPI.logger.info("Loading static crafting recipes");

        if (IC2.version.isClassic()) {
            if (GregTechConfig.DISABLED_RECIPES.depletedUranium8) {
                ModHandler.removeCraftingRecipeFromInputs(ModHandler.emptyCell, ModHandler.emptyCell, ModHandler.emptyCell, ModHandler.emptyCell, IC2Items.getItem("ingot", "uranium"), ModHandler.emptyCell, ModHandler.emptyCell, ModHandler.emptyCell, ModHandler.emptyCell);
            }
            ModHandler.removeCraftingRecipeFromInputs(IC2Items.getItem("crafting", "compressed_plants"), ModHandler.emptyCell);
            ModHandler.removeCraftingRecipeFromInputs(IC2Items.getItem("crafting", "compressed_hydrated_coal"), ModHandler.emptyCell);
            ModHandler.removeCraftingRecipe(ModHandler.emptyCell);
            ModHandler.addShapedRecipe(
                    "emptyCell",
                    null,
                    StackUtil.copyWithSize(ModHandler.emptyCell, 4),
                    " T ", "T T", " T ", 'T', GregTechAPI.getDynamicConfig("harder_recipes", "tin_cells_from_plates", true, "Classic Profile only") ? "plateTin" : "ingotTin"
            );
            if (GregTechAPI.getDynamicConfig("harder_recipes", "machine_block", true, "Classic Profile only")) {
                ItemStack machineBlock = IC2Items.getItem("resource", "machine");
                ModHandler.removeCraftingRecipe(machineBlock);
                ModHandler.addShapedRecipe(
                        "machineBlock",
                        null,
                        machineBlock,
                        "RRR", "RWR", "RRR", 'R', "plateRefinedIron", 'W', "craftingToolWrench"
                );
            }
            ModHandler.addShapedRecipe(
                    "piston_refined_iron",
                    null,
                    new ItemStack(Blocks.PISTON),
                    "WWW", "CBC", "CRC", 'W', "plankWood", 'C', "stoneCobble", 'R', "dustRedstone", 'B', "ingotRefinedIron"
            );
        }

        ModHandler.removeCraftingRecipeFromInputs(new ItemStack(Items.WATER_BUCKET), ModHandler.emptyCell);
        ModHandler.removeCraftingRecipeFromInputs(new ItemStack(Items.LAVA_BUCKET), ModHandler.emptyCell);

        ItemStack ingotCopper = IC2Items.getItem("ingot", "copper");
        ItemStack ingotTin = IC2Items.getItem("ingot", "tin");
        Ingredient ingotCopperIngredient = new OreIngredient("ingotCopper");
        if (!ModHandler.removeCraftingRecipeFromInputs(ingotCopper, ingotCopper, ItemStack.EMPTY, ingotCopper, ingotTin).isEmpty()) {
            GregTechAPI.logger.info("Changing Forestry's Bronze Recipe");
            int count = GregTechConfig.DISABLED_RECIPES.bronzeIngotCrafting ? 1 : 2;
            ModHandler.addShapelessRecipe(
                    "ingotBronze",
                    null,
                    StackUtil.setSize(IC2Items.getItem("ingot", "bronze"), count),
                    ingotCopperIngredient, ingotCopperIngredient, ingotCopperIngredient, new OreIngredient("ingotTin")
            );
        }

        if (GregTechConfig.DISABLED_RECIPES.enchantingTable) {
            GregTechAPI.logger.info("Removing the recipe of the Enchantment Table, to have more fun at enchanting with the Anvil and Books from Dungeons.");
            ModHandler.removeCraftingRecipe(new ItemStack(Blocks.ENCHANTING_TABLE));
        }
        if (GregTechConfig.DISABLED_RECIPES.enderChest) ModHandler.removeCraftingRecipe(new ItemStack(Blocks.ENDER_CHEST));
        if (!GregTechAPI.getDynamicConfig("storage_block_crafting", "blockGlowstone", false)) {
            ItemStack dustGlowstone = new ItemStack(Items.GLOWSTONE_DUST);
            ModHandler.removeCraftingRecipeFromInputs(dustGlowstone, dustGlowstone, ItemStack.EMPTY, dustGlowstone, dustGlowstone);
        }

        ModHandler.removeCraftingRecipe(IC2Items.getItem("ingot", "alloy"));

        GregTechAPI.logger.info("Adding 'The holy Planks of Sengir'");
        ItemStack holyPlanks = new ItemStack(BlockItems.Plate.WOOD.getInstance());
        holyPlanks.setStackDisplayName("The holy Planks of Sengir");
        holyPlanks.addEnchantment(Enchantments.SMITE, 10);
        GameRegistry.addShapedRecipe(
                new ResourceLocation(Reference.MODID, "holy_planks"),
                null,
                holyPlanks,
                "XXX", "XDX", "XXX", 'X', new ItemStack(Items.NETHER_STAR), 'D', new ItemStack(Blocks.DRAGON_EGG)
        );

        ItemStack glass = new ItemStack(Blocks.GLASS);
        ModHandler.addShapedRecipe(
                "doubleInsulaedGoldCable",
                null,
                StackUtil.setSize(IC2Items.getItem("cable", "type:gold,insulation:2"), 4),
                "RRR", "RGR", "RRR", 'G', "ingotGold", 'R', "itemRubber"
        );
        ItemStack glassFibreCable = IC2Items.getItem("cable", "type:glass,insulation:0");
        ModHandler.addShapedRecipe(
                "glassFibreCable",
                null,
                StackUtil.setSize(glassFibreCable, 4),
                "GGG", "XDX", "GGG", 'G', glass, 'X', "dustRedstone", 'D', "itemDiamond"
        );
        ModHandler.addShapedRecipe(
                "glassFibreCable",
                null,
                StackUtil.setSize(glassFibreCable, 6),
                "GGG", "XDX", "GGG", 'G', glass, 'X', "ingotSilver", 'D', "itemDiamond"
        );
        ModHandler.addShapedRecipe(
                "glassFibreCable",
                null,
                StackUtil.setSize(glassFibreCable, 8),
                "GGG", "XDX", "GGG", 'G', glass, 'X', "ingotElectrum", 'D', "itemDiamond"
        );
        ItemStack dustSulfur = OreDictUnificator.get("dustSulfur");
        ModHandler.removeCraftingRecipeFromInputs(dustSulfur, dustSulfur, dustSulfur, dustSulfur, new ItemStack(Items.COAL), dustSulfur, dustSulfur, dustSulfur, dustSulfur);
        ModHandler.removeCraftingRecipeFromInputs(dustSulfur, dustSulfur, dustSulfur, dustSulfur, new ItemStack(Items.COAL, 1, 1), dustSulfur, dustSulfur, dustSulfur, dustSulfur);
        ItemStack seeds = new ItemStack(Items.WHEAT_SEEDS);
        ModHandler.removeCraftingRecipeFromInputs(seeds, seeds, seeds, seeds, ItemStack.EMPTY, seeds, seeds, seeds, seeds);

        if (GregTechAPI.getDynamicConfig("harder_recipes", "wind_generator", true)) ModHandler.removeCraftingRecipe(IC2Items.getItem("te", "wind_generator"));
        if (GregTechAPI.getDynamicConfig("harder_recipes", "water_generator", true)) ModHandler.removeCraftingRecipe(IC2Items.getItem("te", "water_generator"));
        if (GregTechAPI.getDynamicConfig("harder_recipes", "solar_generator", true)) ModHandler.removeCraftingRecipe(IC2Items.getItem("te", "solar_generator"));
    }
}
