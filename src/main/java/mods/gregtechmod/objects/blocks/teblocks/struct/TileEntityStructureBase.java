package mods.gregtechmod.objects.blocks.teblocks.struct;

import ic2.core.block.state.Ic2BlockState;
import ic2.core.block.state.UnlistedBooleanProperty;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityGTMachine;
import mods.gregtechmod.util.struct.Structure;
import mods.gregtechmod.util.struct.StructureElement;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class TileEntityStructureBase<T, R extends IMachineRecipe<RI, List<ItemStack>>, RI, I, RM extends IGtRecipeManager<RI, I, R>> extends TileEntityGTMachine<R, RI, I, RM> {
    public static final IUnlistedProperty<Boolean> PROPERTY_VALID = new UnlistedBooleanProperty("valid_structure");
    public final Structure<T> structure;

    public TileEntityStructureBase(String descriptionKey, int outputSlots, RM recipeManager) {
        super(descriptionKey, outputSlots, recipeManager);
        
        this.structure = new Structure<>(getStructurePattern(), getStructureElements(), this::createStructureInstance);
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.structure.checkWorldStructure(this.pos, this.getFacing());
    }

    protected abstract List<List<String>> getStructurePattern();
    
    protected abstract Map<Character, Collection<StructureElement>> getStructureElements();
    
    protected T createStructureInstance(EnumFacing facing, Map<Character, Collection<BlockPos>> elements) {
        return null;
    }
    
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
                
        if (tickCounter % 5 == 0) {
            this.structure.checkWorldStructure(this.pos, this.getFacing());
        }
    }

    @Override
    protected boolean canOperate(R recipe) {
        return super.canOperate(recipe) && this.structure.isValid();
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        this.structure.checkWorldStructure(this.pos, this.getFacing());
        return super.getExtendedState(state).withProperty(PROPERTY_VALID, this.structure.isValid());
    }
    
    @Override
    protected boolean needsConstantEnergy() {
        return super.needsConstantEnergy() && this.structure.isValid();
    }
}
