package mods.gregtechmod.recipe.ingredient;

import com.google.common.collect.ImmutableList;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiOreIngredient extends Ingredient {
    private final List<String> ores;
    private final List<List<ItemStack>> matchingStacks = new ArrayList<>();
    private IntList itemIds = null;
    private ItemStack[] array = null;
    private int lastSizeA = -1, lastSizeL = -1;

    public MultiOreIngredient(List<String> ores) {
        super(0);
        this.ores = ores;
        ores.stream()
            .map(OreDictionary::getOres)
            .forEach(matchingStacks::add);
    }

    public List<String> getOres() {
        return ImmutableList.copyOf(ores);
    }

    @Override
    @Nonnull
    public ItemStack[] getMatchingStacks() {
        if (array == null || this.lastSizeA != matchingStacks.size()) {
            NonNullList<ItemStack> lst = NonNullList.create();
            this.matchingStacks.stream()
                .flatMap(Collection::stream)
                .forEach(itemstack -> {
                    if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE)
                        itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
                    else lst.add(itemstack);
                });
            this.array = lst.toArray(new ItemStack[0]);
            this.lastSizeA = matchingStacks.size();
        }
        return this.array;
    }


    @Override
    @Nonnull
    public IntList getValidItemStacksPacked() {
        if (this.itemIds == null || this.lastSizeL != matchingStacks.size()) {
            this.itemIds = new IntArrayList(this.matchingStacks.size());

            this.matchingStacks.stream()
                .flatMap(Collection::stream)
                .forEach(stack -> {
                    if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                        NonNullList<ItemStack> lst = NonNullList.create();
                        stack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
                        lst.stream()
                            .map(RecipeItemHelper::pack)
                            .forEach(this.itemIds::add);
                    }
                    else {
                        this.itemIds.add(RecipeItemHelper.pack(stack));
                    }
                });

            this.itemIds.sort(IntComparators.NATURAL_COMPARATOR);
            this.lastSizeL = matchingStacks.size();
        }

        return this.itemIds;
    }


    @Override
    public boolean apply(@Nullable ItemStack input) {
        if (input == null) return false;

        return this.matchingStacks.stream()
            .flatMap(Collection::stream)
            .anyMatch(target -> OreDictionary.itemMatches(target, input, false));
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
