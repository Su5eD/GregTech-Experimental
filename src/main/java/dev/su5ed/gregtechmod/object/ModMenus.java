package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.menu.DestructorPackMenu;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Reference.MODID);

    public static final RegistryObject<MenuType<DestructorPackMenu>> DESTRUCTORPACK = MENU_TYPES.register("destructorpack",
        () -> IForgeMenuType.create((containerId, inv, data) -> new DestructorPackMenu(containerId, data.readEnum(InteractionHand.class), inv)));

    public static void init(IEventBus bus) {
        MENU_TYPES.register(bus);
    }

    private ModMenus() {}
}
