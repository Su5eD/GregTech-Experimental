package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.wrapper.WrapperFuel;
import mods.gregtechmod.gui.GuiFluidGenerator;
import mods.gregtechmod.recipe.fuel.FuelSimple;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.stream.Collectors;

public class CategoryGenerator<R> extends CategoryBase<R, WrapperFuel<?>> {
    private final IFuelManager<? extends IFuel<IRecipeIngredient>, ItemStack> fuelManager;
    protected final Class<? extends GuiFluidGenerator> guiClass;
    
    public CategoryGenerator(String name, Class<R> recipeClass, Class<? extends GuiFluidGenerator> guiClass, IRecipeWrapperFactory<R> recipeWrapperFactory, IFuelManager<? extends IFuel<IRecipeIngredient>, ItemStack> fuelManager, IGuiHelper guiHelper) {
        super(name, recipeClass, recipeWrapperFactory, guiHelper);
        this.fuelManager = fuelManager;
        this.guiClass = guiClass;
    }
    
    public static CategoryGenerator<FuelSimple> createFluidGeneratorCategory(String name, Class<? extends GuiFluidGenerator> guiClass, IFuelManager<? extends IFuel<IRecipeIngredient>, ItemStack> fuelManager, IGuiHelper guiHelper) {
        return new CategoryGenerator<>(name, FuelSimple.class, guiClass, WrapperFuel::new, fuelManager, guiHelper);
    }
    
    @Override
    protected void addRecipeClickArea(IModRegistry registry) {
        registry.addRecipeClickArea(this.guiClass, 81, 35, 14, 15, this.uid);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(new ResourceLocation(Reference.MODID, "textures/gui/jei/fuel.png"), 60, 34, 56, 18)
                .addPadding(20, 20, 60, 60)
                .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return this.fuelManager.getFuels().stream()
                .map(WrapperFuel::new)
                .collect(Collectors.toList());
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 60, 20);
    }

    @Override
    protected void initFluidsSlots(IGuiFluidStackGroup guiFluidStacks) {
        guiFluidStacks.init(0, true, 80, 21);
    }
}
