package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.ModRecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

/**
 * Multi Input, Multi Output recipe
 */
public class MIMORecipe extends ModRecipe<MIMORecipeType<?>, ListRecipeIngredient<ItemStack>, List<ItemStack>, List<ItemStack>, MIMORecipe> {
    public static MIMORecipe canningMachine(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, RecipePropertyMap properties) {
        return new MIMORecipe(ModRecipeTypes.CANNING_MACHINE.get(), ModRecipeSerializers.CANNING_MACHINE.get(), id, ModRecipeTypes.CANNING_MACHINE.get().getInputType().createIngredient(inputs), outputs, properties);
    }

    public static MIMORecipe blastFurnace(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, RecipePropertyMap properties) {
        return new MIMORecipe(ModRecipeTypes.BLAST_FURNACE.get(), ModRecipeSerializers.BLAST_FURNACE.get(), id, ModRecipeTypes.BLAST_FURNACE.get().getInputType().createIngredient(inputs), outputs, properties.withTransient(b -> b.energyCost(128)));
    }

    public MIMORecipe(MIMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, ListRecipeIngredient<ItemStack> input, List<ItemStack> output, RecipePropertyMap properties) {
        super(type, serializer, id, input, output, properties);
    }
}
