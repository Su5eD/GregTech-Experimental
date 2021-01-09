package mods.gregtechmod.plugin;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mods.gregtechmod.api.GregTechAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

@JEIPlugin
public class JEIModule implements IModPlugin {
    public static IIngredientRegistry itemRegistry;
    public static IIngredientBlacklist ingredientBlacklist;
    public static IJeiRuntime jeiRuntime;

    @Override
    public void register(IModRegistry registry) {
        itemRegistry = registry.getIngredientRegistry();
        ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime iJeiRuntime) {
        jeiRuntime = iJeiRuntime;
        GregTechAPI.logger.debug("Hiding some items from JEI... look away");
        List<ItemStack> itemsToRemove = Arrays.asList(
                //TODO: Hide Smalldusts?
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ic2", "dust")), 1, 31),
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ic2", "dust")), 1, 32)
        );
        itemsToRemove.forEach(ingredientBlacklist::addIngredientToBlacklist);
    }
}