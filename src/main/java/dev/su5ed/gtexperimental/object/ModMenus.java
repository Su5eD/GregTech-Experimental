package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.menu.BlockEntityMenu;
import dev.su5ed.gtexperimental.menu.DestructorPackMenu;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.menu.SonictronMenu;
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
    public static final RegistryObject<MenuType<SimpleMachineMenu>> AUTO_MACERATOR = register("auto_macerator", SimpleMachineMenu::autoMacerator);

    public static void init(IEventBus bus) {
        MENU_TYPES.register(bus);
    }

    private static <T extends BlockEntityMenu<?>> RegistryObject<MenuType<T>> register(String name, BlockEntityMenuConstructor<T> factory) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create((windowId, inv, data) -> factory.create(windowId, data.readBlockPos(), inv, inv.player)));
    }

    public interface BlockEntityMenuConstructor<T extends BlockEntityMenu<?>> {
        T create(int containerId, BlockPos pos, Inventory playerInventory, Player player);
    }

    private ModMenus() {}
}
