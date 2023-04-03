package dev.su5ed.gtexperimental.compat;

import cofh.lib.api.fluid.IFluidStackHolder;
import cofh.lib.api.inventory.IItemStackHolder;
import cofh.lib.inventory.ItemStackHolder;
import cofh.thermal.core.util.managers.machine.PulverizerRecipeManager;
import cofh.thermal.lib.util.recipes.EmptyMachineProperties;
import cofh.thermal.lib.util.recipes.IMachineInventory;
import cofh.thermal.lib.util.recipes.MachineProperties;
import cofh.thermal.lib.util.recipes.internal.IMachineRecipe;
import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Map;

public final class ThermalHandler {
    private static final Map<DyeColor, Block> COLOR_ROCKWOOLS;

    static {
        COLOR_ROCKWOOLS = StreamEx.of(DyeColor.values())
            .mapToEntry(color -> new ResourceLocation(ModHandler.THERMAL_MODID, color.getName() + "_rockwool"))
            .mapValues(ForgeRegistries.BLOCKS::getValue)
            .toImmutableMap();
    }

    public static boolean canColorRockwool(Block block, DyeColor color) {
        return COLOR_ROCKWOOLS.containsValue(block) && COLOR_ROCKWOOLS.get(color) != block;
    }

    public static Block getRockWool(DyeColor color) {
        return COLOR_ROCKWOOLS.get(color);
    }

    public static RecipeProvider<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack> pulverizerRecipeTranslator() {
        return new TranslatingRecipeProvider<>("pulverizer", (level, stack) -> PulverizerRecipeManager.instance().validRecipe(stack),
            (level, input) -> {
                IMachineInventory inventory = new SimpleThermalInventory(input);
                IMachineRecipe recipe = PulverizerRecipeManager.instance().getRecipe(inventory);
                return recipe != null ? Pair.of(inventory, recipe) : null;
            },
            (stack, recipe) -> GtUtil.itemName(stack) + "_to_" + GtUtil.itemName(recipe.getSecond().getOutputItems(SimpleThermalInventory.EMPTY).get(0)),
            (ResourceLocation id, ItemStack stack, Pair<IMachineInventory, IMachineRecipe> pair) -> {
                IMachineInventory inventory = pair.getFirst();
                IMachineRecipe recipe = pair.getSecond();
                ItemStack recipeInput = recipe.getInputItems().get(0);
                RecipeIngredient<ItemStack> ingredient = ModRecipeIngredientTypes.ITEM.of(recipeInput);
                List<ItemStack> output = recipe.getOutputItems(inventory);
                List<Float> chances = recipe.getOutputItemChances(inventory);
                int chance = chances.size() > 1 ? (int) (Math.min(chances.get(1), 1) * 100) : 0;
                return SIMORecipe.pulverizer(id, ingredient, output, RecipePropertyMap.builder().energyCost(3).chance(chance).build());
            });
    }

    private record SimpleThermalInventory(List<? extends IItemStackHolder> inputSlots) implements IMachineInventory {
        public static final IMachineInventory EMPTY = new SimpleThermalInventory(List.of());

        public SimpleThermalInventory(ItemStack stack) {
            this(List.of(new ItemStackHolder(stack)));
        }

        @Override
        public List<? extends IFluidStackHolder> inputTanks() {
            return List.of();
        }

        @Override
        public MachineProperties getMachineProperties() {
            return EmptyMachineProperties.INSTANCE;
        }
    }
}
