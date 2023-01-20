package dev.su5ed.gtexperimental.api.machine;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;

public interface HasOwner {
    GameProfile getOwner();

    default boolean isOwnedBy(Player player) {
        return isOwnedBy(player.getGameProfile());
    }

    boolean isOwnedBy(GameProfile profile);

    boolean isPrivate();

    void setPrivate(boolean value);
}
