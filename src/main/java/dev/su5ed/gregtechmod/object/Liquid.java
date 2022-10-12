package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.fluid.ModFluidType;
import dev.su5ed.gregtechmod.item.ModBucketItem;
import dev.su5ed.gregtechmod.util.FluidProvider;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Function;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public enum Liquid implements ItemProvider, FluidProvider {
    BERYLIUM("Be", 1690, 0xFF032900),
    CALCIUM("Ca", 1378, 0xFFAE664F),
    CALCIUM_CARBONATE("CaCO3", 2711, 0xFF8E2D00),
    CHLORITE("Cl", 15625, 0xFF349D9D),
    DIESEL(860, 0xFFCBC000),
    GLYCERYL("C3H5N3O9", 1261, 0xFF00463D),
    // Not technically a liquid, but I'll keep it here
    HELIUM_PLASMA("He", TextureType.CUSTOM, 1000, 0xFFFFFFFF),
    LITHIUM("Li", 512, 0xFF5398D4),
    MERCURY("Hg", 13534, 0xFF7DB1DE),
    NITRO_COALFUEL(0xFF002D24),
    NITRO_DIESEL(832, 0xFFD5EC00),
    OIL(null, TextureType.DENSE, 800, 0xFF020202),
    POTASSIUM("K", 828, 0xFF8EADAD),
    SEED_OIL(918, 0xFFFBFF8E),
    SILICON("Si", 2570, 0xFF13001D),
    SODIUM("Na", 927, 0xFF00219C),
    SODIUM_PERSULFATE("Na2S2O8", 1120, 0xFF007E7E),
    WOLFRAMIUM("W", 17600, 0xFF00000f);

    private final Lazy<FluidType> fluidType;
    private final Lazy<FlowingFluid> sourceFluid;
    private final Lazy<FlowingFluid> flowingFluid;
    private final Lazy<Item> bucketItem;

    Liquid(int tint) {
        this(1000, tint);
    }

    Liquid(int density, int tint) {
        this(null, density, tint);
    }
    
    Liquid(String description, int density, int tint) {
        this(description, TextureType.NORMAL, density, tint);
    }

    Liquid(String description, TextureType type, int density, int tint) {
        String name = getName();
        this.fluidType = Lazy.of(() -> new ModFluidType(new ModFluidType.FluidProperties()
            .density(density)
            .viscosity(density)
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
    
    enum TextureType {
        NORMAL(GtUtil.LIQUID_STILL, GtUtil.LIQUID_FLOW),
        DENSE(GtUtil.LIQUID_DENSE_STILL, GtUtil.LIQUID_FLOW),
        CUSTOM(name -> location("fluid/" + name));
        
        private final Function<String, ResourceLocation> stillFactory;
        private final Function<String, ResourceLocation> flowFactory;

        TextureType(ResourceLocation still, ResourceLocation flow) {
            this(name -> still, name -> flow);
        }
        
        TextureType(Function<String, ResourceLocation> factory) {
            this(factory, factory);
        }
        
        TextureType(Function<String, ResourceLocation> stillFactory, Function<String, ResourceLocation> flowFactory) {
            this.stillFactory = stillFactory;
            this.flowFactory = flowFactory;
        }
        
        public ResourceLocation getStillTexture(String name) {
            return this.stillFactory.apply(name);
        }
        
        public ResourceLocation getFlowTexture(String name) {
            return this.flowFactory.apply(name);
        }
    }
}
