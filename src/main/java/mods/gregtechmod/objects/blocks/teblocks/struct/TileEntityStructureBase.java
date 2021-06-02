package mods.gregtechmod.objects.blocks.teblocks.struct;

import ic2.core.block.state.Ic2BlockState;
import ic2.core.block.state.UnlistedBooleanProperty;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityGTMachine;
import mods.gregtechmod.util.struct.Structure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class TileEntityStructureBase<T, R extends IMachineRecipe<RI, List<ItemStack>>, RI, I, RM extends IGtRecipeManager<RI, I, R>> extends TileEntityGTMachine<R, RI, I, RM> {
    public static final IUnlistedProperty<Boolean> PROPERTY_VALID = new UnlistedBooleanProperty("valid_structure");
    protected final Structure<T> structure;

    public TileEntityStructureBase(String descriptionKey, double maxEnergy, int outputSlots, int defaultTier, RM recipeManager) {
        super(descriptionKey, maxEnergy, outputSlots, defaultTier, recipeManager);
        
        Map<Character, Predicate<IBlockState>> elements = new HashMap<>();
        getStructureElements(elements);
        this.structure = new Structure<>(getStructurePattern(), elements, this::createStructureInstance);
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        checkWorldStructure();
    }

    protected abstract List<List<String>> getStructurePattern();
    
    protected abstract void getStructureElements(Map<Character, Predicate<IBlockState>> map);
    
    protected T createStructureInstance(Collection<IBlockState> states) {
        return null;
    }
    
    public Structure<T>.WorldStructure checkWorldStructure() {
        return this.structure.checkWorldStructure(this.pos, this.getFacing(), this.world);
    }
    
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
                
        if (tickCounter % 5 == 0) {
            this.structure.checkWorldStructure(this.pos, this.getFacing(), this.world);
        }
    }

    @Override
    protected boolean canOperate(R recipe) {
        return super.canOperate(recipe) && checkWorldStructure().valid;
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        return super.getExtendedState(state).withProperty(PROPERTY_VALID, this.structure.checkWorldStructure(this.pos, this.getFacing(), this.world).valid);
    }
    
    
    @Override
    protected boolean needsConstantEnergy() {
        return super.needsConstantEnergy() && checkWorldStructure().valid;
    }
}
