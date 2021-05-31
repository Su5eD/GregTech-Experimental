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
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineSingle;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiVacuumFreezer;
import mods.gregtechmod.recipe.RecipeVacuumFreezer;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CategoryVacuumFreezer implements IRecipeCategory<WrapperBasicMachineSingle> {
    public static final String UID = Reference.MODID + ".vacuum_freezer";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/vacuum_freezer.png");
    
    private final IDrawable background;
    private final IDrawable gauge;
    
    public CategoryVacuumFreezer(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 33, 24, 70, 18)
                        .addPadding(20, 51, 52, 52)
                        .build();
        
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.FREEZING);
    }
    
    public void init(IModRegistry registry) {
        registry.handleRecipes(RecipeVacuumFreezer.class, recipe -> new WrapperBasicMachineSingle(recipe, -10), CategoryVacuumFreezer.UID);
    
        registry.addRecipes(RecipeWrapperFactory.getBasicMachineSingleRecipes(GtRecipes.vacuumFreezer), CategoryVacuumFreezer.UID);
    
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("vacuum_freezer"), CategoryVacuumFreezer.UID);
    
        registry.addRecipeClickArea(GuiVacuumFreezer.class, 58, 28, 20, 11, CategoryVacuumFreezer.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock.vacuum_freezer");
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
    public void setRecipe(IRecipeLayout recipeLayout, WrapperBasicMachineSingle recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        guiItemStacks.init(0, true, 52, 20);
        guiItemStacks.init(1, false, 104, 20);
        
        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 77, 24);
    }
}
