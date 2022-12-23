package dev.su5ed.gregtechmod.util;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.item.ElectricArmorItem;
import dev.su5ed.gregtechmod.object.ModObjects;
import dev.su5ed.gregtechmod.util.KeyboardHandler.Key;
import dev.su5ed.gregtechmod.util.capability.LightSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;
import java.util.function.Consumer;

public enum ArmorPerk {
    REBREATHER((stack, player, armor) -> {
        int air = player.getAirSupply();
        if (ModHandler.canUseEnergy(stack, 1000) && air < 50) {
            player.setAirSupply(air + 250);
            ModHandler.useEnergy(stack, 1000, player);
        }
    }),
    INERTIA_DAMPER((stack, player, armor) -> {}),
    FOOD_REPLICATOR((stack, player, armor) -> {
        if (ModHandler.canUseEnergy(stack, 50000) && player.getFoodData().needsFood()) {
            player.getFoodData().eat(1, 0);
            ModHandler.useEnergy(stack, 50000, player);
        }
    }),
    MEDICINE_MODULE((stack, player, armor) -> {
        if (ModHandler.canUseEnergy(stack, 10000) && player.hasEffect(MobEffects.POISON)) {
            player.removeEffect(MobEffects.POISON);
            ModHandler.useEnergy(stack, 10000, player);
        }
        if (ModHandler.canUseEnergy(stack, 100000) && player.hasEffect(MobEffects.WITHER)) {
            player.removeEffect(MobEffects.WITHER);
            ModHandler.useEnergy(stack, 100000, player);
        }
    }),
    LAMP((stack, player, armor) -> {
        player.getCapability(Capabilities.LIGHT_SOURCE)
            .ifPresent(props -> {
                if (ModHandler.canUseEnergy(stack, 0.5)) {
                    BlockPos pos = props.getSourcePos();
                    BlockPos headPos = new BlockPos(player.getEyePosition());

                    if (pos != null) {
                        if (!pos.closerThan(headPos, 1)) {
                            player.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                            if (!placeLightSource(props, player, stack, headPos)) {
                                props.setSourcePos(null);
                            }
                        }
                        else if (player.level.getBlockState(pos).is(ModObjects.LIGHT_SOURCE_BLOCK.get())) {
                            ModHandler.useEnergy(stack, 0.5, player);
                        }
                        else {
                            props.setSourcePos(null);
                        }
                    }
                    else {
                        placeLightSource(props, player, stack, headPos);
                    }
                }
                else {
                    removeLightSource(props, player);
                }
            });
    }, entity -> entity.getCapability(Capabilities.LIGHT_SOURCE).ifPresent(props -> removeLightSource(props, entity))),
    SOLARPANEL((stack, player, armor) -> {
        if (player.level.getGameTime() % 20 == 0 && player.level.isDay() && player.level.canSeeSky(player.blockPosition().above())) {
            ModHandler.chargeStack(stack, 20, Integer.MAX_VALUE, true, false);
        }
    }),
    EXTINGUISHER_MODULE((stack, player, armor) -> {
        if (!player.level.isClientSide && player.isOnFire()) {
            player.clearFire();
        }
    }),
    JUMP_BOOSTER((stack, player, armor) -> player.getCapability(Capabilities.JUMP_CHARGE).ifPresent(props -> {
        double charge = props.getCharge();

        if (ModHandler.canUseEnergy(stack, 1000) && player.isOnGround() && charge < 1) {
            charge = 1;
            ModHandler.useEnergy(stack, 1000, player);
        }

        Vec3 delta = player.getDeltaMovement();
        if (delta.y() >= 0 && charge > 0 && !player.isInWater()) {
            if (KeyboardHandler.isPressed(player, Key.JUMP) && KeyboardHandler.isPressed(player, Key.SPRINT)) {
                if (charge == 1) {
                    delta = delta.multiply(1.5, 1, 1.5);
                }

                delta = delta.add(0, charge * 0.13, 0);
                player.setDeltaMovement(delta);
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
                charge *= 0.75F;
            }
            else if (charge < 1) {
                charge = 0;
            }
        }

        props.setCharge(charge);
    })),
    SPEED_BOOSTER((stack, player, armor) -> {
        Vec3 delta = player.getDeltaMovement();
        if (ModHandler.canUseEnergy(stack, 100) && player.isSprinting() && (player.isOnGround() || player.isInWater())) {
            ModHandler.useEnergy(stack, 100, player);
            float speed = 0.22F;

            if (player.isInWater()) {
                ModHandler.useEnergy(stack, 100, player);
                speed = 0.1F;

                if (KeyboardHandler.isPressed(player, Key.JUMP)) {
                    player.setDeltaMovement(delta.add(0, 0.10000000149011612D, 0));
                }
            }

            player.moveRelative(speed, new Vec3(0, 0, 1));
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }),
    INVISIBILITY_FIELD((stack, player, armor) -> {
        if (!player.level.isClientSide && ModHandler.canUseEnergy(stack, 10000)) {
            ModHandler.useEnergy(stack, 10000, player);
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 25, 1, false, false));
        }
    }),
    INFINITE_CHARGE((stack, player, armor) -> {});

    private final TriConsumer<ItemStack, ServerPlayer, ElectricArmorItem> onTick;
    private final Consumer<LivingEntity> onUnequipped;
    private final MutableComponent translationKey;

    ArmorPerk(TriConsumer<ItemStack, ServerPlayer, ElectricArmorItem> onTick) {
        this(onTick, player -> {});
    }
    
    ArmorPerk(TriConsumer<ItemStack, ServerPlayer, ElectricArmorItem> onTick, Consumer<LivingEntity> onUnequipped) {
        this.onTick = onTick;
        this.onUnequipped = onUnequipped;
        this.translationKey = GtLocale.key("perk", name().toLowerCase(Locale.ROOT)).toComponent();
    }
    
    public void tick(ItemStack stack, ServerPlayer player, ElectricArmorItem item) {
        this.onTick.accept(stack, player, item);
    }
    
    public void unequip(LivingEntity entity) {
        this.onUnequipped.accept(entity);
    }

    public MutableComponent getTranslationKey() {
        return this.translationKey;
    }
    
    private static boolean placeLightSource(LightSource props, Player player, ItemStack stack, BlockPos headPos) {
        BlockState headPosState = player.level.getBlockState(headPos);
        if (headPosState.isAir() && player.level.setBlockAndUpdate(headPos, ModObjects.LIGHT_SOURCE_BLOCK.get().defaultBlockState())) {
            props.setSourcePos(headPos);
            ModHandler.useEnergy(stack, 0.5, player);
            return true;
        }
        return false;
    }
    
    private static void removeLightSource(LightSource props, LivingEntity entity) {
        BlockPos pos = props.getSourcePos();
        if (pos != null && entity.level.getBlockState(pos).is(ModObjects.LIGHT_SOURCE_BLOCK.get())) {
            entity.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        props.setSourcePos(null);
    }
}
