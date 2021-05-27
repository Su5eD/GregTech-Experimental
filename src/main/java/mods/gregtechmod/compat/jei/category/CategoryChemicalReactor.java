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
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperMultiInput;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiChemicalReactor;
import mods.gregtechmod.recipe.RecipeChemical;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CategoryChemicalReactor implements IRecipeCategory<WrapperMultiInput<?>> {
    public static final String UID = Reference.MODID + ".chemical_reactor";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/chemical_reactor.png");
    
    private final IDrawable background;
    private final IDrawable gauge;
    
    public CategoryChemicalReactor(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 69, 15, 37, 47)
                        .addPadding(10, 32, 69, 69)
                        .build();
        
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_DOWN);
    }
    
    public void init(IModRegistry registry) {
        registry.handleRecipes(RecipeChemical.class, WrapperMultiInput::new, CategoryChemicalReactor.UID);
    
        registry.addRecipes(RecipeWrapperFactory.getMultiRecipes(GtRecipes.chemical, WrapperMultiInput::new), CategoryChemicalReactor.UID);
    
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("chemical_reactor"), CategoryChemicalReactor.UID);
    
        registry.addRecipeClickArea(GuiChemicalReactor.class, 73, 34, 30, 10, CategoryChemicalReactor.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock.chemical_reactor");
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
    public void setRecipe(IRecipeLayout recipeLayout, WrapperMultiInput<?> recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        guiItemStacks.init(0, true, 69, 10);
        guiItemStacks.init(1, true, 89, 10);
        guiItemStacks.init(2, false, 79, 40);
        
        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 73, 29);
        gauge.draw(minecraft, 83, 29);
        gauge.draw(minecraft, 93, 29);
    }
}
