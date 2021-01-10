package mods.gregtechmod.recipe.ingredient;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class GtOreIngredient extends Ingredient {
    private final List<String> ores;
    private final List<ItemStack> matchingStacks;
    private IntList itemIds = null;
    private ItemStack[] array = null;
    private int lastSizeA = -1, lastSizeL = -1;

    public GtOreIngredient(List<String> ores) {
        super(0);
        this.ores = ores;
        matchingStacks = ores.stream()
                .flatMap(name -> OreDictionary.getOres(name).stream())
                .collect(Collectors.toList());
    }

    public List<String> getOres() {
        return this.ores;
    }

    @Override
    @Nonnull
    public ItemStack[] getMatchingStacks() {
        if (array == null || this.lastSizeA != matchingStacks.size()) {
            NonNullList<ItemStack> lst = NonNullList.create();
            for (ItemStack itemstack : this.matchingStacks) {
                if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE) itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
                else lst.add(itemstack);
            }
            this.array = lst.toArray(new ItemStack[lst.size()]);
            this.lastSizeA = matchingStacks.size();
        }
        return this.array;
    }


    @Override
    @Nonnull
    public IntList getValidItemStacksPacked() {
        if (this.itemIds == null || this.lastSizeL != matchingStacks.size()) {
            this.itemIds = new IntArrayList(this.matchingStacks.size());

            for (ItemStack itemstack : this.matchingStacks) {
                if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                    NonNullList<ItemStack> lst = NonNullList.create();
                    itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
                    for (ItemStack item : lst) this.itemIds.add(RecipeItemHelper.pack(item));
                }
                else {
                    this.itemIds.add(RecipeItemHelper.pack(itemstack));
                }
            }

            this.itemIds.sort(IntComparators.NATURAL_COMPARATOR);
            this.lastSizeL = matchingStacks.size();
        }

        return this.itemIds;
    }


    @Override
    public boolean apply(@Nullable ItemStack input) {
        if (input == null) return false;

        for (ItemStack target : this.matchingStacks) {
            if (OreDictionary.itemMatches(target, input, false)) return true;
        }

        return false;
    }

    @Override
    protected void invalidate() {
        this.itemIds = null;
        this.array = null;
    }

    @Override
    public boolean isSimple() {
        return true;
    }
}
