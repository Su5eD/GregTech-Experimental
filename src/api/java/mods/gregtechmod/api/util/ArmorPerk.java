package mods.gregtechmod.api.util;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.util.Keys;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.item.IElectricArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public enum ArmorPerk {
    rebreather((stack, player, armor) -> {
        if (!player.world.isRemote) {
            int air = player.getAir();
            if (ElectricItem.manager.canUse(stack, 1000) && air < 50) {
                player.setAir(air + 250);
                ElectricItem.manager.use(stack, 1000, player);
            }
        }
    }),
    inertia_damper((stack, player, armor) -> {}),
    food_replicator((stack, player, armor) -> {
        if (!player.world.isRemote) {
            if (ElectricItem.manager.canUse(stack, 50000) && player.getFoodStats().needFood()) {
                player.getFoodStats().addStats(1, 0.0F);
                ElectricItem.manager.use(stack, 50000, player);
            }
        }
    }),
    medicine_module((stack, player, armor) -> {
        if (ElectricItem.manager.canUse(stack, 10000) && player.isPotionActive(Potion.getPotionById(19))) {
            player.removeActivePotionEffect(Potion.getPotionById(19));
            ElectricItem.manager.use(stack, 10000, player);
        }
        if (ElectricItem.manager.canUse(stack, 100000) && player.isPotionActive(Potion.getPotionById(20))) {
            player.removeActivePotionEffect(Potion.getPotionById(20));
            ElectricItem.manager.use(stack, 100000, player);
        }
    }),
    lamp(ArmorPerk::tickLampOrSolarPanel),
    solarpanel(ArmorPerk::tickLampOrSolarPanel),
    extinguisher_module((stack, player, armor) -> {
        if (player.isBurning())
            player.extinguish();
    }),
    jump_booster((stack, player, armor) -> {
        float charge = armor.getJumpChargeMap().getOrDefault(player, 1.0F);

        if (ElectricItem.manager.canUse(stack, 1000) && player.onGround && charge < 1.0F) {
            charge = 1.0F;
            ElectricItem.manager.use(stack, 1000, player);
        }

        if (player.motionY >= 0.0D && charge > 0.0F && !player.isInWater()) {
            if (Keys.instance.isJumpKeyDown(player) && Keys.instance.isBoostKeyDown(player)) {
                if (charge == 1.0F) {
                    player.motionX *= 3.5D;
                    player.motionZ *= 3.5D;
                }

                player.motionY += charge * 0.3F;
                charge *= 0.75F;
            } else if (charge < 1.0F) {
                charge = 0.0F;
            }
        }

        armor.getJumpChargeMap().put(player, charge);
    }),
    speed_booster((stack, player, armor) -> {
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

            player.moveRelative(0.0F, 0.0F, 1.0F, speed);
        }
    }),
    invisibility_field((stack, player, armor) -> {
        if (ElectricItem.manager.canUse(stack, 10000)) {
            ElectricItem.manager.use(stack, 10000, player);
            player.addPotionEffect(new PotionEffect(Potion.getPotionById(14), 25, 1, false, false));
        }
    }),
    infinite_charge((stack, player, armor) -> {});

    public final TriConsumer<ItemStack, EntityPlayer, IElectricArmor> onTick;

    ArmorPerk(TriConsumer<ItemStack, EntityPlayer, IElectricArmor> onTick) {
        this.onTick = onTick;
    }

    private static void tickLampOrSolarPanel(ItemStack stack, EntityPlayer player, IElectricArmor armor) {
        if (!player.world.isRemote) {
            if (player.world.getWorldTime()%20==0) {
                ItemStack targetChargeItem = stack;
                ItemStack targetDechargeItem = stack;

                if (ElectricItem.manager.charge(targetChargeItem, 1, Integer.MAX_VALUE, true, true) < 1) {
                    targetChargeItem = player.inventory.armorInventory.get(armor.getSlot().getIndex());
                }
                if (ElectricItem.manager.discharge(targetDechargeItem, 10, Integer.MAX_VALUE, true, true, true) < 10) {
                    targetDechargeItem = player.inventory.armorInventory.get(armor.getSlot().getIndex());
                }

                if (targetChargeItem == ItemStack.EMPTY || !(stack.getItem() instanceof IElectricItem)) {
                    targetChargeItem = null;
                }
                if (targetDechargeItem == ItemStack.EMPTY || !(stack.getItem() instanceof IElectricItem) || !(stack == targetDechargeItem || (targetDechargeItem.getItem() instanceof IElectricItem && ((IElectricItem)targetDechargeItem.getItem()).canProvideEnergy(targetDechargeItem)))) {
                    targetDechargeItem = null;
                }

                if (player.world.isDaytime() && player.world.canSeeSky(player.getPosition().up())) {
                    if (armor.getPerks().contains(ArmorPerk.solarpanel) && targetChargeItem != null) {
                        ElectricItem.manager.charge(targetChargeItem, 20, Integer.MAX_VALUE, true, false);
                    }
                } else {
                    if (armor.getPerks().contains(ArmorPerk.lamp) && targetDechargeItem != null && ElectricItem.manager.canUse(targetDechargeItem, 10)) {
                        if (player.world.getBlockState(player.getPosition().up()).getBlock() == Blocks.AIR)
                            player.world.setBlockState(player.getPosition().up(), BlockItems.lightSource.getDefaultState());
                        ElectricItem.manager.use(targetDechargeItem, 10, player);
                    }
                }
            }
        }
    }
}
