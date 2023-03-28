package dev.su5ed.gtexperimental.compat.jei;

import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.screen.SimpleMachineScreen;
import dev.su5ed.gtexperimental.util.ItemProvider;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

@JeiPlugin
public class JEIModule implements IModPlugin {
    public static final ResourceLocation NAME = location("jei");
    private static final List<SubModule> SUBMODULES = new ArrayList<>();

    private static final List<MachineCategory> MACHINE_CATEGORIES = new ArrayList<>();
    static final String RECIPE_MACERATOR = "macerator";
    static final String RECIPE_EXTRACTOR = "extractor";

    static {
        if (ModHandler.ic2Loaded) {
            SUBMODULES.add(new JEISubmoduleIC2());
        }

        addBasicMachineCategory(GTBlockEntity.AUTO_MACERATOR, SimpleMachineScreen.AutomaticMaceratorScreen.class, RECIPE_MACERATOR);
        addBasicMachineCategory(GTBlockEntity.AUTO_EXTRACTOR, SimpleMachineScreen.AutomaticExtractorScreen.class, RECIPE_EXTRACTOR);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return NAME;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration register) {
        for (MachineCategory category : MACHINE_CATEGORIES) {
            register.addRecipeClickArea(category.containerScreenClass, category.xPos, category.yPos, category.width, category.height, category.recipeTypes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration register) {
        for (MachineCategory category : MACHINE_CATEGORIES) {
            register.addRecipeCatalyst(category.blockEntityProvider.getItemStack(), category.recipeTypes);
        }
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        Item[] electricItems = StreamEx.of(Tool.values())
            .map(ItemProvider::getItem)
            .filter(ModHandler::isEnergyItem)
            .toArray(Item[]::new);
        registration.useNbtForSubtypes(electricItems);
    }

    private static void addBasicMachineCategory(ItemProvider itemProvider, Class<? extends AbstractContainerScreen<?>> screenClass, String recipeName) {
        RecipeType<?>[] recipeTypes = StreamEx.of(SUBMODULES)
            .map(subModule -> subModule.getRecipeType(recipeName))
            .toArray(RecipeType<?>[]::new);
        MACHINE_CATEGORIES.add(new MachineCategory(itemProvider, screenClass, 78, 24, 18, 18, recipeTypes));
    }

    private record MachineCategory(ItemProvider blockEntityProvider, Class<? extends AbstractContainerScreen<?>> containerScreenClass,
                                   int xPos, int yPos, int width, int height, RecipeType<?>... recipeTypes) {}

    interface SubModule {
        RecipeType<?> getRecipeType(String name);
    }
}
