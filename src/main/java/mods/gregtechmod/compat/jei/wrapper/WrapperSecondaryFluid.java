package mods.gregtechmod.compat.jei.wrapper;

import ic2.core.util.StackUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WrapperSecondaryFluid<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends WrapperMultiInput<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> {
    private static final List<ItemStack> FILLER = Collections.singletonList(ItemStack.EMPTY);
    private final int outputCount;

    public WrapperSecondaryFluid(R recipe, int outputCount) {
        super(recipe);
        this.outputCount = outputCount;
    }
    
    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, JEIUtils.getMultiInputs(recipe));
        ingredients.setOutputLists(VanillaTypes.ITEM, getOutputLists());
    }
    
    private List<List<ItemStack>> getOutputLists() {
        List<List<ItemStack>> output = recipe.getOutput().stream()
                .map(Collections::singletonList)
                .collect(Collectors.toList());
        GtUtil.fillEmptyList(output, FILLER, this.outputCount);
        
        IRecipeIngredient fluid = recipe.getInput().get(1);
        int count = fluid.getCount();
        int amount = count * Fluid.BUCKET_VOLUME;
        List<ItemStack> containers = fluid.getMatchingInputs().stream()
                // NOTE: setting the count to the ingredient's count will work as long as the item's capacity equals Fluid.BUCKET_VOLUME
                .map(stack -> StackUtil.copyWithSize(FluidUtil.tryEmptyContainer(stack, GtUtil.VOID_TANK, amount, null, true).result, count))
                .collect(Collectors.toList());
        output.add(containers);
        
        return output;
    }
    
    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        JEIUtils.drawInfo(minecraft, recipe, true);
    }
}
