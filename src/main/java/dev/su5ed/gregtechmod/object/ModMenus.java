package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.menu.BlockEntityMenu;
import dev.su5ed.gregtechmod.menu.DestructorPackMenu;
import dev.su5ed.gregtechmod.menu.SonictronMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
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

    public static final RegistryObject<MenuType<SonictronMenu>> SONICTRON = register("sonictron", SonictronMenu::new);

    public static void init(IEventBus bus) {
        MENU_TYPES.register(bus);
    }

    private static <T extends BlockEntityMenu<?>> RegistryObject<MenuType<T>> register(String name, BlockEntityMenuConstructor<T> factory) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create((windowId, inv, data) -> factory.create(windowId, data.readBlockPos(), inv.player, inv)));
    }

    interface BlockEntityMenuConstructor<T extends BlockEntityMenu<?>> {
        T create(int containerId, BlockPos pos, Player player, Inventory playerInventory);
    }

    private ModMenus() {}
}
