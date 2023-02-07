package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class VacuumFreezerSolidRecipe extends SISORecipe<ItemStack> {
    
    public VacuumFreezerSolidRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, int duration) {
        this(id, input, output, RecipePropertyMap.builder()
            .duration(duration)
            .build());
    }

    public VacuumFreezerSolidRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.VACUUM_FREEZER_SOLID.get(), ModRecipeSerializers.VACUUM_FREEZER_SOLID.get(), id, input, output, properties);
    }

    @Override
    public double getEnergyCost() {
        return 128;
    }
}
