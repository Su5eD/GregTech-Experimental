package dev.su5ed.gtexperimental.recipe.setup;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManagerProvider;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.BlastFurnaceRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.DistillationRecipe;
import dev.su5ed.gtexperimental.recipe.DynamicBenderRecipes;
import dev.su5ed.gtexperimental.recipe.DynamicLatheRecipes;
import dev.su5ed.gtexperimental.recipe.FusionFluidRecipe;
import dev.su5ed.gtexperimental.recipe.FusionSolidRecipe;
import dev.su5ed.gtexperimental.recipe.ImplosionRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialCentrifugeRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialElectrolyzerRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.LatheRecipe;
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
    public static final RecipeManagerProvider<DistillationRecipe, SIMORecipe.Input<FluidStack>> DISTILLATION = create(ModRecipeTypes.DISTILLATION);
    public static final RecipeManagerProvider<FusionSolidRecipe, MISORecipe.Input<FluidStack>> FUSION_SOLID = create(ModRecipeTypes.FUSION_SOLID);
    public static final RecipeManagerProvider<FusionFluidRecipe, MISORecipe.Input<FluidStack>> FUSION_FLUID = create(ModRecipeTypes.FUSION_FLUID);
    public static final RecipeManagerProvider<ImplosionRecipe, SIMORecipe.Input<ItemStack>> IMPLOSION = create(ModRecipeTypes.IMPLOSION);
    public static final RecipeManagerProvider<BlastFurnaceRecipe, MIMORecipe.Input> BLAST_FURNACE = create(ModRecipeTypes.BLAST_FURNACE);
    public static final RecipeManagerProvider<IndustrialCentrifugeRecipe, SIMORecipe.Input<Either<ItemStack, FluidStack>>> INDUSTRIAL_CENTRIFUGE = create(ModRecipeTypes.INDUSTRIAL_CENTRIFUGE);
    public static final RecipeManagerProvider<IndustrialElectrolyzerRecipe, SIMORecipe.Input<Either<ItemStack, FluidStack>>> INDUSTRIAL_ELECTROLYZER = create(ModRecipeTypes.INDUSTRIAL_ELECTROLYZER);
    public static final RecipeManagerProvider<LatheRecipe, SIMORecipe.Input<ItemStack>> LATHE = create(ModRecipeTypes.LATHE);

    static {
        BENDER.registerProvider(DynamicBenderRecipes::new);
        LATHE.registerProvider(DynamicLatheRecipes::new);
    }
    
    private static <R extends BaseRecipe<?, I, ? super R>, I> RecipeManagerProvider<R, I> create(Supplier<? extends RecipeType<R>> recipeType) {
        return new BaseRecipeManagerProvider<>(recipeType, BaseRecipeManager::new);
    }

    private ModRecipeManagers() {}
}
