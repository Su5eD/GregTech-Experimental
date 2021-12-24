package mods.gregtechmod.util;

import com.google.common.base.Predicate;
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
import mods.gregtechmod.inventory.invslot.GtSlotProcessableItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GtUtil {
    public static final ResourceLocation COMMON_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/common.png");
    public static final IFluidHandler VOID_TANK = new VoidTank();
    @SuppressWarnings("Guava")
    public static final Predicate<Fluid> STEAM_PREDICATE = fluid -> fluid == FluidRegistry.getFluid("steam") || fluid == FluidName.steam.getInstance() || fluid == FluidName.superheated_steam.getInstance();
    public static final InvSlot.InvSide INV_SIDE_VERTICAL = addInvside("VERTICAL", EnumFacing.UP, EnumFacing.DOWN);
    public static final InvSlot.InvSide INV_SIDE_NS = addInvside("NS", EnumFacing.NORTH, EnumFacing.SOUTH);
    
    private static final LazyValue<IModFile> MOD_FILE = new LazyValue<>(() -> {
        Path path = Loader.instance().activeModContainer().getSource().toPath();
        return Files.isRegularFile(path) ? new FSModFile(path) : new PathModFile(path);
    });
    
    private GtUtil() {}
    
    public static Path getAssetPath(String name) {
        String path = "assets/" + Reference.MODID + "/" + name;
        
        return MOD_FILE.get().getPath(path);
    }
    
    public static BufferedReader readAsset(String path) {
        try {
            return Files.newBufferedReader(getAssetPath(path));
        } catch (IOException e) {
            throw new RuntimeException("Asset " + path + " not found", e);
        }
    }
    
    public static InvSlot.InvSide addInvside(String name, EnumFacing... sides) {
        return EnumHelper.addEnum(InvSlot.InvSide.class, name, new Class[] { EnumFacing[].class }, (Object) sides);
    }
    
    public static List<ItemStack> copyStackList(List<ItemStack> list) {
        return list.stream()
                .map(ItemStack::copy)
                .collect(Collectors.toList());
    }
    
    public static List<ItemStack> nonEmptyList(ItemStack... elements) {
        return Stream.of(elements)
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
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

    public static ItemStack copyWithMeta(ItemStack stack, int meta) {
        ItemStack ret = stack.copy();
        ret.setItemDamage(meta);
        return stack;
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
    
    public static int moveItemStack(TileEntity from, TileEntity to, EnumFacing fromSide, EnumFacing toSide, int maxTargetSize, int minTargetSize, int maxMove, int minMove) {
        return moveItemStack(from, to, fromSide, toSide, maxMove, dest -> true, (source, dest, sourceStack, sourceSlot) -> {
            for (int j = 0; j < dest.getSlots(); j++) {
                int count = moveSingleItemStack(source, dest, sourceStack, sourceSlot, j, minMove, minTargetSize, maxTargetSize);
                if (count > 0) return count;
            }
            return 0;
        });
    }
    
    public static int moveItemStackIntoSlot(TileEntity from, TileEntity to, EnumFacing fromSide, EnumFacing toSide, int destSlot, int maxTargetSize, int minTargetSize, int maxMove, int minMove) {
        return moveItemStack(from, to, fromSide, toSide, maxMove, dest -> dest.getSlots() >= destSlot, (source, dest, sourceStack, sourceSlot) -> {
            int count = moveSingleItemStack(source, dest, sourceStack, sourceSlot, destSlot, minMove, minTargetSize, maxTargetSize);
            return Math.max(count, 0);
        });
    }
    
    private static int moveItemStack(TileEntity from, TileEntity to, EnumFacing fromSide, EnumFacing toSide, int maxMove,
                                     java.util.function.Predicate<IItemHandler> condition, QuadFunction<IItemHandler, IItemHandler, ItemStack, Integer, Integer> consumer) {
        if (to != null) {
            IItemHandler source = from.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, fromSide);
            if (source != null) {
                IItemHandler dest = to.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, toSide);

                if (dest != null && condition.test(dest)) {
                    for (int i = 0; i < source.getSlots(); i++) {
                        ItemStack sourceStack = source.extractItem(i, maxMove, true);
                        if (!sourceStack.isEmpty()) {
                            return consumer.apply(source, dest, sourceStack, i);
                        }
                    }
                }
            }
        }
        
        return 0;
    }
    
    private static int moveSingleItemStack(IItemHandler source, IItemHandler dest, ItemStack sourceStack, int sourceSlot, int destSlot, int minMove, int minTargetSize, int maxTargetSize) {
        ItemStack destStack = dest.getStackInSlot(destSlot);
        
        ItemStack sourceStackCopy = sourceStack.copy();
        int totalSize = sourceStackCopy.getCount() + destStack.getCount();
        if (totalSize > maxTargetSize)
            sourceStackCopy.setCount(maxTargetSize - destStack.getCount());
        
        if (totalSize >= minTargetSize && (destStack.isEmpty() || sourceStackCopy.getCount() >= minMove && ItemHandlerHelper.canItemStacksStack(sourceStackCopy, destStack))) {
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

    private interface IModFile {
        Path getPath(String path);
    }

    private static class FSModFile implements IModFile {
        private final FileSystem fs;

        private FSModFile(Path path) {
            try {
                this.fs = FileSystems.newFileSystem(path, null);
            } catch (IOException e) {
                throw new RuntimeException("Could not create ModFile instance", e);
            }
        }

        @Override
        public Path getPath(String path) {
            return this.fs.getPath(path);
        }
    }

    private static class PathModFile implements IModFile {
        private final Path path;

        private PathModFile(Path path) {
            this.path = path;
        }

        @Override
        public Path getPath(String path) {
            return this.path.resolve(path);
        }
    }
}
