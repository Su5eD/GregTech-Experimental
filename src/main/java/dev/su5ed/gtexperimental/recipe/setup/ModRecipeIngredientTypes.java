package dev.su5ed.gtexperimental.recipe.setup;

import dev.su5ed.gtexperimental.api.recipe.CompositeRecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.type.CompositeRecipeIngredientTypeImpl;
import dev.su5ed.gtexperimental.recipe.type.FluidRecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.type.HybridRecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredientType;

import java.util.Map;

public final class ModRecipeIngredientTypes {
    public static final VanillaRecipeIngredientType ITEM = new VanillaRecipeIngredientType();
    public static final FluidRecipeIngredientType FLUID = new FluidRecipeIngredientType();
    public static final HybridRecipeIngredientType HYBRID = new HybridRecipeIngredientType();
    public static final CompositeRecipeIngredientType<IFMORecipe.Input> ITEM_FLUID = new CompositeRecipeIngredientTypeImpl<>(Map.of(
        "item", new CompositeRecipeIngredientType.SubIngredientType<>(ITEM, IFMORecipe.Input::item),
        "fluid", new CompositeRecipeIngredientType.SubIngredientType<>(FLUID, IFMORecipe.Input::fluid)
    ));

    private ModRecipeIngredientTypes() {}
}
