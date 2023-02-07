package dev.su5ed.gtexperimental.recipe.setup;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
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
import dev.su5ed.gtexperimental.recipe.IndustrialSawmillRecipe;
import dev.su5ed.gtexperimental.recipe.LatheRecipe;
import dev.su5ed.gtexperimental.recipe.PrinterRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import dev.su5ed.gtexperimental.recipe.VacuumFreezerFluidRecipe;
import dev.su5ed.gtexperimental.recipe.VacuumFreezerSolidRecipe;
import dev.su5ed.gtexperimental.recipe.WiremillRecipe;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MODID);

    public static final RegistryObject<RecipeSerializer<AlloySmelterRecipe>> ALLOY_SMELTER = register("alloy_smelter", ModRecipeTypes.ALLOY_SMELTER);
    public static final RegistryObject<RecipeSerializer<AssemblerRecipe>> ASSEMBLER = register("assembler", ModRecipeTypes.ASSEMBLER);
    public static final RegistryObject<RecipeSerializer<CanningMachineRecipe>> CANNING_MACHINE = register("canning_machine", ModRecipeTypes.CANNING_MACHINE);
    public static final RegistryObject<RecipeSerializer<PulverizerRecipe>> PULVERIZER = register("pulverizer", ModRecipeTypes.PULVERIZER);
    public static final RegistryObject<RecipeSerializer<IndustrialGrinderRecipe>> INDUSTRIAL_GRINDER = register("industrial_grinder", ModRecipeTypes.INDUSTRIAL_GRINDER);
    public static final RegistryObject<RecipeSerializer<BenderRecipe>> BENDER = register("bender", ModRecipeTypes.BENDER);
    public static final RegistryObject<RecipeSerializer<ChemicalRecipe>> CHEMICAL = register("chemical", ModRecipeTypes.CHEMICAL);
    public static final RegistryObject<RecipeSerializer<DistillationRecipe>> DISTILLATION = register("distillation", ModRecipeTypes.DISTILLATION);
    public static final RegistryObject<RecipeSerializer<FusionSolidRecipe>> FUSION_SOLID = register("fusion_solid", ModRecipeTypes.FUSION_SOLID);
    public static final RegistryObject<RecipeSerializer<FusionFluidRecipe>> FUSION_FLUID = register("fusion_fluid", ModRecipeTypes.FUSION_FLUID);
    public static final RegistryObject<RecipeSerializer<ImplosionRecipe>> IMPLOSION = register("implosion", ModRecipeTypes.IMPLOSION);
    public static final RegistryObject<RecipeSerializer<BlastFurnaceRecipe>> BLAST_FURNACE = register("blast_furnace", ModRecipeTypes.BLAST_FURNACE);
    public static final RegistryObject<RecipeSerializer<IndustrialCentrifugeRecipe>> INDUSTRIAL_CENTRIFUGE = register("industrial_centrifuge", ModRecipeTypes.INDUSTRIAL_CENTRIFUGE);
    public static final RegistryObject<RecipeSerializer<IndustrialElectrolyzerRecipe>> INDUSTRIAL_ELECTROLYZER = register("industrial_electrolyzer", ModRecipeTypes.INDUSTRIAL_ELECTROLYZER);
    public static final RegistryObject<RecipeSerializer<LatheRecipe>> LATHE = register("lathe", ModRecipeTypes.LATHE);
    public static final RegistryObject<RecipeSerializer<PrinterRecipe>> PRINTER = register("printer", ModRecipeTypes.PRINTER);
    public static final RegistryObject<RecipeSerializer<IndustrialSawmillRecipe>> INDUSTRIAL_SAWMILL = register("industrial_sawmill", ModRecipeTypes.INDUSTRIAL_SAWMILL);
    public static final RegistryObject<RecipeSerializer<VacuumFreezerSolidRecipe>> VACUUM_FREEZER_SOLID = register("vacuum_freezer_solid", ModRecipeTypes.VACUUM_FREEZER_SOLID);
    public static final RegistryObject<RecipeSerializer<VacuumFreezerFluidRecipe>> VACUUM_FREEZER_FLUID = register("vacuum_freezer_fluid", ModRecipeTypes.VACUUM_FREEZER_FLUID);
    public static final RegistryObject<RecipeSerializer<WiremillRecipe>> WIREMILL = register("wiremill", ModRecipeTypes.WIREMILL);

    public static void init(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
    
    private static <T extends BaseRecipeType<R>, R extends BaseRecipe<?, ?, ? super R>> RegistryObject<RecipeSerializer<R>> register(String name, Supplier<? extends T> type) {
        return RECIPE_SERIALIZERS.register(name, () -> new BaseRecipeSerializer<>(type.get()));
    }
    
    private ModRecipeSerializers() {}
}
