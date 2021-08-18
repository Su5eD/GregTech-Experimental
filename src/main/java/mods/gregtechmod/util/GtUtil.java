package mods.gregtechmod.util;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import ic2.api.item.ElectricItem;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.item.upgrade.ItemUpgradeModule;
import ic2.core.ref.FluidName;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableItemStack;
import mods.gregtechmod.objects.items.base.ItemArmorElectricBase;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GtUtil {
    public static final Random RANDOM = new Random();
    public static final Supplier<String> NULL_SUPPLIER = () -> null;
    public static final IFluidHandler VOID_TANK = new VoidTank();
    @SuppressWarnings("Guava")
    public static final Predicate<Fluid> STEAM_PREDICATE = fluid -> fluid == FluidRegistry.getFluid("steam") || fluid == FluidName.steam.getInstance() || fluid == FluidName.superheated_steam.getInstance();
    
    private static final DecimalFormat INT_FORMAT = new DecimalFormat("#,###,###,##0");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###,###,##0.00");
    private static final LazyValue<Object> MOD_FILE = new LazyValue<>(() -> {
        File file = Loader.instance().activeModContainer().getSource();
        if (file.isFile()) {
            try {
                return FileSystems.newFileSystem(file.toPath(), null);
            } catch (IOException e) {
                throw new RuntimeException("Could not find the mod container source", e);
            }
        }
        else return file.toPath();
    });
    
    public static Path getAssetPath(String name) {
        String path = "assets/" + Reference.MODID + "/" + name;
        Object modFile = MOD_FILE.get();
        
        if (modFile instanceof FileSystem) return ((FileSystem) modFile).getPath(path);
        else return ((Path) modFile).resolve(path);
    }
    
    public static BufferedReader readAsset(String path) {
        InputStream is = GtUtil.class.getResourceAsStream("/assets/" + Reference.MODID + "/" + path);
        if (is == null) throw new RuntimeException("Asset " + path + " not found");
        
        return new BufferedReader(new InputStreamReader(is));
    }

    public static <T, U> BiPredicate<T, U> alwaysTrue() {
        return (a, b) -> true;
    }

    public static String capitalizeString(String str) {
        if (str != null && str.length() > 0) return str.substring(0, 1).toUpperCase() + str.substring(1);
        return "";
    }

    public static boolean getFullInvisibility(EntityPlayer player) {
        return player.isInvisible() && player.inventory.armorInventory.stream()
                .filter(stack -> !stack.isEmpty())
                .anyMatch(stack -> {
                    Item item = stack.getItem();
                    if (item instanceof ItemArmorElectricBase) {
                        return ((ItemArmorElectricBase) item).perks.contains(ArmorPerk.INVISIBILITY_FIELD) && ElectricItem.manager.canUse(stack, 10000);
                    }
                    return false;
                });
    }

    @SafeVarargs
    public static <T> List<T> nonNullList(T... elements) {
        return Stream.of(elements)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static List<ItemStack> nonEmptyList(ItemStack... elements) {
        return Stream.of(elements)
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
    }

    public static List<ItemStack> copyList(List<ItemStack> list) {
        return list.stream()
                .map(ItemStack::copy)
                .collect(Collectors.toList());
    }

    public static ItemStack getWrittenBook(String name, String author, int pages, int ordinal) {
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        stack.setTagInfo("title", new NBTTagString(GtUtil.translate("book."+name+".name")));
        stack.setTagInfo("author", new NBTTagString(author));
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < pages; i++) {
            String page = '\"' + GtUtil.translate("book." + name + ".page" + (i < 10 ? "0" + i : i)) + '\"';
            if (i < 48) {
                if (page.length() < 256) {
                    tagList.appendTag(new NBTTagString(page));
                } else {
                    GregTechMod.logger.warn("String for written book too long: " + page);
                }
            } else {
                GregTechMod.logger.warn("Too many pages for written book: " + name);
                break;
            }
        }
        tagList.appendTag(new NBTTagString("\"Credits to " + author + " for writing this Book. This was Book Nr. " + (ordinal+1) + " at its creation. Gotta get 'em all!\""));
        stack.setTagInfo("pages", tagList);
        return stack;
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

    public static String translateScan(String key, Object... parameters) {
        return I18n.format(Reference.MODID + ".scan." + key, parameters);
    }

    public static String translateTeBlockDescription(String key) {
        return translate("teblock." + key + ".description");
    }

    public static String translateInfo(String key, Object... parameters) {
        return translate("info." + key, parameters);
    }

    public static String translateGenericDescription(String key, Object... parameters) {
        return translateGeneric(key + ".description", parameters);
    }

    public static String translateGeneric(String key, Object... parameters) {
        return translate("generic." + key, parameters);
    }

    public static String translateItemDescription(String key, Object... parameters) {
        return translateItem(key + ".description", parameters);
    }

    public static String translateItem(String key, Object... parameters) {
        return translate("item." + key, parameters);
    }

    public static String translate(String key, Object... parameters) {
        return I18n.format(Reference.MODID + "." + key, parameters);
    }

    public static double getTransferLimit(int tier) {
        return Math.pow(2, tier) * 128;
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

    public static <T> List<T> mergeCollection(Collection<T> first, Collection<T> second) {
        return Stream.of(first, second)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static String formatNumber(int num) {
        return INT_FORMAT.format(num);
    }

    public static String formatNumber(double num) {
        if (num % 1 == 0) return INT_FORMAT.format(num);
        else return DECIMAL_FORMAT.format(num);
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

    public static void sendMessage(EntityPlayer player, String message, Object... args) {
        if (!player.world.isRemote) player.sendMessage(new TextComponentTranslation(message, args));
    }

    public static IC2UpgradeType getUpgradeType(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IUpgradeItem) {
            ItemUpgradeModule.UpgradeType upgradeType = ItemUpgradeModule.UpgradeType.values()[stack.getMetadata()];
            String name = upgradeType.name();
            for (IC2UpgradeType type : IC2UpgradeType.values()) {
                if (type.itemType.equals(name)) return type;
            }
        }
        return null;
    }
    
    public static void consumeMultiInput(List<IRecipeIngredient> input, GtSlotProcessableItemStack<?, ?>... slots) {
        Arrays.stream(slots)
                .forEach(slot -> input.forEach(ingredient -> {
                    if (ingredient.apply(slot.get())) slot.consume(ingredient.getCount(), true);
                }));
    }
    
    public static <T> boolean matchCollections(Collection<T> first, Collection<T> second) {
        return first.size() == second.size() || first.containsAll(second) && second.containsAll(first);
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
    
    public static <T> void fillEmptyList(List<T> list, T fill, int maxSize) {
        int space = maxSize - 1 - list.size();
        for (int i = 0; i < space; i++) {
            list.add(fill);
        }
    }
    
    public static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        Iterator<K> keyIter = keys.iterator();
        Iterator<V> valIter = values.iterator();
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(_i -> keyIter.next(), _i -> valIter.next()));
    }
    
    @Nullable
    public static <T extends Enum<T>> T getEnumConstantSafely(Class<T> clazz, String name) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends Comparable<T>> T getStateProperty(IBlockState state, IProperty<T> property) {
        ImmutableMap<IProperty<?>, Comparable<?>> properties = state.getProperties();
        return (T) properties.get(property);
    }
    
    public static void setPrivateStaticValue(Class<?> clazz, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            GregTechMod.logger.catching(e);
        }
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

    public static Path copyDir(Path source, File target) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(source);
            for (Path path : stream) {
                File dest = new File(Paths.get(target.getPath(), path.getFileName().toString()).toUri());
                if (!dest.exists()) {
                    if (path.toString().endsWith("/")) {
                        dest.mkdirs();
                        copyDir(path, dest);
                        continue;
                    }

                    GregTechMod.logger.debug("Copying file " + path + " to " + dest.toPath());
                    BufferedReader in = Files.newBufferedReader(path);
                    FileOutputStream out = new FileOutputStream(dest);
                    for (int i; (i = in.read()) != -1; ) out.write(i);
                    in.close();
                    out.close();
                }
            }
            return target.toPath();
        } catch (IOException e) {
            GregTechMod.logger.catching(e);
            return null;
        }
    }
    
    public static NBTTagList stacksToNBT(Collection<ItemStack> stacks) {
        NBTTagList list = new NBTTagList();
        stacks.stream()
                .map(stack -> {
                    NBTTagCompound tag = new NBTTagCompound();
                    stack.writeToNBT(tag);
                    return tag;
                })
                .forEach(list::appendTag);
        return list;
    }
    
    public static void stacksFromNBT(Collection<ItemStack> stacks, NBTTagList list) {
        list.forEach(tag -> stacks.add(new ItemStack((NBTTagCompound) tag)));
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
