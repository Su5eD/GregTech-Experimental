package dev.su5ed.gtexperimental;

import com.google.common.base.Strings;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.item.ElectricArmorItem;
import dev.su5ed.gtexperimental.network.GregTechNetwork;
import dev.su5ed.gtexperimental.network.KeyPressUpdate;
import dev.su5ed.gtexperimental.util.ArmorPerk;
import dev.su5ed.gtexperimental.util.KeyboardHandler;
import ic2.api.item.ElectricItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import one.util.streamex.StreamEx;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public final class ClientEventHandler {
    static final Int2ObjectMap<KeyboardHandler.Key> KEY_MAP = new Int2ObjectOpenHashMap<>();

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        String energyTooltip = ModHandler.getEnergyTooltip(event.getItemStack());
        if (!Strings.isNullOrEmpty(energyTooltip)) {
            event.getToolTip().add(Component.literal(energyTooltip));
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        boolean fullInvisibility = player.isInvisible() && StreamEx.of(player.getInventory().armor)
            .remove(ItemStack::isEmpty)
            .anyMatch(stack -> {
                Item item = stack.getItem();
                return item instanceof ElectricArmorItem armor
                    && armor.hasPerk(ArmorPerk.INVISIBILITY_FIELD)
                    && ElectricItem.manager.canUse(stack, 10000);
            });

        if (fullInvisibility) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        KeyboardHandler.Key key = KEY_MAP.get(event.getKey());
        if (key != null) {
            int code = event.getAction();
            KeyPressUpdate.Action action = code == 1 ? KeyPressUpdate.Action.PRESS : code == 0 ? KeyPressUpdate.Action.RELEASE : null;
            if (action != null) {
                GregTechNetwork.updateKeyPress(action, key);
            }
        }
    }

    private ClientEventHandler() {}
}
