package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.blockentity.component.ManagedRecipeHandler;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class MicrowaveOvenBlockEntity extends SimpleMachineBlockEntity {

    public MicrowaveOvenBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.MICROWAVE_OVEN, pos, state, SimpleMachineMenu::microwaveOven, MicrowaveRecipeHandler::new, SlotQueueMode.BOTH);
    }

    public static class MicrowaveOvenRecipeProvider implements RecipeProvider<SISORecipe<ItemStack, ItemStack>, ItemStack> {
        private static final RecipeProvider<SISORecipe<ItemStack, ItemStack>, ItemStack> INSTANCE = new MicrowaveOvenRecipeProvider();

        private final Map<ResourceLocation, SISORecipe<ItemStack, ItemStack>> byName = new HashMap<>();

        @Nullable
        @Override
        public SISORecipe<ItemStack, ItemStack> getRecipeFor(Level level, ItemStack input) {
            SISORecipe<ItemStack, ItemStack> recipe = ModRecipeManagers.FURNACE.getRecipeFor(level, input);
            if (recipe != null) {
                ResourceLocation id = location("microwave", recipe.getId().toString().replace(':', '/'));
                SISORecipe<ItemStack, ItemStack> translated = SISORecipe.furnace(id, recipe.getInput(), recipe.getOutput(), RecipePropertyMap.builder().duration(25).energyCost(4).build());
                this.byName.put(id, translated);
                return translated;
            }
            // Hardcoded egg recipe
            if (input.is(Items.EGG)) {
                return SISORecipe.furnace(location("microwave", "egg"), ModRecipeIngredientTypes.ITEM.of(input), input, RecipePropertyMap.builder().duration(25).energyCost(4).build());
            }
            return null;
        }

        @Nullable
        @Override
        public SISORecipe<ItemStack, ItemStack> getById(Level level, ResourceLocation id) {
            return this.byName.get(id);
        }

        @Override
        public boolean hasRecipeFor(Level level, ItemStack input) {
            return ModRecipeManagers.FURNACE.hasRecipeFor(level, input) || input.is(Items.EGG);
        }

        @Override
        public void reset() {
            this.byName.clear();
        }
    }

    private static class MicrowaveRecipeHandler extends ManagedRecipeHandler<SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack> {
        private static final ResourceLocation NAME = location("microwave_recipe_handler");

        protected MicrowaveRecipeHandler(SimpleMachineBlockEntity parent) {
            super(parent, MicrowaveOvenRecipeProvider.INSTANCE, ModRecipeOutputTypes.ITEM, ModRecipeOutputTypes.ITEM, true, Function.identity(), ManagedRecipeHandler::getSingleInput, ManagedRecipeHandler::canAddSingleOutput, ManagedRecipeHandler::consumeSingleInput, ManagedRecipeHandler::addSingleOutput);
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }

        @Override
        protected void consumeInput(SISORecipe<ItemStack, ItemStack> recipe) {
            recipe.getInput().getItemStacks().forEach(this::checkStack);
            checkStack(recipe.getOutput());

            super.consumeInput(recipe);
        }

        private void checkStack(ItemStack stack) {
            if (stack.is(GregTechTags.MICROWAVE_BLACKLIST)) {
                // You do not put these in a microwave
                MachineSafety.explodeMachine(this.parent, 4);
            }
            else if (ForgeHooks.getBurnTime(stack, null) > 0) {
                // It burns? we burn.
                MachineSafety.setBlockOnFire(this.parent.getLevel(), this.parent.getBlockPos());
            }
        }
    }
}
