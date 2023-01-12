package dev.su5ed.gregtechmod.recipe.setup;

import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.recipe.AlloySmelterRecipe;
import dev.su5ed.gregtechmod.recipe.AssemblerRecipe;
import dev.su5ed.gregtechmod.recipe.CanningMachineRecipe;
import dev.su5ed.gregtechmod.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gregtechmod.recipe.PulverizerRecipe;
import dev.su5ed.gregtechmod.recipe.type.ItemFluidRecipe;
import dev.su5ed.gregtechmod.recipe.type.ItemFluidRecipeType;
import dev.su5ed.gregtechmod.recipe.type.MIMORecipe;
import dev.su5ed.gregtechmod.recipe.type.MIMORecipeType;
import dev.su5ed.gregtechmod.recipe.type.MISORecipe;
import dev.su5ed.gregtechmod.recipe.type.MISORecipeType;
import dev.su5ed.gregtechmod.recipe.type.RecipeOutputType;
import dev.su5ed.gregtechmod.recipe.type.SIMORecipe;
import dev.su5ed.gregtechmod.recipe.type.SIMORecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static dev.su5ed.gregtechmod.api.Reference.location;

public final class ModRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MODID);

    public static final RegistryObject<MISORecipeType<AlloySmelterRecipe>> ALLOY_SMELTER = miso("alloy_smelter", 2, ModRecipeOutputTypes.ITEM, AlloySmelterRecipe::new);
    public static final RegistryObject<MISORecipeType<AssemblerRecipe>> ASSEMBLER = miso("assembler", 2, ModRecipeOutputTypes.ITEM, AssemblerRecipe::new);
    public static final RegistryObject<MIMORecipeType<CanningMachineRecipe>> CANNING_MACHINE = mimo("canning_machine", 2, List.of(ModRecipeOutputTypes.ITEM, ModRecipeOutputTypes.ITEM), CanningMachineRecipe::new);
    public static final RegistryObject<SIMORecipeType<PulverizerRecipe>> PULVERIZER = simo("pulverizer", List.of(ModRecipeOutputTypes.ITEM, ModRecipeOutputTypes.ITEM), PulverizerRecipe::new);
    public static final RegistryObject<ItemFluidRecipeType<IndustrialGrinderRecipe>> INDUSTRIAL_GRINDER = itemFluid("industrial_grinder", 3, IndustrialGrinderRecipe::new);

    public static void init(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }

    private static <R extends MIMORecipe> RegistryObject<MIMORecipeType<R>> mimo(String name, int inputCount, List<RecipeOutputType<ItemStack>> outputTypes, MIMORecipeType.MIMORecipeFactory<R> factory) {
        return register(name, () -> new MIMORecipeType<>(location(name), inputCount, outputTypes, factory));
    }

    private static <R extends SIMORecipe> RegistryObject<SIMORecipeType<R>> simo(String name, List<RecipeOutputType<ItemStack>> outputTypes, SIMORecipeType.SIMORecipeFactory<R> factory) {
        return register(name, () -> new SIMORecipeType<>(location(name), outputTypes, factory));
    }

    private static <R extends MISORecipe> RegistryObject<MISORecipeType<R>> miso(String name, int inputCount, RecipeOutputType<ItemStack> outputType, MISORecipeType.MISORecipeFactory<R> factory) {
        return register(name, () -> new MISORecipeType<>(location(name), inputCount, outputType, factory));
    }

    private static <R extends ItemFluidRecipe> RegistryObject<ItemFluidRecipeType<R>> itemFluid(String name, int outputCount, ItemFluidRecipeType.ItemFluidRecipeFactory<R> factory) {
        return register(name, () -> new ItemFluidRecipeType<>(location(name), outputCount, factory));
    }

    private static <R extends RecipeType<?>> RegistryObject<R> register(String name, Supplier<R> recipeType) {
        return RECIPE_TYPES.register(name, recipeType);
    }

    private ModRecipeTypes() {}
}
