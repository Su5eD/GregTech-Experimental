package mods.gregtechmod.recipe.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Fixes advancements breaking because of replacement of certain vanilla recipes<br></br>
 * 
 * Code taken from Harvestcraft <a href="https://github.com/MatrexsVigil/harvestcraft/pull/242">PR 242</a>
 */
public class DummyRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private final ItemStack output;

    public DummyRecipe(ItemStack output) {
        this.output = output;
    }

    public static IRecipe from(IRecipe other) {
        return new DummyRecipe(other.getRecipeOutput());
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }
}
