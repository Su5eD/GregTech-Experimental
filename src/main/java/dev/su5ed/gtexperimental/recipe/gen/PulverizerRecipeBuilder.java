package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.gen.compat.IC2MachineRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.gen.compat.RCCrusherRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.gen.compat.TEMachineRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.NOT_IC2_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.NOT_RAILCRAFT_LOADED;

public class PulverizerRecipeBuilder extends SIMORecipeBuilder<ItemStack, List<ItemStack>> {
    private static final ItemStack OBSIDIAN = new ItemStack(Items.OBSIDIAN);

    public PulverizerRecipeBuilder(SIMORecipe<ItemStack, List<ItemStack>> recipe) {
        super(recipe);
    }

    @Override
    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeName recipeId) {
        build(finishedRecipeConsumer, recipeId, true);
    }

    @Override
    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeName recipeId, boolean universal) {
        super.build(finishedRecipeConsumer, recipeId, universal);

        if (universal) {
            RecipeIngredient<ItemStack> input = this.recipe.getInput();
            Ingredient ingredient = input.asIngredient();
            List<ItemStack> output = this.recipe.getOutput();
            ItemStack primaryOutput = output.get(0);
            int chance = this.recipe.getProperty(ModRecipeProperty.CHANCE);
            int actualChance = output.size() >= 2 ? Math.max(chance, 10) : 0;

            if (!this.conditions.contains(NOT_IC2_LOADED)) {
                new IC2MachineRecipeBuilder(IC2MachineRecipeBuilder.MACERATOR, ingredient, input.getCount(), output.get(0))
                    .addConditions(this.conditions)
                    .build(finishedRecipeConsumer, recipeId.toForeign(IC2MachineRecipeBuilder.MACERATOR));
            }

            if (!primaryOutput.is(Dust.WOOD.getItem()) && !primaryOutput.is(Smalldust.WOOD.getItem())) {
                if (!input.test(OBSIDIAN) && !this.conditions.contains(NOT_RAILCRAFT_LOADED)) {
                    List<RCCrusherRecipeBuilder.Output> crusherOutput = new ArrayList<>();

                    crusherOutput.add(new RCCrusherRecipeBuilder.Output(output.get(0), 1 / (double) input.getCount()));
                    if (output.size() > 1) {
                        crusherOutput.add(new RCCrusherRecipeBuilder.Output(output.get(1).copy(), 0.01 * (chance <= 0 ? 10 : chance) / input.getCount()));
                    }
                    new RCCrusherRecipeBuilder(ingredient, crusherOutput, 100)
                        .addConditions(this.conditions)
                        .build(finishedRecipeConsumer, recipeId.toForeign(RCCrusherRecipeBuilder.TYPE));
                }
                new TEMachineRecipeBuilder(TEMachineRecipeBuilder.PULVERIZER, ingredient, input.getCount(), output, actualChance, 0)
                    .addConditions(this.conditions)
                    .build(finishedRecipeConsumer, recipeId.toForeign(TEMachineRecipeBuilder.PULVERIZER));
            }
            else {
                new TEMachineRecipeBuilder(TEMachineRecipeBuilder.SAWMILL, ingredient, input.getCount(), output, actualChance, 800)
                    .addConditions(this.conditions)
                    .build(finishedRecipeConsumer, recipeId.toForeign(TEMachineRecipeBuilder.SAWMILL));
            }
        }
    }
}
