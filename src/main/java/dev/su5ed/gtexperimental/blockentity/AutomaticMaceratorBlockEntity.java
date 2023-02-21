package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.blockentity.base.SimpleMachineBlockEntity;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

public class AutomaticMaceratorBlockEntity extends SimpleMachineBlockEntity {

    public AutomaticMaceratorBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.AUTO_MACERATOR, pos, state, SimpleMachineMenu::autoMacerator);
    }

    @Override
    protected RecipeHandler<?, ?, ?, ?> createRecipeHandler() {
        // TODO Get output type from manager?
        return new RecipeHandler.SIMO<>(this, ModRecipeManagers.PULVERIZER, ModRecipeOutputTypes.ITEM, ModRecipeTypes.PULVERIZER.get().getOutputType());
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1000;
    }

    @Override
    public Collection<Direction> getSinkSides() {
        return GtUtil.ALL_FACINGS;
    }

    @Override
    public int getBaseSinkTier() {
        return 1;
    }
}
