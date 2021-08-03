package mods.gregtechmod.objects.items.base;

import ic2.api.item.ElectricItem;
import ic2.core.item.armor.ItemArmorElectric;
import mods.gregtechmod.api.item.IElectricArmor;
import mods.gregtechmod.api.util.ArmorPerk;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class ItemArmorElectricBase extends ItemArmorElectric implements IModelInfoProvider, IElectricArmor {
    private final String name;
    private String folder;
    private final Set<ArmorPerk> perks;
    public static Map<EntityPlayer, Float> jumpChargeMap = new HashMap<>();
    public final boolean chargeProvider;
    public final double absorbtionPercentage;
    public final int damageEnergyCost;

    public ItemArmorElectricBase(String name, EntityEquipmentSlot slot, int maxCharge, int transferLimit, int tier, int damageEnergyCost, double absorbtionPercentage, boolean chargeProvider, ArmorPerk... perks) {
        super(null, null, slot, maxCharge, transferLimit, tier);
        this.name = name;
        this.perks = new HashSet<>(Arrays.asList(perks));
        this.chargeProvider = chargeProvider;
        this.absorbtionPercentage = absorbtionPercentage;
        this.damageEnergyCost = damageEnergyCost;

        MinecraftForge.EVENT_BUS.register(this);
    }

    public Item setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID + ".armor." + this.name + ".name";
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Reference.MODID + ":textures/armor/" + this.name + ".png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(GtUtil.translateInfo("tier", this.tier));
        this.perks.stream()
                .map(perk -> "armor.perk." + perk.name().toLowerCase(Locale.ROOT) + ".name")
                .map(GtUtil::translateItem)
                .forEach(tooltip::add);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
        ItemStack armor = player.inventory.armorInventory.get(armorType.getIndex());
        if (!armor.isEmpty()) {
            for (int i = 0; i < 9; i++) {
                if (player.inventory.mainInventory.get(i) == player.inventory.getCurrentItem()) {
                    player.inventory.armorInventory.set(armorType.getSlotIndex(), player.inventory.mainInventory.get(i));
                    player.inventory.mainInventory.set(i, armor);
                    return ActionResult.newResult(EnumActionResult.SUCCESS, armor);
                }
            }
        }
        return super.onItemRightClick(worldIn, player, handIn);
    }

    private void setCharge(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) nbt = new NBTTagCompound();
        nbt.setInteger("charge", 1000000000);
        stack.setTagCompound(nbt);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (this.perks.isEmpty()) return;

        for (ArmorPerk perk : this.perks) perk.onTick.accept(stack, player, this);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        if (this.perks.contains(ArmorPerk.INFINITE_CHARGE)) setCharge(stack);
        return this.chargeProvider;
    }

    @Override
    public double getMaxCharge(ItemStack stack) {
        if (this.perks.contains(ArmorPerk.INFINITE_CHARGE)) setCharge(stack);
        return this.maxCharge;
    }

    @Override
    public int getTier(ItemStack stack) {
        if (this.perks.contains(ArmorPerk.INFINITE_CHARGE)) setCharge(stack);
        return this.tier;
    }

    @Override
    public double getTransferLimit(ItemStack stack) {
        if (this.perks.contains(ArmorPerk.INFINITE_CHARGE)) setCharge(stack);
        return this.transferLimit;
    }

    @Override
    public boolean isMetalArmor(ItemStack itemstack, EntityPlayer player) {
        return true;
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return this.absorbtionPercentage;
    }

    @Override
    public int getEnergyPerDamage() {
        return this.damageEnergyCost;
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation(this.name, this.folder));
    }

    @SubscribeEvent
    public void onEntityLivingFall(LivingFallEvent event) {
        Entity entity = event.getEntity();
        if (!entity.world.isRemote && entity instanceof EntityPlayer) {
            for (int i = 0; i < 4; i++) {
                ItemStack armor = ((EntityPlayer) entity).inventory.armorInventory.get(i);
                if (!armor.isEmpty() && armor.getItem() == this && this.perks.contains(ArmorPerk.INERTIA_DAMPER)) {
                    int distance = (int) (event.getDistance() - 3);
                    int cost = this.damageEnergyCost * distance / 4;
                    if (cost <= ElectricItem.manager.discharge(armor, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, true)) {
                        ElectricItem.manager.discharge(armor, cost, Integer.MAX_VALUE, true, false, true);
                        event.setCanceled(true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(source == DamageSource.FALL && this.perks.contains(ArmorPerk.INERTIA_DAMPER) ? 10 : 0, getBaseAbsorptionRatio() * this.absorbtionPercentage, this.damageEnergyCost > 0 ? (int) (25 * ElectricItem.manager.discharge(armor, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, true) / this.damageEnergyCost) : 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) Math.round(20D * getBaseAbsorptionRatio() * this.absorbtionPercentage);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        ElectricItem.manager.discharge(stack, damage * this.damageEnergyCost, Integer.MAX_VALUE, true, false, true);
    }

    @Override
    public EntityEquipmentSlot getSlot() {
        return this.armorType;
    }

    @Override
    public Collection<ArmorPerk> getPerks() {
        return this.perks;
    }

    @Override
    public Map<EntityPlayer, Float> getJumpChargeMap() {
        return jumpChargeMap;
    }
}
