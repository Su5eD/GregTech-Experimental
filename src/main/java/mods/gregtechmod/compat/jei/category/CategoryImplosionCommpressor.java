package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperMultiInput;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiImplosionCompressor;
import mods.gregtechmod.recipe.RecipeImplosion;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CategoryImplosionCommpressor implements IRecipeCategory<WrapperMultiInput<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {
    public static final String UID = Reference.MODID + ".implosion_compressor";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/implosion_compressor.png");
    
    private final IDrawable background;
    private final IDrawable gauge;
    
    public CategoryImplosionCommpressor(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 33, 15, 123, 46)
                        .addPadding(10, 33, 26, 26)
                        .build();
        
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.IMPLODING);
    }
    
    public void init(IModRegistry registry) {
        registry.handleRecipes(RecipeImplosion.class, WrapperMultiInput::new, CategoryImplosionCommpressor.UID);
    
        registry.addRecipes(RecipeWrapperFactory.getMultiRecipes(GtRecipes.implosion, WrapperMultiInput::new), CategoryImplosionCommpressor.UID);
    
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("implosion_compressor"), CategoryImplosionCommpressor.UID);
    
        registry.addRecipeClickArea(GuiImplosionCompressor.class, 58, 28, 20, 11, CategoryImplosionCommpressor.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock.implosion_compressor");
    }

    @Override
    public String getModName() {
        return Reference.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, WrapperMultiInput<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        guiItemStacks.init(0, true, 26, 10);
        guiItemStacks.init(1, true, 26, 28);
        guiItemStacks.init(2, false, 78, 19);
        guiItemStacks.init(3, false, 96, 19);
        
        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 51, 23);
    }
}
