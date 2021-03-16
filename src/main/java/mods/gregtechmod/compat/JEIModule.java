package mods.gregtechmod.compat;

import ic2.core.profile.Version;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.IObjectHolder;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JEIPlugin
public class JEIModule implements IModPlugin {
    public static final List<ItemStack> HIDDEN_ITEMS = new ArrayList<>();
    public static IIngredientRegistry itemRegistry;
    public static IIngredientBlacklist ingredientBlacklist;

    @Override
    public void register(IModRegistry registry) {
        itemRegistry = registry.getIngredientRegistry();
        ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        hideEnum(BlockItems.Plate.values());
        hideEnum(BlockItems.Rod.values());

        if (!Version.shouldEnable(ItemCellClassic.class)) {
            BlockItems.CLASSIC_CELLS.values()
                    .stream()
                    .map(ItemStack::new)
                    .forEach(HIDDEN_ITEMS::add);
        }

        HIDDEN_ITEMS.forEach(ingredientBlacklist::addIngredientToBlacklist);
    }

    private void hideEnum(IObjectHolder[] values) {
        Arrays.stream(values)
                .filter(val -> !GtUtil.shouldEnable(val))
                .map(IObjectHolder::getInstance)
                .map(ItemStack::new)
                .forEach(HIDDEN_ITEMS::add);
    }
}