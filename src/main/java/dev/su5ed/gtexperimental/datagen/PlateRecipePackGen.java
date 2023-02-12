package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.GregTechTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;

public class PlateRecipePackGen extends RecipeProvider {

    public PlateRecipePackGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        shaped(Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
            .define('X', GregTechTags.material("plates", "gold"))
            .define('T', GregTechTags.HARD_HAMMER)
            .pattern("XXT")
            .unlockedBy("has_hard_hammer", has(GregTechTags.HARD_HAMMER))
            .save(finishedRecipeConsumer, name(Items.LIGHT_WEIGHTED_PRESSURE_PLATE));
        shaped(Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
            .define('X', GregTechTags.material("plates", "iron"))
            .define('T', GregTechTags.HARD_HAMMER)
            .pattern("XXT")
            .unlockedBy("has_hard_hammer", has(GregTechTags.HARD_HAMMER))
            .save(finishedRecipeConsumer, name(Items.HEAVY_WEIGHTED_PRESSURE_PLATE));
        shaped(Items.IRON_DOOR, 3)
            .define('X', GregTechTags.material("plates", "iron"))
            .define('T', GregTechTags.HARD_HAMMER)
            .pattern("XX ")
            .pattern("XXT")
            .pattern("XX ")
            .unlockedBy("has_hard_hammer", has(GregTechTags.HARD_HAMMER))
            .save(finishedRecipeConsumer, name(Items.IRON_DOOR));
        shaped(Items.IRON_BARS, 8)
            .define('X', GregTechTags.material("plates", "iron"))
            .define('W', GregTechTags.WRENCH)
            .pattern(" W ")
            .pattern("XXX")
            .pattern("XXX")
            .unlockedBy("has_wrench", has(GregTechTags.WRENCH))
            .save(finishedRecipeConsumer, name(Items.IRON_BARS));
        shaped(Items.CAULDRON)
            .define('X', GregTechTags.material("plates", "iron"))
            .define('T', GregTechTags.HARD_HAMMER)
            .pattern("X X")
            .pattern("XTX")
            .pattern("XXX")
            .unlockedBy("has_hard_hammer", has(GregTechTags.HARD_HAMMER))
            .save(finishedRecipeConsumer, name(Items.CAULDRON));
        shaped(Items.HOPPER)
            .define('X', GregTechTags.material("plates", "iron"))
            .define('W', GregTechTags.WRENCH)
            .define('C', Tags.Items.CHESTS_WOODEN)
            .pattern("XWX")
            .pattern("XCX")
            .pattern(" X ")
            .unlockedBy("has_wrench", has(GregTechTags.WRENCH))
            .save(finishedRecipeConsumer, name(Items.HOPPER));
        shaped(Items.SHEARS)
            .define('X', GregTechTags.material("plates", "iron"))
            .define('T', GregTechTags.HARD_HAMMER)
            .define('F', GregTechTags.FILE)
            .pattern("XF")
            .pattern("TX")
            .unlockedBy("has_hard_hammer", has(GregTechTags.HARD_HAMMER))
            .save(finishedRecipeConsumer, name(Items.SHEARS));
        shaped(Items.BUCKET)
            .define('X', GregTechTags.material("plates", "iron"))
            .define('T', GregTechTags.HARD_HAMMER)
            .pattern("XTX")
            .pattern(" X ")
            .unlockedBy("has_hard_hammer", has(GregTechTags.HARD_HAMMER))
            .save(finishedRecipeConsumer, name(Items.BUCKET));
        shaped(Items.MINECART)
            .define('X', GregTechTags.material("plates", "iron"))
            .define('T', GregTechTags.HARD_HAMMER)
            .pattern("XTX")
            .pattern("XXX")
            .unlockedBy("has_hard_hammer", has(GregTechTags.HARD_HAMMER))
            .save(finishedRecipeConsumer, name(Items.MINECART));
    }

    private static ResourceLocation name(ItemLike item) {
        return new ResourceLocation(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath());
    }
}
