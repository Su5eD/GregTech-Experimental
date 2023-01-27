package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.ftb.mods.ftbic.world.ResourceElements;
import dev.ftb.mods.ftbic.world.ResourceType;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.NotCondition;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.FTBIC_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.NOT_IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.fusionFluid;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.fusionSolid;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class FusionRecipesGen implements ModRecipeProvider {
    public static final FusionRecipesGen INSTANCE = new FusionRecipesGen();

    private FusionRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        fusionSolid(ModRecipeIngredientTypes.FLUID.of(ModFluid.WOLFRAMIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.BERYLIUM.getTag()), Dust.PLATINUM.getItemStack(), 512, 32768, 100000000)
            .build(finishedRecipeConsumer, solidId("platinum_dust"));

        fusionFluid(ModRecipeIngredientTypes.FLUID.of(ModFluid.TRITIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.DEUTERIUM.getTag()), ModFluid.HELIUM_PLASMA.getFluidStack(buckets(1)), 128, 4096, 40000000)
            .build(finishedRecipeConsumer, fluidId("helium_plasma_h3_h2"));
        fusionFluid(ModRecipeIngredientTypes.FLUID.of(ModFluid.HELIUM3.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.DEUTERIUM.getTag()), ModFluid.HELIUM_PLASMA.getFluidStack(buckets(1)), 128, 2048, 60000000)
            .build(finishedRecipeConsumer, fluidId("helium_plasma_he3_h2"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // IC2
        fusionSolid(ModRecipeIngredientTypes.FLUID.of(ModFluid.WOLFRAMIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.LITHIUM.getTag()), new ItemStack(Ic2Items.IRIDIUM_ORE), 512, 32768, 150000000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, solidId("ic2/iridium_ore"));

        // FTBIC
        fusionSolid(ModRecipeIngredientTypes.FLUID.of(ModFluid.WOLFRAMIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.LITHIUM.getTag()), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.INGOT).orElseThrow().get()), 512, 32768, 150000000)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, solidId("ftbic/iridium_chunk"));
    }

    private static ResourceLocation solidId(String name) {
        return location("fusion_solid/" + name);
    }

    private static ResourceLocation fluidId(String name) {
        return location("fusion_fluid/" + name);
    }
}
