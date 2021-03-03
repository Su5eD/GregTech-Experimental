package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCrafting extends ItemBase {
    private final int craftingDamage;

    public ItemCrafting(String name, @Nullable String description, int durability, int craftingDamage) {
        super(name, description, durability);
        setMaxStackSize(1);
        setNoRepair();
        this.craftingDamage = craftingDamage;
    }

    public ItemStack getEmptyItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasContainerItem() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        stack = stack.copy();
        if (stack.attemptDamageItem(this.craftingDamage, GtUtil.RANDOM, null)) return this.getEmptyItem();
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getMaxDamage() > 0) tooltip.add((stack.getMaxDamage() - stack.getItemDamage() + 1) + " / " + (stack.getMaxDamage() + 1));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
