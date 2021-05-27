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
import mods.gregtechmod.compat.jei.wrapper.WrapperIndustrialGrinder;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiIndustrialGrinder;
import mods.gregtechmod.recipe.RecipeGrinder;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CategoryIndustrialGrinder implements IRecipeCategory<WrapperIndustrialGrinder> {
    public static final String UID = Reference.MODID + ".industrial_grinder";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/industrial_grinder.png");
    
    private final IDrawable background;
    private final IDrawable gauge;
    
    public CategoryIndustrialGrinder(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 33, 15, 123, 46)
                        .addPadding(10, 33, 26, 26)
                        .build();
        
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.MACERATING);
    }
    
    public void init(IModRegistry registry) {
        registry.handleRecipes(RecipeGrinder.class, WrapperIndustrialGrinder::new, CategoryIndustrialGrinder.UID);
    
        registry.addRecipes(RecipeWrapperFactory.getMultiRecipes(GtRecipes.industrialGrinder, WrapperIndustrialGrinder::new), CategoryIndustrialGrinder.UID);
    
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("industrial_grinder"), CategoryIndustrialGrinder.UID);
    
        registry.addRecipeClickArea(GuiIndustrialGrinder.class, 58, 28, 20, 11, CategoryIndustrialGrinder.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock.industrial_grinder");
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
    public void setRecipe(IRecipeLayout recipeLayout, WrapperIndustrialGrinder recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        guiItemStacks.init(0, true, 26, 10);
        guiItemStacks.init(1, true, 26, 28);
        guiItemStacks.init(2, false, 78, 19);
        guiItemStacks.init(3, false, 96, 19);
        guiItemStacks.init(4, false, 114, 19);
        guiItemStacks.init(5, false, 132, 19);
        
        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 51, 19);
    }
}
