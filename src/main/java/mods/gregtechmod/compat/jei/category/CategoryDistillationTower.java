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
import mods.gregtechmod.compat.jei.factory.ElectrolyzerRecipeFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperCellular;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiDistillationTower;
import mods.gregtechmod.recipe.RecipeDistillation;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CategoryDistillationTower implements IRecipeCategory<WrapperCellular> {
    public static final String UID = Reference.MODID + ".distillation_tower";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/distillation_tower.png");
    
    private final IDrawable background;
    private final IDrawable gauge;
    
    public CategoryDistillationTower(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 61, 4, 54, 72)
                .addPadding(0, 17, 65, 55)
                .build();
                
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.DISTILLING);
    }
    
    public void init(IModRegistry registry) {
        registry.handleRecipes(RecipeDistillation.class, recipe -> new WrapperCellular(recipe, true), CategoryDistillationTower.UID);
    
        registry.addRecipes(ElectrolyzerRecipeFactory.INSTANCE.getCellularRecipes(GtRecipes.distillation, true), CategoryDistillationTower.UID);
    
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("distillation_tower"), CategoryDistillationTower.UID);
    
        registry.addRecipeClickArea(GuiDistillationTower.class, 80, 4, 16, 72, CategoryDistillationTower.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock.distillation_tower");
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
    public void setRecipe(IRecipeLayout recipeLayout, WrapperCellular recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        guiItemStacks.init(1, true, 65, 36);
        guiItemStacks.init(0, true, 65, 54);
        guiItemStacks.init(2, false, 101, 0);
        guiItemStacks.init(3, false, 101, 18);
        guiItemStacks.init(4, false, 101, 36);
        guiItemStacks.init(5, false, 101, 54);
        
        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 84, 0);
    }
}
