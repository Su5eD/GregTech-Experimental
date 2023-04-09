package dev.su5ed.gtexperimental.recipe.setup;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.CompositeRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.IFMORecipeType;
import dev.su5ed.gtexperimental.recipe.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.MIMORecipeType;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.MISORecipeType;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.SIMORecipeType;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.recipe.SISORecipeType;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeFactory;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

@SuppressWarnings("RedundantTypeArguments")
public final class ModRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MODID);
    private static final List<RecipeProperty<?>> NO_PROPERTIES = List.of();
    private static final List<RecipeProperty<?>> BASIC_PROPERTIES = List.of(ModRecipeProperty.DURATION, ModRecipeProperty.ENERGY_COST);
    private static final List<RecipeProperty<?>> FUSION_PROPERTIES = List.of(ModRecipeProperty.DURATION, ModRecipeProperty.ENERGY_COST, ModRecipeProperty.START_ENERGY);
    private static final List<RecipeProperty<?>> FUEL_PROPERTIES = List.of(ModRecipeProperty.ENERGY);

    public static final RegistryObject<MISORecipeType<MISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> ALLOY_SMELTER = ModRecipeTypes.<MISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>miso("alloy_smelter", ModRecipeIngredientTypes.ITEM, 2, ModRecipeOutputTypes.ITEM, MISORecipe::alloySmelter);
    public static final RegistryObject<MISORecipeType<MISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> ASSEMBLER = ModRecipeTypes.<MISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>miso("assembler", ModRecipeIngredientTypes.ITEM, 2, ModRecipeOutputTypes.ITEM, MISORecipe::assembler);
    public static final RegistryObject<MIMORecipeType<MIMORecipe>> CANNING_MACHINE = mimo("canning_machine", 2, 2, MIMORecipe::canningMachine);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>>> PULVERIZER = ModRecipeTypes.<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>>simo("pulverizer", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM.listOf(2), List.of(ModRecipeProperty.ENERGY_COST, ModRecipeProperty.CHANCE), SIMORecipe::pulverizer);
    public static final RegistryObject<IFMORecipeType<IFMORecipe>> INDUSTRIAL_GRINDER = ifmo("industrial_grinder", 3, NO_PROPERTIES, IFMORecipe::industrialGrinder);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> BENDER = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("bender", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::bender);
    public static final RegistryObject<MISORecipeType<MISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>> CHEMICAL = ModRecipeTypes.<MISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>miso("chemical", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.FLUID, List.of(ModRecipeProperty.DURATION), MISORecipe::chemical);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<FluidStack, List<FluidStack>>, FluidStack, List<FluidStack>>> DISTILLATION = ModRecipeTypes.<SIMORecipe<FluidStack, List<FluidStack>>, FluidStack, List<FluidStack>>simo("distillation", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID.listOf(4), List.of(ModRecipeProperty.DURATION), SIMORecipe::distillation);
    public static final RegistryObject<MISORecipeType<MISORecipe<FluidStack, ItemStack>, FluidStack, ItemStack>> FUSION_SOLID = ModRecipeTypes.<MISORecipe<FluidStack, ItemStack>, FluidStack, ItemStack>miso("fusion_solid", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.ITEM, FUSION_PROPERTIES, MISORecipe::fusionSolid);
    public static final RegistryObject<MISORecipeType<MISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>> FUSION_FLUID = ModRecipeTypes.<MISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>miso("fusion_fluid", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.FLUID, FUSION_PROPERTIES, MISORecipe::fusionFluid);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>>> IMPLOSION = ModRecipeTypes.<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>>simo("implosion", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM.listOf(2), List.of(ModRecipeProperty.TNT), SIMORecipe::implosion);
    public static final RegistryObject<MIMORecipeType<MIMORecipe>> BLAST_FURNACE = mimo("blast_furnace", 2, 2, List.of(ModRecipeProperty.DURATION, ModRecipeProperty.HEAT), MIMORecipe::blastFurnace);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>, Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>> INDUSTRIAL_CENTRIFUGE = ModRecipeTypes.<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>, Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>simo("industrial_centrifuge", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.HYBRID.listOf(4), List.of(ModRecipeProperty.DURATION), SIMORecipe::industrialCentrifuge);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>, Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>> INDUSTRIAL_ELECTROLYZER = ModRecipeTypes.<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>, Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>simo("industrial_electrolyzer", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.HYBRID.listOf(4), SIMORecipe::industrialElectrolyzer);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>>> LATHE = ModRecipeTypes.<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>>simo("lathe", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM.listOf(2), SIMORecipe::lathe);
    public static final RegistryObject<MISORecipeType<MISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> PRINTER = ModRecipeTypes.<MISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>miso("printer", ModRecipeIngredientTypes.ITEM, 3, ModRecipeOutputTypes.ITEM, MISORecipe::printer);
    public static final RegistryObject<IFMORecipeType<IFMORecipe>> INDUSTRIAL_SAWMILL = ifmo("industrial_sawmill", 2, NO_PROPERTIES, IFMORecipe::industrialSawmill);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> VACUUM_FREEZER_SOLID = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("vacuum_freezer_solid", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, List.of(ModRecipeProperty.DURATION), SISORecipe::vacuumFreezerSolid);
    public static final RegistryObject<SISORecipeType<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>> VACUUM_FREEZER_FLUID = ModRecipeTypes.<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>siso("vacuum_freezer_fluid", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID, List.of(ModRecipeProperty.DURATION), SISORecipe::vacuumFreezerFluid);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> WIREMILL = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("wiremill", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::wiremill);

    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> MACERATOR = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("macerator", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::macerator);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> EXTRACTOR = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("extractor", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::extractor);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> COMPRESSOR = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("compressor", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::compressor);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> RECYCLER = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("recycler", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::recycler);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> FURNACE = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("furnace", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::furnace);
    
    public static final RegistryObject<SISORecipeType<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>> DENSE_LIQUID_FUEL = ModRecipeTypes.<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>siso("fuels/dense_liquid", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID, FUEL_PROPERTIES, SISORecipe::denseLiquid);
    public static final RegistryObject<SISORecipeType<SISORecipe<Either<ItemStack, FluidStack>, FluidStack>, Either<ItemStack, FluidStack>, FluidStack>> DIESEL_FUEL = ModRecipeTypes.<SISORecipe<Either<ItemStack, FluidStack>, FluidStack>, Either<ItemStack, FluidStack>, FluidStack>siso("fuels/diesel", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.FLUID, FUEL_PROPERTIES, SISORecipe::dieselFuel);
    public static final RegistryObject<SISORecipeType<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>> GAS_FUEL = ModRecipeTypes.<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>siso("fuels/gas", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID, FUEL_PROPERTIES, SISORecipe::gasFuel);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<Either<ItemStack, FluidStack>, List<ItemStack>>, Either<ItemStack, FluidStack>, List<ItemStack>>> HOT_FUEL = ModRecipeTypes.<SIMORecipe<Either<ItemStack, FluidStack>, List<ItemStack>>, Either<ItemStack, FluidStack>, List<ItemStack>>simo("fuels/hot", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.ITEM.listOf(4), FUEL_PROPERTIES, SIMORecipe::hotFuel);
    public static final RegistryObject<SISORecipeType<SISORecipe<Either<ItemStack, FluidStack>, ItemStack>, Either<ItemStack, FluidStack>, ItemStack>> MAGIC_FUEL = ModRecipeTypes.<SISORecipe<Either<ItemStack, FluidStack>, ItemStack>, Either<ItemStack, FluidStack>, ItemStack>siso("fuels/magic", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.ITEM, FUEL_PROPERTIES, SISORecipe::magic);
    public static final RegistryObject<SISORecipeType<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>> PLASMA_FUEL = ModRecipeTypes.<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>siso("fuels/plasma", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID, FUEL_PROPERTIES, SISORecipe::plasma);
    public static final RegistryObject<SISORecipeType<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>> STEAM_FUEL = ModRecipeTypes.<SISORecipe<FluidStack, FluidStack>, FluidStack, FluidStack>siso("fuels/steam", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID, FUEL_PROPERTIES, SISORecipe::steam);

    public static void init(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }
    
    private static <R extends MIMORecipe> RegistryObject<MIMORecipeType<R>> mimo(String name, int inputCount, int outputCount, BaseRecipeFactory<R, List<RecipeIngredient<ItemStack>>, List<ItemStack>> factory) {
        return mimo(name, inputCount, outputCount, BASIC_PROPERTIES, factory);
    }

    private static <R extends MIMORecipe> RegistryObject<MIMORecipeType<R>> mimo(String name, int inputCount, int outputCount, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, List<RecipeIngredient<ItemStack>>, List<ItemStack>> factory) {
        return register(name, () -> new MIMORecipeType<>(location(name), inputCount, outputCount, properties, factory));
    }

    private static <R extends SIMORecipe<IN, OUT>, IN, OUT> RegistryObject<SIMORecipeType<R, IN, OUT>> simo(String name, RecipeIngredientType<? extends RecipeIngredient<IN>, IN> inputType, RecipeOutputType<OUT> outputType, BaseRecipeFactory<R, RecipeIngredient<IN>, OUT> factory) {
        return simo(name, inputType, outputType, BASIC_PROPERTIES, factory);
    }

    private static <R extends SIMORecipe<IN, OUT>, IN, OUT> RegistryObject<SIMORecipeType<R, IN, OUT>> simo(String name, RecipeIngredientType<? extends RecipeIngredient<IN>, IN> inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, RecipeIngredient<IN>, OUT> factory) {
        return register(name, () -> new SIMORecipeType<>(location(name), inputType, outputType, properties, factory));
    }

    private static <R extends MISORecipe<IN, OUT>, IN, OUT> RegistryObject<MISORecipeType<R, IN, OUT>> miso(String name, RecipeIngredientType<RecipeIngredient<IN>, IN> inputType, int inputCount, RecipeOutputType<OUT> outputType, BaseRecipeFactory<R, List<? extends RecipeIngredient<IN>>, OUT> factory) {
        return miso(name, inputType, inputCount, outputType, BASIC_PROPERTIES, factory);
    }
    
    private static <R extends MISORecipe<IN, OUT>, IN, OUT> RegistryObject<MISORecipeType<R, IN, OUT>> miso(String name, RecipeIngredientType<RecipeIngredient<IN>, IN> inputType, int inputCount, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, List<? extends RecipeIngredient<IN>>, OUT> factory) {
        return register(name, () -> new MISORecipeType<>(location(name), inputType.listOf(inputCount), outputType, properties, factory));
    }

    private static <R extends IFMORecipe> RegistryObject<IFMORecipeType<R>> ifmo(String name, int outputCount, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, CompositeRecipeIngredient<IFMORecipe.Input>, List<ItemStack>> factory) {
        return register(name, () -> new IFMORecipeType<>(location(name), outputCount, properties, factory));
    }

    private static <R extends SISORecipe<IN, OUT>, IN, OUT> RegistryObject<SISORecipeType<R, IN, OUT>> siso(String name, RecipeIngredientType<? extends RecipeIngredient<IN>, IN> inputType, RecipeOutputType<OUT> outputType, BaseRecipeFactory<R, RecipeIngredient<IN>, OUT> factory) {
        return siso(name, inputType, outputType, BASIC_PROPERTIES, factory);
    }

    private static <R extends SISORecipe<IN, OUT>, IN, OUT> RegistryObject<SISORecipeType<R, IN, OUT>> siso(String name, RecipeIngredientType<? extends RecipeIngredient<IN>, IN> inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, RecipeIngredient<IN>, OUT> factory) {
        return register(name, () -> new SISORecipeType<>(location(name), inputType, outputType, properties, factory));
    }

    private static <R extends RecipeType<?>> RegistryObject<R> register(String name, Supplier<R> recipeType) {
        return RECIPE_TYPES.register(name, recipeType);
    }

    private ModRecipeTypes() {}
}
