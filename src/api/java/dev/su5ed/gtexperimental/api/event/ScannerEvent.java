package dev.su5ed.gtexperimental.api.event;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Collects additional information for the scanner.
 * This event is <b>cancelable</b>.
 */
@Cancelable
public class ScannerEvent extends LevelEvent {
    public final ItemStack stack;
    public final UseOnContext context;

    private double euCost = 0;

    public ScannerEvent(ItemStack stack, UseOnContext context) {
        super(context.getLevel());

        this.stack = stack;
        this.context = context;
    }

    public double getEUCost() {
        return this.euCost;
    }
    
    public void increaseEUCost(double cost) {
        this.euCost += cost;
    }
}
