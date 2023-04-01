package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.CompositeRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeImpl;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;

public class IFMORecipe extends BaseRecipeImpl<IFMORecipeType<?>, CompositeRecipeIngredient<IFMORecipe.Input>, IFMORecipe.Input, List<ItemStack>, IFMORecipe> {

    public static IFMORecipe industrialGrinder(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs, RecipePropertyMap properties) {
        return industrialGrinder(id, ModRecipeTypes.INDUSTRIAL_GRINDER.get().getInputType().createIngredient(Map.of(
            "item", input,
            "fluid", fluid
        )), outputs, properties.withTransient(b -> b.duration(input.getCount() * 100).energyCost(128)));
    }

    public static IFMORecipe industrialGrinder(ResourceLocation id, CompositeRecipeIngredient<IFMORecipe.Input> input, List<ItemStack> outputs, RecipePropertyMap properties) {
        return new IFMORecipe(ModRecipeTypes.INDUSTRIAL_GRINDER.get(), ModRecipeSerializers.INDUSTRIAL_GRINDER.get(), id, input, outputs, properties.withTransient(b -> b.duration(input.getCount() * 100).energyCost(128)));
    }

    public static IFMORecipe industrialSawmill(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs, RecipePropertyMap properties) {
        return industrialSawmill(id, ModRecipeTypes.INDUSTRIAL_GRINDER.get().getInputType().createIngredient(Map.of(
            "item", input,
            "fluid", fluid
        )), outputs, properties.withTransient(b -> b.duration(input.getCount() * 200).energyCost(32)));
    }

    public static IFMORecipe industrialSawmill(ResourceLocation id, CompositeRecipeIngredient<IFMORecipe.Input> input, List<ItemStack> outputs, RecipePropertyMap properties) {
        return new IFMORecipe(ModRecipeTypes.INDUSTRIAL_SAWMILL.get(), ModRecipeSerializers.INDUSTRIAL_SAWMILL.get(), id, input, outputs, properties.withTransient(b -> b.duration(input.getCount() * 200).energyCost(32)));
    }

    public IFMORecipe(IFMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, CompositeRecipeIngredient<Input> input, List<ItemStack> output, RecipePropertyMap properties) {
        super(type, serializer, id, input, output, properties);
    }

    public record Input(ItemStack item, FluidStack fluid) {}
}
