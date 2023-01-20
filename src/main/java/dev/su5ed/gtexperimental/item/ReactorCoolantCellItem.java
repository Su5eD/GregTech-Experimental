package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.util.GtLocale;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReactorCoolantCellItem extends ResourceItem implements IReactorComponent {

    public ReactorCoolantCellItem(int heatStorage) {
        super(new ExtendedItemProperties<>()
            .durability(heatStorage));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);
        components.add(GtLocale.itemKey("coolant", "stored_heat").toComponent(stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
        if (ModHandler.ic2Loaded && stack.getDamageValue() > 0) {
            components.add(GtLocale.itemKey("coolant", "heat_warning").toComponent().withStyle(ChatFormatting.GRAY));
            components.add(GtLocale.itemKey("coolant", "heat_warning_1").toComponent().withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {}

    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        return false;
    }

    @Override
    public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return true;
    }

    @Override
    public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return stack.getMaxDamage();
    }

    @Override
    public int getCurrentHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return stack.getDamageValue();
    }

    @Override
    public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        int altered;
        int currentHeat = getCurrentHeat(stack, reactor, x, y) + heat;
        int maxHeat = getMaxHeat(stack, reactor, x, y);

        if (currentHeat > maxHeat) {
            reactor.setItemAt(x, y, ItemStack.EMPTY);
            altered = maxHeat - currentHeat + 1;
        }
        else {
            if (currentHeat < 0) {
                altered = currentHeat;
                currentHeat = 0;
            }
            else {
                altered = 0;
            }
            stack.setDamageValue(currentHeat);
        }

        return altered;
    }

    @Override
    public float influenceExplosion(ItemStack stack, IReactor reactor) {
        return 1F + stack.getMaxDamage() / 30000F;
    }

    @Override
    public boolean canBePlacedIn(ItemStack stack, IReactor reactor) {
        return true;
    }
}
