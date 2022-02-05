package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.util.Reference;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class GregTechTab extends CreativeModeTab {
    public static final CreativeModeTab INSTANCE = new GregTechTab();

    private GregTechTab() {
        super(Reference.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.APPLE);
    }
}
