package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.CentrifugeRecipeFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperCellular;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.recipe.RecipeCentrifuge;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CategoryCentrifuge implements IRecipeCategory<WrapperCellular> {
    public static final String UID = Reference.MODID+".industrial_centrifuge";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/industrial_centrifuge.png");
    private final IDrawable background;
    private final IDrawable gaugeUp;
    private final IDrawable gaugeDown;
    private final IDrawable gaugeRight;
    private final IDrawable gaugeLeft;
    private final IDrawable tank;

    public CategoryCentrifuge(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 48, 4, 79, 79)
                .addPadding(0, 0, 48, 48)
                .build();
        
        gaugeUp = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_UP);
        gaugeDown = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_DOWN);
        gaugeRight = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_RIGHT);
        gaugeLeft = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_LEFT);

        tank = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 40, 0, 18, 18);
    }

    public void init(IModRegistry registry) {
        registry.handleRecipes(RecipeCentrifuge.class, WrapperCellular::new, CategoryCentrifuge.UID);

        registry.addRecipes(CentrifugeRecipeFactory.INSTANCE.getCellularRecipes(GtRecipes.industrialCentrifuge, false), CategoryCentrifuge.UID);

        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("industrial_centrifuge"), CategoryCentrifuge.UID);

        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 98, 38, 10, 10, CategoryCentrifuge.UID);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 83, 23, 10, 10, CategoryCentrifuge.UID);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 68, 38, 10, 10, CategoryCentrifuge.UID);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 83, 53, 10, 10, CategoryCentrifuge.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock.industrial_centrifuge");
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

        guiItemStacks.init(0, true, 49, 0);
        guiItemStacks.init(1, true, 79, 30);
        guiItemStacks.init(2, false, 79, 0);
        guiItemStacks.init(3, false, 109, 30);
        guiItemStacks.init(4, false, 79, 60);
        guiItemStacks.init(5, false, 49, 30);

        guiFluidStacks.init(6, true, 110, 60, 16, 16, 1, false, null);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gaugeUp.draw(minecraft, 83, 19);
        gaugeDown.draw(minecraft, 83, 49);
        gaugeRight.draw(minecraft, 98, 34);
        gaugeLeft.draw(minecraft, 68, 34);

        tank.draw(minecraft, 109, 59);
    }
}
