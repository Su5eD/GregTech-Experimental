package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.api.item.ExhaustingItem;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.ModObjects;
import dev.su5ed.gregtechmod.util.ArmorPerk;
import dev.su5ed.gregtechmod.util.GtLocale;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.api.item.IMetalArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Reference.MODID)
public class ElectricArmorItem extends ArmorItem implements ExhaustingItem, IElectricItem, IItemHudInfo, IMetalArmor {
    private final double maxCharge;
    private final double transferLimit;
    private final int energyTier;
    private final boolean providesEnergy;
    
    private final Set<ArmorPerk> perks;
    private final double damageEnergyCost;

    public ElectricArmorItem(ArmorMaterial material, EquipmentSlot slot, ElectricArmorItemProperties properties) {
        super(material, slot, properties.properties
            .setNoRepair()
            .stacksTo(1));

        this.maxCharge = properties.maxCharge;
        this.transferLimit = properties.transferLimit;
        this.energyTier = properties.energyTier;
        this.providesEnergy = properties.providesEnergy;
        this.perks = Collections.unmodifiableSet(properties.perks);
        this.damageEnergyCost = properties.damageEnergyCost;
    }

    public boolean hasPerk(ArmorPerk perk) {
        return this.perks.contains(perk);
    }

    public Set<ArmorPerk> getPerks() {
        return this.perks;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (allowedIn(category) && !hasPerk(ArmorPerk.INFINITE_CHARGE)) {
            items.addAll(ModHandler.getChargedVariants(this));
        }
        else {
            super.fillItemCategory(category, items);
        }
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return checkCharge(stack, this.providesEnergy);
    }

    @Override
    public double getMaxCharge(ItemStack stack) {
        return checkCharge(stack, this.maxCharge);
    }

    @Override
    public int getTier(ItemStack stack) {
        return checkCharge(stack, this.energyTier);
    }

    @Override
    public double getTransferLimit(ItemStack stack) {
        return checkCharge(stack, this.transferLimit);
    }

    @Override
    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        return List.of(
            ModHandler.getEnergyTooltip(stack),
            GtLocale.key("info", "energy_tier").toComponent(this.energyTier).getString()
        );
    }

    @Override
    public boolean isMetalArmor(ItemStack stack, Player player) {
        return true;
    }
    
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return Reference.MODID + ":textures/armor/" + ForgeRegistries.ITEMS.getKey(this).getPath() + ".png";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        components.add(GtLocale.key("info", "energy_tier").toComponent(this.energyTier));
        this.perks.stream()
            .map(perk -> perk.getTranslationKey().withStyle(ChatFormatting.GRAY))
            .forEach(components::add);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        Inventory inventory = player.getInventory();
        ItemStack armor = inventory.getArmor(this.slot.getIndex());
        if (!armor.isEmpty()) {
            ItemStack stack = player.getItemInHand(hand);
            for (int i = 0; i < 9; i++) {
                ItemStack inventoryStack = inventory.getItem(i);
                if (inventoryStack == stack) {
                    inventory.armor.set(this.slot.getIndex(), inventoryStack);
                    inventory.items.set(i, armor);
                    return InteractionResultHolder.success(armor);
                }
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            for (ArmorPerk perk : this.perks) {
                perk.tick(stack, serverPlayer, this);
            }
        }
    }
    
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ModHandler.getEnergyCharge(stack) < this.maxCharge;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) Math.round(ModHandler.getChargeLevel(stack) * 13.0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb((float) (ModHandler.getChargeLevel(stack) / 3.0), 1, 1);
    }

    @SubscribeEvent
    public static void onEntityLivingFall(LivingFallEvent event) {
        Entity entity = event.getEntity();
        if (!entity.level.isClientSide && entity instanceof Player player) {
            for (ItemStack stack : player.getArmorSlots()) {
                if (stack.getItem() instanceof ElectricArmorItem armor && armor.hasPerk(ArmorPerk.INERTIA_DAMPER)) {
                    int distance = (int) (event.getDistance() - 3);
                    double cost = armor.damageEnergyCost * distance / 4;
                    if (cost <= ModHandler.dischargeStack(stack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, true)) {
                        ModHandler.dischargeStack(stack, cost, Integer.MAX_VALUE, true, false, false);
                        event.setCanceled(true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldExhaust(boolean isArmor) {
        return isArmor && !this.perks.isEmpty();
    }

    private <T> T checkCharge(ItemStack stack, T ret) {
        if (hasPerk(ArmorPerk.INFINITE_CHARGE)) {
            stack.getOrCreateTag().putDouble("charge", 1000000000);
        }
        return ret;
    }

    public static class ElectricArmorItemProperties {
        private final Properties properties;

        private double maxCharge;
        private double transferLimit;
        private int energyTier;
        private boolean providesEnergy;
        private final Set<ArmorPerk> perks = new HashSet<>();
        private double damageEnergyCost;

        public ElectricArmorItemProperties() {
            this(ModObjects.itemProperties());
        }

        public ElectricArmorItemProperties(Properties properties) {
            this.properties = properties;
        }

        public ElectricArmorItemProperties maxCharge(double maxCharge) {
            this.maxCharge = maxCharge;
            return this;
        }

        public ElectricArmorItemProperties transferLimit(double transferLimit) {
            this.transferLimit = transferLimit;
            return this;
        }

        public ElectricArmorItemProperties energyTier(int energyTier) {
            this.energyTier = energyTier;
            return this;
        }

        public ElectricArmorItemProperties providesEnergy(boolean providesEnergy) {
            this.providesEnergy = providesEnergy;
            return this;
        }
        
        public ElectricArmorItemProperties perks(ArmorPerk... perks) {
            Collections.addAll(this.perks, perks);
            return this;
        }
        
        public ElectricArmorItemProperties rarity(Rarity rarity) {
            this.properties.rarity(rarity);
            return this;
        }
        
        public ElectricArmorItemProperties damageEnergyCost(double damageEnergyCost) {
            this.damageEnergyCost = damageEnergyCost;
            return this;
        }
    }
}
