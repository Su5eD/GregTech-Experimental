package mods.gregtechmod.core;

import mods.gregtechmod.api.util.GtUtil;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (GtUtil.getFullInvisibility(event.getEntityPlayer())) event.setCanceled(true);
    }
}
