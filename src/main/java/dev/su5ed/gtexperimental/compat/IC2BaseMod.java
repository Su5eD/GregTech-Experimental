package dev.su5ed.gtexperimental.compat;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.machine.ElectricBlockEntity;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.power.IC2EnergyStorage;
import dev.su5ed.gtexperimental.util.power.PowerStorage;
import dev.su5ed.gtexperimental.util.upgrade.IC2UpgradeCapabilityProvider;
import ic2.api.energy.EnergyNet;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.recipe.input.RecipeInputIngredient;
import ic2.core.recipe.v2.RecipeHolder;
import ic2.core.ref.Ic2Items;
import ic2.core.ref.Ic2RecipeSerializers;
import ic2.core.ref.Ic2RecipeTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class IC2BaseMod implements BaseMod {
    @Override
    public boolean isEnergyItem(Item item) {
        return item instanceof IElectricItem;
    }

    @Override
    public double getEnergyCharge(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack);
    }

    @Override
    public double getChargeLevel(ItemStack stack) {
        return ElectricItem.manager.getChargeLevel(stack);
    }

    @Override
    public boolean canUseEnergy(ItemStack stack, double energy) {
        return ElectricItem.manager.canUse(stack, energy);
    }

    @Override
    public boolean useEnergy(ItemStack stack, double energy, @Nullable LivingEntity user) {
        return ElectricItem.manager.use(stack, energy, user);
    }

    @Nullable
    @Override
    public String getEnergyTooltip(ItemStack stack) {
        return ElectricItem.manager.getToolTip(stack);
    }

    @Override
    public void depleteStackEnergy(ItemStack stack) {
        if (stack.getItem() instanceof IElectricItem) {
            ElectricItem.manager.discharge(stack, Double.MAX_VALUE, Integer.MAX_VALUE, true, false, false);
        }
    }

    @Override
    public List<ItemStack> getChargedVariants(Item item) {
        return Arrays.asList(getChargedStack(item, 0), getChargedStack(item, Double.MAX_VALUE));
    }

    @Override
    public ItemStack getChargedStack(Item item, double charge) {
        ItemStack stack = new ItemStack(item);
        chargeStack(stack, charge, Integer.MAX_VALUE, true, false);
        return stack;
    }

    @Override
    public double chargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        return ElectricItem.manager.charge(stack, amount, tier, ignoreTransferLimit, simulate);
    }

    @Override
    public double dischargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        return ElectricItem.manager.discharge(stack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    @Override
    public double getEnergyFromTier(int tier) {
        return EnergyNet.instance.getPowerFromTier(tier);
    }

    @Override
    public <T extends BaseBlockEntity & ElectricBlockEntity> PowerStorage createEnergyProvider(T parent) {
        return new IC2EnergyStorage<>(parent);
    }

    public static ItemStack getFilledFuelCan(int value) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("value", value);
        return new ItemStack(Ic2Items.FILLED_FUEL_CAN, 1, tag);
    }

    public static void initRecyclerBlackList() {
        Recipes.recyclerBlacklist.add(new RecipeInputIngredient(Ingredient.of(GregTechTags.SMALL_DUSTS), 1));
        Recipes.recyclerBlacklist.add(new RecipeInputIngredient(Ingredient.of(Tags.Items.NUGGETS), 1));
    }

    public static Recipe<?> generateCompressorRecipe(String name, Ingredient input, int count, ItemStack stack) {
        MachineRecipe<IRecipeInput, Collection<ItemStack>> compressorRecipe = new MachineRecipe<>(new RecipeInputIngredient(input, count), List.of(stack));
        return new RecipeHolder<>(compressorRecipe, location("generated", "ic2", "compressor", name), Ic2RecipeSerializers.COMPRESSOR, Ic2RecipeTypes.COMPRESSOR);
    }

    public static Recipe<?> generateMaceratorRecipe(TagKey<Item> input, int count, ItemStack stack) {
        MachineRecipe<IRecipeInput, Collection<ItemStack>> maceratorRecipe = new MachineRecipe<>(new RecipeInputIngredient(Ingredient.of(input), count), List.of(stack));
        return new RecipeHolder<>(maceratorRecipe, location("generated", "ic2", "macerator", GtUtil.tagName(input) + "_to_" + GtUtil.itemName(stack)), Ic2RecipeSerializers.MACERATOR, Ic2RecipeTypes.MACERATOR);
    }

    public static <C extends Container, T extends Recipe<C>> boolean compressorRecipeExists(RecipeManager manager, ItemStack stack) {
        return recipeExists(manager, Ic2RecipeTypes.COMPRESSOR, stack);
    }

    public static <C extends Container, T extends Recipe<C>> boolean maceratorRecipeExists(RecipeManager manager, TagKey<Item> input) {
        return recipeExists(manager, Ic2RecipeTypes.MACERATOR, input);
    }

    private static <C extends Container, T extends Recipe<C>> boolean recipeExists(RecipeManager manager, RecipeType<T> type, TagKey<Item> input) {
        return RecipeUtil.findFirstItem(input)
            .map(item -> recipeExists(manager, type, new ItemStack(item)))
            .orElse(false);
    }

    @SuppressWarnings("unchecked")
    private static <C extends Container, T extends Recipe<C>> boolean recipeExists(RecipeManager manager, RecipeType<T> type, ItemStack stack) {
        return manager.getAllRecipesFor(type).stream()
            .anyMatch(holder -> {
                try {
                    return ((RecipeHolder<IRecipeInput, ?>) holder).recipe().getInput().getIngredient().test(stack);
                } catch (Throwable t) {
                    return false;
                }
            });
    }

    private static void onItemStackCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.getItem() instanceof IUpgradeItem upgrade) {
            IC2UpgradeCapabilityProvider.IC2UpgradeAdapter.of(stack, upgrade)
                .map(adapter -> new IC2UpgradeCapabilityProvider(stack, adapter))
                .ifPresent(provider -> event.addCapability(IC2UpgradeCapabilityProvider.NAME, provider));
        }
    }

    @EventBusSubscriber
    public static class Provider implements BaseMod.Provider {
        @Override
        public String getModid() {
            return ModHandler.IC2_MODID;
        }

        @Override
        public String mapItemName(String name) {
            return name;
        }

        @Override
        public BaseMod createBaseMod() {
            return new IC2BaseMod();
        }

        @SubscribeEvent
        public static void onItemStackCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
            if (ModHandler.ic2Loaded) {
                IC2BaseMod.onItemStackCapabilityAttach(event);
            }
        }
    }
}
