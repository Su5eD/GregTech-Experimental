package dev.su5ed.gtexperimental.datagen.pack;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.recipe.gen.compat.RCRollingRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import dev.su5ed.gtexperimental.recipe.type.VanillaFluidIngredient;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.*;
import static dev.su5ed.gtexperimental.recipe.crafting.ConditionalShapedRecipeBuilder.conditionalShaped;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.tagsIngredient;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;

public class HarderRecipesPackGen extends RecipeProvider {

    public HarderRecipesPackGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        conditionalShaped(Ic2Items.MACHINE)
            .define('R', GregTechTags.UNIVERSAL_IRON_PLATE)
            .define('W', GregTechTags.WRENCH)
            .pattern("RRR")
            .pattern("RWR")
            .pattern("RRR")
            .unlockedBy("has_wrench", has(GregTechTags.WRENCH))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, ic2("shaped/machine"));
        conditionalShaped(Ic2Items.RE_BATTERY)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('C', GregTechTags.INSULATED_COPPER_CABLE)
            .define('T', Plate.TIN)
            .pattern(" C ")
            .pattern("TRT")
            .pattern("TRT")
            .unlockedBy("has_wrench", has(GregTechTags.WRENCH))
            .addCondition(IC2_LOADED, SelectedProfileCondition.CLASSIC)
            .save(finishedRecipeConsumer, ic2("shaped/re_battery"));
        conditionalShaped(Ic2Items.THICK_NEUTRON_REFLECTOR)
            .define('B', VanillaFluidIngredient.of(ModFluid.BERYLIUM.getTag(), buckets(1)))
            .define('N', Ic2Items.NEUTRON_REFLECTOR)
            .pattern(" N ")
            .pattern("NBN")
            .pattern(" N ")
            .unlockedBy("has_neutron_reflector", has(Ic2Items.NEUTRON_REFLECTOR))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, ic2("shaped/thick_neutron_reflector"));
//        conditionalShaped(Ic2Items.CHAINSAW)
//            .define('C', GregTechTags.CIRCUIT)
//            .define('B', GregTechTags.RE_BATTERY)
//            .define('S', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.STEEL.getTag()))
//            .pattern(" SS")
//            .pattern("SCS")
//            .pattern("BS ")
//            .unlockedBy("has_circuit", has(GregTechTags.CIRCUIT))
//            .addCondition(IC2_LOADED)
//            .save(finishedRecipeConsumer, ic2("shaped/chainsaw"));
        conditionalShaped(Ic2Items.DRILL)
            .define('C', GregTechTags.CIRCUIT)
            .define('B', GregTechTags.RE_BATTERY)
            .define('S', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.STEEL.getTag()))
            .pattern(" S ")
            .pattern("SCS")
            .pattern("SBS")
            .unlockedBy("has_re_battery", has(GregTechTags.RE_BATTERY))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, ic2("shaped/drill"));
//        conditionalShaped(Ic2Items.ELECTRIC_HOE)
//            .define('C', GregTechTags.CIRCUIT)
//            .define('B', GregTechTags.RE_BATTERY)
//            .define('S', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.STEEL.getTag()))
//            .pattern("SS ")
//            .pattern(" C ")
//            .pattern(" B ")
//            .unlockedBy("has_circuit", has(GregTechTags.CIRCUIT))
//            .addCondition(IC2_LOADED)
//            .save(finishedRecipeConsumer, ic2("shaped/electric_hoe"));
        conditionalShaped(Ic2Items.ELECTRIC_TREETAP)
            .define('C', GregTechTags.CIRCUIT)
            .define('B', GregTechTags.RE_BATTERY)
            .define('S', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.STEEL.getTag()))
            .pattern(" B ")
            .pattern("SCS")
            .pattern("S  ")
            .unlockedBy("has_re_battery", has(GregTechTags.RE_BATTERY))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, ic2("shaped/electric_treetap"));
        conditionalShaped(Ic2Items.ELECTRIC_WRENCH)
            .define('C', GregTechTags.CIRCUIT)
            .define('B', GregTechTags.RE_BATTERY)
            .define('S', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.STEEL.getTag()))
            .pattern("S S")
            .pattern("SCS")
            .pattern(" B ")
            .unlockedBy("has_re_battery", has(GregTechTags.RE_BATTERY))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, ic2("shapeless/electric_wrench"));
        conditionalShaped(Ic2Items.NANO_SABER)
            .define('L', GregTechTags.MEDIUM_EU_STORE)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('P', Plate.PLATINUM.getTag())
            .define('C', ModCoverItem.ENERGY_FLOW_CIRCUIT.getTag())
            .pattern("PI ")
            .pattern("PI ")
            .pattern("CLC")
            .unlockedBy("has_iridium_alloy", has(GregTechTags.IRIDIUM_ALLOY))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, ic2("shaped/nano_saber"));
        conditionalShaped(Ic2Items.DIAMOND_DRILL)
            .define('M', Ic2Items.DRILL)
            .define('D', Tags.Items.GEMS_DIAMOND)
            .define('T', Plate.TITANIUM.getTag())
            .define('A', GregTechTags.ADVANCED_CIRCUIT)
            .pattern(" D ")
            .pattern("DMD")
            .pattern("TAT")
            .unlockedBy("has_drill", has(Ic2Items.DRILL))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, ic2("shaped/diamond_drill"));
        shapeless(Items.FLINT_AND_STEEL)
            .requires(Items.FLINT)
            .requires(Nugget.STEEL.getTag())
            .unlockedBy("has_steel_nugget", has(Nugget.STEEL.getTag()))
            .save(finishedRecipeConsumer, new ResourceLocation("flint_and_steel"));
        RecipeName iridiumAlloyId = RecipeName.common(ModHandler.IC2_MODID, "shaped", "iridium");
        ConditionalRecipe.builder()
            .addCondition(RAILCRAFT_LOADED).addCondition(IC2_LOADED)
            .addRecipe(cons -> new RCRollingRecipeBuilder(Ingot.IRIDIUM_ALLOY.getItemStack()).define('I', Plate.IRIDIUM.getTag()).define('A', Ic2Items.ALLOY).define('D', Dust.DIAMOND.getTag()).pattern("IAI").pattern("ADA").pattern("IAI").build(cons, iridiumAlloyId))
            .addCondition(NOT_RAILCRAFT_LOADED).addCondition(IC2_LOADED)
            .addRecipe(cons -> ShapedRecipeBuilder.shaped(Ingot.IRIDIUM_ALLOY).define('I', Plate.IRIDIUM.getTag()).define('A', Ic2Items.ALLOY).define('D', Dust.DIAMOND.getTag()).pattern("IAI").pattern("ADA").pattern("IAI").unlockedBy("has_iridium_plate", has(Plate.IRIDIUM.getTag())).save(cons, iridiumAlloyId.toLocation()))
            .build(finishedRecipeConsumer, iridiumAlloyId.toLocation());
    }

    private static ResourceLocation ic2(String name) {
        return new ResourceLocation(ModHandler.IC2_MODID, name);
    }
}
