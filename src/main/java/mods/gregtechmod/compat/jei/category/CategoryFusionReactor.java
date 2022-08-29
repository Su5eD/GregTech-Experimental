package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.wrapper.WrapperFusion;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiFusionReactor;
import mods.gregtechmod.recipe.RecipeFusionFluid;
import mods.gregtechmod.recipe.RecipeFusionSolid;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;

public class CategoryFusionReactor extends CategoryBase<RecipeFusionFluid, WrapperFusion> {
    public static final ResourceLocation BACKGROUND = GtUtil.getGuiTexture("jei/fusion_reactor");
    
    private final IDrawable gauge;

    public CategoryFusionReactor(IGuiHelper guiHelper) {
        super("fusion", RecipeFusionFluid.class, WrapperFusion::new, guiHelper);
        
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.FUSION);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(BACKGROUND, 47, 16, 82, 54)
            .addPadding(5, 43, 47, 47)
            .build();
    }

    @Override
    public void init(IModRegistry registry) {
        registry.handleRecipes(this.recipeClass, this.recipeWrapperFactory, this.uid);
        registry.handleRecipes(RecipeFusionSolid.class, WrapperFusion::new, this.uid);
        registry.addRecipes(getRecipes(), this.uid);
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("fusion_computer"), this.uid);
        registry.addRecipeClickArea(GuiFusionReactor.class, 155, 5, 16, 16, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        super.initSlots(guiItemStacks);
        guiItemStacks.init(2, false, 107, 23);
    }

    @Override
    protected void initFluidsSlots(IGuiFluidStackGroup guiFluidStacks) {
        guiFluidStacks.init(0, true, 48, 6, 16, 16, 1, false, null);
        guiFluidStacks.init(1, true, 48, 42, 16, 16, 1, false, null);
        guiFluidStacks.init(2, false, 108, 24, 16, 16, 1, false, null);
    }

    @Override
    protected Collection<?> getRecipes() {
        return StreamEx.of(GtRecipes.fusionFluid, GtRecipes.fusionSolid)
            .flatMap(manager -> StreamEx.of(manager.getRecipes()))
            .remove(recipe -> StreamEx.of(recipe.getInput())
                .map(IRecipeIngredientFluid::getMatchingFluids)
                .anyMatch(List::isEmpty))
            .map(WrapperFusion::new)
            .toList();
    }

    @Override
    public String getTitle() {
        return I18n.format(GtLocale.buildKey("jei", this.name));
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 70, 23);
    }
}
