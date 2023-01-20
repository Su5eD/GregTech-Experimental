package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.util.ModFluidType;
import dev.su5ed.gtexperimental.item.ModBucketItem;
import dev.su5ed.gtexperimental.util.FluidProvider;
import dev.su5ed.gtexperimental.util.FluidTextureType;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.ItemProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public enum ModFluid implements ItemProvider, FluidProvider {
    // Liquids
    BERYLIUM("Be", 1690, 0xFF032900),
    CALCIUM("Ca", 1378, 0xFFAE664F),
    CALCIUM_CARBONATE("CaCO3", 2711, 0xFF8E2D00),
    CHLORITE("Cl", 15625, 0xFF349D9D),
    DIESEL(860, 0xFFCBC000),
    GLYCERYL("C3H5N3O9", 1261, 0xFF00463D),
    HELIUM_PLASMA("He", FluidTextureType.CUSTOM, 1000, 0xFFFFFFFF),
    LITHIUM("Li", 512, 0xFF5398D4),
    MERCURY("Hg", 13534, 0xFF7DB1DE),
    NITRO_COALFUEL(0xFF002D24),
    NITRO_DIESEL(832, 0xFFD5EC00),
    OIL(null, FluidTextureType.DENSE_LIQUID, 800, 0xFF020202),
    POTASSIUM("K", 828, 0xFF8EADAD),
    SEED_OIL(918, 0xFFFBFF8E),
    SILICON("Si", 2570, 0xFF13001D),
    SODIUM("Na", 927, 0xFF00219C),
    SODIUM_PERSULFATE("Na2S2O8", 1120, 0xFF007E7E),
    WOLFRAMIUM("W", 17600, 0xFF00000f),
    // Gases
    DEUTERIUM("H-2", 0xFFFCFF00),
    HELIUM("He", 0xFFFCFF90),
    HELIUM3("He-3", 0xFFFBFF63),
    HYDROGEN("H", 0xFF000042),
    METHANE("CH4", 0xFFFF3B97),
    NITROGEN("N", 0xFF00BCF1),
    NITROGEN_DIOXIDE("NO2", 0xFF85FCFF),
    STEAM("H2O", FluidTextureType.CUSTOM, -200, 0xFFFFFFFF),
    TRITIUM("H-3", 0xFFFF0000);

    private final Lazy<FluidType> fluidType;
    private final Lazy<FlowingFluid> sourceFluid;
    private final Lazy<FlowingFluid> flowingFluid;
    private final Lazy<Item> bucketItem;

    ModFluid(int tint) {
        this(1000, tint);
    }

    ModFluid(int density, int tint) {
        this(null, density, tint);
    }
    
    ModFluid(String description, int tint) {
        this(description, FluidTextureType.GAS, -200, tint);
    }

    ModFluid(String description, int density, int tint) {
        this(description, FluidTextureType.LIQUID, density, tint);
    }

    ModFluid(String description, FluidTextureType type, int density, int tint) {
        String name = getName();
        this.fluidType = Lazy.of(() -> new ModFluidType(new ModFluidType.FluidProperties()
            .density(density)
            .tintColor(tint)
            .stillTexture(type.getStillTexture(name))
            .flowingTexture(type.getFlowTexture(name))
            .overlayTexture(GtUtil.FLUID_OVERLAY)
            .renderOverlayTexture(GtUtil.FLUID_RENDER_OVERLAY)));
        ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(this.fluidType, this::getSourceFluid, this::getFlowingFluid)
            .bucket(this::getItem);
        this.sourceFluid = Lazy.of(() -> new ForgeFlowingFluid.Source(properties));
        this.flowingFluid = Lazy.of(() -> new ForgeFlowingFluid.Flowing(properties));
        this.bucketItem = Lazy.of(() -> new ModBucketItem(this.sourceFluid, description != null ? Component.literal(description) : null, ModObjects.itemProperties()
            .stacksTo(1)
            .craftRemainder(Items.BUCKET)));
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "bucket");
    }

    @Override
    public Item getItem() {
        return this.bucketItem.get();
    }

    @Override
    public String getFluidRegistryName() {
        return getName();
    }

    @Override
    public FluidType getType() {
        return this.fluidType.get();
    }

    @Override
    public FlowingFluid getSourceFluid() {
        return this.sourceFluid.get();
    }

    @Override
    public FlowingFluid getFlowingFluid() {
        return this.flowingFluid.get();
    }
}
