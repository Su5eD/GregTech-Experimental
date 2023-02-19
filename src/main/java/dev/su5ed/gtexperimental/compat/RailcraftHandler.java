package dev.su5ed.gtexperimental.compat;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.item.TurbineRotorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = Reference.MODID)
public final class RailcraftHandler {
    private static final RegistryObject<Item> RC_TURBINE_ROTOR = RegistryObject.create(new ResourceLocation(ModHandler.RAILCRAFT_MODID, "turbine_rotor"), ForgeRegistries.ITEMS);
    private static final ICapabilityProvider RC_TURBINE_ROTOR_PROVIDER = new TurbineRotorItem.TurbineRotorHandler(80, 20, 2);

    @SubscribeEvent
    public static void onItemCapabilitiesAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (!stack.isEmpty() && stack.is(RC_TURBINE_ROTOR.orElse(null))) {
            event.addCapability(TurbineRotorItem.TurbineRotorHandler.NAME, RC_TURBINE_ROTOR_PROVIDER);
        }
    }

    public static void registerBoilerFuels() {
        // TODO
    }

    private RailcraftHandler() {}
}
