package dev.su5ed.gtexperimental.recipe.setup;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeManager;
import dev.su5ed.gtexperimental.recipe.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public final class ModRecipeManagers {
    private static final List<RecipeManager<?, ?>> RECIPE_MANAGERS = new ArrayList<>();

    public static final RecipeManager<MISORecipe<ItemStack, ItemStack>, List<ItemStack>> ALLOY_SMELTER = create(ModRecipeTypes.ALLOY_SMELTER);
    public static final RecipeManager<MISORecipe<ItemStack, ItemStack>, List<ItemStack>> ASSEMBLER = create(ModRecipeTypes.ASSEMBLER);
    public static final RecipeManager<MIMORecipe, List<ItemStack>> CANNING_MACHINE = create(ModRecipeTypes.CANNING_MACHINE);
    public static final RecipeManager<IFMORecipe, IFMORecipe.Input> INDUSTRIAL_GRINDER = create(ModRecipeTypes.INDUSTRIAL_GRINDER);
    public static final RecipeManager<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack> PULVERIZER = create(ModRecipeTypes.PULVERIZER);
    public static final RecipeManager<SISORecipe<ItemStack, ItemStack>, ItemStack> BENDER = create(ModRecipeTypes.BENDER);
    public static final RecipeManager<MISORecipe<FluidStack, FluidStack>, List<FluidStack>> CHEMICAL = create(ModRecipeTypes.CHEMICAL);
    public static final RecipeManager<SIMORecipe<FluidStack, List<FluidStack>>, FluidStack> DISTILLATION = create(ModRecipeTypes.DISTILLATION);
    public static final RecipeManager<MISORecipe<FluidStack, ItemStack>, List<FluidStack>> FUSION_SOLID = create(ModRecipeTypes.FUSION_SOLID);
    public static final RecipeManager<MISORecipe<FluidStack, FluidStack>, List<FluidStack>> FUSION_FLUID = create(ModRecipeTypes.FUSION_FLUID);
    public static final RecipeManager<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack> IMPLOSION = create(ModRecipeTypes.IMPLOSION);
    public static final RecipeManager<MIMORecipe, List<ItemStack>> BLAST_FURNACE = create(ModRecipeTypes.BLAST_FURNACE);
    public static final RecipeManager<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>, Either<ItemStack, FluidStack>> INDUSTRIAL_CENTRIFUGE = create(ModRecipeTypes.INDUSTRIAL_CENTRIFUGE);
    public static final RecipeManager<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>, Either<ItemStack, FluidStack>> INDUSTRIAL_ELECTROLYZER = create(ModRecipeTypes.INDUSTRIAL_ELECTROLYZER);
    public static final RecipeManager<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack> LATHE = create(ModRecipeTypes.LATHE);
    public static final RecipeManager<MISORecipe<ItemStack, ItemStack>, List<ItemStack>> PRINTER = create(ModRecipeTypes.PRINTER);
    public static final RecipeManager<IFMORecipe, IFMORecipe.Input> INDUSTRIAL_SAWMILL = create(ModRecipeTypes.INDUSTRIAL_SAWMILL);
    public static final RecipeManager<SISORecipe<ItemStack, ItemStack>, ItemStack> VACUUM_FREEZER_SOLID = create(ModRecipeTypes.VACUUM_FREEZER_SOLID);
    public static final RecipeManager<SISORecipe<FluidStack, FluidStack>, FluidStack> VACUUM_FREEZER_FLUID = create(ModRecipeTypes.VACUUM_FREEZER_FLUID);

    public static final RecipeManager<SISORecipe<ItemStack, ItemStack>, ItemStack> MACERATOR = create(ModRecipeTypes.MACERATOR);

    public static final RecipeManager<SISORecipe<FluidStack, FluidStack>, FluidStack> DENSE_LIQUID_FUEL = create(ModRecipeTypes.DENSE_LIQUID_FUEL);
    public static final RecipeManager<SISORecipe<Either<ItemStack, FluidStack>, FluidStack>, Either<ItemStack, FluidStack>> DIESEL_FUEL = create(ModRecipeTypes.DIESEL_FUEL);
    public static final RecipeManager<SISORecipe<FluidStack, FluidStack>, FluidStack> GAS_FUEL = create(ModRecipeTypes.GAS_FUEL);
    public static final RecipeManager<SIMORecipe<Either<ItemStack, FluidStack>, List<ItemStack>>, Either<ItemStack, FluidStack>> HOT_FUEL = create(ModRecipeTypes.HOT_FUEL);
    public static final RecipeManager<SISORecipe<Either<ItemStack, FluidStack>, ItemStack>, Either<ItemStack, FluidStack>> MAGIC_FUEL = create(ModRecipeTypes.MAGIC_FUEL);
    public static final RecipeManager<SISORecipe<FluidStack, FluidStack>, FluidStack> PLASMA_FUEL = create(ModRecipeTypes.PLASMA_FUEL);
    public static final RecipeManager<SISORecipe<FluidStack, FluidStack>, FluidStack> STEAM_FUEL = create(ModRecipeTypes.STEAM_FUEL);

    private static <R extends BaseRecipe<?, IN, ?, ? super R>, IN> RecipeManager<R, IN> create(RegistryObject<? extends RecipeType<R>> recipeType) {
        RecipeManager<R, IN> manager = new BaseRecipeManager<>(recipeType);
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
