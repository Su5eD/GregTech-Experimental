package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.factory.ElectrolyzerRecipeFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperCellular;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiIndustrialElectrolyzer;
import mods.gregtechmod.recipe.RecipeElectrolyzer;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CategoryElectrolyzer implements IRecipeCategory<WrapperCellular> {
    public static final String UID = Reference.MODID+".industrial_electrolyzer";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/industrial_electrolyzer.png");
    private final IDrawable background;
    private final IDrawable gauge;
    private final IDrawable tank;

    public CategoryElectrolyzer(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 48, 9, 79, 74)
                .addPadding(0, 15, 48, 48)
                .build();

        IDrawableStatic gaugeUpStatic = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 20, 0, 10, 10);
        gauge = guiHelper.createAnimatedDrawable(gaugeUpStatic, 200, IDrawableAnimated.StartDirection.BOTTOM, false);

        tank = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 40, 0, 18, 18);
    }

    public static void init(IModRegistry registry) {
        registry.handleRecipes(RecipeElectrolyzer.class, WrapperCellular::new, CategoryElectrolyzer.UID);

        registry.addRecipes(ElectrolyzerRecipeFactory.INSTANCE.getCellularRecipes(GtRecipes.industrialElectrolyzer, true), CategoryElectrolyzer.UID);

        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("industrial_electrolyzer"), CategoryElectrolyzer.UID);

        registry.addRecipeClickArea(GuiIndustrialElectrolyzer.class, 73, 30, 30, 10, CategoryElectrolyzer.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock.industrial_electrolyzer");
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
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 49, 36);
        guiItemStacks.init(1, true, 79, 36);
        guiItemStacks.init(2, false, 49, 6);
        guiItemStacks.init(3, false, 69, 6);
        guiItemStacks.init(4, false, 89, 6);
        guiItemStacks.init(5, false, 109, 6);

        guiFluidStacks.init(6, true, 110, 37, 16, 16, 1, false, null);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 73, 25);
        gauge.draw(minecraft, 83, 25);
        gauge.draw(minecraft, 93, 25);

        tank.draw(minecraft, 109, 36);
    }
}
