package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.blockentity.base.SimpleMachineBlockEntity;
import dev.su5ed.gtexperimental.blockentity.component.RecipeHandler;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AutomaticMaceratorBlockEntity extends SimpleMachineBlockEntity {

    public AutomaticMaceratorBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.AUTO_MACERATOR, pos, state, SimpleMachineMenu::autoMacerator);
    }

    @Override
    protected RecipeHandler<?, ?, ?, ?> createRecipeHandler() {
        // TODO Get output type from manager?
        return new RecipeHandler.SISO(this, ModRecipeManagers.MACERATOR, ModRecipeOutputTypes.ITEM, ModRecipeTypes.MACERATOR.get().getOutputType());
    }
}
