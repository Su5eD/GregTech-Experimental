package mods.gregtechmod.objects.items;

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

@NotExperimental
public class ItemCellClassic extends ItemBase {
    
    static {
        try {
            GregTechMod.LOGGER.info("Injecting custom CellFluidHandler into ItemClassicCell");
            Field capsField = ItemIC2.class.getDeclaredField("caps");
            capsField.setAccessible(true);
            ItemClassicCell cell = ItemName.cell.getInstance();
            //noinspection unchecked
            Map<Capability<?>, com.google.common.base.Function<ItemStack, ?>> caps = (Map<Capability<?>, com.google.common.base.Function<ItemStack, ?>>) capsField.get(cell);
            caps.put(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, ItemCellClassic::getHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            GregTechMod.LOGGER.catching(e);
        }
    }

    private final Fluid fluid;

    public ItemCellClassic(String name, String description, Fluid fluid) {
        super(name, description);
        this.fluid = fluid;
        setFolder("cell/classic");
        setRegistryName("cell_classic_" + name);
        setTranslationKey("cell_classic_" + name);
        if (GregTechMod.classic) setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    private static GtCellFluidHandler getHandler(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemCellClassic) {
            return new GtCellFluidHandler(stack);
        } else if (item instanceof ItemClassicCell) {
            ItemClassicCell cell = ItemName.cell.getInstance();
            CellType type = cell.getType(stack);

            if (type != null && type.isFluidContainer()) {
                return new GtCellFluidHandler(stack, cell::getType);
            }
        }

        return null;
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

        @SuppressWarnings("unchecked")
        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
                return (T) getHandler(this.stack);
            }

            return null;
        }
    }

    public static class GtCellFluidHandler extends CellType.CellFluidHandler {

        public GtCellFluidHandler(ItemStack container) {
            this(container, stack -> CellType.empty);
        }

        public GtCellFluidHandler(ItemStack container, Function<ItemStack, CellType> typeGetter) {
            super(container, typeGetter);
        }

        private static boolean isValidFluid(Fluid fluid) {
            return fluid != null && BlockItems.classicCells.containsKey(fluid.getName());
        }

        @Nullable
        private Fluid getContainedFluid() {
            Item item = this.container.getItem();
            return item instanceof ItemCellClassic ? ((ItemCellClassic) item).fluid : null;
        }

        @Override
        public FluidStack getFluid() {
            Fluid fluid = getContainedFluid();
            return fluid != null ? new FluidStack(fluid, Fluid.BUCKET_VOLUME) : super.getFluid();
        }

        @Override
        protected void setFluid(FluidStack stack) {
            if (stack == null) {
                if (getContainedFluid() != null) {
                    this.container = ItemName.cell.getItemStack(CellType.empty);
                    return;
                }
            } else {
                String name = stack.getFluid().getName();
                if (this.typeGetter.get() == CellType.empty && getContainedFluid() == null && BlockItems.classicCells.containsKey(name)) {
                    this.container = new ItemStack(BlockItems.classicCells.get(name));
                    return;
                }
            }

            super.setFluid(stack);
        }

        @Override
        public boolean canFillFluidType(FluidStack stack) {
            return super.canFillFluidType(stack) || getContainedFluid() == null && this.typeGetter.get() == CellType.empty && isValidFluid(stack.getFluid());
        }
    }
}
