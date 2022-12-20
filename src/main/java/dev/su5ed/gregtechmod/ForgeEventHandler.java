package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.item.ElectricArmorItem;
import dev.su5ed.gregtechmod.util.capability.JumpChargeProvider;
import dev.su5ed.gregtechmod.util.capability.LightSourceProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MODID)
public final class ForgeEventHandler {

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.level.isClientSide && event.getFrom().getItem() instanceof ElectricArmorItem armor && !(event.getTo().getItem() instanceof ElectricArmorItem)) {
            armor.getPerks().forEach(perk -> perk.unequip(entity));
        }
    }

    @SubscribeEvent
    public static void onEntityCapabilityAttach(AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(JumpChargeProvider.NAME, new JumpChargeProvider());
        event.addCapability(LightSourceProvider.NAME, new LightSourceProvider());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player clone = event.getEntity();
        if (!clone.level.isClientSide && event.isWasDeath()) {
            cloneCapability(Capabilities.JUMP_CHARGE, original, clone);
            cloneCapability(Capabilities.LIGHT_SOURCE, original, clone);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        entity.getCapability(Capabilities.JUMP_CHARGE)
            .ifPresent(props -> props.setCharge(0));
        entity.getCapability(Capabilities.LIGHT_SOURCE)
            .ifPresent(props -> props.setSourcePos(null));
    }

    private static <T extends INBTSerializable<CompoundTag>> void cloneCapability(Capability<T> capability, Player original, Player clone) {
        original.getCapability(capability)
            .ifPresent(props -> {
                CompoundTag old = props.serializeNBT();
                clone.getCapability(capability)
                    .ifPresent(fatigue -> fatigue.deserializeNBT(old));
            });
    }

    private ForgeEventHandler() {}
}
