package mods.gregtechmod.compat.jei;

import ic2.core.profile.Version;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.jei.category.CategoryBasicMachineMulti;
import mods.gregtechmod.compat.jei.category.CategoryBasicMachineSingle;
import mods.gregtechmod.compat.jei.category.CategoryCentrifuge;
import mods.gregtechmod.gui.*;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.recipe.RecipeAlloySmelter;
import mods.gregtechmod.recipe.RecipeCanner;
import mods.gregtechmod.recipe.RecipeDualInput;
import mods.gregtechmod.recipe.RecipeSimple;
import mods.gregtechmod.recipe.util.DamagedOreIngredientFixer;
import mods.gregtechmod.util.IObjectHolder;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JEIPlugin
public class JEIModule implements IModPlugin {
    public static final List<ItemStack> HIDDEN_ITEMS = new ArrayList<>();
    public static IIngredientRegistry itemRegistry;
    public static IIngredientBlacklist ingredientBlacklist;

    private CategoryBasicMachineSingle<? extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>> categoryWiremill;
    private CategoryBasicMachineMulti<RecipeAlloySmelter> categoryAlloySmelter;
    private CategoryBasicMachineMulti<RecipeCanner> categoryAutoCanner;
    private CategoryBasicMachineSingle<? extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>> categoryBender;
    private CategoryBasicMachineMulti<? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> categoryAssembler;

    @Override
    public void register(IModRegistry registry) {
        itemRegistry = registry.getIngredientRegistry();
        ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();

        CategoryCentrifuge.init(registry);
        categoryWiremill.init(registry);
        categoryAlloySmelter.init(registry);
        categoryAutoCanner.init(registry);
        categoryBender.init(registry);
        categoryAssembler.init(registry);

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
                categoryWiremill = new CategoryBasicMachineSingle<>("wiremill", RecipeSimple.class, GuiWiremill.class, true, guiHelper, GtRecipes.wiremill),
                categoryAlloySmelter = new CategoryBasicMachineMulti<>("alloy_smelter", RecipeAlloySmelter.class, GuiAlloySmelter.class, GtRecipes.alloySmelter, true, false, guiHelper),
                categoryAutoCanner = new CategoryBasicMachineMulti<>("auto_canner", RecipeCanner.class, GuiAutoCanner.class, GtRecipes.canner, true, guiHelper),
                categoryBender = new CategoryBasicMachineSingle<>("bender", RecipeSimple.class, GuiBender.class, true, guiHelper, GtRecipes.bender),
                categoryAssembler = new CategoryBasicMachineMulti<>("assembler", RecipeDualInput.class, GuiAssembler.class, GtRecipes.assembler, true, false, guiHelper)
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

        IRecipeRegistry registry = jeiRuntime.getRecipeRegistry();
        DamagedOreIngredientFixer.FIXED_RECIPES.stream()
                .map(recipe -> registry.getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING))
                .filter(Objects::nonNull)
                .forEach(recipe -> registry.hideRecipe(recipe, VanillaRecipeCategoryUid.CRAFTING));
    }

    private void hideEnum(IObjectHolder[] values) {
        Arrays.stream(values)
                .filter(val -> !ProfileDelegate.shouldEnable(val))
                .map(IObjectHolder::getInstance)
                .map(ItemStack::new)
                .forEach(HIDDEN_ITEMS::add);
    }
}
