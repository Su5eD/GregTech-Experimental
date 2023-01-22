package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.compat.CompatRecipeBuilders.compressor;
import static dev.su5ed.gtexperimental.recipe.gen.compat.CompatRecipeBuilders.ic2Compressor;

public final class CompressorRecipesGen implements ModRecipeProvider {
    public static final CompressorRecipesGen INSTANCE = new CompressorRecipesGen();

    private CompressorRecipesGen() {}
    
    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        compressor(Dust.WOOD.getTag(), 8, Plate.WOOD.getItemStack(), finishedRecipeConsumer);
        compressor(GregTechTags.material("dusts", "iridium"), 1, Ingot.IRIDIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Dust.OSMIUM.getTag(), 1, Ingot.OSMIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Dust.PLUTONIUM.getTag(), 1, Ingot.PLUTONIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Dust.THORIUM.getTag(), 1, Ingot.THORIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Dust.LAZURITE.getTag(), 8, Miscellaneous.LAZURITE_CHUNK.getItemStack(), finishedRecipeConsumer);
        compressor(Nugget.IRIDIUM.getTag(), 9, Ingot.IRIDIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Nugget.OSMIUM.getTag(), 9, Ingot.OSMIUM.getItemStack(), finishedRecipeConsumer);
        compressor(GregTechTags.material("nuggets", "plutonium"), 9, Ingot.PLATINUM.getItemStack(), finishedRecipeConsumer);
        compressor(GregTechTags.material("nuggets", "thorium"), 9, Ingot.THORIUM.getItemStack(), finishedRecipeConsumer);
        compressor(Tags.Items.GEMS_PRISMARINE, 9, new ItemStack(Items.PRISMARINE_BRICKS), finishedRecipeConsumer);
        
        // IC2
        // Classic
        ic2Compressor(Ic2Items.IRIDIUM_ORE, 1, Ingot.IRIDIUM.getItemStack(), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        ic2Compressor(GregTechTags.material("nuggets", "uranium"), 1, new ItemStack(Ic2Items.URANIUM_INGOT), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        ic2Compressor(ItemTags.SAPLINGS, 4, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        ic2Compressor(ItemTags.LEAVES, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        ic2Compressor(Tags.Items.CROPS_WHEAT, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        ic2Compressor(Items.SUGAR_CANE, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        ic2Compressor(Items.CACTUS, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        ic2Compressor(Miscellaneous.INDIGO_BLOSSOM, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        ic2Compressor(Dust.URANIUM, 8, new ItemStack(Ic2Items.URANIUM_INGOT), finishedRecipeConsumer, SelectedProfileCondition.CLASSIC);
        
        // Experimental
        ic2Compressor(Dust.URANIUM, 1, new ItemStack(Ic2Items.URANIUM_238), finishedRecipeConsumer, SelectedProfileCondition.EXPERIMENTAL);
    }
}
