package mods.gregtechmod.recipe.ingredient;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import one.util.streamex.StreamEx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MultiOreIngredient extends Ingredient {
    private final List<String> ores;
    private final ItemStack filter;
    private final List<List<ItemStack>> matchingStacks = new ArrayList<>();
    private IntList itemIds = null;
    private ItemStack[] array = null;
    private int lastSizeA = -1, lastSizeL = -1;

    public MultiOreIngredient(List<String> ores) {
        this(ores, ItemStack.EMPTY);
    }

    public MultiOreIngredient(List<String> ores, ItemStack filter) {
        super(0);
        this.ores = Collections.unmodifiableList(ores);
        this.filter = filter;
        ores.stream()
            .map(OreDictionary::getOres)
            .forEach(matchingStacks::add);
    }

    public List<String> getOres() {
        return this.ores;
    }

    @Override
    @Nonnull
    public ItemStack[] getMatchingStacks() {
        if (this.array == null || this.lastSizeA != this.matchingStacks.size()) {
            NonNullList<ItemStack> lst = NonNullList.create();
            matchingStacksFiltered()
                .forEach(itemstack -> {
                    if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE)
                        itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
                    else lst.add(itemstack);
                });
            this.array = lst.toArray(new ItemStack[0]);
            this.lastSizeA = this.matchingStacks.size();
        }
        return this.array;
    }

    @Override
    @Nonnull
    public IntList getValidItemStacksPacked() {
        if (this.itemIds == null || this.lastSizeL != matchingStacks.size()) {
            this.itemIds = new IntArrayList(this.matchingStacks.size());

            matchingStacksFiltered()
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

    private StreamEx<ItemStack> matchingStacksFiltered() {
        return StreamEx.of(this.matchingStacks)
            .flatMap(Collection::stream)
            .remove(stack -> GtUtil.stackEquals(stack, this.filter));
    }

    @Override
    public boolean apply(@Nullable ItemStack input) {
        return input != null && matchingStacksFiltered()
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("ores", ores)
            .add("filter", filter)
            .toString();
    }
}
