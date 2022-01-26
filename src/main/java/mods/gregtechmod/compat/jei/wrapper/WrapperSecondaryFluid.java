package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import java.util.Collections;
import java.util.List;

public class WrapperSecondaryFluid<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends WrapperMultiInput<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> {
    private static final List<ItemStack> FILLER = Collections.singletonList(ItemStack.EMPTY);
    
    private final int outputCount;

    public WrapperSecondaryFluid(R recipe, int outputCount) {
        super(recipe);
        this.outputCount = outputCount;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, JEIUtils.getMultiInputs(this.recipe));
        ingredients.setOutputLists(VanillaTypes.ITEM, getOutputLists());
    }

    private List<List<ItemStack>> getOutputLists() {
        List<List<ItemStack>> output = StreamEx.of(this.recipe.getOutput())
            .map(Collections::singletonList)
            .toList();
        JavaUtil.fillEmptyList(output, FILLER, this.outputCount);

        IRecipeIngredient fluid = this.recipe.getInput().get(1);
        int count = fluid.getCount();
        int amount = count * Fluid.BUCKET_VOLUME;
        List<ItemStack> containers = StreamEx.of(fluid.getMatchingInputs())
            // NOTE: setting the count to the ingredient's count will work as long as the item's capacity equals Fluid.BUCKET_VOLUME
            .map(stack -> ItemHandlerHelper.copyStackWithSize(FluidUtil.tryEmptyContainer(stack, GtUtil.VOID_TANK, amount, null, true).result, count))
            .toList();
        output.add(containers);

        return output;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        JEIUtils.drawInfo(minecraft, this.recipe, true);
    }
}
