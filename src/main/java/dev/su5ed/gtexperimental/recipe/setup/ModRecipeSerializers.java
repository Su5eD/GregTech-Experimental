package dev.su5ed.gtexperimental.recipe.setup;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.DistillationRecipe;
import dev.su5ed.gtexperimental.recipe.FusionFluidRecipe;
import dev.su5ed.gtexperimental.recipe.FusionSolidRecipe;
import dev.su5ed.gtexperimental.recipe.ImplosionRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MODID);

    public static final RegistryObject<RecipeSerializer<AlloySmelterRecipe>> ALLOY_SMELTER = RECIPE_SERIALIZERS.register("alloy_smelter", AlloySmelterRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<AssemblerRecipe>> ASSEMBLER = RECIPE_SERIALIZERS.register("assembler", AssemblerRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<CanningMachineRecipe>> CANNING_MACHINE = RECIPE_SERIALIZERS.register("canning_machine", CanningMachineRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<PulverizerRecipe>> PULVERIZER = RECIPE_SERIALIZERS.register("pulverizer", PulverizerRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<IndustrialGrinderRecipe>> INDUSTRIAL_GRINDER = RECIPE_SERIALIZERS.register("industrial_grinder", IndustrialGrinderRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<BenderRecipe>> BENDER = RECIPE_SERIALIZERS.register("bender", BenderRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<ChemicalRecipe>> CHEMICAL = RECIPE_SERIALIZERS.register("chemical", ChemicalRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<DistillationRecipe>> DISTILLATION = RECIPE_SERIALIZERS.register("distillation", DistillationRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<FusionSolidRecipe>> FUSION_SOLID = RECIPE_SERIALIZERS.register("fusion_solid", FusionSolidRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<FusionFluidRecipe>> FUSION_FLUID = RECIPE_SERIALIZERS.register("fusion_fluid", FusionFluidRecipe::createSerializer);
    public static final RegistryObject<RecipeSerializer<ImplosionRecipe>> IMPLOSION = RECIPE_SERIALIZERS.register("implosion", ImplosionRecipe::createSerializer);

    public static void init(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
    
    private ModRecipeSerializers() {}
}
