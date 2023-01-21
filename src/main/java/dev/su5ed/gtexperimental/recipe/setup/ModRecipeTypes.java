package dev.su5ed.gtexperimental.recipe.setup;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipeType;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipeType;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.MISORecipeType;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipeType;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import dev.su5ed.gtexperimental.recipe.type.SISORecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static dev.su5ed.gtexperimental.api.Reference.location;

public final class ModRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MODID);

    public static final RegistryObject<MISORecipeType<AlloySmelterRecipe, ItemStack>> ALLOY_SMELTER = ModRecipeTypes.<AlloySmelterRecipe, ItemStack>miso("alloy_smelter", ModRecipeIngredientTypes.ITEM, 2, ModRecipeOutputTypes.ITEM, AlloySmelterRecipe::new);
    public static final RegistryObject<MISORecipeType<AssemblerRecipe, ItemStack>> ASSEMBLER = ModRecipeTypes.<AssemblerRecipe, ItemStack>miso("assembler", ModRecipeIngredientTypes.ITEM, 2, ModRecipeOutputTypes.ITEM, AssemblerRecipe::new);
    public static final RegistryObject<MIMORecipeType<CanningMachineRecipe>> CANNING_MACHINE = mimo("canning_machine", 2, List.of(ModRecipeOutputTypes.ITEM, ModRecipeOutputTypes.ITEM), CanningMachineRecipe::new);
    public static final RegistryObject<SIMORecipeType<PulverizerRecipe>> PULVERIZER = simo("pulverizer", List.of(ModRecipeOutputTypes.ITEM, ModRecipeOutputTypes.ITEM), PulverizerRecipe::new);
    public static final RegistryObject<IFMORecipeType<IndustrialGrinderRecipe>> INDUSTRIAL_GRINDER = ifmo("industrial_grinder", 3, IndustrialGrinderRecipe::new);
    public static final RegistryObject<SISORecipeType<BenderRecipe>> BENDER = siso("bender", ModRecipeOutputTypes.ITEM, BenderRecipe::new);
    @SuppressWarnings("RedundantTypeArguments")
    public static final RegistryObject<MISORecipeType<ChemicalRecipe, FluidStack>> CHEMICAL = ModRecipeTypes.<ChemicalRecipe, FluidStack>miso("chemical", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.FLUID, ChemicalRecipe::new);

    public static void init(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }

    private static <R extends MIMORecipe> RegistryObject<MIMORecipeType<R>> mimo(String name, int inputCount, List<RecipeOutputType<ItemStack>> outputTypes, MIMORecipeType.MIMORecipeFactory<R> factory) {
        return register(name, () -> new MIMORecipeType<>(location(name), inputCount, outputTypes, factory));
    }

    private static <R extends SIMORecipe> RegistryObject<SIMORecipeType<R>> simo(String name, List<RecipeOutputType<ItemStack>> outputTypes, SIMORecipeType.SIMORecipeFactory<R> factory) {
        return register(name, () -> new SIMORecipeType<>(location(name), outputTypes, factory));
    }

    private static <R extends MISORecipe<T>, T> RegistryObject<MISORecipeType<R, T>> miso(String name, RecipeIngredientType<? extends RecipeIngredient<T>> inputType, int inputCount, RecipeOutputType<T> outputType, MISORecipeType.MISORecipeFactory<R, T> factory) {
        return register(name, () -> new MISORecipeType<>(location(name), inputType, inputCount, outputType, factory));
    }

    private static <R extends IFMORecipe> RegistryObject<IFMORecipeType<R>> ifmo(String name, int outputCount, IFMORecipeType.IFMORecipeFactory<R> factory) {
        return register(name, () -> new IFMORecipeType<>(location(name), outputCount, factory));
    }

    private static <R extends SISORecipe> RegistryObject<SISORecipeType<R>> siso(String name, RecipeOutputType<ItemStack> outputType, SISORecipeType.SISORecipeFactory<R> factory) {
        return register(name, () -> new SISORecipeType<>(location(name), outputType, factory));
    }

    private static <R extends RecipeType<?>> RegistryObject<R> register(String name, Supplier<R> recipeType) {
        return RECIPE_TYPES.register(name, recipeType);
    }

    private ModRecipeTypes() {}
}
