package dev.su5ed.gtexperimental.recipe.setup;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.BlastFurnaceRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.DistillationRecipe;
import dev.su5ed.gtexperimental.recipe.DynamicBenderRecipes;
import dev.su5ed.gtexperimental.recipe.FusionFluidRecipe;
import dev.su5ed.gtexperimental.recipe.FusionSolidRecipe;
import dev.su5ed.gtexperimental.recipe.ImplosionRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialCentrifugeRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialElectrolyzerRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialSawmillRecipe;
import dev.su5ed.gtexperimental.recipe.LatheRecipe;
import dev.su5ed.gtexperimental.recipe.PrinterRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import dev.su5ed.gtexperimental.recipe.VacuumFreezerFluidRecipe;
import dev.su5ed.gtexperimental.recipe.VacuumFreezerSolidRecipe;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeManager;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class ModRecipeManagers {
    private static final List<RecipeManager<?, ?>> RECIPE_MANAGERS = new ArrayList<>();

    public static final RecipeManager<AlloySmelterRecipe, MISORecipe.Input<ItemStack>> ALLOY_SMELTER = create(ModRecipeTypes.ALLOY_SMELTER);
    public static final RecipeManager<AssemblerRecipe, MISORecipe.Input<ItemStack>> ASSEMBLER = create(ModRecipeTypes.ASSEMBLER);
    public static final RecipeManager<CanningMachineRecipe, MIMORecipe.Input> CANNING_MACHINE = create(ModRecipeTypes.CANNING_MACHINE);
    public static final RecipeManager<IndustrialGrinderRecipe, IFMORecipe.Input> INDUSTRIAL_GRINDER = create(ModRecipeTypes.INDUSTRIAL_GRINDER);
    public static final RecipeManager<PulverizerRecipe, SIMORecipe.Input<ItemStack>> PULVERIZER = create(ModRecipeTypes.PULVERIZER);
    public static final RecipeManager<BenderRecipe, ItemStack> BENDER = create(ModRecipeTypes.BENDER);
    public static final RecipeManager<ChemicalRecipe, MISORecipe.Input<FluidStack>> CHEMICAL = create(ModRecipeTypes.CHEMICAL);
    public static final RecipeManager<DistillationRecipe, SIMORecipe.Input<FluidStack>> DISTILLATION = create(ModRecipeTypes.DISTILLATION);
    public static final RecipeManager<FusionSolidRecipe, MISORecipe.Input<FluidStack>> FUSION_SOLID = create(ModRecipeTypes.FUSION_SOLID);
    public static final RecipeManager<FusionFluidRecipe, MISORecipe.Input<FluidStack>> FUSION_FLUID = create(ModRecipeTypes.FUSION_FLUID);
    public static final RecipeManager<ImplosionRecipe, SIMORecipe.Input<ItemStack>> IMPLOSION = create(ModRecipeTypes.IMPLOSION);
    public static final RecipeManager<BlastFurnaceRecipe, MIMORecipe.Input> BLAST_FURNACE = create(ModRecipeTypes.BLAST_FURNACE);
    public static final RecipeManager<IndustrialCentrifugeRecipe, SIMORecipe.Input<Either<ItemStack, FluidStack>>> INDUSTRIAL_CENTRIFUGE = create(ModRecipeTypes.INDUSTRIAL_CENTRIFUGE);
    public static final RecipeManager<IndustrialElectrolyzerRecipe, SIMORecipe.Input<Either<ItemStack, FluidStack>>> INDUSTRIAL_ELECTROLYZER = create(ModRecipeTypes.INDUSTRIAL_ELECTROLYZER);
    public static final RecipeManager<LatheRecipe, SIMORecipe.Input<ItemStack>> LATHE = create(ModRecipeTypes.LATHE);
    public static final RecipeManager<PrinterRecipe, MISORecipe.Input<ItemStack>> PRINTER = create(ModRecipeTypes.PRINTER);
    public static final RecipeManager<IndustrialSawmillRecipe, IFMORecipe.Input> INDUSTRIAL_SAWMILL = create(ModRecipeTypes.INDUSTRIAL_SAWMILL);
    public static final RecipeManager<VacuumFreezerSolidRecipe, ItemStack> VACUUM_FREEZER_SOLID = create(ModRecipeTypes.VACUUM_FREEZER_SOLID);
    public static final RecipeManager<VacuumFreezerFluidRecipe, FluidStack> VACUUM_FREEZER_FLUID = create(ModRecipeTypes.VACUUM_FREEZER_FLUID);

    static {
        BENDER.registerProvider(new DynamicBenderRecipes());
    }

    private static <R extends BaseRecipe<?, I, ? super R>, I> RecipeManager<R, I> create(Supplier<? extends RecipeType<R>> recipeType) {
        RecipeManager<R, I> manager = new BaseRecipeManager<>(recipeType);
        RECIPE_MANAGERS.add(manager);
        return manager;
    }

    private ModRecipeManagers() {}

    public static class RecipeReloadListener implements ResourceManagerReloadListener {
        public static RecipeReloadListener INSTANCE = new RecipeReloadListener();

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            RECIPE_MANAGERS.forEach(RecipeManager::reset);
        }
    } 
}
