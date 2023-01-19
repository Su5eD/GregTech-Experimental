package dev.su5ed.gregtechmod.recipe.setup;

import dev.su5ed.gregtechmod.recipe.AlloySmelterRecipe;
import dev.su5ed.gregtechmod.recipe.AssemblerRecipe;
import dev.su5ed.gregtechmod.recipe.BenderRecipe;
import dev.su5ed.gregtechmod.recipe.CanningMachineRecipe;
import dev.su5ed.gregtechmod.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gregtechmod.recipe.PulverizerRecipe;
import dev.su5ed.gregtechmod.recipe.type.BaseRecipe;
import dev.su5ed.gregtechmod.recipe.type.BaseRecipeManager;
import dev.su5ed.gregtechmod.recipe.type.ItemFluidRecipe;
import dev.su5ed.gregtechmod.recipe.type.MIMORecipe;
import dev.su5ed.gregtechmod.recipe.type.MISORecipe;
import dev.su5ed.gregtechmod.recipe.type.RecipeManager;
import dev.su5ed.gregtechmod.recipe.type.SIMORecipe;
import dev.su5ed.gregtechmod.recipe.type.SISORecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public final class ModRecipeManagers {
    public static final Lazy<RecipeManager<AlloySmelterRecipe, MISORecipe.Input>> ALLOY_SMELTER = create(ModRecipeTypes.ALLOY_SMELTER);
    public static final Lazy<RecipeManager<AssemblerRecipe, MISORecipe.Input>> ASSEMBLER = create(ModRecipeTypes.ASSEMBLER);
    public static final Lazy<RecipeManager<CanningMachineRecipe, MIMORecipe.Input>> CANNING_MACHINE = create(ModRecipeTypes.CANNING_MACHINE);
    public static final Lazy<RecipeManager<IndustrialGrinderRecipe, ItemFluidRecipe.Input>> INDUSTRIAL_GRINDER = create(ModRecipeTypes.INDUSTRIAL_GRINDER);
    public static final Lazy<RecipeManager<PulverizerRecipe, SIMORecipe.Input>> PULVERIZER = create(ModRecipeTypes.PULVERIZER);
    public static final Lazy<RecipeManager<BenderRecipe, SISORecipe.Input>> BENDER = create(ModRecipeTypes.BENDER);

    private static <R extends BaseRecipe<?, I, ? super R>, I> Lazy<RecipeManager<R, I>> create(Supplier<? extends RecipeType<R>> recipeType) {
        return Lazy.of(() -> new BaseRecipeManager<>(recipeType.get()));
    }

    private ModRecipeManagers() {}
}
