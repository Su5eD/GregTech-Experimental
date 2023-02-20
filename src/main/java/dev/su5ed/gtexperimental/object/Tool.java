package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.AdvancedDrillItem;
import dev.su5ed.gtexperimental.item.AdvancedSawItem;
import dev.su5ed.gtexperimental.item.AdvancedWrenchItem;
import dev.su5ed.gtexperimental.item.BugSprayItem;
import dev.su5ed.gtexperimental.item.CrowbarItem;
import dev.su5ed.gtexperimental.item.DebugScannerItem;
import dev.su5ed.gtexperimental.item.DestructorPackItem;
import dev.su5ed.gtexperimental.item.ElectricItem;
import dev.su5ed.gtexperimental.item.ElectricItem.ElectricItemProperties;
import dev.su5ed.gtexperimental.item.FoamSprayItem;
import dev.su5ed.gtexperimental.item.HardenerSprayItem;
import dev.su5ed.gtexperimental.item.HydrationSprayItem;
import dev.su5ed.gtexperimental.item.IceSprayItem;
import dev.su5ed.gtexperimental.item.PepperSprayItem;
import dev.su5ed.gtexperimental.item.RockCutterItem;
import dev.su5ed.gtexperimental.item.RubberHammerItem;
import dev.su5ed.gtexperimental.item.ScannerItem;
import dev.su5ed.gtexperimental.item.ScrewdriverItem;
import dev.su5ed.gtexperimental.item.SolderingToolItem;
import dev.su5ed.gtexperimental.item.TeslaStaffItem;
import dev.su5ed.gtexperimental.util.ProfileManager;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Tool implements TaggedItemProvider {
    ADVANCED_DRILL(AdvancedDrillItem::new, GregTechTags.LARGE_DRILL),
    ADVANCED_SAW(AdvancedSawItem::new, GregTechTags.SAW),
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
        .maxCharge(ProfileManager.INSTANCE.isClassic() ? 10000000 : 100000000)
        .transferLimit(8192)
        .energyTier(ProfileManager.INSTANCE.isClassic() ? 4 : 5)
        .providesEnergy(true)
        .rarity(Rarity.RARE)), GregTechTags.LARGE_EU_STORE),
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
