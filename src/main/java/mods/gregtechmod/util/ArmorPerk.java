package mods.gregtechmod.util;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.util.Keys;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.TriConsumer;
import mods.gregtechmod.objects.items.base.ItemArmorElectricBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

public enum ArmorPerk {
    REBREATHER((stack, player, armor) -> {
        if (!player.world.isRemote) {
            int air = player.getAir();
            if (ElectricItem.manager.canUse(stack, 1000) && air < 50) {
                player.setAir(air + 250);
                ElectricItem.manager.use(stack, 1000, player);
            }
        }
    }),
    INERTIA_DAMPER((stack, player, armor) -> {}),
    FOOD_REPLICATOR((stack, player, armor) -> {
        if (!player.world.isRemote) {
            if (ElectricItem.manager.canUse(stack, 50000) && player.getFoodStats().needFood()) {
                player.getFoodStats().addStats(1, 0);
                ElectricItem.manager.use(stack, 50000, player);
            }
        }
    }),
    MEDICINE_MODULE((stack, player, armor) -> {
        if (ElectricItem.manager.canUse(stack, 10000) && player.isPotionActive(MobEffects.POISON)) {
            player.removeActivePotionEffect(Potion.getPotionById(19));
            ElectricItem.manager.use(stack, 10000, player);
        }
        if (ElectricItem.manager.canUse(stack, 100000) && player.isPotionActive(MobEffects.WITHER)) {
            player.removeActivePotionEffect(Potion.getPotionById(20));
            ElectricItem.manager.use(stack, 100000, player);
        }
    }),
    LAMP(ArmorPerk::tickLampOrSolarPanel),
    SOLARPANEL(ArmorPerk::tickLampOrSolarPanel),
    EXTINGUISHER_MODULE((stack, player, armor) -> {
        if (player.isBurning())
            player.extinguish();
    }),
    JUMP_BOOSTER((stack, player, armor) -> {
        float charge = ItemArmorElectricBase.jumpChargeMap.getOrDefault(player, 1F);

        if (ElectricItem.manager.canUse(stack, 1000) && player.onGround && charge < 1) {
            charge = 1;
            ElectricItem.manager.use(stack, 1000, player);
        }

        if (player.motionY >= 0 && charge > 0 && !player.isInWater()) {
            if (Keys.instance.isJumpKeyDown(player) && Keys.instance.isBoostKeyDown(player)) {
                if (charge == 1) {
                    player.motionX *= 3.5D;
                    player.motionZ *= 3.5D;
                }

                player.motionY += charge * 0.3F;
                charge *= 0.75F;
            }
            else if (charge < 1) {
                charge = 0;
            }
        }

        ItemArmorElectricBase.jumpChargeMap.put(player, charge);
    }),
    SPEED_BOOSTER((stack, player, armor) -> {
        if (ElectricItem.manager.canUse(stack, 100) && player.isSprinting() && (player.onGround && Math.abs(player.motionX) + Math.abs(player.motionZ) > 0.10000000149011612D || player.isInWater())) {
            ElectricItem.manager.use(stack, 100, player);
            float speed = 0.22F;

            if (player.isInWater()) {
                ElectricItem.manager.use(stack, 100, player);
                speed = 0.1F;

                if (Keys.instance.isJumpKeyDown(player)) {
                    player.motionY += 0.10000000149011612D;
                }
            }

            player.moveRelative(0, 0, 1, speed);
        }
    }),
    INVISIBILITY_FIELD((stack, player, armor) -> {
        if (ElectricItem.manager.canUse(stack, 10000)) {
            ElectricItem.manager.use(stack, 10000, player);
            player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 25, 1, false, false));
        }
    }),
    INFINITE_CHARGE((stack, player, armor) -> {});

    public final TriConsumer<ItemStack, EntityPlayer, ItemArmorElectricBase> onTick;

    ArmorPerk(TriConsumer<ItemStack, EntityPlayer, ItemArmorElectricBase> onTick) {
        this.onTick = onTick;
    }

    private static void tickLampOrSolarPanel(ItemStack stack, EntityPlayer player, ItemArmorElectricBase armor) {
        if (!player.world.isRemote && player.world.getTotalWorldTime() % 20 == 0) {
            ItemStack targetChargeItem = stack;
            ItemStack targetDechargeItem = stack;

            if (ElectricItem.manager.charge(targetChargeItem, 1, Integer.MAX_VALUE, true, true) < 1) {
                targetChargeItem = player.inventory.armorInventory.get(armor.armorType.getIndex());
            }
            if (ElectricItem.manager.discharge(targetDechargeItem, 10, Integer.MAX_VALUE, true, true, true) < 10) {
                targetDechargeItem = player.inventory.armorInventory.get(armor.armorType.getIndex());
            }

            if (targetChargeItem.isEmpty() || !(stack.getItem() instanceof IElectricItem)) {
                targetChargeItem = null;
            }
            if (targetDechargeItem.isEmpty() || !(stack.getItem() instanceof IElectricItem) || !(stack == targetDechargeItem || targetDechargeItem.getItem() instanceof IElectricItem && ((IElectricItem) targetDechargeItem.getItem()).canProvideEnergy(targetDechargeItem))) {
                targetDechargeItem = null;
            }

            if (player.world.isDaytime() && player.world.canSeeSky(player.getPosition().up())) {
                if (armor.perks.contains(ArmorPerk.SOLARPANEL) && targetChargeItem != null) {
                    ElectricItem.manager.charge(targetChargeItem, 20, Integer.MAX_VALUE, true, false);
                }
            }
            else {
                if (armor.perks.contains(ArmorPerk.LAMP) && targetDechargeItem != null && ElectricItem.manager.canUse(targetDechargeItem, 10)) {
                    BlockPos up = player.getPosition().up();
                    if (player.world.getBlockState(up).getBlock() == Blocks.AIR) {
                        player.world.setBlockState(up, GregTechObjectAPI.getBlock("light_source").getDefaultState());
                    }
                    ElectricItem.manager.use(targetDechargeItem, 10, player);
                }
            }
        }
    }
}
