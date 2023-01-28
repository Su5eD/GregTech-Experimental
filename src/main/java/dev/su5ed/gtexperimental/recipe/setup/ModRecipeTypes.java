package dev.su5ed.gtexperimental.recipe.setup;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.BlastFurnaceRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.DistillationRecipe;
import dev.su5ed.gtexperimental.recipe.FusionFluidRecipe;
import dev.su5ed.gtexperimental.recipe.FusionSolidRecipe;
import dev.su5ed.gtexperimental.recipe.ImplosionRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialCentrifugeRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialElectrolyzerRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.LatheRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipeType;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipeType;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.MISORecipeType;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
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

@SuppressWarnings("RedundantTypeArguments")
public final class ModRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MODID);
    private static final List<RecipeProperty<?>> NO_PROPERTIES = List.of();
    private static final List<RecipeProperty<?>> BASIC_PROPERTIES = List.of(ModRecipeProperty.DURATION, ModRecipeProperty.ENERGY_COST);
    private static final List<RecipeProperty<?>> FUSION_PROPERTIES = List.of(ModRecipeProperty.DURATION, ModRecipeProperty.ENERGY_COST, ModRecipeProperty.START_ENERGY);

    public static final RegistryObject<MISORecipeType<AlloySmelterRecipe, ItemStack, ItemStack>> ALLOY_SMELTER = ModRecipeTypes.<AlloySmelterRecipe, ItemStack, ItemStack>miso("alloy_smelter", ModRecipeIngredientTypes.ITEM, 2, ModRecipeOutputTypes.ITEM, AlloySmelterRecipe::new);
    public static final RegistryObject<MISORecipeType<AssemblerRecipe, ItemStack, ItemStack>> ASSEMBLER = ModRecipeTypes.<AssemblerRecipe, ItemStack, ItemStack>miso("assembler", ModRecipeIngredientTypes.ITEM, 2, ModRecipeOutputTypes.ITEM, AssemblerRecipe::new);
    public static final RegistryObject<MIMORecipeType<CanningMachineRecipe>> CANNING_MACHINE = mimo("canning_machine", 2, 2, CanningMachineRecipe::new);
    public static final RegistryObject<SIMORecipeType<PulverizerRecipe, ItemStack>> PULVERIZER = ModRecipeTypes.<PulverizerRecipe, ItemStack>simo("pulverizer", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, 2, PulverizerRecipe::new);
    public static final RegistryObject<IFMORecipeType<IndustrialGrinderRecipe>> INDUSTRIAL_GRINDER = ifmo("industrial_grinder", 3, NO_PROPERTIES, IndustrialGrinderRecipe::new);
    public static final RegistryObject<SISORecipeType<BenderRecipe>> BENDER = siso("bender", ModRecipeOutputTypes.ITEM, BenderRecipe::new);
    public static final RegistryObject<MISORecipeType<ChemicalRecipe, FluidStack, FluidStack>> CHEMICAL = ModRecipeTypes.<ChemicalRecipe, FluidStack, FluidStack>miso("chemical", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.FLUID, List.of(ModRecipeProperty.DURATION), ChemicalRecipe::new);
    public static final RegistryObject<SIMORecipeType<DistillationRecipe, FluidStack>> DISTILLATION = ModRecipeTypes.<DistillationRecipe, FluidStack>simo("distillation", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID, 4, List.of(ModRecipeProperty.DURATION), DistillationRecipe::new);
    public static final RegistryObject<MISORecipeType<FusionSolidRecipe, FluidStack, ItemStack>> FUSION_SOLID = ModRecipeTypes.<FusionSolidRecipe, FluidStack, ItemStack>miso("fusion_solid", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.ITEM, FUSION_PROPERTIES, FusionSolidRecipe::new);
    public static final RegistryObject<MISORecipeType<FusionFluidRecipe, FluidStack, FluidStack>> FUSION_FLUID = ModRecipeTypes.<FusionFluidRecipe, FluidStack, FluidStack>miso("fusion_fluid", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.FLUID, FUSION_PROPERTIES, FusionFluidRecipe::new);
    public static final RegistryObject<SIMORecipeType<ImplosionRecipe, ItemStack>> IMPLOSION = ModRecipeTypes.<ImplosionRecipe, ItemStack>simo("implosion", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, 2, List.of(ModRecipeProperty.TNT), ImplosionRecipe::new);
    public static final RegistryObject<MIMORecipeType<BlastFurnaceRecipe>> BLAST_FURNACE = mimo("blast_furnace", 2, 2, List.of(ModRecipeProperty.DURATION, ModRecipeProperty.HEAT), BlastFurnaceRecipe::new);
    public static final RegistryObject<SIMORecipeType<IndustrialCentrifugeRecipe, Either<ItemStack, FluidStack>>> INDUSTRIAL_CENTRIFUGE = ModRecipeTypes.<IndustrialCentrifugeRecipe, Either<ItemStack, FluidStack>>simo("industrial_centrifuge", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.HYBRID, 4, List.of(ModRecipeProperty.DURATION), IndustrialCentrifugeRecipe::new);
    public static final RegistryObject<SIMORecipeType<IndustrialElectrolyzerRecipe, Either<ItemStack, FluidStack>>> INDUSTRIAL_ELECTROLYZER = ModRecipeTypes.<IndustrialElectrolyzerRecipe, Either<ItemStack, FluidStack>>simo("industrial_electrolyzer", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.HYBRID, 4, IndustrialElectrolyzerRecipe::new);
    public static final RegistryObject<SIMORecipeType<LatheRecipe, ItemStack>> LATHE = ModRecipeTypes.<LatheRecipe, ItemStack>simo("lathe", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, 2, LatheRecipe::new);

    public static void init(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }
    
    private static <R extends MIMORecipe> RegistryObject<MIMORecipeType<R>> mimo(String name, int inputCount, int outputCount, MIMORecipeType.MIMORecipeFactory<R> factory) {
        return mimo(name, inputCount, outputCount, BASIC_PROPERTIES, factory);
    }

    private static <R extends MIMORecipe> RegistryObject<MIMORecipeType<R>> mimo(String name, int inputCount, int outputCount, List<RecipeProperty<?>> properties, MIMORecipeType.MIMORecipeFactory<R> factory) {
        return register(name, () -> new MIMORecipeType<>(location(name), inputCount, outputCount, properties, factory));
    }

    private static <R extends SIMORecipe<T>, T> RegistryObject<SIMORecipeType<R, T>> simo(String name, RecipeIngredientType<? extends RecipeIngredient<T>> inputType, RecipeOutputType<T> outputType, int outputCount, SIMORecipeType.SIMORecipeFactory<R, T> factory) {
        return simo(name, inputType, outputType, outputCount, BASIC_PROPERTIES, factory);
    }

    private static <R extends SIMORecipe<T>, T> RegistryObject<SIMORecipeType<R, T>> simo(String name, RecipeIngredientType<? extends RecipeIngredient<T>> inputType, RecipeOutputType<T> outputType, int outputCount, List<RecipeProperty<?>> properties, SIMORecipeType.SIMORecipeFactory<R, T> factory) {
        return register(name, () -> new SIMORecipeType<>(location(name), inputType, outputType, outputCount, properties, factory));
    }

    private static <R extends MISORecipe<IN, OUT>, IN, OUT> RegistryObject<MISORecipeType<R, IN, OUT>> miso(String name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, int inputCount, RecipeOutputType<OUT> outputType, MISORecipeType.MISORecipeFactory<R, IN, OUT> factory) {
        return miso(name, inputType, inputCount, outputType, BASIC_PROPERTIES, factory);
    }
    
    private static <R extends MISORecipe<IN, OUT>, IN, OUT> RegistryObject<MISORecipeType<R, IN, OUT>> miso(String name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, int inputCount, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, MISORecipeType.MISORecipeFactory<R, IN, OUT> factory) {
        return register(name, () -> new MISORecipeType<>(location(name), inputType, inputCount, outputType, properties, factory));
    }

    private static <R extends IFMORecipe> RegistryObject<IFMORecipeType<R>> ifmo(String name, int outputCount, List<RecipeProperty<?>> properties, IFMORecipeType.IFMORecipeFactory<R> factory) {
        return register(name, () -> new IFMORecipeType<>(location(name), outputCount, properties, factory));
    }

    private static <R extends SISORecipe> RegistryObject<SISORecipeType<R>> siso(String name, RecipeOutputType<ItemStack> outputType, SISORecipeType.SISORecipeFactory<R> factory) {
        return register(name, () -> new SISORecipeType<>(location(name), outputType, factory));
    }

    private static <R extends RecipeType<?>> RegistryObject<R> register(String name, Supplier<R> recipeType) {
        return RECIPE_TYPES.register(name, recipeType);
    }

    private ModRecipeTypes() {}
}
