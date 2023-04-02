package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.blockentity.component.ManagedRecipeHandler;
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
    private static final ResourceLocation RECYCLER_RECIPE_HANDLER = location("recycler_recipe_handler");
    private static final int OUTPUT_CHANCE = 8;

    public AutomaticRecyclerBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.AUTO_RECYCLER, pos, state, SimpleMachineMenu::autoRecycler, be -> createRecycler(be, ModRecipeManagers.RECYCLER), SlotQueueMode.BOTH);
    }

    public static RecipeHandler<SimpleMachineBlockEntity, SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack> createRecycler(SimpleMachineBlockEntity parent, RecipeManager<SISORecipe<ItemStack, ItemStack>, RecipeIngredientType<? extends RecipeIngredient<ItemStack>, ItemStack>, ItemStack, ItemStack> manager) {
        RecipeOutputType<ItemStack> outputType = manager.getRecipeType().getOutputType();
        // TODO DISABLED Due to IC2 bug
        // RecipeInputItemStack#listStacks returns an immutable list unlike other implementations,
        // causing a crash in RecipeInputBase#getInputs when it tries to call replaceAll on the returned list
        return new ManagedRecipeHandler<>(parent, manager, outputType, outputType, RECYCLER_RECIPE_HANDLER, stack -> ItemStack.EMPTY,
            ManagedRecipeHandler::getSingleInput, ManagedRecipeHandler::canAddSingleOutput, ManagedRecipeHandler::consumeSingleInput, AutomaticRecyclerBlockEntity::addRecyclerOutput);
    }

    private static void addRecyclerOutput(ManagedRecipeHandler<?, ?, ?> handler, SISORecipe<ItemStack, ItemStack> recipe) {
        if (handler.getParent().getLevel().random.nextInt(OUTPUT_CHANCE) == 0) {
            ManagedRecipeHandler.addSingleOutput(handler, recipe);
        }
    }
}
