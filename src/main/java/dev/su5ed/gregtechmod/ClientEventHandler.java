package dev.su5ed.gregtechmod;

import com.google.common.base.Strings;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public final class ClientEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        String energyTooltip = ModHandler.getEnergyTooltip(event.getItemStack());
        if (!Strings.isNullOrEmpty(energyTooltip)) {
            event.getToolTip().add(new TextComponent(energyTooltip));
        }
    }

    private ClientEventHandler() {}
}
