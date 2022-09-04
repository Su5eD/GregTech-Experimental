package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.container.DestructorPackContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModContainers {
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MODID);

    public static final RegistryObject<MenuType<DestructorPackContainer>> DESTRUCTORPACK = CONTAINERS.register("destructorpack",
        () -> IForgeMenuType.create((containerId, inv, data) -> new DestructorPackContainer(containerId, data.readEnum(InteractionHand.class), inv)));

    public static void init(IEventBus bus) {
        CONTAINERS.register(bus);
    }

    private ModContainers() {}
}
