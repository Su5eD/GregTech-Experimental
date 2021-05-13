package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperChemicalReactor;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiChemicalReactor;
import mods.gregtechmod.recipe.RecipeChemical;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class CategoryChemicalReactor implements IRecipeCategory<WrapperChemicalReactor> {
    public static final String UID = Reference.MODID+".chemical_reactor";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/chemical_reactor.png");
    
    private final IDrawable background;
    private final IDrawable gauge;
    
    public CategoryChemicalReactor(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 69, 15, 37, 47)
                        .addPadding(10, 32, 69, 69)
                        .build();
        
        IDrawableStatic gaugeDownStatic = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 0, 0, 10, 10);
        gauge = guiHelper.createAnimatedDrawable(gaugeDownStatic, 200, IDrawableAnimated.StartDirection.TOP, false);
    }
    
    public static void init(IModRegistry registry) {
        registry.handleRecipes(RecipeChemical.class, WrapperChemicalReactor::new, CategoryChemicalReactor.UID);
    
        registry.addRecipes(RecipeWrapperFactory.getMultiRecipes(GtRecipes.chemical, WrapperChemicalReactor::new), CategoryChemicalReactor.UID);
    
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
    public void setRecipe(IRecipeLayout recipeLayout, WrapperChemicalReactor recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        guiItemStacks.init(0, true, 69, 10);
        guiItemStacks.init(1, true, 89, 10);
        guiItemStacks.init(2, false, 79, 40);
        
        guiItemStacks.set(ingredients);
    }
}
