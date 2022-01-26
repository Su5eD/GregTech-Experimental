package mods.gregtechmod.util;

import ic2.api.upgrade.IUpgradeItem;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.upgrade.ItemUpgradeModule;
import ic2.core.ref.FluidName;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.util.QuadFunction;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GtUtil {
    public static final ResourceLocation COMMON_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/common.png");
    public static final IFluidHandler VOID_TANK = new VoidTank();
    public static final Predicate<Fluid> STEAM_PREDICATE = fluid -> fluid == FluidRegistry.getFluid("steam") || fluid == FluidName.steam.getInstance() || fluid == FluidName.superheated_steam.getInstance();
    public static final InvSlot.InvSide INV_SIDE_VERTICAL = addInvside("VERTICAL", EnumFacing.UP, EnumFacing.DOWN);
    public static final InvSlot.InvSide INV_SIDE_NS = addInvside("NS", EnumFacing.NORTH, EnumFacing.SOUTH);

    private static final LazyValue<Path> MOD_FILE = new LazyValue<>(() -> {
        Path source = Loader.instance().activeModContainer().getSource().toPath();
        if (source.toString().endsWith(".jar")) {
            try {
                FileSystem fs = FileSystems.newFileSystem(source, null);
                return fs.getPath("/");
            } catch (IOException e) {
                throw new RuntimeException("Could not create ModFile filesystem", e);
            }
        }

        return source;
    });

    private GtUtil() {
    }

    public static Path getAssetPath(String name) {
        String path = "assets/" + Reference.MODID + "/" + name;

        return MOD_FILE.get().resolve(path);
    }

    public static BufferedReader readAsset(String path) {
        try {
            return Files.newBufferedReader(getAssetPath(path));
        } catch (IOException e) {
            throw new RuntimeException("Asset " + path + " not found", e);
        }
    }

    public static InvSlot.InvSide addInvside(String name, EnumFacing... sides) {
        return EnumHelper.addEnum(InvSlot.InvSide.class, name, new Class[]{EnumFacing[].class}, (Object) sides);
    }

    public static List<ItemStack> copyStackList(List<ItemStack> list) {
        return StreamEx.of(list)
            .map(ItemStack::copy)
            .toList();
    }

    public static List<ItemStack> nonEmptyList(ItemStack... elements) {
        return StreamEx.of(elements)
            .remove(ItemStack::isEmpty)
            .toList();
    }

    public static boolean damageStack(EntityPlayer player, ItemStack stack, int damage) {
        if (stack.isItemStackDamageable()) {
            if (!player.capabilities.isCreativeMode) {
                if (stack.attemptDamageItem(damage, player.getRNG(), player instanceof EntityPlayerMP ? (EntityPlayerMP) player : null)) {
                    if (stack.getItem().hasContainerItem(stack)) {
                        ItemStack containerStack = stack.getItem().getContainerItem(stack);
                        if (!containerStack.isEmpty()) {
                            player.setHeldItem(player.getActiveHand(), containerStack.copy());
                            return true;
                        }
                    }
                    player.renderBrokenItemStack(stack);
                    stack.shrink(1);
                    player.addStat(StatList.getObjectBreakStats(stack.getItem()));
                    stack.setItemDamage(0);
                }
            }
            return true;
        }
        return false;
    }

    public static void damageEntity(EntityLivingBase entity, EntityLivingBase attacker, float damage) {
        int oldHurtResistanceTime = entity.hurtResistantTime;
        entity.hurtResistantTime = 0;
        entity.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage);
        entity.hurtResistantTime = oldHurtResistanceTime;
    }

    public static List<ItemStack> correctStacksize(List<ItemStack> list) {
        return list.stream()
            .flatMap(stack -> {
                int maxSize = stack.getMaxStackSize();
                if (stack.getCount() > maxSize) {
                    int cycles = stack.getCount() / maxSize + 1;
                    ItemStack split = stack.splitStack(maxSize);
                    return Stream.generate(() -> split)
                        .limit(cycles - 1);
                }
                return Stream.of(stack);
            })
            .collect(Collectors.toList());
    }

    public static boolean stackEquals(ItemStack first, ItemStack second) {
        return stackEquals(first, second, true);
    }

    public static boolean stackEquals(ItemStack first, ItemStack second, boolean matchNbt) {
        if (first.isEmpty() || second.isEmpty()) return false;

        return first.getItem() == second.getItem()
            && (first.getMetadata() == OreDictionary.WILDCARD_VALUE || first.getMetadata() == second.getMetadata())
            && (!matchNbt || StackUtil.checkNbtEquality(first.getTagCompound(), second.getTagCompound()));
    }

    public static boolean stackItemEquals(ItemStack first, ItemStack second) {
        if (first.isEmpty() || second.isEmpty()) return false;

        return first.getItem() == second.getItem() && (first.isItemStackDamageable() || first.getItemDamage() == second.getItemDamage());
    }
    
    public static ItemStack copyWithMetaSize(ItemStack stack, int count, int meta) {
        ItemStack ret = ItemHandlerHelper.copyStackWithSize(stack, count);
        ret.setItemDamage(meta);
        return stack;
    }

    public static ItemStack getEmptyFluidContainer(ItemStack stack) {
        IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack.copy());
        if (handler != null) {
            handler.drain(Integer.MAX_VALUE, true);
            return handler.getContainer();
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack getFluidContainer(ItemStack stack, Fluid fluid) {
        ItemStack container = stack.copy();
        IFluidHandler fluidHandler = FluidUtil.getFluidHandler(container);
        if (fluidHandler != null) {
            fluidHandler.fill(new FluidStack(fluid, Integer.MAX_VALUE), true);
            return container;
        }

        return ItemStack.EMPTY;
    }

    public static void sendMessage(EntityPlayer player, String key, Object... args) {
        if (!player.world.isRemote) player.sendMessage(new TextComponentTranslation(key, args));
    }

    public static IC2UpgradeType getUpgradeType(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IUpgradeItem) {
            ItemUpgradeModule.UpgradeType upgradeType = ItemUpgradeModule.UpgradeType.values()[stack.getMetadata()];
            String name = upgradeType.name();
            return Arrays.stream(IC2UpgradeType.values())
                .filter(type -> type.itemType.equals(name))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    public static void consumeMultiInput(List<IRecipeIngredient> input, GtSlotProcessableItemStack<?, ?>... slots) {
        Arrays.stream(slots)
            .forEach(slot -> input.forEach(ingredient -> {
                if (ingredient.apply(slot.get())) slot.consume(ingredient.getCount(), true);
            }));
    }

    public static boolean isWrench(ItemStack stack) {
        return containsStack(stack, GregTechAPI.instance().getWrenches());
    }

    public static boolean isScrewdriver(ItemStack stack) {
        return containsStack(stack, GregTechAPI.instance().getScrewdrivers());
    }

    public static boolean isSoftHammer(ItemStack stack) {
        return containsStack(stack, GregTechAPI.instance().getSoftHammers());
    }

    public static boolean isHardHammer(ItemStack stack) {
        return containsStack(stack, GregTechAPI.instance().getHardHammers());
    }

    public static boolean isCrowbar(ItemStack stack) {
        return containsStack(stack, GregTechAPI.instance().getCrowbars());
    }

    public static boolean containsStack(ItemStack stack, Collection<ItemStack> stacks) {
        return stacks.stream()
            .anyMatch(s -> stackEquals(s, stack, false));
    }

    public static boolean isAir(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos);
    }

    public static boolean findBlock(IBlockAccess world, BlockPos pos, Block... blocks) {
        Block block = world.getBlockState(pos).getBlock();
        return Arrays.asList(blocks).contains(block);
    }

    public static boolean findTileEntity(IBlockAccess world, BlockPos pos, Class<?>... classes) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null && Arrays.stream(classes)
            .anyMatch(clazz -> clazz.isInstance(tileEntity));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends Comparable<T>> T getStateValueSafely(IBlockState state, IProperty<T> property) {
        return (T) state.getProperties().get(property);
    }

    public static void withModContainerOverride(ModContainer container, Runnable runnable) {
        ModContainer old = Loader.instance().activeModContainer();
        Loader.instance().setActiveModContainer(container);

        runnable.run();

        Loader.instance().setActiveModContainer(old);
    }

    public static ResourceLocation getModelResourceLocation(String name, String folder) {
        if (folder == null) return new ResourceLocation(Reference.MODID, name);
        return new ResourceLocation(String.format("%s:%s/%s", Reference.MODID, folder, name));
    }

    public static ResourceLocation getGuiTexture(String name) {
        return new ResourceLocation(Reference.MODID, "textures/gui/" + name + ".png");
    }

    public static ResourceLocation getCoverTexture(String name) {
        return new ResourceLocation(Reference.MODID, "blocks/covers/" + name);
    }

    public static int moveItemStack(TileEntity from, TileEntity to, EnumFacing fromSide, EnumFacing toSide, int maxTargetSize, int minTargetSize) {
        return moveItemStack(from, to, fromSide, toSide, maxTargetSize, minTargetSize, stack -> true);
    }

    public static int moveItemStack(TileEntity from, TileEntity to, EnumFacing fromSide, EnumFacing toSide, int maxTargetSize, int minTargetSize, java.util.function.Predicate<ItemStack> filter) {
        return moveItemStack(from, to, fromSide, toSide, dest -> true, filter, (source, dest, sourceStack, sourceSlot) -> {
            for (int j = 0; j < dest.getSlots(); j++) {
                int count = moveSingleItemStack(source, dest, sourceStack, sourceSlot, j, minTargetSize, maxTargetSize);
                if (count > 0) return count;
            }
            return 0;
        });
    }

    public static int moveItemStackIntoSlot(TileEntity from, TileEntity to, EnumFacing fromSide, EnumFacing toSide, int destSlot, int maxTargetSize, int minTargetSize) {
        return moveItemStack(from, to, fromSide, toSide, dest -> dest.getSlots() - 1 >= destSlot, stack -> true, (source, dest, sourceStack, sourceSlot) -> {
            int count = moveSingleItemStack(source, dest, sourceStack, sourceSlot, destSlot, minTargetSize, maxTargetSize);
            return Math.max(count, 0);
        });
    }

    private static int moveItemStack(TileEntity from, TileEntity to, EnumFacing fromSide, EnumFacing toSide,
                                     java.util.function.Predicate<IItemHandler> condition, java.util.function.Predicate<ItemStack> filter, QuadFunction<IItemHandler, IItemHandler, ItemStack, Integer, Integer> consumer) {
        if (from != null && to != null) {
            IItemHandler source = from.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, fromSide);
            if (source != null) {
                IItemHandler dest = to.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, toSide);

                if (dest != null && condition.test(dest)) {
                    for (int i = 0; i < source.getSlots(); i++) {
                        ItemStack sourceStack = source.extractItem(i, source.getSlotLimit(i), true);
                        if (!sourceStack.isEmpty() && filter.test(sourceStack)) {
                            return consumer.apply(source, dest, sourceStack, i);
                        }
                    }
                }
            }
        }

        return 0;
    }

    private static int moveSingleItemStack(IItemHandler source, IItemHandler dest, ItemStack sourceStack, int sourceSlot, int destSlot, int minTargetSize, int maxTargetSize) {
        ItemStack destStack = dest.getStackInSlot(destSlot);

        ItemStack sourceStackCopy = sourceStack.copy();
        int free = destStack.getMaxStackSize() - destStack.getCount();
        sourceStackCopy.setCount(Math.min(sourceStackCopy.getCount(), Math.min(maxTargetSize - destStack.getCount(), free)));

        int totalSize = sourceStackCopy.getCount() + destStack.getCount();
        if (totalSize >= minTargetSize && (destStack.isEmpty() || ItemHandlerHelper.canItemStacksStack(sourceStackCopy, destStack))) {
            ItemStack inserted = dest.insertItem(destSlot, sourceStackCopy, false);
            int count = sourceStackCopy.getCount();
            if (inserted.isEmpty()) source.extractItem(sourceSlot, count, false);
            return count;
        }

        return 0;
    }

    public static Set<EnumFacing> allSidesExcept(EnumFacing side) {
        Set<EnumFacing> sides = new HashSet<>(Util.allFacings);
        sides.remove(side);
        return sides;
    }

    public static Set<EnumFacing> allSidesExcept(Collection<EnumFacing> sides) {
        Set<EnumFacing> facings = new HashSet<>(Util.allFacings);
        facings.removeAll(sides);
        return facings;
    }

    public static ItemStack collectItemFromArea(World world, BlockPos begin, BlockPos end) {
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(begin, end));
        if (list.size() > 0) {
            EntityItem entityItem = list.get(0);
            world.removeEntity(entityItem);
            return entityItem.getItem();
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack copyWithoutDamage(ItemStack stack, int count) {
        ItemStack copy = stack.copy();
        copy.setCount(count);
        copy.setItemDamage(0);
        return copy;
    }

    public static EnumFacing getNextFacing(EnumFacing facing) {
        int index = (facing.ordinal() + 1) % EnumFacing.VALUES.length;
        return EnumFacing.VALUES[index];
    }

    public static boolean canGrowStack(ItemStack existing, ItemStack addition) {
        return addition.getCount() + existing.getCount() <= existing.getMaxStackSize();
    }

    public static Pair<ItemStack, FluidStack> fillContainer(ItemStack container, IFluidHandler source, int maxFill) {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
        IFluidHandlerItem dest = FluidUtil.getFluidHandler(containerCopy);

        if (dest != null) {
            FluidStack transfer = FluidUtil.tryFluidTransfer(dest, source, maxFill, true);
            if (transfer != null) {
                ItemStack stack = dest.getContainer();
                return Pair.of(stack, transfer);
            }
        }

        return Pair.of(ItemStack.EMPTY, null);
    }
    
    public static Predicate<Fluid> fluidPredicate(Fluid... fluids) {
        return fluid -> ArrayUtils.contains(fluids, fluid);
    }
    
    public static Path extractConfigAsset(String name) {
        Path source = getAssetPath(name);
        try {
            Path dest = GregTechMod.modConfigDir.resolve(name);
            if (Files.notExists(dest)) return Files.copy(source, dest);
        } catch (IOException e) {
            GregTechMod.LOGGER.error("Couldn't extract oredict config", e);
        }
        return source;
    }

    private static class VoidTank implements IFluidHandler {

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return resource.amount;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }
}
