package mods.gregtechmod.objects.items;

import ic2.core.IC2;
import ic2.core.item.ItemClassicCell;
import ic2.core.item.ItemIC2;
import ic2.core.item.type.CellType;
import ic2.core.profile.NotExperimental;
import ic2.core.ref.ItemName;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.base.ItemBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@NotExperimental
public class ItemCellClassic extends ItemBase {
    private final Fluid fluid;

    public ItemCellClassic(String name, @Nullable String description, Fluid fluid) {
        super(name, description);
        this.fluid = fluid;
        setFolder("cell/classic");
        setRegistryName("cell_classic_"+name);
        setTranslationKey("cell_classic_"+name);
        if (IC2.version.isClassic()) setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    static {
        try {
            GregTechMod.logger.info("Injecting custom CellFluidHandler into ItemClassicCell");
            Field capsField = ItemIC2.class.getDeclaredField("caps");
            capsField.setAccessible(true);
            ItemClassicCell cell = ItemName.cell.getInstance();
            Map<Capability<?>, com.google.common.base.Function<ItemStack, ?>> caps = (Map<Capability<?>, com.google.common.base.Function<ItemStack, ?>>) capsField.get(cell);
            caps.put(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, ItemCellClassic::getHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityProvider(stack);
    }

    private static class CapabilityProvider implements ICapabilityProvider {
        private final ItemStack stack;

        private CapabilityProvider(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
                return (T) getHandler(this.stack);
            }

            return null;
        }
    }

    private static GtCellFluidHandler getHandler(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemClassicCell) {
            ItemClassicCell cell = ItemName.cell.getInstance();
            CellType type = cell.getType(stack);

            if (type != null && type.isFluidContainer()) {
                return new GtCellFluidHandler(stack, cell::getType);
            }
        } else if (item instanceof ItemCellClassic) {
            return new GtCellFluidHandler(() -> ((ItemCellClassic) item).fluid, stack);
        }

        return null;
    }

    public static class GtCellFluidHandler extends CellType.CellFluidHandler {
        private final Supplier<Fluid> fluidSupplier;

        public GtCellFluidHandler(Supplier<Fluid> fluidSupplier, ItemStack container) {
            super(container, stack -> CellType.empty);
            this.fluidSupplier = fluidSupplier;
        }

        public GtCellFluidHandler(ItemStack container, Function<ItemStack, CellType> typeGetter) {
            super(container, typeGetter);
            this.fluidSupplier = () -> null;
        }

        @Override
        public FluidStack getFluid() {
            Fluid fluid = this.fluidSupplier.get();
            if (fluid != null) return new FluidStack(fluid, Fluid.BUCKET_VOLUME);

            return super.getFluid();
        }

        @Override
        public boolean canFillFluidType(FluidStack stack) {
            if (stack == null) return false;
            Fluid fluid = stack.getFluid();
            return super.canFillFluidType(stack) || (this.fluidSupplier.get() == null && super.typeGetter.get() == CellType.empty && isValidFluid(fluid));
        }

        @Override
        protected void setFluid(FluidStack stack) {
            String name;
            if (stack == null) {
                if (this.fluidSupplier.get() != null) {
                    this.container = ItemName.cell.getItemStack(CellType.empty);
                    return;
                }
            } else if (super.typeGetter.get() == CellType.empty && this.fluidSupplier.get() == null && BlockItems.CLASSIC_CELLS.containsKey(name = stack.getFluid().getName())){
                this.container = new ItemStack(BlockItems.CLASSIC_CELLS.get(name));
                return;
            }

            super.setFluid(stack);
        }

        private static boolean isValidFluid(Fluid fluid) {
            if (fluid == null) return false;

            return BlockItems.CLASSIC_CELLS.containsKey(fluid.getName());
        }
    }
}
