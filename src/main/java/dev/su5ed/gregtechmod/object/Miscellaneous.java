package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.SolderingMetalItem;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public enum Miscellaneous implements ItemProvider {
    SOLDERING_LEAD(() -> new SolderingMetalItem(10)),
    SOLDERING_TIN(() -> new SolderingMetalItem(50));
    
    private final Lazy<Item> instance;

    Miscellaneous(Supplier<Item> supplier) {
        this.instance = Lazy.of(() -> supplier.get().setRegistryName(getName()));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
