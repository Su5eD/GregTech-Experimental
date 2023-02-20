package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ImplosionRecipe extends SIMORecipe<ItemStack, ItemStack> {

    public ImplosionRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> outputs, int tnt) {
        super(ModRecipeTypes.IMPLOSION.get(), ModRecipeSerializers.IMPLOSION.get(), id, input, outputs, RecipePropertyMap.builder()
            .tnt(tnt)
            .build());
    }

    public ImplosionRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> output, RecipePropertyMap properties) {
        super(ModRecipeTypes.IMPLOSION.get(), ModRecipeSerializers.IMPLOSION.get(), id, input, output, properties);
    }

    @Override
    public int getDuration() {
        return 20;
    }

    @Override
    public double getEnergyCost() {
        return 32;
    }
}
