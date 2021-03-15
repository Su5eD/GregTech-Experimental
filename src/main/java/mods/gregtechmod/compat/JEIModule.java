package mods.gregtechmod.compat;

import ic2.core.IC2;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.gregtechmod.objects.BlockItems;
import net.minecraft.item.ItemStack;

import java.util.stream.Collectors;

@JEIPlugin
public class JEIModule implements IModPlugin {
    public static IIngredientRegistry itemRegistry;
    public static IIngredientBlacklist ingredientBlacklist;

    @Override
    public void register(IModRegistry registry) {
        itemRegistry = registry.getIngredientRegistry();
        ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        if (!IC2.version.isClassic()) {
            itemRegistry.removeIngredientsAtRuntime(VanillaTypes.ITEM, BlockItems.CLASSIC_CELLS.values()
                    .stream()
                    .map(ItemStack::new)
                    .collect(Collectors.toList()));
        }
    }
}