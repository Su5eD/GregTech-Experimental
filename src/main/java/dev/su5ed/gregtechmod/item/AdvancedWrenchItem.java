package dev.su5ed.gregtechmod.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.ElectricCraftingTool;
import ic2.api.item.IElectricItem;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AdvancedWrenchItem extends WrenchItem implements IElectricItem, ElectricCraftingTool {

    public AdvancedWrenchItem() {
        super(new ToolItemProperties<>(), 1000, 3000);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return HashMultimap.create();
    }

    @Override
    protected boolean canUseWrench(ItemStack stack, int amount) {
        return ModHandler.canUseEnergy(stack, amount);
    }

    @Override
    protected void useWrench(ItemStack stack, int amount, Player player, InteractionHand hand) {
        ModHandler.useEnergy(stack, amount, player);
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack stack) {
        return 128000;
    }

    @Override
    public int getTier(ItemStack stack) {
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack stack) {
        return 128;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ModHandler.getEnergyCharge(stack) < 128000;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) Math.round(ModHandler.getChargeLevel(stack) * 13.0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb((float) (ModHandler.getChargeLevel(stack) / 3.0), 1, 1);
    }

    @Override
    public boolean canUseInCrafting(ItemStack stack) {
        return ModHandler.canUseEnergy(stack, 8000);
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ModHandler.useEnergy(stack, 8000, null);
        return stack.copy();
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (allowdedIn(category)) {
            items.addAll(ModHandler.getChargedVariants(this));
        }
    }
}
