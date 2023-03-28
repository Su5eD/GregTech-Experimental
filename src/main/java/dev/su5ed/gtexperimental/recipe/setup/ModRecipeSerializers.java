package dev.su5ed.gtexperimental.recipe.setup;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
import dev.su5ed.gtexperimental.recipe.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.recipe.crafting.FluidShapedRecipe;
import dev.su5ed.gtexperimental.recipe.crafting.ToolShapedRecipe;
import dev.su5ed.gtexperimental.recipe.crafting.ToolShapelessRecipe;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public final class ModRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MODID);

    public static final RegistryObject<RecipeSerializer<MISORecipe<ItemStack, ItemStack>>> ALLOY_SMELTER = register("alloy_smelter", ModRecipeTypes.ALLOY_SMELTER);
    public static final RegistryObject<RecipeSerializer<MISORecipe<ItemStack, ItemStack>>> ASSEMBLER = register("assembler", ModRecipeTypes.ASSEMBLER);
    public static final RegistryObject<RecipeSerializer<MIMORecipe>> CANNING_MACHINE = register("canning_machine", ModRecipeTypes.CANNING_MACHINE);
    public static final RegistryObject<RecipeSerializer<SIMORecipe<ItemStack, List<ItemStack>>>> PULVERIZER = register("pulverizer", ModRecipeTypes.PULVERIZER);
    public static final RegistryObject<RecipeSerializer<IFMORecipe>> INDUSTRIAL_GRINDER = register("industrial_grinder", ModRecipeTypes.INDUSTRIAL_GRINDER);
    public static final RegistryObject<RecipeSerializer<SISORecipe<ItemStack, ItemStack>>> BENDER = register("bender", ModRecipeTypes.BENDER);
    public static final RegistryObject<RecipeSerializer<MISORecipe<FluidStack, FluidStack>>> CHEMICAL = register("chemical", ModRecipeTypes.CHEMICAL);
    public static final RegistryObject<RecipeSerializer<SIMORecipe<FluidStack, List<FluidStack>>>> DISTILLATION = register("distillation", ModRecipeTypes.DISTILLATION);
    public static final RegistryObject<RecipeSerializer<MISORecipe<FluidStack, ItemStack>>> FUSION_SOLID = register("fusion_solid", ModRecipeTypes.FUSION_SOLID);
    public static final RegistryObject<RecipeSerializer<MISORecipe<FluidStack, FluidStack>>> FUSION_FLUID = register("fusion_fluid", ModRecipeTypes.FUSION_FLUID);
    public static final RegistryObject<RecipeSerializer<SIMORecipe<ItemStack, List<ItemStack>>>> IMPLOSION = register("implosion", ModRecipeTypes.IMPLOSION);
    public static final RegistryObject<RecipeSerializer<MIMORecipe>> BLAST_FURNACE = register("blast_furnace", ModRecipeTypes.BLAST_FURNACE);
    public static final RegistryObject<RecipeSerializer<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>>> INDUSTRIAL_CENTRIFUGE = register("industrial_centrifuge", ModRecipeTypes.INDUSTRIAL_CENTRIFUGE);
    public static final RegistryObject<RecipeSerializer<SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>>>> INDUSTRIAL_ELECTROLYZER = register("industrial_electrolyzer", ModRecipeTypes.INDUSTRIAL_ELECTROLYZER);
    public static final RegistryObject<RecipeSerializer<SIMORecipe<ItemStack, List<ItemStack>>>> LATHE = register("lathe", ModRecipeTypes.LATHE);
    public static final RegistryObject<RecipeSerializer<MISORecipe<ItemStack, ItemStack>>> PRINTER = register("printer", ModRecipeTypes.PRINTER);
    public static final RegistryObject<RecipeSerializer<IFMORecipe>> INDUSTRIAL_SAWMILL = register("industrial_sawmill", ModRecipeTypes.INDUSTRIAL_SAWMILL);
    public static final RegistryObject<RecipeSerializer<SISORecipe<ItemStack, ItemStack>>> VACUUM_FREEZER_SOLID = register("vacuum_freezer_solid", ModRecipeTypes.VACUUM_FREEZER_SOLID);
    public static final RegistryObject<RecipeSerializer<SISORecipe<FluidStack, FluidStack>>> VACUUM_FREEZER_FLUID = register("vacuum_freezer_fluid", ModRecipeTypes.VACUUM_FREEZER_FLUID);
    public static final RegistryObject<RecipeSerializer<SISORecipe<ItemStack, ItemStack>>> WIREMILL = register("wiremill", ModRecipeTypes.WIREMILL);

    public static final RegistryObject<RecipeSerializer<SISORecipe<ItemStack, ItemStack>>> MACERATOR = register("macerator", ModRecipeTypes.MACERATOR);
    public static final RegistryObject<RecipeSerializer<SISORecipe<ItemStack, ItemStack>>> EXTRACTOR = register("extractor", ModRecipeTypes.EXTRACTOR);
    public static final RegistryObject<RecipeSerializer<SISORecipe<ItemStack, ItemStack>>> COMPRESSOR = register("compressor", ModRecipeTypes.COMPRESSOR);
    public static final RegistryObject<RecipeSerializer<SISORecipe<ItemStack, ItemStack>>> RECYCLER = register("recycler", ModRecipeTypes.RECYCLER);

    public static final RegistryObject<RecipeSerializer<SISORecipe<FluidStack, FluidStack>>> DENSE_LIQUID_FUEL = register("fuels/dense_liquid", ModRecipeTypes.DENSE_LIQUID_FUEL);
    public static final RegistryObject<RecipeSerializer<SISORecipe<Either<ItemStack, FluidStack>, FluidStack>>> DIESEL_FUEL = register("fuels/diesel", ModRecipeTypes.DIESEL_FUEL);
    public static final RegistryObject<RecipeSerializer<SISORecipe<FluidStack, FluidStack>>> GAS_FUEL = register("fuels/gas", ModRecipeTypes.GAS_FUEL);
    public static final RegistryObject<RecipeSerializer<SIMORecipe<Either<ItemStack, FluidStack>, List<ItemStack>>>> HOT_FUEL = register("fuels/hot", ModRecipeTypes.HOT_FUEL);
    public static final RegistryObject<RecipeSerializer<SISORecipe<Either<ItemStack, FluidStack>, ItemStack>>> MAGIC_FUEL = register("fuels/magic", ModRecipeTypes.MAGIC_FUEL);
    public static final RegistryObject<RecipeSerializer<SISORecipe<FluidStack, FluidStack>>> PLASMA_FUEL = register("fuels/plasma", ModRecipeTypes.PLASMA_FUEL);
    public static final RegistryObject<RecipeSerializer<SISORecipe<FluidStack, FluidStack>>> STEAM_FUEL = register("fuels/steam", ModRecipeTypes.STEAM_FUEL);

    public static final RegistryObject<RecipeSerializer<ShapedRecipe>> TOOL_SHAPED_RECIPE = RECIPE_SERIALIZERS.register("tool_crafting_shaped", () -> ToolShapedRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<ShapelessRecipe>> TOOL_SHAPELESS_RECIPE = RECIPE_SERIALIZERS.register("tool_crafting_shapeless", () -> ToolShapelessRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<ShapedRecipe>> FLUID_SHAPED_RECIPE = RECIPE_SERIALIZERS.register("fluid_crafting_shaped", () -> FluidShapedRecipe.SERIALIZER);

    public static void init(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }

    private static <T extends BaseRecipeType<R, ?>, R extends BaseRecipe<?, ?, ?, ? super R>> RegistryObject<RecipeSerializer<R>> register(String name, Supplier<? extends T> type) {
        return RECIPE_SERIALIZERS.register(name, () -> new BaseRecipeSerializer<>(type.get()));
    }

    private ModRecipeSerializers() {}
}
