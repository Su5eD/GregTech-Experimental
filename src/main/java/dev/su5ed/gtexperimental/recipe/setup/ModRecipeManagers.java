package dev.su5ed.gtexperimental.recipe.setup;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManagerProvider;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.DynamicBenderRecipes;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeManager;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeManagerProvider;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

public final class ModRecipeManagers {
    public static final RecipeManagerProvider<AlloySmelterRecipe, MISORecipe.Input<ItemStack>> ALLOY_SMELTER = create(ModRecipeTypes.ALLOY_SMELTER);
    public static final RecipeManagerProvider<AssemblerRecipe, MISORecipe.Input<ItemStack>> ASSEMBLER = create(ModRecipeTypes.ASSEMBLER);
    public static final RecipeManagerProvider<CanningMachineRecipe, MIMORecipe.Input> CANNING_MACHINE = create(ModRecipeTypes.CANNING_MACHINE);
    public static final RecipeManagerProvider<IndustrialGrinderRecipe, IFMORecipe.Input> INDUSTRIAL_GRINDER = create(ModRecipeTypes.INDUSTRIAL_GRINDER);
    public static final RecipeManagerProvider<PulverizerRecipe, SIMORecipe.Input<ItemStack>> PULVERIZER = create(ModRecipeTypes.PULVERIZER);
    public static final RecipeManagerProvider<BenderRecipe, SISORecipe.Input> BENDER = create(ModRecipeTypes.BENDER);
    public static final RecipeManagerProvider<ChemicalRecipe, MISORecipe.Input<FluidStack>> CHEMICAL = create(ModRecipeTypes.CHEMICAL);

    static {
        BENDER.registerProvider(DynamicBenderRecipes::new);
    }
    
    private static <R extends BaseRecipe<?, I, ? super R>, I> RecipeManagerProvider<R, I> create(Supplier<? extends RecipeType<R>> recipeType) {
        return new BaseRecipeManagerProvider<>(recipeType, BaseRecipeManager::new);
    }

    private ModRecipeManagers() {}
}
