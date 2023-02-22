package dev.su5ed.gtexperimental.recipe.setup;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.DistillationRecipe;
import dev.su5ed.gtexperimental.recipe.FusionFluidRecipe;
import dev.su5ed.gtexperimental.recipe.FusionSolidRecipe;
import dev.su5ed.gtexperimental.recipe.ImplosionRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialCentrifugeRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialSawmillRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import dev.su5ed.gtexperimental.recipe.VacuumFreezerFluidRecipe;
import dev.su5ed.gtexperimental.recipe.VacuumFreezerSolidRecipe;
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
    public static final RegistryObject<SIMORecipeType<PulverizerRecipe, ItemStack, List<ItemStack>>> PULVERIZER = ModRecipeTypes.<PulverizerRecipe, ItemStack, List<ItemStack>>simo("pulverizer", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM.listOf(2), List.of(ModRecipeProperty.ENERGY_COST, ModRecipeProperty.CHANCE), PulverizerRecipe::new);
    public static final RegistryObject<IFMORecipeType<IndustrialGrinderRecipe>> INDUSTRIAL_GRINDER = ifmo("industrial_grinder", 3, NO_PROPERTIES, IndustrialGrinderRecipe::new);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> BENDER = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("bender", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::bender);
    public static final RegistryObject<MISORecipeType<ChemicalRecipe, FluidStack, FluidStack>> CHEMICAL = ModRecipeTypes.<ChemicalRecipe, FluidStack, FluidStack>miso("chemical", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.FLUID, List.of(ModRecipeProperty.DURATION), ChemicalRecipe::new);
    public static final RegistryObject<SIMORecipeType<DistillationRecipe, FluidStack, List<FluidStack>>> DISTILLATION = ModRecipeTypes.<DistillationRecipe, FluidStack, List<FluidStack>>simo("distillation", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID.listOf(4), List.of(ModRecipeProperty.DURATION), DistillationRecipe::new);
    public static final RegistryObject<MISORecipeType<FusionSolidRecipe, FluidStack, ItemStack>> FUSION_SOLID = ModRecipeTypes.<FusionSolidRecipe, FluidStack, ItemStack>miso("fusion_solid", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.ITEM, FUSION_PROPERTIES, FusionSolidRecipe::new);
    public static final RegistryObject<MISORecipeType<FusionFluidRecipe, FluidStack, FluidStack>> FUSION_FLUID = ModRecipeTypes.<FusionFluidRecipe, FluidStack, FluidStack>miso("fusion_fluid", ModRecipeIngredientTypes.FLUID, 2, ModRecipeOutputTypes.FLUID, FUSION_PROPERTIES, FusionFluidRecipe::new);
    public static final RegistryObject<SIMORecipeType<ImplosionRecipe, ItemStack, List<ItemStack>>> IMPLOSION = ModRecipeTypes.<ImplosionRecipe, ItemStack, List<ItemStack>>simo("implosion", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM.listOf(2), List.of(ModRecipeProperty.TNT), ImplosionRecipe::new);
    public static final RegistryObject<MIMORecipeType<MIMORecipe>> BLAST_FURNACE = mimo("blast_furnace", 2, 2, List.of(ModRecipeProperty.DURATION, ModRecipeProperty.HEAT), MIMORecipe::blastFurnace);
    public static final RegistryObject<SIMORecipeType<IndustrialCentrifugeRecipe, Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>> INDUSTRIAL_CENTRIFUGE = ModRecipeTypes.<IndustrialCentrifugeRecipe, Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>simo("industrial_centrifuge", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.HYBRID.listOf(4), List.of(ModRecipeProperty.DURATION), IndustrialCentrifugeRecipe::new);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>, Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>> INDUSTRIAL_ELECTROLYZER = ModRecipeTypes.<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>, Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>simo("industrial_electrolyzer", ModRecipeIngredientTypes.HYBRID, ModRecipeOutputTypes.HYBRID.listOf(4), SIMORecipe::industrialElectrolyzer);
    public static final RegistryObject<SIMORecipeType<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>>> LATHE = ModRecipeTypes.<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>>simo("lathe", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM.listOf(2), SIMORecipe::lathe);
    public static final RegistryObject<MISORecipeType<MISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> PRINTER = ModRecipeTypes.<MISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>miso("printer", ModRecipeIngredientTypes.ITEM, 2, ModRecipeOutputTypes.ITEM, MISORecipe::printer);
    public static final RegistryObject<IFMORecipeType<IndustrialSawmillRecipe>> INDUSTRIAL_SAWMILL = ifmo("industrial_sawmill", 2, NO_PROPERTIES, IndustrialSawmillRecipe::new);
    public static final RegistryObject<SISORecipeType<VacuumFreezerSolidRecipe, ItemStack, ItemStack>> VACUUM_FREEZER_SOLID = ModRecipeTypes.<VacuumFreezerSolidRecipe, ItemStack, ItemStack>siso("vacuum_freezer_solid", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, List.of(ModRecipeProperty.DURATION), VacuumFreezerSolidRecipe::new);
    public static final RegistryObject<SISORecipeType<VacuumFreezerFluidRecipe, FluidStack, FluidStack>> VACUUM_FREEZER_FLUID = ModRecipeTypes.<VacuumFreezerFluidRecipe, FluidStack, FluidStack>siso("vacuum_freezer_fluid", ModRecipeIngredientTypes.FLUID, ModRecipeOutputTypes.FLUID, List.of(ModRecipeProperty.DURATION), VacuumFreezerFluidRecipe::new);
    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> WIREMILL = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("wiremill", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::wiremill);

    public static final RegistryObject<SISORecipeType<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>> MACERATOR = ModRecipeTypes.<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack>siso("macerator", ModRecipeIngredientTypes.ITEM, ModRecipeOutputTypes.ITEM, SISORecipe::bender);
    
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
    
    private static <R extends MIMORecipe> RegistryObject<MIMORecipeType<R>> mimo(String name, int inputCount, int outputCount, MIMORecipeType.MIMORecipeFactory<R> factory) {
        return mimo(name, inputCount, outputCount, BASIC_PROPERTIES, factory);
    }

    private static <R extends MIMORecipe> RegistryObject<MIMORecipeType<R>> mimo(String name, int inputCount, int outputCount, List<RecipeProperty<?>> properties, MIMORecipeType.MIMORecipeFactory<R> factory) {
        return register(name, () -> new MIMORecipeType<>(location(name), inputCount, outputCount, properties, factory));
    }

    private static <R extends SIMORecipe<IN, OUT>, IN, OUT> RegistryObject<SIMORecipeType<R, IN, OUT>> simo(String name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, RecipeOutputType<OUT> outputType, SIMORecipeType.SIMORecipeFactory<R, IN, OUT> factory) {
        return simo(name, inputType, outputType, BASIC_PROPERTIES, factory);
    }

    private static <R extends SIMORecipe<IN, OUT>, IN, OUT> RegistryObject<SIMORecipeType<R, IN, OUT>> simo(String name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, SIMORecipeType.SIMORecipeFactory<R, IN, OUT> factory) {
        return register(name, () -> new SIMORecipeType<>(location(name), inputType, outputType, properties, factory));
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

    private static <R extends SISORecipe<IN, OUT>, IN, OUT> RegistryObject<SISORecipeType<R, IN, OUT>> siso(String name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, RecipeOutputType<OUT> outputType, SISORecipeType.SISORecipeFactory<R, IN, OUT> factory) {
        return siso(name, inputType, outputType, BASIC_PROPERTIES, factory);
    }

    private static <R extends SISORecipe<IN, OUT>, IN, OUT> RegistryObject<SISORecipeType<R, IN, OUT>> siso(String name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, SISORecipeType.SISORecipeFactory<R, IN, OUT> factory) {
        return register(name, () -> new SISORecipeType<>(location(name), inputType, outputType, properties, factory));
    }

    private static <R extends RecipeType<?>> RegistryObject<R> register(String name, Supplier<R> recipeType) {
        return RECIPE_TYPES.register(name, recipeType);
    }

    private ModRecipeTypes() {}
}
