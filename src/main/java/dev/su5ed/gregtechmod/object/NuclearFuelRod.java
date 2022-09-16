package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.NuclearFuelRodItem;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum NuclearFuelRod implements ItemProvider {
    THORIUM(1, 25000, 0.25F, 1, 0.25F),
    DUAL_THORIUM(2, 25000, 0.25F, 1, 0.25F),
    QUAD_THORIUM(4, 25000, 0.25F, 1, 0.25F),
    PLUTONIUM(1, 20000, 2, 2, 2),
    DUAL_PLUTONIUM(2, 20000, 2, 2, 2),
    QUAD_PLUTONIUM(4, 20000, 2, 2, 2);

    private final Lazy<Item> instance;

    NuclearFuelRod(int cells, int duration, float energy, int radiation, float heat) {
        this.instance = Lazy.of(() -> new NuclearFuelRodItem(cells, duration, energy, radiation, heat));
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "fuel_rod");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
