package mods.gregtechmod.init;

import ic2.api.item.IC2Items;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.recipe.crafting.ToolCraftingRecipeShaped;
import mods.gregtechmod.recipe.crafting.ToolOreIngredient;
import mods.gregtechmod.util.OreDictUnificator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;

public class CraftingRecipeLoader {

    public static void init() {
        GregTechMod.logger.info("Loading static crafting recipes");
        
        ModHandler.removeCraftingRecipeFromInputs(new ItemStack(Items.WATER_BUCKET), ModHandler.emptyCell);
        ModHandler.removeCraftingRecipeFromInputs(new ItemStack(Items.LAVA_BUCKET), ModHandler.emptyCell);
                
        ItemStack glassFibreCable = IC2Items.getItem("cable", "type:glass,insulation:0");
        if (GregTechMod.classic) {
            if (GregTechConfig.DISABLED_RECIPES.depletedUranium8) {
                ModHandler.removeCraftingRecipeFromInputs(ModHandler.emptyCell, ModHandler.emptyCell, ModHandler.emptyCell, ModHandler.emptyCell, IC2Items.getItem("ingot", "uranium"), ModHandler.emptyCell, ModHandler.emptyCell, ModHandler.emptyCell, ModHandler.emptyCell);
            }
            ModHandler.removeCraftingRecipeFromInputs(IC2Items.getItem("crafting", "compressed_plants"), ModHandler.emptyCell);
            ModHandler.removeCraftingRecipeFromInputs(IC2Items.getItem("crafting", "compressed_hydrated_coal"), ModHandler.emptyCell);
            
            ModHandler.removeCraftingRecipe(glassFibreCable);
            ModHandler.addShapedRecipe(
                    "glassFibreCable",
                    StackUtil.copyWithSize(glassFibreCable, 4),
                    getCableRecipeIngredients("dustRedstone", null)
            );
            ModHandler.addShapedRecipe(
                    "glassFibreCableSilver",
                    StackUtil.copyWithSize(glassFibreCable, 6),
                    getCableRecipeIngredients("ingotSilver", "dustSilver")
            );
        }
        ModHandler.addShapedRecipe(
                "glassFibreCableElectrum",
                StackUtil.copyWithSize(glassFibreCable, 8),
                getCableRecipeIngredients("ingotElectrum", "dustElectrum")
        );

        ItemStack ingotCopper = IC2Items.getItem("ingot", "copper");
        ItemStack ingotTin = IC2Items.getItem("ingot", "tin");
        Ingredient ingotCopperIngredient = new OreIngredient("ingotCopper");
        ModHandler.removeCraftingRecipeFromInputs(ingotCopper, ingotCopper, ItemStack.EMPTY, ingotCopper, ingotTin)
                .ifPresent(output -> {
                    GregTechMod.logger.info("Changing Forestry's Bronze Recipe");
                    int count = GregTechConfig.DISABLED_RECIPES.bronzeIngotCrafting ? 1 : 2;
                    ModHandler.addShapelessRecipe(
                            "ingotBronze",
                            null,
                            StackUtil.setSize(IC2Items.getItem("ingot", "bronze"), count),
                            ingotCopperIngredient, ingotCopperIngredient, ingotCopperIngredient, new OreIngredient("ingotTin")
                    );
                });

        if (GregTechConfig.DISABLED_RECIPES.enchantingTable) {
            GregTechMod.logger.info("Removing the recipe of the Enchantment Table, to have more fun at enchanting with the Anvil and Books from Dungeons.");
            ModHandler.removeCraftingRecipe(new ItemStack(Blocks.ENCHANTING_TABLE));
        }
        if (GregTechConfig.DISABLED_RECIPES.enderChest) ModHandler.removeCraftingRecipe(new ItemStack(Blocks.ENDER_CHEST));
        if (!GregTechAPI.getDynamicConfig("storage_block_crafting", "blockGlowstone", false)) {
            ItemStack dustGlowstone = new ItemStack(Items.GLOWSTONE_DUST);
            ModHandler.removeCraftingRecipeFromInputs(dustGlowstone, dustGlowstone, ItemStack.EMPTY, dustGlowstone, dustGlowstone);
        }

        ModHandler.removeCraftingRecipe(IC2Items.getItem("ingot", "alloy"));

        GregTechMod.logger.info("Adding 'The holy Planks of Sengir'");
        ItemStack holyPlanks = new ItemStack(BlockItems.Plate.WOOD.getInstance());
        holyPlanks.setStackDisplayName("The holy Planks of Sengir");
        holyPlanks.addEnchantment(Enchantments.SMITE, 10);
        GameRegistry.addShapedRecipe(
                new ResourceLocation(Reference.MODID, "holy_planks"),
                null,
                holyPlanks,
                "XXX", "XDX", "XXX", 'X', new ItemStack(Items.NETHER_STAR), 'D', new ItemStack(Blocks.DRAGON_EGG)
        );
        
        ModHandler.addShapedRecipe(
                "doubleInsulatedGoldCable",
                StackUtil.copyWithSize(IC2Items.getItem("cable", "type:gold,insulation:2"), 4),
                "RRR", "RGR", "RRR", 'G', "ingotGold", 'R', "itemRubber"
        );
        
        ItemStack dustSulfur = OreDictUnificator.get("dustSulfur");
        ModHandler.removeCraftingRecipeFromInputs(dustSulfur, dustSulfur, dustSulfur, dustSulfur, new ItemStack(Items.COAL), dustSulfur, dustSulfur, dustSulfur, dustSulfur);
        ModHandler.removeCraftingRecipeFromInputs(dustSulfur, dustSulfur, dustSulfur, dustSulfur, new ItemStack(Items.COAL, 1, 1), dustSulfur, dustSulfur, dustSulfur, dustSulfur);
        ItemStack seeds = new ItemStack(Items.WHEAT_SEEDS);
        ModHandler.removeCraftingRecipeFromInputs(seeds, seeds, seeds, seeds, ItemStack.EMPTY, seeds, seeds, seeds, seeds);

        if (GregTechAPI.getDynamicConfig("harder_recipes", "wind_generator", true)) ModHandler.removeCraftingRecipe(IC2Items.getItem("te", "wind_generator"));
        if (GregTechAPI.getDynamicConfig("harder_recipes", "water_generator", true)) ModHandler.removeCraftingRecipe(IC2Items.getItem("te", "water_generator"));
        if (GregTechAPI.getDynamicConfig("harder_recipes", "solar_generator", true)) ModHandler.removeCraftingRecipe(IC2Items.getItem("te", "solar_generator"));

        ItemStack planks = new ItemStack(Blocks.PLANKS);
        ModHandler.removeCraftingRecipeFromInputs(planks, ItemStack.EMPTY, ItemStack.EMPTY, planks)
                .ifPresent(output -> {
                    IRecipe sawingRecipe = ToolCraftingRecipeShaped.makeSawingRecipe(
                            output,
                            "S", "P", "P", 'P', "plankWood", 'S', ToolOreIngredient.saw()
                    ).setRegistryName(new ResourceLocation(Reference.MODID, "planks_sawing"));
                    ForgeRegistries.RECIPES.register(sawingRecipe);
                                
                    ModHandler.addShapedRecipe(
                            "sticksFromPlanks",
                            StackUtil.copyWithSize(output, GregTechConfig.GENERAL.woodNeedsSawForCrafting ? output.getCount() / 2 : output.getCount()),
                            "P", "P", 'P', "plankWood"
                    );
                });
    }
    
    private static Object[] getCableRecipeIngredients(String classicMaterial, String expMaterial) {
        ItemStack glass = new ItemStack(Blocks.GLASS);
        Object x = GregTechMod.classic ? classicMaterial : IC2Items.getItem("dust", "energium");
        String d = GregTechMod.classic ? "itemDiamond" : expMaterial;
        
        return new Object[] { "GGG", "XDX", "GGG", 'G', glass, 'X', x, 'D', d };
    }
}
