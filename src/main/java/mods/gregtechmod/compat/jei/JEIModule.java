package mods.gregtechmod.compat.jei;

import ic2.core.profile.Version;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.jei.category.CategoryBasicMachine;
import mods.gregtechmod.compat.jei.category.CategoryCentrifuge;
import mods.gregtechmod.gui.*;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.recipe.RecipeSimple;
import mods.gregtechmod.util.IObjectHolder;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JEIPlugin
public class JEIModule implements IModPlugin {
    public static final List<ItemStack> HIDDEN_ITEMS = new ArrayList<>();
    public static IIngredientRegistry itemRegistry;
    public static IIngredientBlacklist ingredientBlacklist;

    private CategoryBasicMachine categoryWiremill;

    @Override
    public void register(IModRegistry registry) {
        itemRegistry = registry.getIngredientRegistry();
        ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();

        CategoryCentrifuge.init(registry);
        categoryWiremill.init(registry);

        initBasicMachine(registry, GuiAutoMacerator.class, "macerator");
        initBasicMachine(registry, GuiAutoExtractor.class, "extractor");
        initBasicMachine(registry, GuiAutoCompressor.class, "compressor");
        initBasicMachine(registry, GuiAutoElectricFurnace.class, "auto_electric_furnace", "minecraft.smelting");
    }

    private void initBasicMachine(IModRegistry registry, Class<? extends GuiBasicMachine<?>> guiClass, String name) {
        initBasicMachine(registry, guiClass, "auto_"+name, name);
    }

    private void initBasicMachine(IModRegistry registry, Class<? extends GuiBasicMachine<?>> guiClass, String name, String categoryName) {
        registry.addRecipeClickArea(guiClass, 78, 24, 18, 18, categoryName);
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity(name), categoryName);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new CategoryCentrifuge(guiHelper),
                categoryWiremill = new CategoryBasicMachine("wiremill", RecipeSimple.class, GuiWiremill.class, GtRecipes.wiremill, guiHelper)
        );
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        hideEnum(BlockItems.Plate.values());
        hideEnum(BlockItems.Rod.values());

        if (!Version.shouldEnable(ItemCellClassic.class)) {
            BlockItems.classicCells.values()
                    .stream()
                    .map(ItemStack::new)
                    .forEach(HIDDEN_ITEMS::add);
        }

        if (!ModHandler.buildcraftLib) {
            HIDDEN_ITEMS.add(new ItemStack(BlockItems.Upgrade.PNEUMATIC_GENERATOR.getInstance()));
            HIDDEN_ITEMS.add(new ItemStack(BlockItems.Upgrade.RS_ENERGY_CELL.getInstance()));
        }

        HIDDEN_ITEMS.forEach(ingredientBlacklist::addIngredientToBlacklist);
    }

    private void hideEnum(IObjectHolder[] values) {
        Arrays.stream(values)
                .filter(val -> !ProfileDelegate.shouldEnable(val))
                .map(IObjectHolder::getInstance)
                .map(ItemStack::new)
                .forEach(HIDDEN_ITEMS::add);
    }
}