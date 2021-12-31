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

import java.util.List;
import java.util.function.Predicate;

public class TileEntityElectricTypeSorter extends TileEntityElectricSorterBase {
    @NBTPersistent
    private ItemType type = ItemType.ORE;
    
    public void nextType() {
        int index = (this.type.ordinal() + 1) % ItemType.VALUES.length;
        this.type = ItemType.VALUES[index];
    }
    
    public void previousType() {
        int index = (this.type.ordinal() - 1) % ItemType.VALUES.length;
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
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("type");
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
