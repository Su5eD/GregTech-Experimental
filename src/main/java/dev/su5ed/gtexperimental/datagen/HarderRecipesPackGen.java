package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import dev.su5ed.gtexperimental.recipe.type.VanillaFluidIngredient;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.crafting.ConditionalShapedRecipeBuilder.conditionalShaped;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.tagsIngredient;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

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
            .define('C', GregTechTags.CIRCUIT_TIER_7)
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
    }

    private static ResourceLocation ic2(String name) {
        return new ResourceLocation(ModHandler.IC2_MODID, name);
    }

    private static ResourceLocation name(ItemLike item) {
        return new ResourceLocation(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath());
    }
}
