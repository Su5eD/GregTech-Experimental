package dev.su5ed.gtexperimental.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.player.Player;

public final class KeyboardHandler {
    private static final Multimap<Player, Key> pressedKeys = HashMultimap.create();
    
    public static void pressKey(Player player, Key key) {
        pressedKeys.put(player, key);
    }
    
    public static void releaseKey(Player player, Key key) {
        pressedKeys.remove(player, key);
    }
    
    public static boolean isPressed(Player player, Key key) {
        return pressedKeys.get(player).contains(key);
    }
    
    public enum Key {
        SPRINT,
        JUMP,
        LEFT_ALT
    }
    
    private KeyboardHandler() {}
}
