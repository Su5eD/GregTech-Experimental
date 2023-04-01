package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.blockentity.component.RecipeHandler;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class AutomaticRecyclerBlockEntity extends SimpleMachineBlockEntity {

    public AutomaticRecyclerBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.AUTO_RECYCLER, pos, state, SimpleMachineMenu::autoRecycler, be -> RecyclerRecipeHandler.createRecycler(be, ModRecipeManagers.RECYCLER), true);
    }

    private static class RecyclerRecipeHandler extends RecipeHandler.SISO {
        private static final ResourceLocation NAME = location("recycler_recipe_handler");
        private static final int CHANCE = 8;

        public static RecyclerRecipeHandler createRecycler(SimpleMachineBlockEntity parent, RecipeManager<SISORecipe<ItemStack, ItemStack>, RecipeIngredientType<? extends RecipeIngredient<ItemStack>, ItemStack>, ItemStack, ItemStack> manager) {
            RecipeOutputType<ItemStack> outputType = manager.getRecipeType().getOutputType();
            return new RecyclerRecipeHandler(parent, manager, outputType, outputType);
        }

        public RecyclerRecipeHandler(SimpleMachineBlockEntity parent, RecipeManager<SISORecipe<ItemStack, ItemStack>, RecipeIngredientType<? extends RecipeIngredient<ItemStack>, ItemStack>, ItemStack, ItemStack> manager, RecipeOutputType<ItemStack> inputSerializer, RecipeOutputType<ItemStack> outputSerializer) {
            super(parent, manager, inputSerializer, outputSerializer);
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }

        @Override
        public boolean accepts(ItemStack input) {
            // TODO DISABLED Due to IC2 bug
            // RecipeInputItemStack#listStacks returns an immutable list unlike other implementations,
            // causing a crash in RecipeInputBase#getInputs when it tries to call replaceAll on the returned list
            return false;
        }

        @Override
        protected void addOutput(SISORecipe<ItemStack, ItemStack> recipe) {
            if (this.parent.getLevel().random.nextInt(CHANCE) == 0) {
                super.addOutput(recipe);
            }
        }
    }
}
