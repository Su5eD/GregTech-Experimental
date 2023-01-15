package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.item.AdvancedDrillItem;
import dev.su5ed.gregtechmod.item.AdvancedSawItem;
import dev.su5ed.gregtechmod.item.AdvancedWrenchItem;
import dev.su5ed.gregtechmod.item.BugSprayItem;
import dev.su5ed.gregtechmod.item.CrowbarItem;
import dev.su5ed.gregtechmod.item.DebugScannerItem;
import dev.su5ed.gregtechmod.item.DestructorPackItem;
import dev.su5ed.gregtechmod.item.ElectricItem;
import dev.su5ed.gregtechmod.item.ElectricItem.ElectricItemProperties;
import dev.su5ed.gregtechmod.item.FoamSprayItem;
import dev.su5ed.gregtechmod.item.HardenerSprayItem;
import dev.su5ed.gregtechmod.item.HydrationSprayItem;
import dev.su5ed.gregtechmod.item.IceSprayItem;
import dev.su5ed.gregtechmod.item.PepperSprayItem;
import dev.su5ed.gregtechmod.item.RockCutterItem;
import dev.su5ed.gregtechmod.item.RubberHammerItem;
import dev.su5ed.gregtechmod.item.ScannerItem;
import dev.su5ed.gregtechmod.item.ScrewdriverItem;
import dev.su5ed.gregtechmod.item.SolderingToolItem;
import dev.su5ed.gregtechmod.item.TeslaStaffItem;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Tool implements TaggedItemProvider {
    ADVANCED_DRILL(AdvancedDrillItem::new, GregTechTags.LARGE_DRILL),
    ADVANCED_SAW(AdvancedSawItem::new),
    ADVANCED_WRENCH(AdvancedWrenchItem::new),
    CROWBAR(CrowbarItem::new, GregTechTags.CROWBAR),
    DEBUG_SCANNER(DebugScannerItem::new),
    DESTRUCTORPACK(DestructorPackItem::new),
    BUG_SPRAY(BugSprayItem::new),
    FOAM_SPRAY(FoamSprayItem::new),
    HARDENER_SPRAY(HardenerSprayItem::new),
    HYDRATION_SPRAY(HydrationSprayItem::new),
    ICE_SPRAY(IceSprayItem::new),
    PEPPER_SPRAY(PepperSprayItem::new),
    LAPOTRONIC_ENERGY_ORB(() -> new ElectricItem(new ElectricItemProperties()
        .maxCharge(GregTechMod.PROFILE_MANAGER.isClassic() ? 10000000 : 100000000)
        .transferLimit(8192)
        .energyTier(GregTechMod.PROFILE_MANAGER.isClassic() ? 4 : 5)
        .providesEnergy(true)
        .rarity(Rarity.RARE)), GregTechMod.PROFILE_MANAGER.isClassic() ? GregTechTags.CRAFTING_10KK_EU_STORE : GregTechTags.CRAFTING_100KK_EU_STORE),
    ROCK_CUTTER(RockCutterItem::new),
    RUBBER_HAMMER(RubberHammerItem::new, GregTechTags.SOFT_HAMMER),
    SCANNER(ScannerItem::new),
    SCREWDRIVER(ScrewdriverItem::new, GregTechTags.SCREWDRIVER),
    SOLDERING_TOOL(SolderingToolItem::new, GregTechTags.SOLDERING_IRON),
    TESLA_STAFF(TeslaStaffItem::new);
    // TODO Portable Sonictron

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

    Tool(Supplier<Item> supplier) {
        this(supplier, null);
    }

    Tool(Supplier<Item> supplier, TagKey<Item> tag) {
        this.instance = Lazy.of(supplier);
        this.tag = tag;
    }

    @Override
    public String getRegistryName() {
        return getName();
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Nullable
    @Override
    public TagKey<Item> getTag() {
        return this.tag;
    }
}
