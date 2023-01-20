package dev.su5ed.gtexperimental.api;

import com.fasterxml.jackson.databind.JsonNode;
import dev.su5ed.gtexperimental.api.util.SonictronSound;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface IGregTechAPI {
    void registerSonictronSound(SonictronSound sound);
    void registerSonictronSounds(Collection<SonictronSound> sounds);
    List<SonictronSound> getSonictronSounds();
    String getSoundFor(Item item);
    
    void registerCondition(String type, Predicate<JsonNode> predicate);
    boolean testCondition(String type, JsonNode node);
    
    void addComputerCubeGuidePage(String translationKey, int length, List<ItemStack> stacks);
}
