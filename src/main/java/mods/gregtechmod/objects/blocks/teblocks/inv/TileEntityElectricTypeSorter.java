package mods.gregtechmod.objects.blocks.teblocks.inv;

import com.google.common.base.CaseFormat;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GuiElectricTypeSorter;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricTypeSorter;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class TileEntityElectricTypeSorter extends TileEntityElectricSorterBase {
    @NBTPersistent
    private ItemType type = ItemType.ORE;

    public void nextType() {
        switchType(1);
    }

    public void previousType() {
        switchType(-1);
    }

    private void switchType(int step) {
        int index = (ItemType.VALUES.length + this.type.ordinal() + step) % ItemType.VALUES.length;
        this.type = ItemType.VALUES[index];
    }

    public ItemType getType() {
        return this.type;
    }

    @Override
    protected boolean applyFilter(ItemStack stack) {
        return this.type.filter.test(stack);
    }

    @Override
    public ContainerElectricTypeSorter getGuiContainer(EntityPlayer player) {
        return new ContainerElectricTypeSorter(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricTypeSorter(getGuiContainer(player));
    }

    public enum ItemType {
        ORE,
        GEM,
        NUGGET,
        DUST_SMALL,
        DUST,
        INGOT,
        BLOCK,
        TREE_LEAVES,
        TREE_SAPLING,
        LOG,
        PLANK,
        FOOD(stack -> stack.getItem() instanceof ItemFood),
        GRINDER(stack -> GtRecipes.industrialGrinder.hasRecipeForPrimaryInput(stack)),
        BEE_COMB;

        public static final ItemType[] VALUES = values();
        public final Predicate<ItemStack> filter;

        ItemType() {
            String prefix = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
            this.filter = stack -> OreDictUnificator.isItemInstanceOf(stack, prefix, true);
        }

        ItemType(Predicate<ItemStack> filter) {
            this.filter = filter;
        }
    }
}
