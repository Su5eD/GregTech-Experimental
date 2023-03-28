package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Plate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.compat.CompatRecipeBuilders.compressor;

public final class CompressorRecipesGen implements ModRecipeProvider {
    public static final CompressorRecipesGen INSTANCE = new CompressorRecipesGen();

    private CompressorRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        compressor(Dust.WOOD.getTag(), 8, Plate.WOOD.getItemStack(), finishedRecipeConsumer);
        compressor(GregTechTags.dust("iridium"), 1, Ingot.IRIDIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Dust.OSMIUM.getTag(), 1, Ingot.OSMIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Dust.PLUTONIUM.getTag(), 1, Ingot.PLUTONIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Dust.THORIUM.getTag(), 1, Ingot.THORIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Dust.LAZURITE.getTag(), 8, Miscellaneous.LAZURITE_CHUNK.getItemStack(), finishedRecipeConsumer);
        compressor(Nugget.IRIDIUM.getTag(), 9, Ingot.IRIDIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Nugget.OSMIUM.getTag(), 9, Ingot.OSMIUM.getItemStack(), finishedRecipeConsumer);
        compressor(GregTechTags.material("nuggets", "plutonium"), 9, Ingot.PLUTONIUM.getItemStack(), finishedRecipeConsumer);
        compressor(GregTechTags.material("nuggets", "thorium"), 9, Ingot.THORIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Tags.Items.GEMS_PRISMARINE, 9, new ItemStack(Items.PRISMARINE_BRICKS), finishedRecipeConsumer);
    }
}
