package dev.su5ed.gregtechmod.recipe.setup;

import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.recipe.AlloySmelterRecipe;
import dev.su5ed.gregtechmod.recipe.AssemblerRecipe;
import dev.su5ed.gregtechmod.recipe.CanningMachineRecipe;
import dev.su5ed.gregtechmod.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gregtechmod.recipe.PulverizerRecipe;
import dev.su5ed.gregtechmod.recipe.type.ItemFluidRecipeType;
import dev.su5ed.gregtechmod.recipe.type.MIMORecipe;
import dev.su5ed.gregtechmod.recipe.type.MIMORecipeType;
import dev.su5ed.gregtechmod.recipe.type.MISORecipe;
import dev.su5ed.gregtechmod.recipe.type.MISORecipeType;
import dev.su5ed.gregtechmod.recipe.type.RecipeOutputType;
import dev.su5ed.gregtechmod.recipe.type.SIMORecipe;
import dev.su5ed.gregtechmod.recipe.type.SIMORecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static dev.su5ed.gregtechmod.api.Reference.location;

public final class ModRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MODID);

    public static final RegistryObject<MISORecipeType<AlloySmelterRecipe, ItemStack>> ALLOY_SMELTER = register("alloy_smelter", miso(2, ModRecipeOutputTypes.ITEM, AlloySmelterRecipe::new));
    public static final RegistryObject<MISORecipeType<AssemblerRecipe, ItemStack>> ASSEMBLER = register("assembler", miso(2, ModRecipeOutputTypes.ITEM, AssemblerRecipe::new));
    public static final RegistryObject<MIMORecipeType<CanningMachineRecipe, ItemStack>> CANNING_MACHINE = register("canning_machine", mimo(2, List.of(ModRecipeOutputTypes.ITEM, ModRecipeOutputTypes.ITEM), CanningMachineRecipe::new));
    public static final RegistryObject<SIMORecipeType<PulverizerRecipe, ItemStack>> PULVERIZER = register("pulverizer", simo(List.of(ModRecipeOutputTypes.ITEM, ModRecipeOutputTypes.ITEM), PulverizerRecipe::new));
    public static final RegistryObject<ItemFluidRecipeType<IndustrialGrinderRecipe>> INDUSTRIAL_GRINDER = register("industrial_grinder", new ItemFluidRecipeType<>(3, IndustrialGrinderRecipe::new));

    public static void init(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }

    public static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
        return register(name, RecipeType.simple(location(name)));
    }

    public static <T extends RecipeType<?>> RegistryObject<T> register(String name, T recipeType) {
        return RECIPE_TYPES.register(name, () -> recipeType);
    }

    public static <T extends MIMORecipe<O>, O> MIMORecipeType<T, O> mimo(int inputCount, List<RecipeOutputType<O>> outputTypes, MIMORecipeType.MIMORecipeFactory<T, O> factory) {
        return new MIMORecipeType<>(inputCount, outputTypes, factory);
    }

    public static <T extends SIMORecipe<O>, O> SIMORecipeType<T, O> simo(List<RecipeOutputType<O>> outputTypes, SIMORecipeType.SIMORecipeFactory<T, O> factory) {
        return new SIMORecipeType<>(outputTypes, factory);
    }

    public static <T extends MISORecipe<O>, O> MISORecipeType<T, O> miso(int inputCount, RecipeOutputType<O> outputType, MISORecipeType.MISORecipeFactory<T, O> factory) {
        return new MISORecipeType<>(inputCount, outputType, factory);
    }

    private ModRecipeTypes() {}
}
