package dev.su5ed.gtexperimental.recipe.setup;

import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeImpl;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeManager;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

public final class ModRecipeManagers {
    public static final Lazy<RecipeManager<AlloySmelterRecipe, MISORecipe.Input<ItemStack>>> ALLOY_SMELTER = create(ModRecipeTypes.ALLOY_SMELTER);
    public static final Lazy<RecipeManager<AssemblerRecipe, MISORecipe.Input<ItemStack>>> ASSEMBLER = create(ModRecipeTypes.ASSEMBLER);
    public static final Lazy<RecipeManager<CanningMachineRecipe, MIMORecipe.Input>> CANNING_MACHINE = create(ModRecipeTypes.CANNING_MACHINE);
    public static final Lazy<RecipeManager<IndustrialGrinderRecipe, IFMORecipe.Input>> INDUSTRIAL_GRINDER = create(ModRecipeTypes.INDUSTRIAL_GRINDER);
    public static final Lazy<RecipeManager<PulverizerRecipe, SIMORecipe.Input>> PULVERIZER = create(ModRecipeTypes.PULVERIZER);
    public static final Lazy<RecipeManager<BenderRecipe, SISORecipe.Input>> BENDER = create(ModRecipeTypes.BENDER);
    public static final Lazy<RecipeManager<ChemicalRecipe, MISORecipe.Input<FluidStack>>> CHEMICAL = create(ModRecipeTypes.CHEMICAL);

    private static <R extends BaseRecipeImpl<?, I, ? super R>, I> Lazy<RecipeManager<R, I>> create(Supplier<? extends RecipeType<R>> recipeType) {
        return Lazy.of(() -> new BaseRecipeManager<>(recipeType.get()));
    }

    private ModRecipeManagers() {}
}
