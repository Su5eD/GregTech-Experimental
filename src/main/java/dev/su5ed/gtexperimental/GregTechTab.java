package dev.su5ed.gtexperimental;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Armor;
import dev.su5ed.gtexperimental.object.Cell;
import dev.su5ed.gtexperimental.object.ColorSpray;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.File;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.object.Hammer;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.JackHammer;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModBlock;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.object.NuclearFuelRod;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Rod;
import dev.su5ed.gtexperimental.object.Saw;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.object.TurbineRotor;
import dev.su5ed.gtexperimental.object.Upgrade;
import dev.su5ed.gtexperimental.object.Wrench;
import dev.su5ed.gtexperimental.util.ItemProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

public final class GregTechTab extends CreativeModeTab {
    public static final CreativeModeTab INSTANCE = new GregTechTab();

    private GregTechTab() {
        super(Reference.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return Miscellaneous.GREG_COIN.getItemStack();
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
            .append(NuclearCoolantPack.values())
            .append(NuclearFuelRod.values())
            .append(Wrench.values())
            .append(JackHammer.values())
            .append(Hammer.values())
            .append(Saw.values())
            .append(File.values())
            .append(Upgrade.values())
            .append(Armor.values())
            .append(Miscellaneous.values())
            .append(ColorSpray.values())
            .append(Cell.values())
            .map(ItemProvider::getItem)
            .forEach(item -> item.fillItemCategory(this, items));
    }
}
