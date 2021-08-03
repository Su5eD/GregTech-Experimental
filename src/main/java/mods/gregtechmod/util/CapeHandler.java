package mods.gregtechmod.util;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class CapeHandler {
    private final List<UUID> gtCapes;
    private final List<UUID> capes;
    private static final ResourceLocation GT_CAPE_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gregorious_cape.png");
    private static final ResourceLocation CAPE_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gregtech_cape.png");
    
    // Currently disabled
    private CapeHandler() {
        List<UUID> gtCapes = new ArrayList<>();
        gtCapes.add(UUID.fromString("989e39a1-7d39-4829-87f1-286a06fab3bd")); // Su5eD
        this.gtCapes = Collections.unmodifiableList(gtCapes);
                    
        List<UUID> capes = new ArrayList<>();
        GtUtil.readAsset("GregTechCapes.txt").lines()
                .forEach(line -> {
                    String id = (line.contains("#") ? line.split("#")[0] : line).trim();
                    capes.add(UUID.fromString(id));
                });
        this.capes = Collections.unmodifiableList(capes);
    }
    
    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (GregTechConfig.GENERAL.showCapes) {
            AbstractClientPlayer clientPlayer = (AbstractClientPlayer) event.getEntityPlayer();
            UUID playerId = clientPlayer.getUniqueID();
            boolean gtCape = gtCapes.contains(playerId);
            boolean cape = capes.contains(playerId);
            if ((gtCape || cape) && clientPlayer.hasPlayerInfo() && clientPlayer.getLocationCape() == null) {
                NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, clientPlayer, "field_175157_a");
                Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, "field_187107_a");
                
                playerTextures.put(MinecraftProfileTexture.Type.CAPE, gtCape ? GT_CAPE_TEXTURE : CAPE_TEXTURE);
            }
        }
    }
}
