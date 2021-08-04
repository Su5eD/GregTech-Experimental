package mods.gregtechmod.recipe.crafting;

import mods.gregtechmod.core.GregTechMod;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdvancementRecipeFixer {
    public static final Map<String, IRecipe> DUMMY_RECIPES = new HashMap<>();
    public static final Set<IRecipe> REPLACED_RECIPES = new HashSet<>();
    
    public static void fixAdvancementRecipes() {
        GregTechMod.logger.info("Fixing Advancements");
        
        REPLACED_RECIPES
                .forEach(recipe -> {
                    ResourceLocation output = recipe.getRecipeOutput().getItem().getRegistryName();
                    if (output.getNamespace().equals("minecraft")) {
                        DUMMY_RECIPES.put(output.getPath(), recipe);
                    }
                });
        
        DUMMY_RECIPES
                .forEach((name, recipe) -> {
                    ResourceLocation location = new ResourceLocation(name);
                    IRecipe dummy = DummyRecipe.from(recipe)
                            .setRegistryName(location);
                    ForgeRegistries.RECIPES.register(dummy);
                });
    }
}
