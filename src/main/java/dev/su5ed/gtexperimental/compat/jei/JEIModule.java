package dev.su5ed.gtexperimental.compat.jei;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import dev.su5ed.gtexperimental.screen.RecipeProgressBar;
import dev.su5ed.gtexperimental.screen.SimpleMachineScreen;
import dev.su5ed.gtexperimental.util.ItemProvider;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

@JeiPlugin
public class JEIModule implements IModPlugin {
    public static final ResourceLocation NAME = location("jei");
    private static final List<SubModule> SUBMODULES = new ArrayList<>();

    private static final List<ModMachineCategory> MACHINE_CATEGORIES = new ArrayList<>();
    static final String RECIPE_MACERATOR = "macerator";
    static final String RECIPE_EXTRACTOR = "extractor";
    static final String RECIPE_COMPRESSOR = "compressor";
    private static final RecipeType<SISORecipe<ItemStack, ItemStack>> RECIPE_WIREMILL = createRecipeType(Reference.MODID, "wiremill", SISORecipe.class);
    private static final RecipeType<SISORecipe<ItemStack, ItemStack>> RECIPE_BENDER = createRecipeType(Reference.MODID, "bender", SISORecipe.class);
    private static final RecipeType<MISORecipe<ItemStack, ItemStack>> RECIPE_ALLOY_SMELTER = createRecipeType(Reference.MODID, "alloy_smelter", MISORecipe.class);
    private static final RecipeType<MISORecipe<ItemStack, ItemStack>> RECIPE_ASSEMBLER = createRecipeType(Reference.MODID, "assembler", MISORecipe.class);

    static {
        if (ModHandler.ic2Loaded) {
            SUBMODULES.add(new JEISubmoduleIC2());
        }

        addBasicMachineCategory(GTBlockEntity.AUTO_MACERATOR, SimpleMachineScreen.AutomaticMaceratorScreen.class, RECIPE_MACERATOR);
        addBasicMachineCategory(GTBlockEntity.AUTO_EXTRACTOR, SimpleMachineScreen.AutomaticExtractorScreen.class, RECIPE_EXTRACTOR);
        addBasicMachineCategory(GTBlockEntity.AUTO_COMPRESSOR, SimpleMachineScreen.AutomaticCompressorScreen.class, RECIPE_COMPRESSOR);
        addBasicMachineCategory(GTBlockEntity.AUTO_ELECTRIC_FURNACE, SimpleMachineScreen.AutomaticElectricFurnaceScreen.class, RecipeTypes.SMELTING);
        addBasicMachineCategory(GTBlockEntity.WIREMILL, SimpleMachineScreen.WiremillScreen.class, RECIPE_WIREMILL);
        addBasicMachineCategory(GTBlockEntity.BENDER, SimpleMachineScreen.BenderScreen.class, RECIPE_BENDER);
        addBasicMachineCategory(GTBlockEntity.ALLOY_SMELTER, SimpleMachineScreen.AlloySmelterScreen.class, RECIPE_ALLOY_SMELTER);
        addBasicMachineCategory(GTBlockEntity.ASSEMBLER, SimpleMachineScreen.AssemblerScreen.class, RECIPE_ASSEMBLER);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return NAME;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration register) {
        for (ModMachineCategory category : MACHINE_CATEGORIES) {
            register.addRecipeClickArea(category.containerScreenClass, category.xPos, category.yPos, category.width, category.height, category.recipeTypes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration register) {
        for (ModMachineCategory category : MACHINE_CATEGORIES) {
            register.addRecipeCatalyst(category.blockEntityProvider.getItemStack(), category.recipeTypes);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration register) {
        IGuiHelper guiHelper = register.getJeiHelpers().getGuiHelper();
        register.addRecipeCategories(
            new SimpleMachineRecipeCategory.SISO(GTBlockEntity.WIREMILL, "wiremill", RECIPE_WIREMILL, guiHelper, RecipeProgressBar.EXTRUDING, -5, true),
            new SimpleMachineRecipeCategory.SISO(GTBlockEntity.BENDER, "bender", RECIPE_BENDER, guiHelper, RecipeProgressBar.BENDING, -5, true),
            new SimpleMachineRecipeCategory.MISO(GTBlockEntity.ALLOY_SMELTER, "alloy_smelter", RECIPE_ALLOY_SMELTER, guiHelper, RecipeProgressBar.SMELTING, -5, true),
            new SimpleMachineRecipeCategory.MISO(GTBlockEntity.ASSEMBLER, "assembler", RECIPE_ASSEMBLER, guiHelper, RecipeProgressBar.ASSEMBLING, -5, true)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration register) {
        Level level = Minecraft.getInstance().level;
        register.addRecipes(RECIPE_WIREMILL, ModRecipeManagers.WIREMILL.getRecipes(level));
        register.addRecipes(RECIPE_BENDER, ModRecipeManagers.BENDER.getRecipes(level));
        register.addRecipes(RECIPE_ALLOY_SMELTER, ModRecipeManagers.ALLOY_SMELTER.getRecipes(level));
        register.addRecipes(RECIPE_ASSEMBLER, ModRecipeManagers.ASSEMBLER.getRecipes(level));
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        Item[] electricItems = StreamEx.of(Tool.values())
            .map(ItemProvider::getItem)
            .filter(ModHandler::isEnergyItem)
            .toArray(Item[]::new);
        registration.useNbtForSubtypes(electricItems);
    }

    public static <T> RecipeType<T> createRecipeType(String nameSpace, String path, Class<? super T> recipeClass) {
        ResourceLocation uid = new ResourceLocation(nameSpace, path);
        //noinspection unchecked
        return new RecipeType<>(uid, (Class<T>) recipeClass);
    }

    private static void addBasicMachineCategory(ItemProvider itemProvider, Class<? extends AbstractContainerScreen<?>> screenClass, String recipeName) {
        RecipeType<?>[] recipeTypes = StreamEx.of(SUBMODULES)
            .map(subModule -> subModule.getRecipeType(recipeName))
            .toArray(RecipeType<?>[]::new);
        addBasicMachineCategory(itemProvider, screenClass, recipeTypes);
    }

    private static void addBasicMachineCategory(ItemProvider itemProvider, Class<? extends AbstractContainerScreen<?>> screenClass, RecipeType<?>... recipeTypes) {
        MACHINE_CATEGORIES.add(new ModMachineCategory(itemProvider, screenClass, 78, 24, 18, 18, recipeTypes));
    }

    private record ModMachineCategory(ItemProvider blockEntityProvider, Class<? extends AbstractContainerScreen<?>> containerScreenClass,
                                      int xPos, int yPos, int width, int height, RecipeType<?>... recipeTypes) {}

    interface SubModule {
        RecipeType<?> getRecipeType(String name);
    }
}
