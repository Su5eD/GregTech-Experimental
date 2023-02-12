package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.recipe.gen.ConditionalShapedRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;

public class HarderRecipesPackGen extends RecipeProvider {

    public HarderRecipesPackGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.MACHINE)
            .define(SelectedProfileCondition.REFINED_IRON, 'R', Plate.REFINED_IRON.getTag())
            .define(SelectedProfileCondition.REGULAR_IRON, 'R', Plate.IRON.getTag())
            .define('W', GregTechTags.WRENCH)
            .pattern("RRR")
            .pattern("RWR")
            .pattern("RRR")
            .unlockedBy("has_wrench", has(GregTechTags.WRENCH))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, ic2("shaped/machine"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.RE_BATTERY)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('C', GregTechTags.INSULATED_COPPER_CABLE)
            .define('T', Plate.TIN)
            .pattern(" C ")
            .pattern("TRT")
            .pattern("TRT")
            .unlockedBy("has_wrench", has(GregTechTags.WRENCH))
            .addCondition(IC2_LOADED, SelectedProfileCondition.CLASSIC)
            .save(finishedRecipeConsumer, ic2("shaped/re_battery"));
    }

    private static ResourceLocation ic2(String name) {
        return new ResourceLocation(ModHandler.IC2_MODID, name);
    }

    private static ResourceLocation name(ItemLike item) {
        return new ResourceLocation(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath());
    }
}
