package dev.su5ed.gtexperimental.api;

import dev.su5ed.gtexperimental.api.util.SonictronSound;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public interface IGregTechAPI {
    void registerSonictronSound(SonictronSound sound);
    void registerSonictronSounds(Collection<SonictronSound> sounds);
    List<SonictronSound> getSonictronSounds();
    String getSoundFor(Item item);
    
    void addComputerCubeGuidePage(String translationKey, int length, List<ItemStack> stacks);
}
