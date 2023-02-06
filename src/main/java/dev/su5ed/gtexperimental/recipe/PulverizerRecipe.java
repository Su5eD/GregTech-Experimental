package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PulverizerRecipe extends SIMORecipe<ItemStack> {

    public PulverizerRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> outputs, double energyCost, int chance) {
        super(ModRecipeTypes.PULVERIZER.get(), ModRecipeSerializers.PULVERIZER.get(), id, input, outputs, RecipePropertyMap.builder()
            .energyCost(energyCost)
            .chance(chance)
            .build());
    }

    public PulverizerRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> output, RecipePropertyMap properties) {
        super(ModRecipeTypes.PULVERIZER.get(), ModRecipeSerializers.PULVERIZER.get(), id, input, output, properties);
    }
    
    public int getChance() {
        return this.properties.get(ModRecipeProperty.CHANCE);
    }

    @Override
    public int getDuration() {
        return 300 * this.input.getCount();
    }
}
