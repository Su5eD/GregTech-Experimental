package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BlastFurnaceRecipe extends MIMORecipe {

    public BlastFurnaceRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, int duration, int heat) {
        this(id, inputs, outputs, RecipePropertyMap.builder()
            .duration(duration)
            .heat(heat)
            .build());
    }

    public BlastFurnaceRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, RecipePropertyMap properties) {
        super(ModRecipeTypes.BLAST_FURNACE.get(), ModRecipeSerializers.BLAST_FURNACE.get(), id, inputs, outputs, properties);
    }

    @Override
    public double getEnergyCost() {
        return 128;
    }

    public int getHeat() {
        return this.properties.get(ModRecipeProperty.HEAT);
    }
}
