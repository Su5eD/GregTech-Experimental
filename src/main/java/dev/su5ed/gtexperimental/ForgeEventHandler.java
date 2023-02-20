package dev.su5ed.gtexperimental;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.item.ElectricArmorItem;
import dev.su5ed.gtexperimental.recipe.crafting.RecipeProcessor;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import dev.su5ed.gtexperimental.util.capability.JumpChargeProvider;
import dev.su5ed.gtexperimental.util.capability.LightSourceProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Reference.MODID)
public final class ForgeEventHandler {
    private static final List<Runnable> recipeReplaceListeners = new ArrayList<>();

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

    @SubscribeEvent
    public static void resourceReload(AddReloadListenerEvent event) {
        recipeReplaceListeners.add(() -> new RecipeProcessor(event.getServerResources()).run());
        event.addListener(ModRecipeManagers.RecipeReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public static void tagsUpdated(TagsUpdatedEvent event) {
        recipeReplaceListeners.forEach(Runnable::run);
        recipeReplaceListeners.clear();
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
