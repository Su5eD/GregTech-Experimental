package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.ColorSpray;
import dev.su5ed.gregtechmod.object.Component;
import dev.su5ed.gregtechmod.object.Dust;
import dev.su5ed.gregtechmod.object.File;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.Hammer;
import dev.su5ed.gregtechmod.object.Ingot;
import dev.su5ed.gregtechmod.object.JackHammer;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.ModCoverItem;
import dev.su5ed.gregtechmod.object.Nugget;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.object.Plate;
import dev.su5ed.gregtechmod.object.Rod;
import dev.su5ed.gregtechmod.object.Saw;
import dev.su5ed.gregtechmod.object.Smalldust;
import dev.su5ed.gregtechmod.object.Tool;
import dev.su5ed.gregtechmod.object.TurbineRotor;
import dev.su5ed.gregtechmod.object.Upgrade;
import dev.su5ed.gregtechmod.object.Wrench;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import one.util.streamex.StreamEx;

public final class GregTechTab extends CreativeModeTab {
    public static final CreativeModeTab INSTANCE = new GregTechTab();

    private GregTechTab() {
        super(Reference.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.APPLE);
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> items) {
        StreamEx.<ItemProvider>of(ModBlock.values())
            .append(Ore.values())
            .append(GTBlockEntity.values())
            .append(Ingot.values())
            .append(Nugget.values())
            .append(Rod.values())
            .append(Dust.values())
            .append(Smalldust.values())
            .append(Plate.values())
            .append(TurbineRotor.values())
            .append(Component.values())
            .append(ModCoverItem.values())
            .append(Tool.values())
            .append(Wrench.values())
            .append(JackHammer.values())
            .append(Hammer.values())
            .append(Saw.values())
            .append(File.values())
            .append(Upgrade.values())
            .append(Miscellaneous.values())
            .append(ColorSpray.values())
            .map(ItemProvider::getItem)
            .forEach(item -> item.fillItemCategory(this, items));
    }
}
